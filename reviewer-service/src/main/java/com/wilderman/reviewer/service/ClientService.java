package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Client getClient(Long id) {
        return clientRepository.getOne(id);
    }

    public Page<Client> getClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Client getClientByUname(String uname) {
        return clientRepository.findFirstByUname(uname);
    }
}
