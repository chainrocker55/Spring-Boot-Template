package com.scbs.authservice.scbpartnerservice.mock;

import com.scbs.authservice.scbpartnerservice.SCBPartnerService;
import com.scbs.authservice.scbpartnerservice.SCBPartnerServiceImpl;
import com.scbs.authservice.scbpartnerservice.config.SCBPartnerAPIConfig;
import com.scbs.authservice.scbpartnerservice.response.*;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class SCBPartnerServiceMockImpl implements SCBPartnerService {
    private final static Logger logger = LoggerFactory.getLogger(SCBPartnerServiceMockImpl.class);

    private SCBPartnerAPIConfig config;

    @Autowired
    public SCBPartnerServiceMockImpl(SCBPartnerAPIConfig config) {
        this.config = config;
    }

    @Override
    public Single<SCBIdentityCard> customerIdentityCard(UUID requestUUID, String mockType) {

        if (config.getMockProfile().containsKey(mockType)) {
            SCBProfile profile = config.getMockProfile().get(mockType);

            SCBIdentityCard identityCard = SCBIdentityCard.from(profile);

            return Single.just(identityCard);
        } else {
            return Single.error(new Exception("Response contained generic business error <user doesn't exist>"));
        }
    }

    @Override
    public Single<SCBPartnerAuthorizeResponse> queryDeepLink() {
       SCBPartnerAuthorizeResponse deeplink = config.getMockDeeplink().get("deeplink");

        return Single.just(deeplink);
    }
}
