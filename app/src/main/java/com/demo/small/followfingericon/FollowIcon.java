package com.demo.small.followfingericon;

import android.graphics.Canvas;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

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

    private float mPreLength = 0;
    public FollowIcon(Drawable drawable, int delay){
        this.drawable = drawable;
        this.delay = delay;
        halfHeight = drawable.getIntrinsicHeight();
        halfWidth = drawable.getIntrinsicWidth();
        rect = new Rect(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }


    public void updataView(float fx, float fy, PathMeasure pathMeasure, float currLength, float preLength,  double Velocity){

        if (delay == 0) {
            x = (int) fx;
            y = (int) fy;
            rect.set(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight);
            return;
        }

        if (Velocity >= 800) {
            if (preLength - mPreLength >= delay) {
                //匀速
                mPreLength += currLength - preLength;
            } else {
                mPreLength += 5;
            }

        } else {
            mPreLength += 20;
        }

        mPreLength = Math.max(0, Math.min(currLength, mPreLength));
        Log.d("updataView", currLength+"  " + preLength +"  "+ mPreLength +"  "+Velocity);

        pathMeasure.getPosTan(mPreLength, pox, tan);
        x = (int) pox[0];
        y = (int) pox[1];
        rect.set(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight);
       /* if (delay == 0) {
            x = (int) fx;
            y = (int) fy;
        } else {
            float dis = pathMeasure.getLength() - preLength;
            mPreLength = mPreLength + dis;
            mPreLength = Math.max(0, mPreLength + dis + Math.min(0, accumulation * 10 - delay));
            pathMeasure.getPosTan(mPreLength, pox, tan);
            x = (int) pox[0];
            y = (int) pox[1];
            Log.d("MyThread", dis+"  " + preLength +"  "+ mPreLength);
        }
        rect.set(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight);*/
    }


    public void onDraw(Canvas canvas){
        drawable.setBounds(rect);
        drawable.draw(canvas);
    }

}
