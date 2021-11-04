package com.scbs.authservice.intercepter;

import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LogInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sourceSystem = request.getHeader("sourceSystem");
        String resourceOwnerID = request.getHeader("resourceOwnerID");
        String requestUID = request.getHeader("requestUID");
        MDC.clear(); // Clear the attributes before we start attaching the attributes for the new request
        MDC.put("requestUID", StringUtils.isNotEmpty(requestUID)?requestUID: UUID.randomUUID().toString());
        MDC.put("sourceSystem", sourceSystem);
        MDC.put("resourceOwnerID", resourceOwnerID);
        MDC.put("threadId", Thread.currentThread().getName());
        return true;
    }
}
