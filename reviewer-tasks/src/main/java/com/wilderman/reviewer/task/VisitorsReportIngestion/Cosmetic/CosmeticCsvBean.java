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

//    @CsvBindByName(column = "OHIP")
//    private String ohip;

//    @CsvBindByName(column = "Birthdate")
//    @CsvDate(value = "MM/dd/yyyy")
//    private Date birthdate;

    @CsvBindByName(column = "Phone")
    private String phone;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Client Name")
    private String fullname;

    @CsvBindByName(column = "Date Visited")
    @CsvDate(value = "MM/dd/yyyy")
    private Date lastVisit;


    public PatientVisitRecord toPatientVisitRecord() {
        PatientVisitRecord patientVisitRecord = new PatientVisitRecord();
        patientVisitRecord.setPhone(getPhone());
        patientVisitRecord.setVisitedOn(getLastVisit());

        patientVisitRecord.setFname(sanitizeName(firstName));
        patientVisitRecord.setLname(sanitizeName(lastName));
        /*
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
         */


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
