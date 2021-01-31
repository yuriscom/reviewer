package com.wilderman.reviewer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpErrors {

    public static void Unauthorized(HttpServletResponse response) throws IOException {
        response.sendError(401, "Unauthorized");
    }

    public static void InternalError(HttpServletResponse response) throws IOException {
        response.sendError(500, "Error");
    }
}
