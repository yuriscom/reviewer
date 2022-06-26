package com.wilderman.reviewer.data.dto;

import com.wilderman.reviewer.db.primary.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ClientOutput {
    private Long id;
    private String uname;
    private String name;
    private String website;
    private String linkGoogleMobile;
    private String linkGoogleDesktop;
    private String logo;
    private Map<String, String> additionalDetails;

    public ClientOutput(Client client) {
        id = client.getId();
        uname = client.getUname();
        name = client.getName();
        website = client.getWebsite();
        linkGoogleMobile = client.getLinkGoogleMobile();
        linkGoogleDesktop = client.getLinkGoogleDesktop();
        logo = client.getLogo();
        additionalDetails = client.getData();
    }
}
