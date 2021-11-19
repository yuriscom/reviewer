package com.wilderman.reviewer.task.VisitorsReportIngestion;

public interface ICsvBean {
    String getPhone();
    PatientVisitRecord toPatientVisitRecord();

    default String sanitizeName(String name) {
        return name.replaceAll("[^\\w|\\s]*", "")
                .replaceAll("(\\r\\n|\\n)", "")
                .trim();
    }
}
