package redix.booxtown.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import redix.booxtown.R;
import redix.booxtown.custom.SeekBarWithTwoThumb.SeekBarChangeListener;

/**
 * Created by thuyetpham94 on 26/08/2016.
 */
public class TestActivity extends AppCompatActivity implements SeekBarChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request for window feature action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter_sort);
    }

    @Override
    public void SeekBarValueChanged(int Thumb1Value, int Thumb2Value) {

    }
}