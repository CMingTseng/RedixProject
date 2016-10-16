package com.booxtown.custom;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.booxtown.R;

/**
 * Created by Administrator on 26/08/2016.
 */
public class CustomSearch {

    private EditText editSeach;
    private ImageView btn_filter;

    public CustomSearch(View view, final Context ct){

        editSeach=(EditText) view.findViewById(R.id.editSearch);
        btn_filter=(ImageView) view.findViewById(R.id.btn_filter_explore);

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ct, "hihi",Toast.LENGTH_LONG).show();
            }
        });
    }

}
