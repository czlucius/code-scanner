package com.czlucius.scan.objects;

public class Availability {
    private static class _ON extends Availability{}
    private static class _OFF extends Availability{}
    private static class _UNAVAILABLE extends Availability{}


    public static Availability ON = new _ON();
    public static Availability OFF = new _OFF();
    public static Availability UNAVAILABLE = new _UNAVAILABLE();


    public boolean toBoolean() {
        if (this instanceof _ON) {
            return true;
        } else {
            return false;
        }
    }
}
