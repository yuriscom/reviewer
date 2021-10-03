package com.wilderman.reviewer.task.VisitorsReportIngestion.Cosmetic;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.wilderman.reviewer.task.VisitorsReportIngestion.ICsvBean;
import com.wilderman.reviewer.task.VisitorsReportIngestion.PatientVisitRecord;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;

@Getter
@Setter
public class CosmeticCsvBean implements ICsvBean {

    @CsvBindByName(column = "OHIP")
    private String ohip;

    @CsvBindByName(column = "Birthdate")
    @CsvDate(value = "MM/dd/yyyy")
    private Date birthdate;

    @CsvBindByName(column = "Phone")
    private String phone;

    @CsvBindByName(column = "Client Name")
    private String fullname;

    @CsvBindByName(column = "Appointment Date")
    @CsvDate(value = "MM/dd/yyyy")
    private Date visitedOn;

    @CsvBindByName(column = "Last Visit")
    @CsvDate(value = "MM/dd/yyyy")
    private Date lastVisit;

    @CsvBindByName(column = "Email Address")
    private String email;

    public PatientVisitRecord toPatientVisitRecord() {
        PatientVisitRecord patientVisitRecord = new PatientVisitRecord();
        patientVisitRecord.setBirthdate(getBirthdate());
        patientVisitRecord.setEmail(getEmail());
        patientVisitRecord.setOhip(getOhip());
        patientVisitRecord.setPhone(getPhone());
        patientVisitRecord.setVisitedOn(getVisitedOn() != null ? getVisitedOn() : getLastVisit());
        if (fullname != null) {
            String[] nameParts = fullname.split("\\s+");
            if (nameParts.length > 0) {
                patientVisitRecord.setFname(nameParts[0]);
                if (nameParts.length > 1) {
                    String[] lnameParts = Arrays.copyOfRange(nameParts, 1, nameParts.length);
                    patientVisitRecord.setLname(String.join(" ", lnameParts));
                }
            }
        }


        return patientVisitRecord;
    }

    public static String sanitizePhone(String phone) {
        String phoneSanitized = phone.replaceAll("[\\D]*", "");
        if (phoneSanitized.length() == 10) {
            phoneSanitized = "+1" + phoneSanitized;
        }
        return phoneSanitized;
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
        this.phone = sanitizePhone(phone);
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }
}
