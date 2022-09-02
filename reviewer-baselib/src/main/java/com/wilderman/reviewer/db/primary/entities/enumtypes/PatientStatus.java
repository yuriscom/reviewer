package com.wilderman.reviewer.db.primary.entities.enumtypes;

import java.util.Arrays;
import java.util.List;

public enum PatientStatus {
    NEW, SEEN, RATED, LEFT_BAD_REVIEW, ACKNOWLEDGED,
    NONE, SENT, DISABLED;

    public static List<PatientStatus> processed() {
        PatientStatus[] brandStatuses = {
                PatientStatus.RATED,
                PatientStatus.SENT,
                PatientStatus.SEEN,
                PatientStatus.DISABLED,
                PatientStatus.LEFT_BAD_REVIEW
        };
        return Arrays.asList(brandStatuses);
    }

    public static List<PatientStatus> sendable() {
        PatientStatus[] brandStatuses = {
                PatientStatus.SENT,
                PatientStatus.SEEN,
                PatientStatus.NEW,
                PatientStatus.NONE
        };
        return Arrays.asList(brandStatuses);
    }

    public static List<PatientStatus> unprocessed() {
        PatientStatus[] brandStatuses = {
                PatientStatus.NEW,
                PatientStatus.NONE
        };
        return Arrays.asList(brandStatuses);
    }
}
