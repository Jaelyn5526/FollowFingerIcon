package com.demo.small.followfingericon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import java.util.ArrayList;

public class FollowView extends View {

    private Path path;
    private PathMeasure pathMeasure;
    private Float fingerX, fingerY;
    private Paint paint;

    private ArrayList<FollowIcon> icons = new ArrayList<>();

    private VelocityTracker mVelocityTracker;

    private float preLength = 0l;
    private float curLength = 0l;

    public FollowView(Context context) {
        super(context);
        init();
    }

    public FollowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FollowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    int dis = 50;
    private void init(){
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        FollowIcon icon0 = new FollowIcon(getResources().getDrawable(R.mipmap.icon4), 0);
        FollowIcon icon1 = new FollowIcon(getResources().getDrawable(R.mipmap.icon1), 200 + dis);
        FollowIcon icon2 = new FollowIcon(getResources().getDrawable(R.mipmap.icon2), 200 + dis * 2);
        FollowIcon icon3 = new FollowIcon(getResources().getDrawable(R.mipmap.icon3), 200 + dis * 3);
        FollowIcon icon4 = new FollowIcon(getResources().getDrawable(R.mipmap.icon4), 200 + dis* 4);
        FollowIcon icon5 = new FollowIcon(getResources().getDrawable(R.mipmap.icon5), 200 + dis* 5);
        FollowIcon icon6 = new FollowIcon(getResources().getDrawable(R.mipmap.icon1), 200 + dis* 6);
        FollowIcon icon7 = new FollowIcon(getResources().getDrawable(R.mipmap.icon4), 200 + dis* 7);
        FollowIcon icon8 = new FollowIcon(getResources().getDrawable(R.mipmap.icon2), 200 + dis* 8);
        FollowIcon icon9 = new FollowIcon(getResources().getDrawable(R.mipmap.icon4), 200 + dis* 9);
        icons.add(icon0);
        icons.add(icon1);
        icons.add(icon2);
        icons.add(icon3);
        icons.add(icon4);
        icons.add(icon5);
        icons.add(icon6);
        icons.add(icon7);
        icons.add(icon8);
        icons.add(icon9);
    }

    @Override
    protected void onAttachedToWindow() {
        mVelocityTracker = VelocityTracker.obtain();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        Log.d("onTouch", event.getAction()+"");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                touchUp(event);
                break;
        }
        return true;
    }

    private void touchDown(MotionEvent event){
        fingerX = event.getX();
        fingerY = event.getY();
        path.reset();
        path.moveTo(fingerX, fingerY);
        pathMeasure = new PathMeasure(path, false);
        curLength = pathMeasure.getLength();
        updataIcon();
    }

    private void touchMove(MotionEvent event){
        mPointerId = event.getPointerId(0);
        float x = (fingerX + event.getX()) / 2;
        float y = (fingerY + event.getY()) / 2;
        path.quadTo(fingerX, fingerY, x, y);
        pathMeasure.setPath(path, false);
        curLength = pathMeasure.getLength();
        countVelocity(event);
        fingerX = event.getX();
        fingerY = event.getY();
        updataIcon();
        preLength = pathMeasure.getLength();
    }

    private void touchUp(MotionEvent event){
        mVelocityTracker.clear();
        fingerX = event.getX();
        fingerY = event.getY();
        path.lineTo(fingerX, fingerY);
        pathMeasure.setPath(path, false);
        curLength = pathMeasure.getLength();
        updataIcon();
    }

    public void updataIcon(){

        for (int i = 0; i < icons.size(); i++) {
            icons.get(i).updataView(fingerX, fingerY, pathMeasure, curLength, preLength, fingerVelocity);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawPath(path, paint);
        for (int i = icons.size() - 1; i >= 0; i--) {
            icons.get(i).onDraw(canvas);
        }
    }

    int mPointerId = 0;
    float xVelocity;
    float yVelocity;
    double fingerVelocity;
    float[] pos;
    float[] tan;
    public void countVelocity(MotionEvent event){
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = mVelocityTracker.getXVelocity(event.getPointerId(mPointerId));
        float yVelocity = mVelocityTracker.getYVelocity(event.getPointerId(mPointerId));
        fingerVelocity = Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);
//        Log.d("velocity", xVelocity+" "+yVelocity + "  " + fingerVelocity);
    }
}
