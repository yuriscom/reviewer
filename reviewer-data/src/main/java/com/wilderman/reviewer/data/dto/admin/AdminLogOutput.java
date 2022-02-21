package com.wilderman.reviewer.data.dto.admin;

import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitorFetchLogStatus;
import com.wilderman.reviewer.utils.DateTimeHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
public class AdminLogOutput {
    private Long id;
    private String s3key;
    private VisitorFetchLogStatus status;
    private LocalDateTime uploadedAt;
    private Integer numNotificationsSent;
    private String lastPushTime;

    public AdminLogOutput(VisitorFetchLog log) {
        id = log.getId();
        s3key = log.getS3key();
        status = log.getStatus();
        uploadedAt = log.getEventTime();
        numNotificationsSent = log.getAttempts();
        lastPushTime = log.getPushHistoryList().stream()
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .map(history -> DateTimeHelper.convertLocalDateTimeToString(history.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"))
                .orElse(null);

        String s = "";
    }
}