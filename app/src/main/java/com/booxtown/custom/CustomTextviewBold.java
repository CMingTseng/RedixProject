package com.booxtown.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by thuyetpham94 on 03/10/2016.
 */
public class CustomTextviewBold extends TextView {
    public CustomTextviewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Futura_Medium_BT.ttf"));
    }
}
