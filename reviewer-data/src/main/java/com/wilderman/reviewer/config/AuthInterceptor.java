package com.wilderman.reviewer.config;

import com.wilderman.reviewer.BaseInterceptor;
import com.wilderman.reviewer.annotation.RequireValidLogin;
import com.wilderman.reviewer.data.annotation.RequireValidHash;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class AuthInterceptor extends BaseInterceptor {


    protected boolean _preHandle(HttpServletRequest request, HttpServletResponse response, Object handler, Method method) throws Exception {
//        boolean requireValidLogin = method.isAnnotationPresent(RequireValidHash.class);
//
//        Map<String, String> q = splitQueryString(request);
//        if (!q.containsKey("hash")) {
//            return false;
//        }


//        return false;
        return true;
    }

    private Map<String, String> splitQueryString(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = request.getQueryString();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }
        return query_pairs;
    }

}
