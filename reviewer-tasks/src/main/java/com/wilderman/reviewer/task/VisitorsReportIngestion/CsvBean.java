package com.wilderman.reviewer.task.VisitorsReportIngestion;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.sql.Date;
import java.text.ParseException;

public class CsvBean {

    @CsvBindByName(column = "OHIP")
    private String ohip;

    @CsvBindByName(column = "Birthdate")
    @CsvDate(value = "MM/dd/yyyy")
    private Date birthdate;

    @CsvBindByName(column = "Phone")
    private String phone;

    @CsvBindByName(column = "Appointment Date")
    @CsvDate(value = "MM/dd/yyyy")
    private Date visitedOn;

    @CsvBindByName(column = "Email Address")
    private String email;

    public PatientVisitRecord toPatientVisitRecord() {
        PatientVisitRecord patientVisitRecord = new PatientVisitRecord();
        patientVisitRecord.setBirthdate(getBirthdate());
        patientVisitRecord.setEmail(getEmail());
        patientVisitRecord.setOhip(getOhip());
        patientVisitRecord.setPhone(getPhone());
        patientVisitRecord.setVisitedOn(getVisitedOn());


        return patientVisitRecord;
    }

    public String getOhip() {
        return ohip;
    }

    public void setOhip(String ohip) {
        this.ohip = ohip;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getVisitedOn() {
        return visitedOn;
    }

    public void setVisitedOn(Date visitedOn) {
        this.visitedOn = visitedOn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
