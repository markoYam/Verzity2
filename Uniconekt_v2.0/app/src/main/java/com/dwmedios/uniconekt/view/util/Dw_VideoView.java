package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class Dw_VideoView extends VideoView {
    public Dw_VideoView(Context context) {
        super(context);
    }

    public Dw_VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Dw_VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
