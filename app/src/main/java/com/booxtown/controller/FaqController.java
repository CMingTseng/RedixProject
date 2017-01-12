package com.booxtown.controller;

import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.model.Faq;
import com.booxtown.model.FaqResult;
import com.booxtown.model.Result;

import java.util.ArrayList;
import java.util.Hashtable;

import retrofit2.Call;

/**
 * Created by Administrator on 12/01/2017.
 */

public class FaqController {
    private ServiceInterface service;
    public FaqController(){
        service = ServiceGenerator.GetInstance();
    }

    public ArrayList<Faq> getAllFAQ(){

        Call<FaqResult> invite = service.getAllFAQ();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            FaqResult str = invite.execute().body();
            if (str.getCode()==200){
                return str.getFaq();
            }
        } catch (Exception ex) {
        }
        return null;
    }
}
