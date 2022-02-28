/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2021 Lucius Chee Zihan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.czlucius.scan.objects;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import java.util.List;

public abstract class BaseAnalyser implements ImageAnalysis.Analyzer {

    private final SuccessCallback successCallback;
    private final FailureHandler failureHandler;

    protected BaseAnalyser(SuccessCallback successCallback, FailureHandler failureHandler) {
        this.successCallback = successCallback;
        this.failureHandler = failureHandler;
    }


    public abstract void analyze(@NonNull ImageProxy image, SuccessCallback successCallback, FailureHandler failureHandler);

    @Override
    public final void analyze(@NonNull ImageProxy imageProxy) {
        analyze(imageProxy, successCallback, failureHandler);
        imageProxy.close();
    }


    public interface SuccessCallback {
        void scannedBarcodes(List<Code> barcodes);
    }

    public interface FailureHandler {
        void handleException(Exception e);
    }

}
