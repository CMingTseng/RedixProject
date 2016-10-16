package com.booxtown.controller;

import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.model.DashBoard;
import com.booxtown.model.DashBoardResult;

import java.util.List;

import retrofit2.Call;

/**
 * Created by thuyetpham94 on 01/10/2016.
 */
public class DashBoardController {
    private ServiceInterface service;
    public DashBoardController(){
        service = ServiceGenerator.GetInstance();
    }

    public List<DashBoard> getDashBoard(String session_id, int top, int from){
        Call<DashBoardResult> getall = service.getDashBoard(session_id,top,from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            DashBoardResult str = getall.execute().body();
            if (str.getCode()==200){
                return str.getTransaction();
            }
        } catch (Exception ex) {
            String sss= ex.getMessage();
        }
        return null;
    }
}
