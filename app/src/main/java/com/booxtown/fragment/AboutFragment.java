package com.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.booxtown.R;
import com.booxtown.activity.SignIn_Activity;
import com.booxtown.controller.AboutController;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.controller.UserController;
import com.booxtown.model.About;
import com.booxtown.model.Notification;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        TextView abouts= (TextView)view.findViewById(R.id.abouts);
        AboutAsync aboutAsync= new AboutAsync(getActivity(),abouts);
        aboutAsync.execute();
        return view;
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
            return  userController.getBooxTownInfor(0);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<About> about) {
            try {
                abouts.setText(Html.fromHtml(about.get(0).getContent()));
            } catch (Exception e) {
                //Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
