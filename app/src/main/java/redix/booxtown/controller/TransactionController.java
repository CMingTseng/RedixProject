package redix.booxtown.controller;

import android.os.StrictMode;

import java.util.Hashtable;
import java.util.List;

import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.api.ServiceInterface;
import redix.booxtown.model.Book;
import redix.booxtown.model.Result;
import redix.booxtown.model.Transaction;
import redix.booxtown.model.TransactionResult;
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
        return result;
    }

    public String transactionUpdateStatus( String session_id, String transaction_id, String status_id, String book_seller_id){
        Hashtable obj= new Hashtable();
        obj.put("session_id",session_id);
        obj.put("trans_id",transaction_id);
        obj.put("status_id",status_id);
        obj.put("book_seller_id",book_seller_id);
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

    public Boolean updateRating(int trans_id,float user_promp,float user_cour,float user_quality){
        Hashtable obj = new Hashtable();
        obj.put("trans_id",trans_id);
        obj.put("user_promp",user_promp);
        obj.put("user_cour",user_cour);
        obj.put("user_quality",user_quality);
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
