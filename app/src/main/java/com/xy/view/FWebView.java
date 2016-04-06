package com.xy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

import xy.com.utils.ToastUtil;

/**
 * Created by FQ.CHINA on 2016/3/3.
 */
public class FWebView extends WebView {
    final int RIGHT = 0;
    final int LEFT = 1;
    private Context mContext;
    private GestureDetector gestureDetector;
    public FWebView(Context context) {
        super(context);
        this.mContext = context;
    }

    public FWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 0) {
                        doResult(RIGHT);
                    } else if (x < 0) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ToastUtil.showToast(mContext, "event");
        gestureDetector =  new GestureDetector(mContext,onGestureListener);
        return gestureDetector.onTouchEvent(event);
    }

    public void doResult(int action) {

        switch (action) {
            case RIGHT:
                ToastUtil.showToast(mContext, "go right");
                break;

            case LEFT:
                ToastUtil.showToast(mContext, "go left");
                break;

        }
    }
}
