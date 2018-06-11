package com.ryan.a0peninganimation;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


public class OpeningAnimView extends View {

    private PositionPoint currentPoint;
    private Paint mPaint;
    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private boolean dirPort = true;

    public OpeningAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        //float density = dm.density;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        dirPort = true;
        if(SCREEN_WIDTH > SCREEN_HEIGHT){
            dirPort = false;
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentPoint == null) {
            currentPoint = new PositionPoint(0, SCREEN_HEIGHT/2);
            drawRect(canvas);
            startPropertyAni();
        } else {
            drawRect(canvas);
        }
    }

    private void drawRect(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        if(dirPort){
            canvas.drawRect(0, 0, SCREEN_WIDTH, y, mPaint);
            canvas.drawRect(0, SCREEN_HEIGHT - y, SCREEN_WIDTH, SCREEN_HEIGHT, mPaint);
        }
        else{
            canvas.drawRect(0, 0, x, SCREEN_HEIGHT, mPaint);
            canvas.drawRect(SCREEN_WIDTH - x, 0, SCREEN_WIDTH, SCREEN_HEIGHT, mPaint);
        }
    }

    private void startPropertyAni() {
        ValueAnimator animator = ValueAnimator.ofObject(
                new PositionEvaluator(),
                new PositionPoint(SCREEN_WIDTH/2, SCREEN_HEIGHT/2),
                new PositionPoint(0, 0));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (PositionPoint) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(500).start();
    }
}

class PositionEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        PositionPoint startPoint = (PositionPoint) startValue;
        PositionPoint endPoint = (PositionPoint) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        PositionPoint point = new PositionPoint(x, y);
        return point;
    }
}

class PositionPoint {
    private float x;
    private float y;

    public PositionPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}