package com.wilderman.reviewer.db.primary.entities.enumtypes;

import java.util.Arrays;
import java.util.List;

public enum PatientStatus {
    NEW, RATED, LEFT_BAD_REVIEW, ACKNOWLEDGED,
    NONE, SENT, CLICKED, REVIEWED,
    DISABLED;

    public static List<PatientStatus> processed() {
        PatientStatus[] brandStatuses = {
                PatientStatus.RATED,
                PatientStatus.REVIEWED,
                PatientStatus.SENT,
                PatientStatus.DISABLED,
                PatientStatus.CLICKED,
                PatientStatus.LEFT_BAD_REVIEW
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
