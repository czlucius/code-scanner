/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2020 Lucius Chee Zihan
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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class OverlayView2 extends View {

    private final Paint paint = new Paint();
    private int width = 400;
    private int height = 400;
    private int alpha = 40;
    private final RectArea rectArea = new RectArea(width, height);

    public OverlayView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.BLACK);
        paint.setAlpha(alpha);
    }


    public void setWidth(int width) {
        this.width = width;
        rectArea.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
        rectArea.height = height;
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
        // Draw around the rect.
        canvas.drawRect(0, 0, width, height / 2 - rectArea.height, paint);
        canvas.drawRect(0, rectArea.height + (height / 2), width, height, paint);
        canvas.drawRect(0, height / 2 - rectArea.height, width / 2 - rectArea.width, height / 2 + rectArea.height, paint);
        canvas.drawRect(width / 2 + rectArea.width, height / 2 - rectArea.height, width, height / 2 + rectArea.height, paint);
        canvas.drawLine(width / 2 - rectArea.width,
                height / 2 - rectArea.height,
                width / 2 + rectArea.width, height / 2 + rectArea.height, paint
        );
    }



    public static class RectArea {
        private int width;
        private int height;

        public RectArea(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }


}
