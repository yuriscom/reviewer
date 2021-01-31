package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitorFetchLogStatus;

import java.time.LocalDateTime;
import java.util.Optional;

//@Repository
public interface VisitorFetchLogRepository extends ExtendedRepository<VisitorFetchLog, Long>, CustomRepository<VisitorFetchLog> {

//    @Query("select l from DecipherFetchLog l where l.status='PENDING' and l.numRecords>0 order by l.createdOn desc")
//    Optional<DecipherFetchLog> getFirstPending();

    Optional<VisitorFetchLog> findTopByStatusAndNumRecordsGreaterThanOrderByCreatedAtAsc(VisitorFetchLogStatus status, Integer numRecords);

    Optional<VisitorFetchLog> getTopByStatusAndCreatedAtAfterOrderByCreatedAtAsc(VisitorFetchLogStatus status, LocalDateTime lastCreatedAt);


}
