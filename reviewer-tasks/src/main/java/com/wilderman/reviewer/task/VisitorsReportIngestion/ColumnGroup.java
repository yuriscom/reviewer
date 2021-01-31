package com.wilderman.reviewer.task.VisitorsReportIngestion;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name= "ColumnGroup", strict = false)
public class ColumnGroup {

    @Attribute(name = "Textbox4")
    private String key;

    @Attribute(name = "Textbox6", required = false)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
