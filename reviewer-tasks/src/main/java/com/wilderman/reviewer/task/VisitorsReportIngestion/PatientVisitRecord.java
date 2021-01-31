package com.wilderman.reviewer.task.VisitorsReportIngestion;

import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;

import java.util.Date;

public class PatientVisitRecord {
    private String fname;
    private String lname;
    private String phone;
    private Date visitedOn;
    private String ohip;
    private Date birthdate;
    private String email;


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}