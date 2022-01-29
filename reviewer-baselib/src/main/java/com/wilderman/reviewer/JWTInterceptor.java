package com.wilderman.reviewer;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.wilderman.reviewer.annotation.RequireValidAdmin;
import com.wilderman.reviewer.annotation.RequireValidLogin;
import com.wilderman.reviewer.annotation.RequireValidUser;
import com.wilderman.reviewer.auth.Authentication;
import com.wilderman.reviewer.auth.JWTFactory;
import com.wilderman.reviewer.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;



@Configuration
public class JWTInterceptor extends BaseInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JWTInterceptor.class);

    @Autowired
    private final JWTFactory jwt = null;

    @Value("${com.auth0.clientSecret}")
    String auth0Secret;

    public boolean _preHandle(HttpServletRequest request, HttpServletResponse response, Object handler, Method method) throws Exception {
        String jwtToken = request.getHeader(Constants.JWT_HEADER_NAME);

        boolean requireValidLogin = method.isAnnotationPresent(RequireValidLogin.class);
        boolean requireValidUser = method.isAnnotationPresent(RequireValidUser.class);
        boolean requireValidAdmin = method.isAnnotationPresent(RequireValidAdmin.class);


        if (jwtToken != null && !jwtToken.isEmpty()) {
            try {
                Authentication auth = jwt.verifyToken(jwtToken);

            } catch (TokenExpiredException e) {
                if (requireValidLogin || requireValidUser || requireValidAdmin) {
                    log.error("The jwt token is expired");
                    HttpErrors.Unauthorized(response);
                    return false;
                }
            } catch (JWTVerificationException e) {
                log.error("The jwt token is invalid");
                HttpErrors.Unauthorized(response);
                return false;
            } catch (Exception e) {
                log.error("An error occurred", e);
                HttpErrors.InternalError(response);
                return false;
            }
        } else {
            if (requireValidUser || requireValidLogin || requireValidAdmin) {
                HttpErrors.Unauthorized(response);
                return false;
            }
        }

        return true;
    }
}
