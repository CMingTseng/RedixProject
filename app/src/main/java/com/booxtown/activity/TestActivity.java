package com.booxtown.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.booxtown.custom.SeekBarWithTwoThumb;

import com.booxtown.R;

/**
 * Created by thuyetpham94 on 26/08/2016.
 */
public class TestActivity extends AppCompatActivity implements SeekBarWithTwoThumb.SeekBarChangeListener {

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