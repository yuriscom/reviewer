package com.wilderman.reviewer.utils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.binary.Base64;

import java.util.List;


public class Jwt {

	public static final String JWT_HEADER_NAME = "Authorization";

    public static TokenData decode(String jwtToken) {
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];


        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
        String body = new String(base64Url.decode(base64EncodedBody));

        Gson g = new Gson();
        return g.fromJson(body, TokenData.class);

    }

    public class TokenData {
        @SerializedName("cognito:groups")
        public List<String> groups;

        @SerializedName("cognito:username")
        public String cognitoUsername;

        @SerializedName("cognito:roles")
        public List<String> cognitoRoles;

        public List<Identity> identities;

        public String email;

        @SerializedName("given_name")
        public String firstName;

        @SerializedName("family_name")
        public String lastName;
        
        public String sub;
    }

    public class Identity {
        public String userId;

    }

}
