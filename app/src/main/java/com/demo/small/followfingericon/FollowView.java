package com.demo.small.followfingericon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class FollowView extends View {

    private Path path;
    private PathMeasure pathMeasure;
    private Float fingerX, fingerY;
    private Paint paint;

    private ArrayList<FollowIcon> icons = new ArrayList<>();

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


    private void init(){
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        FollowIcon icon0 = new FollowIcon(getResources().getDrawable(R.mipmap.ic_launcher), 0);
        FollowIcon icon1 = new FollowIcon(getResources().getDrawable(R.mipmap.ic_launcher), 100);
        FollowIcon icon2 = new FollowIcon(getResources().getDrawable(R.mipmap.ic_launcher), 150);
        FollowIcon icon3 = new FollowIcon(getResources().getDrawable(R.mipmap.ic_launcher), 200);
        icons.add(icon0);
        icons.add(icon1);
        icons.add(icon2);
        icons.add(icon3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
        updataIcon();
    }

    private void touchMove(MotionEvent event){
        float x = (fingerX + event.getX()) / 2;
        float y = (fingerY + event.getY()) / 2;
        path.quadTo(fingerX, fingerY, x, y);
        pathMeasure.setPath(path, false);
        fingerX = event.getX();
        fingerY = event.getY();
        updataIcon();
    }

    private void touchUp(MotionEvent event){
        fingerX = event.getX();
        fingerY = event.getY();
        path.lineTo(fingerX, fingerY);
        pathMeasure.setPath(path, false);
        updataIcon();
    }

    public void updataIcon(){
        for (int i = 0; i < icons.size(); i++) {
            icons.get(i).updataView(fingerX, fingerY, pathMeasure);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
        for (int i = icons.size() - 1; i >= 0; i--) {
            icons.get(i).onDraw(canvas);
        }
    }

}
