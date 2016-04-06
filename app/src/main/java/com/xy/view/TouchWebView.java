package com.xy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by FQ.CHINA on 2015/9/14.
 */
public class TouchWebView extends WebView{
    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TouchWebView(Context context) {
        super(context);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }

}
