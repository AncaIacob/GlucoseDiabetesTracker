package com.utcn.glucosediabetestracker;

public class InsertFood {
    String value_glucose_before;
    String value_glucose_after;
    String value_food;


    public InsertFood(String value_glucose_before, String value_glucose_after, String value_food) {
        this.value_glucose_before = value_glucose_before;
        this.value_glucose_after = value_glucose_after;
        this.value_food = value_food;

    }

    public String getValue_glucose_before() {
        return value_glucose_before;
    }

    public String getValue_glucose_after() {
        return value_glucose_after;
    }

    public String getValue_food() {
        return value_food;
    }
}
