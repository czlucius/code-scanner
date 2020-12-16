package com.czlucius.scan.callbacks;

@FunctionalInterface
public interface Observer<T> {
    void valueChanged(T newValue);
}