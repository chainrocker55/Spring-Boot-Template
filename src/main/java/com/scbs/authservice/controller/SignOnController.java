package com.scbs.authservice.controller;

import com.scbs.authservice.consts.PartnerConst;
import com.scbs.authservice.consts.ResponseMessageConst;
import com.scbs.authservice.consts.ResponseStatusConst;
import com.scbs.authservice.exception.BadRequestException;
import com.scbs.authservice.exception.InternalException;
import com.scbs.authservice.exception.NotFoundException;
import com.scbs.authservice.exception.UnauthorizedException;
import com.scbs.authservice.model.AuthParams;
import com.scbs.authservice.model.BaseResponse;
import com.scbs.authservice.model.SessionData;
import com.scbs.authservice.model.UserInfo;
import com.scbs.authservice.repository.entity.LoginLog;
import com.scbs.authservice.repository.entity.User;
import com.scbs.authservice.scbpartnerservice.SCBPartnerService;
import com.scbs.authservice.scbpartnerservice.response.SCBData;
import com.scbs.authservice.scbpartnerservice.response.SCBIdentityCard;
import com.scbs.authservice.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/singon")
public class SignOnController {
    private static final Logger logger = LoggerFactory.getLogger(SignOnController.class);
    static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String SCHEME_TRACING_HEADER = "X-requested-scheme";

    @Autowired
    private SCBPartnerService scbPartnerService;
    @Autowired
    private UserDataService userDataService;


    @GetMapping(value = "/deeplink/{partner}")
    public BaseResponse<SCBData> easyAppDeepLink(@PathVariable("partner") String partner, HttpServletRequest request) throws NotFoundException {
        logger.trace("deeplink: Received session ID {} and had session {}", request.getRequestedSessionId(), request.getSession().getId());

        switch (partner) {
            case PartnerConst.SCB_EASY :
                return scbPartnerService.queryDeepLink()
                        .map(response -> {
                            if (response.getStatus().getCode() == 1000) {
                                logger.debug("Sending deeplink response {} to user session {}", response, request.getRequestedSessionId());
                                return BaseResponse.<SCBData>builder().build().success(response.getData());
                            } else {
                                logger.warn("Failed to retrieve deeplink for session {}", request.getRequestedSessionId());
                                throw new BadRequestException(response.getStatus().getDescription() + " code: "+response.getStatus());
                            }
                        })
                        .onErrorReturn(throwable -> {
                            logger.warn("Unable to retrieve easy app deeplink due to", throwable);
                            throw new InternalException(ResponseMessageConst.INTERNAL_ERROR);
                        })
                        .blockingGet();
            default:
                throw new NotFoundException();
        }

    }

    @PostMapping(value = "/authenticate/partner/token")
    public BaseResponse<UserInfo> authenticate(@RequestBody AuthParams authParams, HttpServletRequest request) {
        MDC.put("session.id", request.getSession().getId());
        logger.trace("authenticate: Received session ID {} and had session {} with params {}", request.getRequestedSessionId(), request.getSession().getId(), authParams);

        if (PartnerConst.SCB_EASY.equals(authParams.getScheme())) {
            Optional<User> optionalUser = Optional.empty();
            Optional<SCBIdentityCard> card = Optional.empty();

            logger.info("Attempted SSO login with token {}", authParams.getToken());

            card = scbPartnerService.customerIdentityCard(UUID.randomUUID(), authParams.getToken())
                    .map(Optional::ofNullable)
                    .onErrorReturn(throwable -> {
                        throwable.printStackTrace();
                        logger.error("Error retrieving customer identity card from partner", throwable);
                        return Optional.empty();
                    }).blockingGet();

            if (card.isPresent()) {
                optionalUser = userDataService.findByCard(card.get());
                if (optionalUser.isPresent() && !optionalUser.get().isDisabled()) {
                    User user = optionalUser.get();
                    UserInfo userInfo = generateAuthResponse(user, LoginLog.LoginType.EASY_APP);
                    return BaseResponse.<UserInfo>builder().build().success(userInfo);
                }
                return BaseResponse.<UserInfo>builder().build().success(null, ResponseStatusConst.SUCCESS_BUT_NOT_FOUND_USER, ResponseMessageConst.SUCCESS_BUT_NOT_FOUND_USER);

            }
        }
        throw new UnauthorizedException();
    }
    private UserInfo generateAuthResponse(User user, LoginLog.LoginType loginType) {
        logger.info("User {} logged in successfully by {}", user.getEmail(), loginType.name());

        return UserInfo.builder()
                .titleEn(user.getTitleEn())
                .titleTh(user.getTitleTh())
                .firstNameEn(user.getFirstNameEn())
                .firstNameTh(user.getFirstNameTh())
                .lastNameEn(user.getLastNameEn())
                .lastNameTh(user.getLastNameTh())
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }

    public void setSourceIP(SessionData sessionData, HttpServletRequest request) {
        String forwardedFor = request.getHeader(X_FORWARDED_FOR);
        String sourceIp = request.getRemoteAddr();

        if (forwardedFor != null && !forwardedFor.isEmpty()) {

            String[] addresses = forwardedFor.split(",");
            if (addresses[0] != null && !addresses[0].isEmpty()) {
                sourceIp = addresses[0].trim();
            }
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(sourceIp);
            if (sessionData.getSourceIp() == null || sessionData.getSourceIp().equals(inetAddress)) {
                sessionData.setSourceIp(inetAddress);
            } else {
                logger.warn("Received inconsistent source IP for session, expected {} but had {}", sessionData.getSourceIp().toString(), inetAddress.toString());
            }
        } catch (UnknownHostException e) {
            logger.warn("Source IP for session not a valid IP address {}", sourceIp);
        }
    }
    void setUsername(SessionData sessionData, AuthParams authParams) {
        if (authParams.getUsername() != null && !authParams.getUsername().isEmpty()) {
            if (sessionData.getUserName() == null || sessionData.getUserName().isEmpty() || authParams.getUsername().equals(sessionData.getUserName())) {
                logger.debug("Setting username on session to {}", authParams.getUsername());
                sessionData.setUserName(authParams.getUsername());
            } else {
                logger.warn("Received inconsistent usernames had {} but got {}", sessionData.getUserName(), authParams.getUsername());
            }
        }
    }

    void setOs(SessionData sessionData, AuthParams authParams) {
        if (authParams.getOs() != null && !authParams.getOs().isEmpty()) {
            if (sessionData.getOs() == null || sessionData.getOs().isEmpty() || authParams.getOs().equals(sessionData.getOs())) {
                logger.debug("Setting OS on session to {}", authParams.getOs());
                sessionData.setOs(authParams.getOs());
            } else {
                logger.warn("Received inconsistent os had {} but got {}", sessionData.getOs(), authParams.getOs());
            }
        }
    }

    void setClientVersion(SessionData sessionData, AuthParams authParams) {
        if (authParams.getVersion() != null && !authParams.getVersion().isEmpty()) {
            if (sessionData.getClientVersion() == null || sessionData.getClientVersion().isEmpty() || authParams.getVersion().equals(sessionData.getClientVersion())) {
                logger.debug("Setting client version on session to {}", authParams.getVersion());
                sessionData.setClientVersion(authParams.getVersion());
            } else {
                logger.warn("Received inconsistent client version had {} but got {}", sessionData.getClientVersion(), authParams.getVersion());
            }
        }
    }

    void setDeviceId(SessionData sessionData, AuthParams authParams) {
        if (sessionData.getDeviceId() == null || sessionData.getDeviceId().isEmpty()) {
            if (authParams.getDeviceId() == null || authParams.getDeviceId().isEmpty()) {
                throw new IllegalArgumentException("deviceID must be supplied");
            }
            sessionData.setDeviceId(authParams.getDeviceId());
        } else if (!sessionData.getDeviceId().equals(authParams.getDeviceId())) {
            logger.warn("Received inconsistent device ID for session, expected {} but had {}", sessionData.getDeviceId(), authParams.getDeviceId());
        }
    }

}
