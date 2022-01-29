package com.wilderman.reviewer.auth;

import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wilderman.reviewer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Configuration
public class JWTFactory {

    @Value("${auth0.config.jwtSecret:one two three}")
    private String jwtSecret;

    @Value("${com.auth0.domain}")
    private String domain;

    @Value("${com.auth0.pubkey}")
    private String pubkeyPath;

    @Autowired
    private final JWTVerifier verify = null;

//    @Autowired
//    SessionManager sessionManager;

    @Bean
    public JWTVerifier createVerifier() throws Exception {
        Algorithm algorithm = createAlgorithm();

        return JWT.require(algorithm)
                .withIssuer(String.format("https://%s/", domain))
                .acceptExpiresAt(1)
                .acceptLeeway(1)
                .acceptIssuedAt(1)
                .build();
    }

    //    public Algorithm createAlgorithm() {
//        return Algorithm.HMAC256(jwtSecret);
//    }

    public Algorithm createAlgorithm() throws Exception {
        RSAPublicKey publicKey = (RSAPublicKey) PemUtils.readPublicKeyFromFile(pubkeyPath, "RSA");
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        return algorithm;
    }


    public String getToken(Tokens tokens, String userId, String authToken) throws ServiceException {
        try {

            Date now = new Date();
            Date expires = Date.from(Instant.now().plus(Duration.ofSeconds(tokens.getExpiresIn())));

            Algorithm algorithm = createAlgorithm();
            return JWT.create()
                    .withIssuer("auth0")
                    .withIssuedAt(now)
                    .withExpiresAt(expires)
                    .withClaim("userId", userId)
                    .withClaim("accessToken", tokens.getAccessToken())
                    .withClaim("refreshToken", tokens.getRefreshToken())
                    .withClaim("idToken", tokens.getIdToken())
                    .withClaim("type", tokens.getType())
                    .withClaim("authToken", authToken)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            //Invalid Signing configuration / Couldn't convert Claims.
            throw new ServiceException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e);
        }
    }

    public Authentication verifyToken(String token) {
        DecodedJWT decoded = verify.verify(token);
//        sessionManager.verify(token);
        return Authentication.createAuthentication(decoded);
    }
}
