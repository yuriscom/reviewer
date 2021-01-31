package com.wilderman.reviewer.utils;

import com.auth0.SessionUtils;
import com.google.common.hash.Hashing;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

public class HttpUtils {

    public static final String HEADER_X_FORWARDED_PROTO = "X-Forwarded-Proto";
    public static final String HEADER_X_FORWARDED_PORT = "X-Forwarded-Port";

    public static final Integer RECORDS_PER_PAGE = 50;

    public static String buildLocalUrl(HttpServletRequest req) {
        String proto = getRequestScheme(req);
        String serverName = req.getServerName();
        String port = getRequestPort(req);

        String returnUri = proto + "://" + serverName;
        if (!port.equals("80") && !port.equals("443")) {
            returnUri += ":" + port;
        }

        return returnUri;
    }

    public static String getRequestScheme(HttpServletRequest req) {
        String proto = req.getScheme();
        if (req.getHeader(HttpUtils.HEADER_X_FORWARDED_PROTO) != null) {
            proto = req.getHeader(HttpUtils.HEADER_X_FORWARDED_PROTO);
        }
        return proto;
    }

    public static String getRequestPort(HttpServletRequest req) {
        String port = String.valueOf(req.getServerPort());
        if (req.getHeader(HttpUtils.HEADER_X_FORWARDED_PORT) != null) {
            port = req.getHeader(HttpUtils.HEADER_X_FORWARDED_PORT);
        }
        return port;
    }

    public static String getSha256(String input) {
        return Hashing.sha256()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
    }


    public static void setSessionToken(HttpServletRequest req, String token) {
        SessionUtils.set(req, "systemAuthToken", token);
    }

    public static String getSessionToken(HttpServletRequest req) {
        return (String)SessionUtils.get(req, "systemAuthToken");
    }
}
