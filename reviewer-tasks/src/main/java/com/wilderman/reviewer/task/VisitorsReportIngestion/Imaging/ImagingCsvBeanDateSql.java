package com.wilderman.reviewer.task.VisitorsReportIngestion.Imaging;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.wilderman.reviewer.task.VisitorsReportIngestion.Accuro.AccuroCsvBeanBase;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
public class ImagingCsvBeanDateSql extends ImagingCsvBeanBase {

    @CsvBindByName(column = "Service Date")
    @CsvDate(value = "MM/dd/yyyy")
    private Date lastVisit;

}
