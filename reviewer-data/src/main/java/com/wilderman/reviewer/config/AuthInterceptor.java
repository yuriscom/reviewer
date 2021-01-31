package com.wilderman.reviewer.config;

import com.wilderman.reviewer.BaseInterceptor;
import com.wilderman.reviewer.annotation.RequireValidLogin;
import com.wilderman.reviewer.data.annotation.RequireValidHash;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Configuration
public class AuthInterceptor extends BaseInterceptor {


    protected boolean _preHandle(HttpServletRequest request, HttpServletResponse response, Object handler, Method method) throws Exception {
//        boolean requireValidLogin = method.isAnnotationPresent(RequireValidHash.class);
//
//        request.getQueryString()
//        return false;
        return true;
    }

}
