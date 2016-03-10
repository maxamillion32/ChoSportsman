package com.chokavo.chosportsman.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.p_v.flexiblecalendar.view.CircularEventCellView;

/**
 * Created by repitch on 10.03.16.
 */
public class ExampleCellView extends CircularEventCellView {

    public ExampleCellView(Context context) {
        super(context);
    }

    public ExampleCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExampleCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height =  (3*width)/5;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}