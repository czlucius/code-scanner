package com.czlucius.scan.callbacks;

@FunctionalInterface
public interface CameraFailureCallback {
    void run(Exception e);
}
