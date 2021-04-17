package com.utcn.glucosediabetestracker;

public class DisplayData {
    private String value_glucose;
    private String value_data;
    private String value_time;
    private String value_type;

    public DisplayData(){}
    public DisplayData(String value_glucose, String value_data, String value_time, String value_type)
    {
        this.value_glucose = value_glucose;
        this.value_data = value_data;
        this.value_time = value_time;
        this.value_type = value_type;
    }

    public String getValue_glucose() {
        return value_glucose;
    }

    public void setValue_glucose(String value_glucose) {
        this.value_glucose = value_glucose;
    }

    public String getValue_data() {
        return value_data;
    }

    public void setValue_data(String value_data) {
        this.value_data = value_data;
    }

    public String getValue_time() {
        return value_time;
    }

    public void setValue_time(String value_time) {
        this.value_time = value_time;
    }

    public String getValue_type() {
        return value_type;
    }

    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }
}
