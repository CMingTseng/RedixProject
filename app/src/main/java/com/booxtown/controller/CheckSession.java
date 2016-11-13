package com.booxtown.controller;

import android.app.Activity;
import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.model.Result;

import java.util.Hashtable;

import retrofit2.Call;

/**
 * Created by thuyetpham94 on 12/11/2016.
 */
public class CheckSession {
    private ServiceInterface service;
    Boolean success;
    Activity mActivity;

    public CheckSession(){
        service = ServiceGenerator.GetInstance();
    }
    public boolean checkSession_id(String Session_id){
        Hashtable obj = new Hashtable();
        if(Session_id==null){
            Session_id="";
        }
        obj.put("session_id",Session_id);
        Call<Result> status = service.checkSession(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = status.execute().body();
            if (str.getCode() == 200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }
}
