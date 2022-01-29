package com.wilderman.reviewer.auth;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class Authentication {

    public String sub;
    public String emailVerified;
    public String updatedAt;
    public String nickname;
    public String name;
    public String iss;
    public String picture;
    public String email;
    public Date issuedAt;
    public Date expiresAt;


    public Authentication() {}

    public Authentication(String sub, String emailVerified, String updatedAt, String nickname, String name, String iss, String picture, String email, Date issuedAt, Date expiresAt) {
        this.sub = sub;
        this.emailVerified = emailVerified;
        this.updatedAt = updatedAt;
        this.nickname = nickname;
        this.name = name;
        this.iss = iss;
        this.picture = picture;
        this.email = email;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public static Authentication createAuthentication(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();

        String sub = (claims.get("sub") != null) ? claims.get("sub").asString() : null;
        String emailVerified = (claims.get("email_verified") != null) ? claims.get("email_verified").asString() : null;
        String updatedAt = (claims.get("updated_at") != null) ? claims.get("updated_at").asString() : null;
        String nickname = (claims.get("nickname") != null) ? claims.get("nickname").asString() : null;
        String name = (claims.get("name") != null) ? claims.get("name").asString() : null;
        String iss = (claims.get("iss") != null) ? claims.get("iss").asString() : null;
        String picture = (claims.get("picture") != null) ? claims.get("picture").asString() : null;
        String email = (claims.get("email") != null) ? claims.get("email").asString() : null;


        return new Authentication(
                sub,
                emailVerified,
                updatedAt,
                nickname,
                name,
                iss,
                picture,
                email,
                jwt.getIssuedAt(),
                jwt.getExpiresAt()
        );
    }

    @Override
    public String toString() {
        return "sub:" + this.sub;
    }
}
