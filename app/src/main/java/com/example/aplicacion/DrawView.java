package com.example.aplicacion;

import android.content.Context;
import android.graphics.Bitmap; 
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF; 
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {

    private static class Stroke {
        Path path;  
        int color;  

        Stroke(Path path, int color) {
            this.path = path;
            this.color = color;
        }
    }

    private List<Stroke> strokes; // guardar trazos
    private Paint drawingPaint;   
    private Path currentPath;     
    private int currentDrawingColor; 

    private Bitmap loadedImage; 
    private RectF imageRect;    

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        strokes = new ArrayList<>(); 

        drawingPaint = new Paint();
        drawingPaint.setStyle(Paint.Style.STROKE); 
        drawingPaint.setStrokeWidth(10); 
        drawingPaint.setAntiAlias(true); 

        currentDrawingColor = Color.BLACK; 
    }

    public void setPaintColor(int color) {
        currentDrawingColor = color;
    }

    public void clearDrawing() {
        strokes.clear();     
        currentPath = null;  
        invalidate();        
    }

    public void setLoadedImage(Bitmap bitmap) {
        this.loadedImage = bitmap;
        if (loadedImage != null) {
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            if (viewWidth == 0 || viewHeight == 0) {
                imageRect = null; 
            } else {
                calculateImageRect(viewWidth, viewHeight);
            }
        } else {
            imageRect = null;
        }
        invalidate(); 
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (loadedImage != null) {
            calculateImageRect(w, h);
        }
    }

    private void calculateImageRect(int viewWidth, int viewHeight) {
        if (loadedImage == null || viewWidth == 0 || viewHeight == 0) {
            imageRect = null;
            return;
        }

        float imageAspect = (float) loadedImage.getWidth() / loadedImage.getHeight();
        float viewAspect = (float) viewWidth / viewHeight;

        float drawWidth;
        float drawHeight;

        if (imageAspect > viewAspect) {
            drawWidth = viewWidth;
            drawHeight = viewWidth / imageAspect;
        } else {
            drawHeight = viewHeight;
            drawWidth = viewHeight * imageAspect;
        }

        float left = (viewWidth - drawWidth) / 2;
        float top = (viewHeight - drawHeight) / 2;
        imageRect = new RectF(left, top, left + drawWidth, top + drawHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (loadedImage != null && imageRect != null) {
            canvas.drawBitmap(loadedImage, null, imageRect, null);
        }

        for (Stroke stroke : strokes) {
            drawingPaint.setColor(stroke.color); 
            canvas.drawPath(stroke.path, drawingPaint); 
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath = new Path(); 
                currentPath.moveTo(x, y); 
                strokes.add(new Stroke(currentPath, currentDrawingColor));
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentPath != null) { 
                    currentPath.lineTo(x, y); 
                }
                break;
            case MotionEvent.ACTION_UP:
                currentPath = null; 
                break;
        }

        invalidate(); 
        return true; 
    }
}
