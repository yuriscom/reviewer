package com.wilderman.reviewer.task.VisitorsReportIngestion;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Report", strict = false)
public class Report {

    @Path("Tablix1")
    @ElementList(name = "CustomerID_Collection")
    private List<CustomerElement> Customers;

    public Report() {
    }

    public List<CustomerElement> getCustomers() {
        return Customers;
    }
}
