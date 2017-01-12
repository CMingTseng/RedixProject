package com.booxtown.controller;

import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.model.About;
import com.booxtown.model.AboutResult;
import com.booxtown.model.Faq;
import com.booxtown.model.FaqResult;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Administrator on 12/01/2017.
 */

public class AboutController {
    private ServiceInterface service;
    public AboutController(){
        service = ServiceGenerator.GetInstance();
    }

    public ArrayList<About> getBooxTownInfor(int status){

        Call<AboutResult> invite = service.getBooxTownInfor(status);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            AboutResult str = invite.execute().body();
            if (str.getCode()==200){
                return str.getInfor();
            }
        } catch (Exception ex) {
        }
        return null;
    }
}
