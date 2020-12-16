package com.czlucius.scan.callbacks;

import androidx.camera.core.UseCase;

@FunctionalInterface
public interface UseCaseCreator {
    UseCase[] create();
}

