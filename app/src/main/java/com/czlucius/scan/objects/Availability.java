package com.czlucius.scan.objects;

public enum Availability {
    ON, OFF, UNAVAILABLE;


    public boolean toBoolean() {
        return this == ON;
    }
}
