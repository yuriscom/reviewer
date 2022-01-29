package com.wilderman.reviewer.data.dto.admin;

import com.wilderman.reviewer.db.primary.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminClientOutput {
    private Long id;
    private String uname;
    private String name;
    private String website;
    private String linkGoogleMobile;
    private String logo;

    public AdminClientOutput(Client client) {
        id = client.getId();
        uname = client.getUname();
        name = client.getName();
        website = client.getWebsite();
        linkGoogleMobile = client.getLinkGoogleMobile();
        logo = client.getLogo();
    }
}
