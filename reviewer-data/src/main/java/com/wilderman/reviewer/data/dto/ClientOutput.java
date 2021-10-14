package com.wilderman.reviewer.data.dto;

import com.wilderman.reviewer.db.primary.entities.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientOutput {
    private Long id;
    private String uname;
    private String name;
    private String website;
    private String linkGoogle;
    private String logo;

    public ClientOutput(Client client) {
        id = client.getId();
        uname = client.getUname();
        name = client.getName();
        website = client.getWebsite();
        linkGoogle = client.getLinkGoogle();
        logo = client.getLogo();
    }
}
