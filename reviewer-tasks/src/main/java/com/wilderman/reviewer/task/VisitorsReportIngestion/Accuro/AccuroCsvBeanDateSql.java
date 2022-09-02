package com.wilderman.reviewer.task.VisitorsReportIngestion.Accuro;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class AccuroCsvBeanDateSql extends AccuroCsvBeanBase {

    @CsvBindByName(column = "Birthdate")
    @CsvDate(value = "yyyy-MM-dd")
    private Date birthdate;

    @CsvBindByName(column = "Appointment Date")
    @CsvDate(value = "yyyy-MM-dd")
    private Date visitedOn;

}
