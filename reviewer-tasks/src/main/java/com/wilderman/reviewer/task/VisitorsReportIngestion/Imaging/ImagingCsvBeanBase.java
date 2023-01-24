package com.wilderman.reviewer.task.VisitorsReportIngestion.Imaging;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.wilderman.reviewer.task.VisitorsReportIngestion.ICsvBean;
import com.wilderman.reviewer.task.VisitorsReportIngestion.PatientVisitRecord;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Getter
@Setter
public abstract class ImagingCsvBeanBase implements ICsvBean {


    @CsvBindByName(column = "Cell Phone")
    private String phone;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Client Name")
    private String fullname;

    protected abstract Date getLastVisit();

    public PatientVisitRecord toPatientVisitRecord() {
        PatientVisitRecord patientVisitRecord = new PatientVisitRecord();
        patientVisitRecord.setPhone(getPhone());
        patientVisitRecord.setFname(sanitizeName(firstName));
        patientVisitRecord.setLname(sanitizeName(lastName));
        patientVisitRecord.setVisitedOn(getLastVisit());

        return patientVisitRecord;
    }

    public void setPhone(String phone) {
        this.phone = sanitizePhone(phone);
    }

    public static String sanitizePhone(String phone) {
        String phoneSanitized = phone.replaceAll("[\\D]*", "");
        if (phoneSanitized.length() == 10) {
            phoneSanitized = "+1" + phoneSanitized;
        }
        return phoneSanitized;
    }

}
