package redix.booxtown.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by thuyetpham94 on 03/10/2016.
 */
public class CustomCheckBox extends CheckBox {
    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Futura_Light_BT.ttf"));
    }
}
