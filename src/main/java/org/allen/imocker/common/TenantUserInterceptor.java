package org.allen.imocker.common;

import org.allen.imocker.dto.Constants;
import org.allen.imocker.dto.SessionObj;
import org.allen.imocker.exception.NoCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class TenantUserInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        SessionObj sessionObj = (SessionObj) session.getAttribute(Constants.SESSION_KEY);
        if (sessionObj != null) {
            request.setAttribute(Constants.ATTR_TENANT_ID, sessionObj.getTenantId());
            request.setAttribute(Constants.ATTR_USER_ID, sessionObj.getUserId());
            request.setAttribute(Constants.ATTR_NICK_NAME, sessionObj.getNickName());
            request.setAttribute(Constants.ATTR_USER_TYPE, sessionObj.getUserType());
        } else {
            throw new NoCredentialsException();
        }

        return super.preHandle(request, response, handler);
    }
}
