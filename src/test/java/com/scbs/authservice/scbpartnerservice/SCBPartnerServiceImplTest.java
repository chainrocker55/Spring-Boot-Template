package com.scbs.authservice.scbpartnerservice;

import com.scbs.authservice.scbpartnerservice.config.SCBPartnerAPIConfig;
import com.scbs.authservice.scbpartnerservice.response.SCBCardType;
import com.scbs.authservice.scbpartnerservice.response.SCBIdentityCard;
import io.micrometer.core.instrument.Metrics;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.function.Supplier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SCBPartnerServiceImplTest {

    SCBPartnerService scbPartnerService;

    @Mock
    private SCBPartnerAPIConfig scbPartnerAPIConfig;

    @Mock
    private Supplier<Response.Builder> mockResponseSupplier;
    private final String citizenId = "1100100112323";
    private final String alienId = "1234567890";
    private final String passportNumber = "AB5110010";
    private final String countryCode = "VN";
    private final String responseToken = "{\n" +
            "   \"status\":{\n" +
            "      \"code\":1000,\n" +
            "      \"description\":\"Success\"\n" +
            "   },\n" +
            "   \"data\":{\n" +
            "      \"accessToken\":\"7ad3e6a3-b539-4e06-aa04-cdb6977ebbdb\",\n" +
            "      \"tokenType\":\"Bearer\",\n" +
            "      \"expiresIn\":1800,\n" +
            "      \"expiresAt\":1538025199,\n" +
            "      \"refreshToken\":\"a51c841e-f2be-4225-aeb0-10aa5a9a33ae\",\n" +
            "      \"refreshExpiresIn\":3600,\n" +
            "      \"refreshExpiresAt\":1538026999\n" +
            "   }\n" +
            "}";

    @BeforeEach
    void setUp() {
        when(scbPartnerAPIConfig.getUrl()).thenReturn("http://test");
        when(scbPartnerAPIConfig.isAllowAllCerts()).thenReturn(true);
        when(scbPartnerAPIConfig.getAcceptLanguage()).thenReturn("en");
        when(scbPartnerAPIConfig.getResourceOwnerId()).thenReturn("2ed1533a-f080-46e3-976d-fc110e1acfe4");
        when(scbPartnerAPIConfig.getApikey()).thenReturn("apiKey");
        when(scbPartnerAPIConfig.getApisecret()).thenReturn("apiSecret");
        when(scbPartnerAPIConfig.getLogType()).thenReturn("BODY");

        scbPartnerService = new SCBPartnerServiceImpl(scbPartnerAPIConfig, new MockInterceptor(), Metrics.globalRegistry);
    }


    @Test
    void testRetrieveCustomerIdentityCardCitizen() {
        UUID requestId = UUID.randomUUID();
        String authCode = UUID.randomUUID().toString();
        String responseProfile = getResponseProfile(SCBCardType.P1.toString());
        when(mockResponseSupplier.get())
                .thenReturn(new Response.Builder()
                        .header("resourceOwnerId", "some-resource-owner-id")
                        .body(ResponseBody.create(responseToken, MediaType.parse("application/json"))))
                .thenReturn(new Response.Builder()
                        .body(ResponseBody.create(responseProfile,MediaType.parse("application/json"))));
        SCBIdentityCard expect = SCBIdentityCard.builder().cardId(citizenId).cardType(SCBCardType.P1).build();
        scbPartnerService.customerIdentityCard(requestId, authCode)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertResult(expect);

    }
    @Test
    void testRetrieveCustomerIdentityCardAlien() {
        UUID requestId = UUID.randomUUID();
        String authCode = UUID.randomUUID().toString();
        String responseProfile = getResponseProfile(SCBCardType.P7.toString());
        when(mockResponseSupplier.get())
                .thenReturn(new Response.Builder()
                        .header("resourceOwnerId", "some-resource-owner-id")
                        .body(ResponseBody.create(responseToken, MediaType.parse("application/json"))))
                .thenReturn(new Response.Builder()
                        .body(ResponseBody.create(responseProfile,MediaType.parse("application/json"))));
        SCBIdentityCard expect = SCBIdentityCard.builder().cardId(alienId).cardType(SCBCardType.P7).build();
        scbPartnerService.customerIdentityCard(requestId, authCode)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertResult(expect);

    }
    @Test
    void testRetrieveCustomerIdentityCardPassport() {
        UUID requestId = UUID.randomUUID();
        String authCode = UUID.randomUUID().toString();
        String responseProfile = getResponseProfile(SCBCardType.P8.toString());
        when(mockResponseSupplier.get())
                .thenReturn(new Response.Builder()
                        .header("resourceOwnerId", "some-resource-owner-id")
                        .body(ResponseBody.create(responseToken, MediaType.parse("application/json"))))
                .thenReturn(new Response.Builder()
                        .body(ResponseBody.create(responseProfile, MediaType.parse("application/json"))));
        SCBIdentityCard expect = SCBIdentityCard.builder().cardId(passportNumber).cardType(SCBCardType.P8).countryCode(countryCode).build();
        scbPartnerService.customerIdentityCard(requestId, authCode)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertResult(expect);

    }
    @Test
    void testRetrieveResponseBusinessError() {
        when(mockResponseSupplier.get()).thenReturn(new Response.Builder()
                .body(ResponseBody.create(
                        "{" +
                                "\"status\":" +
                                "{" +
                                "\"code\":\"1999\"," +
                                "\"description\":\"Fubar\"" +
                                "}" +
                                "}"
                , MediaType.parse("application/json"))));

        scbPartnerService.customerIdentityCard(UUID.randomUUID(), "1234")
                .test()
                .assertError(Exception.class);

    }
    @Test
    void testRetrieveDeeplink() {
        String deeplink = "authapp://partners/login?applicationId=820fd1de-eb04-416a-91f0-3e6e8c8c9d96";
        when(mockResponseSupplier.get()).thenReturn(new Response.Builder()
                .body(ResponseBody.create(
                        "{\n" +
                                "   \"status\":{\n" +
                                "      \"code\":\"1000\",\n" +
                                "      \"description\":\"Success\"\n" +
                                "   },\n" +
                                "   \"data\":{\n" +
                                "      \"callbackUrl\":\""+deeplink+"\"\n" +
                                "   }\n" +
                                "}"
                                        , MediaType.parse("application/json"))));

        scbPartnerService.queryDeepLink()
                .test()
                .assertNoErrors()
                .assertValue(authorizeResponse -> authorizeResponse.getData().getCallbackUrl().equals(deeplink));
    }
    @Test
    void testRetrieveDeeplinkBusinessError() {
        when(mockResponseSupplier.get()).thenReturn(new Response.Builder()
                .body(ResponseBody.create(
                        "{" +
                                "\"status\":" +
                                "{" +
                                "\"code\":\"1999\"," +
                                "\"description\":\"Fubar\"" +
                                "}" +
                                "}",
                        MediaType.parse("application/json")
                )));

        scbPartnerService.queryDeepLink()
                .test()
                .assertError(Exception.class);

    }

    private class MockInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(Chain chain) {
            int responseCode = 200;
            return mockResponseSupplier.get()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(responseCode)
                    .message("success")
                    .build();
        }
    }
    private String getResponseProfile(String cardType){
       return "{\n" +
                "\"status\":" +
                "{" +
                "\"code\":\"1000\"," +
                "\"description\":\"Success\"" +
                "},\n" +
                "\"data\":" +
                "{" +
                "\"profile\":" +
                "{" +
                "\"citizenID\":\""+citizenId+"\"," +
                "\"passportNumber\":\""+passportNumber+"\"," +
                "\"alienID\":\""+alienId+"\"," +
                "\"partnerID\":\"00001231231234\"," +
                "\"thaiFirstName\":\"จอห์น\"," +
                "\"thaiLastName\":\"โดว์\"," +
                "\"engFirstName\":\"John\"," +
                "\"engLastName\":\"Doe\"," +
                "\"birthDate\":\"1990-01-31\"," +
                "\"mobile_number\":\"0910110111\"," +
                "\"email\":\"john.doe@scb.co.th\"," +
                "\"genderCode\":\"M\"," +
                "\"cardType\": \""+cardType+"\"," +
                "\"countryCode\": \""+countryCode+"\"," +
                "\"address\":" +
                "{" +
                "\"zipCode\":\"10900\"," +
                "\"thaiAddressThanon\":\"รัชดาภิเษก\"," +
                "\"engAddressThanon\":\"Ratchadaphisek\"," +
                "\"engAddressState\":\"Bangkok\"," +
                "\"thaiAddressProvince\":\"กรุงเทพฯ\"," +
                "\"unitNumber\":\"23/12\"," +
                "\"engAddressNumber\":\"209/128\"," +
                "\"engAddressDistrict\":\"Chatuchak\"," +
                "\"engAddressProvince\":\"Bangkok\"," +
                "\"countryCode\":\"TH\"," +
                "\"floorNumber\":\"14\"," +
                "\"thaiAddressState\":\"กรุงเทพฯ\"," +
                "\"engAddressAmphur\":\"Chatuchak\"," +
                "\"thaiAddressMoo\":\"2\"," +
                "\"thaiAddressSoi\":\"อารีย์ 23\"," +
                "\"thaiAddressAmphur\":\"จตุจักร\"," +
                "\"thaiAddressTrok\":\"อารีย์\"," +
                "\"engAddressTrok\":\"Ari\"," +
                "\"engAddressVillage\":\"SP Building\"," +
                "\"thaiAddressDistrict\":\"จตุจักร\"," +
                "\"thaiAddressName\":\"TBD\"," +
                "\"engAddressName\":\"TBD\"," +
                "\"thaiAddressVillage\":\"อาคารเอสพี\"," +
                "\"thaiAddressNumber\":\"209/128\"," +
                "\"engAddressMoo\":\"2\"," +
                "\"engAddressSoi\":\"Ari 23\"" +
                "}" +
                "}" +
                "}" +
                "}";
    }
}