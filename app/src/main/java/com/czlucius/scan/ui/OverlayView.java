/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2022 czlucius
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

package com.czlucius.scan.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class OverlayView extends View {

    private final Paint paint = new Paint();
    private int alpha = 85;
    private final RectArea rectArea = new RectArea();

    public OverlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.BLACK);
        paint.setAlpha(alpha);
    }


    public void setAlpha(int alpha) {
        this.alpha = alpha;
        paint.setAlpha(alpha);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw 4 rects
        float width = getWidth();
        float height = getHeight();


        if (getWidth() < getHeight()) {
            // Portrait
            rectArea.width = (getRight() - getLeft()) / 3;
            rectArea.height = (getBottom() - getTop()) / 5;
        } else {
            // Landscape
            rectArea.width = (getRight() - getLeft()) / 5;
            rectArea.height = (getBottom() - getTop()) / 3;

        }
        Log.i("OverlayView", "onDraw: " + rectArea.width + "?" + rectArea.height);


        // Draw around the rect.
        canvas.drawRect(0, 0, width, height / 2 - rectArea.height, paint);
        canvas.drawRect(0, rectArea.height + (height / 2), width, height, paint);
        canvas.drawRect(0, height / 2 - rectArea.height, width / 2 - rectArea.width, height / 2 + rectArea.height, paint);
        canvas.drawRect(width / 2 + rectArea.width, height / 2 - rectArea.height, width, height / 2 + rectArea.height, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public static class RectArea {
        private int width;
        private int height;

        public RectArea(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public RectArea() {}
    }


}
