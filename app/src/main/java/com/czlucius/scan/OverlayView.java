//package com.czlucius.scan;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//
//import java.util.ArrayList;
//
//
////TODO display overlay reticle over QR code area.
//public class OverlayView extends View {
//
//    private final ArrayList<CodeArea> codeAreaList = new ArrayList<>();
//
////    private Bitmap[] points = new Bitmap[4];
//    private Paint paint = new Paint();
//
//    public OverlayView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//
////        points[0] = BitmapFactory.decodeResource(getResources(), R.drawable.corners_lt);
////        points[1] = BitmapFactory.decodeResource(getResources(), R.drawable.corners_rt);
////        points[2] = BitmapFactory.decodeResource(getResources(), R.drawable.corners_lb);
////        points[3] = BitmapFactory.decodeResource(getResources(), R.drawable.corners_rb);
//
//    }
//
//
//    public void renderQRArea(Rect area, Canvas canvas) {
//        CodeArea codeArea = new CodeArea(area);
//        codeAreaList.add(codeArea);
//
//        canvas.drawBitmap(points[0], area.left, area.top, paint);
//        canvas.drawBitmap(points[1], area.right, area.top, paint);
//        canvas.drawBitmap(points[2], area.left, area.bottom, paint);
//        canvas.drawBitmap(points[3], area.right, area.bottom, paint);
//    }
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        Rect r;
//        for (CodeArea codeArea : codeAreaList) {
//            r = codeArea.area;
//            if (isPointInRect(event.getX(), event.getY(), r)) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//        throw new RuntimeException("An exception has happened.");
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        renderQRArea(new Rect(6,9,99,10), canvas);
//    }
//
//    private boolean isPointInRect(float x, float y, Rect r) {
//        if (x < r.left || x > r.right || y < r.top || y > r.bottom) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//
//
//
//
//    public static class CodeArea {
//        private Rect area;
//        private Point[] overlayImagePoints;
//        private CodeArea(Rect area) {
//            this.area = area;
//        }
//    }
//}
