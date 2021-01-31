package com.wilderman.reviewer.auth;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class Authentication {

    public String userId;
    public String idToken;
    public String accessToken;
    public String refreshToken;
    public String type;
    public String authToken;
    public Date issued;
    public Date expires;

    public Authentication() {}

    public Authentication(String userId, String idToken, String accessToken, String refreshToken, String type, Date issued, Date expires, String authToken) {
        this.userId = userId;
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.type = type;
        this.issued = issued;
        this.expires = expires;
        this.authToken = authToken;
    }

    public static Authentication createAuthentication(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();

        String userId = (claims.get("userId") != null) ? claims.get("userId").asString() : null;
        String idToken = (claims.get("idToken") != null) ? claims.get("idToken").asString() : null;
        String accessToken = (claims.get("accessToken") != null) ? claims.get("accessToken").asString() : null;
        String refreshToken = (claims.get("refreshToken") != null) ? claims.get("refreshToken").asString() : null;
        String type = (claims.get("type") != null) ? claims.get("type").asString() : null;
        String internalToken = (claims.get("authToken") != null) ? claims.get("authToken").asString() : null;

        return new Authentication(
                userId,
                idToken,
                accessToken,
                refreshToken,
                type,
                jwt.getIssuedAt(),
                jwt.getExpiresAt(),
                internalToken
        );
    }

    @Override
    public String toString() {
        return "userId:" + this.userId;
    }
}
