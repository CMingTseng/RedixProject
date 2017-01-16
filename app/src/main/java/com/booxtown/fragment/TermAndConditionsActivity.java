package com.booxtown.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.R;
import com.booxtown.controller.AboutController;
import com.booxtown.model.About;

import java.util.ArrayList;

/**
 * Created by Administrator on 17/01/2017.
 */

public class TermAndConditionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_conditions);
        // Inflate the layout for this fragment

        TextView txt_title=(TextView) findViewById(R.id.txt_title);
        txt_title.setText("Terms and conditions");

        ImageView imageView_back=(ImageView) findViewById(R.id.img_menu);
        Bitmap btm = BitmapFactory.decodeResource(getResources(),R.drawable.btn_sign_in_back);
        imageView_back.setImageBitmap(btm);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        TextView term_condition=(TextView) findViewById(R.id.term_condition);
        AboutAsync aboutAsync= new AboutAsync(TermAndConditionsActivity.this,term_condition);
        aboutAsync.execute();

    }

    class AboutAsync extends AsyncTask<Void, Void, ArrayList<About>> {
        Context context;
        TextView abouts;
        public AboutAsync(Context context, TextView abouts) {
            this.context = context;
            this.abouts= abouts;
        }

        ProgressDialog dialog;

        @Override
        protected ArrayList<About> doInBackground(Void... pra) {
            AboutController userController = new AboutController();
            return  userController.getBooxTownInfor(1);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(TermAndConditionsActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<About> about) {
            try {
                if(about.get(0).getContent()!=null) {
                    abouts.setText(Html.fromHtml(about.get(0).getContent()));
                }else {
                    abouts.setText("");
                }
            } catch (Exception e) {
                //Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
