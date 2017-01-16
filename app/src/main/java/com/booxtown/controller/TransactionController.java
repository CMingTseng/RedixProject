package com.booxtown.controller;

import android.os.StrictMode;
import android.widget.Toast;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;

import java.util.Hashtable;

import com.booxtown.model.DayUsed;
import com.booxtown.model.Result;
import com.booxtown.model.Transaction;
import com.booxtown.model.TransactionResult;
import com.booxtown.model.User;

import retrofit2.Call;

/**
 * Created by Administrator on 28/09/2016.
 */
public class TransactionController {
    private ServiceInterface service;
    Boolean success;
    String result;
    public TransactionController(){
        service = ServiceGenerator.GetInstance();
    }

    public String transactionInsert(String buyUserID, String sellUserID, String buyBookID, String sellBookID,String action, String session_id){
        UserController userController= new UserController();
        DayUsed used= userController.GetDayUsed(session_id);
        if(Integer.parseInt(used.getDayUsed())<=14 || used.getIs_active().equals("1")||action.equals("free")){
            Hashtable obj= new Hashtable();
            obj.put("session_id",session_id);
            obj.put("buyUserID",buyUserID);
            obj.put("sellUserID",sellUserID);
            obj.put("buyBookID",buyBookID);
            obj.put("sellBookID",sellBookID);
            obj.put("action",action);
            Call<Result> transactionInsert = service.transactionInsert(obj);
            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                Result str = transactionInsert.execute().body();
                if (str.getCode() == 200){
                    result= str.getSession_id();
                }

            } catch (Exception ex) {
                result = null;
            }
        }else{
            result="isTrial";
        }

        return result;
    }
    public boolean CheckExitsTransaction(String user_seller_id, String book_seller_id,String session_id){
        Hashtable obj= new Hashtable();
        obj.put("session_id",session_id);
        obj.put("user_seller_id",user_seller_id);
        obj.put("book_seller_id",book_seller_id);

        Call<Result> check = service.CheckExitsTransaction(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = check.execute().body();
            if (str.getCode() == 200){
               return true;
            }

        } catch (Exception ex) {
            return  false;
        }
        return false;
    }

    public boolean SetDone(String session_id, String trans_id){
        Hashtable obj= new Hashtable();
        obj.put("session_id",session_id);
        obj.put("trans_id",trans_id);
        Call<Result> check = service.SetDone(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = check.execute().body();
            if (str.getCode() == 200){
                return true;
            }

        } catch (Exception ex) {
            return  false;
        }
        return false;
    }

    public String transactionUpdateStatus( String session_id, String transaction_id, String status_id, String book_seller_id){
        Hashtable obj= new Hashtable();
        obj.put("session_id",session_id);
        obj.put("trans_id",transaction_id);
        obj.put("status_id",status_id);
        if(book_seller_id.equals("")){
            obj.put("book_seller_id", "0");
        }
        else {
            obj.put("book_seller_id", book_seller_id);
        }
        Call<Result> transactionInsert = service.transactionUpdateStatus(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = transactionInsert.execute().body();
            if (str.getCode() == 200){
                result= str.getSession_id();
            }
        } catch (Exception ex) {
            result = null;
        }
        return result;
    }

    public Transaction getTransactionId(String tranhisid){
        Call<TransactionResult> getalls = service.getBookTransaction(tranhisid);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            TransactionResult str = getalls.execute().body();
            if (str.getCode() == 200){
                return str.getTransaction();
            }
        } catch (Exception ex) {
            String exxx= ex.getMessage();
        }
        return null;
    }

    public Boolean updateRating(int trans_id,float user_promp,float user_cour,float user_quality,int status_id){
        Hashtable obj = new Hashtable();
        obj.put("trans_id",trans_id);
        obj.put("user_promp",user_promp);
        obj.put("user_cour",user_cour);
        obj.put("user_quality",user_quality);
        obj.put("status_id",status_id);
        Call<Result> rating = service.updateRating(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = rating.execute().body();
            if (str.getCode() == 200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }
}
