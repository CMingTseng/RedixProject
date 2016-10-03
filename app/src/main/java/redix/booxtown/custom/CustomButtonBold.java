package redix.booxtown.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by thuyetpham94 on 03/10/2016.
 */
public class CustomButtonBold extends Button {
    public CustomButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Futura_Medium_BT.ttf"));
    }
}
