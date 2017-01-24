package com.booxtown.controller;

import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.model.Wishboard;
import com.booxtown.model.WishboardResult;

import java.util.Hashtable;
import java.util.List;

import com.booxtown.model.CommentBook;
import com.booxtown.model.CommentBookResult;
import com.booxtown.model.Result;

import retrofit2.Call;

/**
 * Created by thuyetpham94 on 27/09/2016.
 */
public class WishboardController {
    private ServiceInterface service;
    public WishboardController(){
        service = ServiceGenerator.GetInstance();
    }

    public List<Wishboard> getWishboardByTop(int top, int from, String session_id){
        Call<WishboardResult> callService = service.getWishboardByTop(top,from,session_id);
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

    public List<Wishboard> getWishboardByID(String post_id){
        Call<WishboardResult> callService = service.getWishboardByID(post_id);
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

    public List<CommentBook> getCommnetWishboard(String post_id,int top, int from ){
        Call<CommentBookResult> getall = service.getCommentWishboard(post_id, top,from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            CommentBookResult str = getall.execute().body();
            if (str.getCode() == 200){
                return str.getComment();
            }
        } catch (Exception ex) {
            String sss= ex.getMessage();
        }
        return null;
    }

}
