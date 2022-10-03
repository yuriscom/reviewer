package com.wilderman.reviewer.task.VisitorsReportIngestion.Imaging;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.wilderman.reviewer.task.VisitorsReportIngestion.ICsvBean;
import com.wilderman.reviewer.task.VisitorsReportIngestion.PatientVisitRecord;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class ImagingCsvBean implements ICsvBean {

//    @CsvBindByName(column = "OHIP")
//    private String ohip;

//    @CsvBindByName(column = "Birthdate")
//    @CsvDate(value = "MM/dd/yyyy")
//    private Date birthdate;

    @CsvBindByName(column = "Cell Phone")
    private String phone;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Client Name")
    private String fullname;

    @CsvBindByName(column = "Service Date")
    @CsvDate(value = "dd-MMM-yyyy HH:mm")
    private Date lastVisit;

//    @CsvBindByName(column = "Email Address")
//    private String email;

    public PatientVisitRecord toPatientVisitRecord() {
        PatientVisitRecord patientVisitRecord = new PatientVisitRecord();
        patientVisitRecord.setPhone(getPhone());

//        try {
//            Date date = new SimpleDateFormat("dd-MMM-yyyy HH:mm").parse(lastVisit);
//            patientVisitRecord.setVisitedOn(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        patientVisitRecord.setVisitedOn(lastVisit);
        patientVisitRecord.setFname(sanitizeName(firstName));
        patientVisitRecord.setLname(sanitizeName(lastName));

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
