package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitorFetchLogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//@Repository
public interface VisitorFetchLogRepository extends ExtendedRepository<VisitorFetchLog, Long>, CustomRepository<VisitorFetchLog> {

//    @Query("select l from DecipherFetchLog l where l.status='PENDING' and l.numRecords>0 order by l.createdOn desc")
//    Optional<DecipherFetchLog> getFirstPending();

    Optional<VisitorFetchLog> findTopByStatusAndNumRecordsGreaterThanOrderByCreatedAtAsc(VisitorFetchLogStatus status, Integer numRecords);

    @Query("select l from VisitorFetchLog l where l.status=:status and l.s3key like :s3keyPrefix% and l.numRecords>0 order by l.id asc")
    Optional<VisitorFetchLog> findNextToProcess(@Param("status") VisitorFetchLogStatus status,
                                                @Param("s3keyPrefix") String s3keyPrefix);

    @Query("select l from VisitorFetchLog l where l.status=:status and l.numRecords>0 order by l.id asc")
    List<VisitorFetchLog> findNextToProcessAllClients(@Param("status") VisitorFetchLogStatus status);

    Optional<VisitorFetchLog> getTopByStatusAndCreatedAtAfterOrderByCreatedAtAsc(VisitorFetchLogStatus status, LocalDateTime lastCreatedAt);

    Page<VisitorFetchLog> findByS3keyStartsWith(String prefix, Pageable pageable);

}
