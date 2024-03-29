package com.wilderman.reviewer.task.VisitorsReportIngestion.Accuro;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.wilderman.reviewer.task.VisitorsReportIngestion.ICsvBean;
import com.wilderman.reviewer.task.VisitorsReportIngestion.PatientVisitRecord;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public abstract class AccuroCsvBeanBase implements ICsvBean {

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Cell Phone")
    private String phone;

    private String fullname;


    protected abstract Date getVisitedOn();
    protected abstract Date getBirthdate();


    public PatientVisitRecord toPatientVisitRecord() {
        PatientVisitRecord patientVisitRecord = new PatientVisitRecord();
        patientVisitRecord.setBirthdate(getBirthdate());
        patientVisitRecord.setPhone(getPhone());
        patientVisitRecord.setVisitedOn(getVisitedOn());
        patientVisitRecord.setFname(sanitizeName(firstName));
        patientVisitRecord.setLname(sanitizeName(lastName));

        return patientVisitRecord;
    }

    public static String sanitizePhone(String phone) {
        String phoneSanitized = phone.replaceAll("[\\D]*", "");
        if (phoneSanitized.length() == 10) {
            phoneSanitized = "+1" + phoneSanitized;
        }
        return phoneSanitized;
    }


    public void setPhone(String phone) {
        this.phone = sanitizePhone(phone);
    }

    public String getFullname() {
        return String.format("%s %s", firstName, lastName);
    }
}
