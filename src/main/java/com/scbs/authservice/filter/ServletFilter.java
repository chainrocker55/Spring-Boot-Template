package com.scbs.authservice.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.logging.Logger;
@Component
public class ServletFilter implements Filter {
    private static final Logger logger = Logger.getLogger(ServletFilter.class.getName());
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest servletRequest = (HttpServletRequest) request;
//        HttpServletResponse servletResponse = (HttpServletResponse) response;

//        HttpSession session = servletRequest.getSession(false);
//        if (session == null) {
//            logger.log(Level.INFO, "Denying KeyMaster access: no session");
//            servletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            return;
//        }

//        System.out.println("Remote Host:"+request.getRemoteHost());
//        System.out.println("Remote Address:"+request.getRemoteAddr());

        chain.doFilter(request, response);
    }
}
