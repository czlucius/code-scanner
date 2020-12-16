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
