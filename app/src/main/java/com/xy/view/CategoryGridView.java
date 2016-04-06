package com.xy.view;

import android.widget.GridView;

/**
 * Created by sara on 15-8-30.
 */
public class CategoryGridView extends GridView {
    public CategoryGridView(android.content.Context context,
                      android.util.AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
