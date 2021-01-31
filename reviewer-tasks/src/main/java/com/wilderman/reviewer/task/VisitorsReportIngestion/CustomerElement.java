package com.wilderman.reviewer.task.VisitorsReportIngestion;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Root(name = "CustomerID", strict = false)
public class CustomerElement {

    @ElementList(name = "ColumnGroup_Collection")
    private List<ColumnGroup> cgList;

    public List<ColumnGroup> getCgList() {
        return cgList;
    }

    public PatientVisitRecord toPatientVisitRecord() throws ParseException {
        PatientVisitRecord patientVisitRecord = new PatientVisitRecord();
        for (ColumnGroup cg : getCgList()) {
            switch (cg.getKey()) {
                case "Customer First Name":
                    patientVisitRecord.setFname(cg.getValue());
                    break;
                case "Customer Last Name":
                    patientVisitRecord.setLname(cg.getValue());
                    break;
                case "Phone":
                    patientVisitRecord.setPhone(cg.getValue());
                    break;
                case "Last Visit Date":
                    Date dt = new SimpleDateFormat("MM/dd/yyyy").parse(cg.getValue());
                    patientVisitRecord.setVisitedOn(dt);
                    break;
            }
        }

        return patientVisitRecord;
    }

}






