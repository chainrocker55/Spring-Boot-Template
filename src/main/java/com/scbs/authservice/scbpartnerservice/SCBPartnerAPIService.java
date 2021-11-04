package com.scbs.authservice.scbpartnerservice;

import com.scbs.authservice.scbpartnerservice.request.ScbPartnerTokenParams;
import com.scbs.authservice.scbpartnerservice.response.SCBPartnerAuthorizeResponse;
import com.scbs.authservice.scbpartnerservice.response.SCBPartnerProfileResponse;
import com.scbs.authservice.scbpartnerservice.response.SCBPartnerTokenResponse;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

import java.util.Map;

public interface SCBPartnerAPIService {

    @GET("v1/customers/profile")
    Single<SCBPartnerProfileResponse> SCBProfile(@HeaderMap Map<String, String> headers);

    @POST("v1/oauth/token")
    Single<Response<SCBPartnerTokenResponse>> token(@HeaderMap Map<String, String> headers, @Body ScbPartnerTokenParams partnerTokenBody);

    @GET("v2/oauth/authorize")
    Single<SCBPartnerAuthorizeResponse> authorize(@HeaderMap Map<String, String> headers);
}
