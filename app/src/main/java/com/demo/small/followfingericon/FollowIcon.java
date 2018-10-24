package com.demo.small.followfingericon;

import android.graphics.Canvas;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class FollowIcon {

    private long delay;
    private float[] pox = new float[2];
    private float[] tan = new float[2];
    private int x ;
    private int y;
    private Drawable drawable;
    private Rect rect;

    private int halfHeight;
    private int halfWidth;

    public FollowIcon(Drawable drawable, int delay){
        this.drawable = drawable;
        this.delay = delay;
        halfHeight = drawable.getIntrinsicHeight();
        halfWidth = drawable.getIntrinsicWidth();
        rect = new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }


    public void updataView(float fx, float fy, PathMeasure pathMeasure){
        if (delay == 0) {
            x = (int) fx;
            y = (int) fy;
        } else {
            float lenght = pathMeasure.getLength() - delay;
            if (lenght < 0 ){
                lenght = 0;
            }
            pathMeasure.getPosTan(lenght, pox, tan);
            x = (int) pox[0];
            y = (int) pox[1];

        }
        rect.set(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight);
    }

    public void onDraw(Canvas canvas){
        drawable.setBounds(rect);
        drawable.draw(canvas);
    }

}
