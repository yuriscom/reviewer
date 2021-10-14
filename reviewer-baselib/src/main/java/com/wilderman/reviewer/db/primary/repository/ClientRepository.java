package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends ExtendedRepository<Client, Long>, CustomRepository<Client> {
    Client findFirstByUname(String uname);
}
