package com.scbs.authservice.scbpartnerservice;

import com.scbs.authservice.scbpartnerservice.response.SCBIdentityCard;
import com.scbs.authservice.scbpartnerservice.response.SCBPartnerAuthorizeResponse;
import io.reactivex.Single;

import java.util.UUID;

public interface SCBPartnerService {
    Single<SCBIdentityCard> customerIdentityCard(UUID requestUUID, String authToken);

    Single<SCBPartnerAuthorizeResponse> queryDeepLink();
}
