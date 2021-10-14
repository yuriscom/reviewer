package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Value("${spring.profiles.active:accuro}")
    private String activeProfile;

    private Client client;

    @PostConstruct
    public void init() {
        client = clientRepository.findFirstByUname(activeProfile);
    }

    public Client getClient() {
        return client;
    }
}
