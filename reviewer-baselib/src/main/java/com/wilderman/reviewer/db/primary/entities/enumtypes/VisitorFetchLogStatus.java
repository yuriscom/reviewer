package com.wilderman.reviewer.db.primary.entities.enumtypes;

public enum VisitorFetchLogStatus {
    PENDING, PROCESSED, FAILED;

    public static VisitorFetchLogStatus[] uniqueStatuses() {
        VisitorFetchLogStatus[] valsAr = {VisitorFetchLogStatus.PENDING, VisitorFetchLogStatus.PROCESSED};
        return valsAr;
    }
}
