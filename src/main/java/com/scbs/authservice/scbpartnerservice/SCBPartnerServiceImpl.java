package com.scbs.authservice.scbpartnerservice;

import com.scbs.authservice.config.UnsafeOkHttpClient;
import com.scbs.authservice.scbpartnerservice.config.SCBPartnerAPIConfig;
import com.scbs.authservice.scbpartnerservice.request.ScbPartnerTokenParams;
import com.scbs.authservice.scbpartnerservice.response.SCBIdentityCard;
import com.scbs.authservice.scbpartnerservice.response.SCBPartnerAuthorizeResponse;
import com.scbs.authservice.scbpartnerservice.response.SCBPartnerResponseStatus;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.reactivex.Single;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SCBPartnerServiceImpl implements SCBPartnerService {

    private final static Logger logger = LoggerFactory.getLogger(SCBPartnerServiceImpl.class);
    private static final String PARTNER_REQUEST_MONITORING_KEY = "partner.request";
    private static final String TAG_TOKEN = "token";
    private static final String TAG_DEEPLINK = "deeplink";
    private static final String TAG_PROFILE = "SCBProfile";
    private final SCBPartnerAPIService service;
    private final MeterRegistry registry;
    private SCBPartnerAPIConfig config;

    @Autowired
    SCBPartnerServiceImpl(SCBPartnerAPIConfig config, Interceptor interceptor, MeterRegistry registry) {
        this.config = config;
        this.registry = registry;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (config.isAllowAllCerts()) {
            httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder();
        }

        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept-Language", config.getAcceptLanguage())
                    .build();

            return chain.proceed(request);
        });

        if (!config.getLogType().equals("NONE")) {
            Logger packetLogger = LoggerFactory.getLogger("partnerDbPacketLogger");

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(packetLogger::info);
            logging.setLevel(HttpLoggingInterceptor.Level.valueOf(config.getLogType()));
            httpClient.addInterceptor(logging);
        }

        if (interceptor != null) {
            httpClient.addInterceptor(interceptor);
        }

        service = new Retrofit.Builder()
                .baseUrl(config.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(SCBPartnerAPIService.class);

    }

    public SCBPartnerServiceImpl(@Autowired SCBPartnerAPIConfig config, MeterRegistry registry) {
        this(config, null, registry);
    }

    @Override
    public Single<SCBIdentityCard> customerIdentityCard(UUID requestUUID, String authToken) {
        Map<String, String> tokenHeaders = new HashMap<>();
        tokenHeaders.put("resourceOwnerId", config.getResourceOwnerId());
        tokenHeaders.put("requestUId", requestUUID.toString());

        AtomicReference<Timer.Sample> tokenSample = new AtomicReference<>();
        AtomicReference<Timer.Sample> profileSample = new AtomicReference<>();
        return service.token(tokenHeaders, new ScbPartnerTokenParams(config.getApikey(), config.getApisecret(), authToken))
                .doOnSubscribe(disposable -> tokenSample.set(Timer.start(registry)))
                .doOnSuccess(ignore -> tokenSample.get().stop(registry.timer(PARTNER_REQUEST_MONITORING_KEY, "result", "OK", "type", TAG_TOKEN)))
                .doOnError(throwable -> tokenSample.get().stop(registry.timer(PARTNER_REQUEST_MONITORING_KEY, "result", throwable.getMessage())))
                .flatMap(tokenResponse -> {
                    if (tokenResponse.isSuccessful() && tokenResponse.body() != null) {
                        Map<String, String> profileHeaders = new HashMap<>();
                        profileHeaders.put("requestUId", requestUUID.toString());
                        profileHeaders.put("resourceOwnerId", tokenResponse.headers().get("resourceOwnerId"));
//                        profileHeaders.put("resourceOwnerId", config.getResourceOwnerId());
                        profileHeaders.put("Authorization", tokenResponse.body().getAccessToken());

                        return service.SCBProfile(profileHeaders)
                                .doOnSubscribe(disposable -> profileSample.set(Timer.start(registry)))
                                .doOnSuccess(next -> profileSample.get().stop(registry.timer(PARTNER_REQUEST_MONITORING_KEY, "result", String.valueOf(next.getStatus().getCode()), "type", TAG_PROFILE)))
                                .doOnError(throwable -> profileSample.get().stop(registry.timer(PARTNER_REQUEST_MONITORING_KEY, "result", throwable.getMessage(), "type", TAG_PROFILE))) ;

                    } else {
                        return Single.error(new HttpException(tokenResponse));
                    }
                })
                .doOnSuccess(partnerProfileResponse -> {
                    SCBPartnerResponseStatus responseStatus = partnerProfileResponse.getStatus();
                    if (responseStatus.getCode() == SCBPartnerResponseStatus.GENERIC_BUSINESS_ERROR) {

                        logger.warn("Unable to retrieve SCBProfile for request due to {}: {}", responseStatus.getCode(), responseStatus.getDescription());
                        throw new Exception("Response contained generic business error <" + responseStatus.getDescription() + ">");
                    }
                })
                .map(it -> SCBIdentityCard.from(it.getData().getProfile()));
    }

    @Override
    public Single<SCBPartnerAuthorizeResponse> queryDeepLink() {
        Map<String, String> headers = new HashMap<>();
        headers.put("requestUId", UUID.randomUUID().toString());
        headers.put("apikey", config.getApikey());
        headers.put("apisecret", config.getApisecret());
        headers.put("response-channel", "mobile");
        headers.put("endState", "mobile_app");
        headers.put("resourceOwnerID", config.getResourceOwnerId());

        AtomicReference<Timer.Sample> deepLinkSample = new AtomicReference<>();
        return service.authorize(headers)
                .doOnSubscribe(disposable -> deepLinkSample.set(Timer.start()))
                .doOnSuccess(partnerProfileResponse -> {
                    SCBPartnerResponseStatus responseStatus = partnerProfileResponse.getStatus();
                    deepLinkSample.get().stop(registry.timer(PARTNER_REQUEST_MONITORING_KEY, "result", String.valueOf(SCBPartnerResponseStatus.GENERIC_BUSINESS_ERROR), "type", TAG_DEEPLINK));
                    if (responseStatus.getCode() == SCBPartnerResponseStatus.GENERIC_BUSINESS_ERROR) {
                        logger.warn("Unable to retrieve SCBProfile for request due to {}: {}", responseStatus.getCode(), responseStatus.getDescription());
                        throw new Exception("Response contained generic business error <" + responseStatus.getDescription() + ">");
                    }
                })
                .doOnError(throwable -> {
                    deepLinkSample.get().stop(registry.timer(PARTNER_REQUEST_MONITORING_KEY, "result", throwable.getMessage(), "type", TAG_DEEPLINK));
                });
    }
}
