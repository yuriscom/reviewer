package com.wilderman.reviewer.task.VisitorsReportIngestion;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class PatientVisitRecord {
    private String fname;
    private String lname;
    private String phone;
    private Date visitedOn;
    private String ohip;
    private Date birthdate;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientVisitRecord that = (PatientVisitRecord) o;
        return Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}