package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.db.primary.repository.VisitRepository;
import com.wilderman.reviewer.db.primary.repository.VisitorFetchLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VisitorService {
    @Autowired
    VisitorFetchLogRepository fetchLogRepository;

    @Autowired
    VisitRepository visitRepository;

    public Page<VisitorFetchLog> getLogs(Client client, Pageable pageable) {
        String s3keyPrefix = String.format("visitors/%s", client.getUname());
        return fetchLogRepository.findByS3keyStartsWith(s3keyPrefix, pageable);
    }

    public VisitorFetchLog getLogById(Long id) {
        return fetchLogRepository.findById(id).orElse(null);
    }
}
