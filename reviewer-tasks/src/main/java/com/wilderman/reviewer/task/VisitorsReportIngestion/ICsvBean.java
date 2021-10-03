package com.wilderman.reviewer.task.VisitorsReportIngestion;

public interface ICsvBean {
    String getPhone();
    PatientVisitRecord toPatientVisitRecord();
}
