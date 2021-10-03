package com.wilderman.reviewer.task.VisitorsReportIngestion.Accuro;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.wilderman.reviewer.task.VisitorsReportIngestion.ICsvBean;
import com.wilderman.reviewer.task.VisitorsReportIngestion.PatientVisitRecord;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Arrays;

@Getter
@Setter
public class AccuroCsvBean implements ICsvBean {

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Birthdate")
    @CsvDate(value = "MM/dd/yyyy")
    private Date birthdate;

    @CsvBindByName(column = "Cell Phone")
    private String phone;

    private String fullname;

    @CsvBindByName(column = "Appointment Date")
    @CsvDate(value = "MM/dd/yyyy")
    private Date visitedOn;


    public PatientVisitRecord toPatientVisitRecord() {
        PatientVisitRecord patientVisitRecord = new PatientVisitRecord();
        patientVisitRecord.setBirthdate(getBirthdate());
        patientVisitRecord.setPhone(getPhone());
        patientVisitRecord.setVisitedOn(getVisitedOn());
        patientVisitRecord.setFname(firstName);
        patientVisitRecord.setLname(lastName);

        return patientVisitRecord;
    }

    public static String sanitizePhone(String phone) {
        String phoneSanitized = phone.replaceAll("[\\D]*", "");
        if (phoneSanitized.length() == 10) {
            phoneSanitized = "+1" + phoneSanitized;
        }
        return phoneSanitized;
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

    public String getFullname() {
        return String.format("%s %s", firstName, lastName);
    }
}
