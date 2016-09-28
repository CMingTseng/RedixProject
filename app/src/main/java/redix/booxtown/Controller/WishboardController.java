package redix.booxtown.controller;

import android.os.StrictMode;

import java.util.Hashtable;
import java.util.List;

import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.api.ServiceInterface;
import redix.booxtown.model.Result;
import redix.booxtown.model.Wishboard;
import redix.booxtown.model.WishboardResult;
import retrofit2.Call;

/**
 * Created by thuyetpham94 on 27/09/2016.
 */
public class WishboardController {
    private ServiceInterface service;
    public WishboardController(){
        service = ServiceGenerator.GetInstance();
    }

    public List<Wishboard> getAllWishboard(int top,int from,String session_id){
        Call<WishboardResult> callService = service.getAllWishboard(top,from,session_id);
        try{
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            WishboardResult result = callService.execute().body();
            if (result.getCode()==200){
                return result.getList();
            }
        }
        catch (Exception ex){
        }
        return null;
    }
    public boolean insertWishboard(String title,String author,String comment,String session_id){
        Hashtable obj = new Hashtable();
        obj.put("session_id",session_id);
        obj.put("title",title);
        obj.put("author",author);
        obj.put("comment",comment);
        Call<Result> callService = service.insertWishboard(obj);
        try{
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result result = callService.execute().body();
            if (result.getCode()==200){
                return true;
            }
        }
        catch (Exception ex){

        }
        return false;
    }
}
