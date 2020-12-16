package com.czlucius.scan.callbacks;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}
