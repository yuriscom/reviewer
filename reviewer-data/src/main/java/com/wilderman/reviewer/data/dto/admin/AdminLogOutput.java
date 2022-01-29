package com.wilderman.reviewer.data.dto.admin;

import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitorFetchLogStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AdminLogOutput {
    private Long id;
    private String s3key;
    private VisitorFetchLogStatus status;
    private LocalDateTime uploadedAt;

    public AdminLogOutput(VisitorFetchLog log) {
        id = log.getId();
        s3key = log.getS3key();
        status = log.getStatus();
        uploadedAt = log.getEventTime();
    }
}
