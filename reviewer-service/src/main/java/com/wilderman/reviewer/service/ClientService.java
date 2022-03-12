package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

//    @Value("${spring.profiles.active:accuro}")
//    private String activeProfile;

    private Map<String, Client> clients = new HashMap<>();


    @PostConstruct
    public void init() {
//        client = clientRepository.findFirstByUname(activeProfile);
        clients = clientRepository.findAll().stream().collect(Collectors.toMap(x -> x.getUname(), x -> x));
    }

    public Client getClient(Long id) {
        return clientRepository.getOne(id);
    }

    public Page<Client> getClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    public Client getClientByUname(String uname) {
        return clients.containsKey(uname) ? clients.get(uname) :
                clientRepository.findFirstByUname(uname);
    }
}
