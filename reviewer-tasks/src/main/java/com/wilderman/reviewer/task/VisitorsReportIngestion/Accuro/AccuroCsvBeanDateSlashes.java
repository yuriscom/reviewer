package com.wilderman.reviewer.task.VisitorsReportIngestion.Accuro;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class AccuroCsvBeanDateSlashes extends AccuroCsvBeanBase {

    @CsvBindByName(column = "Birthdate")
    @CsvDate(value = "MM/dd/yyyy")
    private Date birthdate;

    @CsvBindByName(column = "Appointment Date")
    @CsvDate(value = "MM/dd/yyyy")
    private Date visitedOn;

}
