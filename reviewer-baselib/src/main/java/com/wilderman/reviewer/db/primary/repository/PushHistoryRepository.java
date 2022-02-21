package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.PushHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface PushHistoryRepository extends ExtendedRepository<PushHistory, Long>, CustomRepository<PushHistory> {

}
