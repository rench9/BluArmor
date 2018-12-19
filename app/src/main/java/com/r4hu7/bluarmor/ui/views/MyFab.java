package com.r4hu7.bluarmor.ui.views;

import android.content.Context;
import android.graphics.*;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import com.r4hu7.bluarmor.R;

public class MyFab extends FloatingActionButton {

    private float MIN_RADIUS_VALUE;
    private float MAX_RADIUS_VALUE;
    private float mRadius;
    private boolean initialized = false;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Rect r = new Rect();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MAX_RADIUS_VALUE = getMeasuredWidth();
        MIN_RADIUS_VALUE = getMeasuredWidth() / 2;
    }

    private void init() {
        if (initialized)
            return;
        initialized = true;
        setWillNotDraw(true);

        getMeasuredContentRect(r);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4f);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        paint.setColor(getResources().getColor(R.color.colorAccent));

        mRadius = MIN_RADIUS_VALUE;
    }

    public MyFab(Context context) {
        super(context);
    }

    public MyFab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void reDraw(float level) {
        mRadius = MAX_RADIUS_VALUE * level;
        paint.setAlpha((int) (level * 255));
        invalidate();
    }

    public void stopAnimation() {
    }

    public void startAnimation() {
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        canvas.drawCircle(r.centerX(), r.centerY(), mRadius, paint);
    }


}