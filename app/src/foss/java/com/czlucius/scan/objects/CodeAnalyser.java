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

import static android.graphics.ImageFormat.YUV_420_888;
import static android.graphics.ImageFormat.YUV_422_888;
import static android.graphics.ImageFormat.YUV_444_888;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageProxy;

import com.czlucius.scan.Utils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CodeAnalyser extends BaseAnalyser{
    private final List<Integer> yuvFormats = new ArrayList<>();
    private final MultiFormatReader reader = new MultiFormatReader();

    /* Class Initialiser */
    {
        yuvFormats.add(YUV_420_888);
        // TODO for M & above. If the min API is lowered, wrap this in if block.
        yuvFormats.add(YUV_422_888);
        yuvFormats.add(YUV_444_888);
    }

    public CodeAnalyser(SuccessCallback successCallback, FailureHandler failureHandler) {
        super(successCallback, failureHandler);
    }

    @Override
    public void analyze(@NonNull ImageProxy image, SuccessCallback successCallback, FailureHandler failureHandler) {
        if (!(yuvFormats.contains(image.getFormat()))) {
            Log.e("CodeAnalyzer", "Expected YUV, but image format is " + image.getFormat());
            return;
        }

        ByteBuffer cameraBuffer = image.getPlanes()[0].getBuffer();
        byte[] data = Utils.byteBufferToArray(cameraBuffer);

        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                data,
                image.getWidth(),
                image.getHeight(),
                0,
                0,
                image.getWidth(),
                image.getHeight(),
                false
        );
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            // Whenever reader fails to detect a QR code in image
            // it throws NotFoundException
            Result result = reader.decode(binaryBitmap);
            successCallback.scannedBarcodes(); // TODO we need to add translation code here.
        } catch (NotFoundException e) {
            e.printStackTrace();
            failureHandler.handleException(e);
        }
    }


}
