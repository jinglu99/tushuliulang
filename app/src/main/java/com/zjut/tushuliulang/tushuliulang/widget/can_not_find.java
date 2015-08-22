package com.zjut.tushuliulang.tushuliulang.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.zjut.tushuliulang.tushuliulang.R;

/**
 * Created by Ben on 2015/8/21.
 */
public class can_not_find extends LinearLayout {
    public can_not_find(Context context) {
        this(context, null);
    }

    public can_not_find(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public can_not_find(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.can_not_find,this,true);
    }
}
