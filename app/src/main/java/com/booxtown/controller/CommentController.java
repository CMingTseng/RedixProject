package com.booxtown.controller;

import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.model.Comment;
import com.booxtown.model.CommentResult;

import java.util.Hashtable;
import java.util.List;

import com.booxtown.model.Result;
import retrofit2.Call;

/**
 * Created by thuyetpham94 on 17/09/2016.
 */
public class CommentController {
    private ServiceInterface service;
    public CommentController(){
        service = ServiceGenerator.GetInstance();
    }

    public List<Comment> getTopComment(String thread_id, int top, int from){
        Call<CommentResult> getopcomment= service.getTopCommentThread(thread_id,top,from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            CommentResult str = getopcomment.execute().body();
            if (str.getCode()==200){
                return str.getComment();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public List<Comment> getcomment_top(String thread_id,int top,int from){
        Call<CommentResult> gettop = service.getTopCommentThread(thread_id,top,from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            CommentResult str = gettop.execute().body();
            if (str.getCode()==200){
                return str.getComment();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public Boolean insertComment(String session_id,String content,String thread_id, String book_id, String post_id){
        Hashtable obj = new Hashtable();
        obj.put("session_id",session_id);
        obj.put("content",content);
        obj.put("thread_id",thread_id);
        obj.put("book_id",book_id);
        obj.put("post_id",post_id);
        Call<Result> insertComment = service.inser_comment_thread(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = insertComment.execute().body();
            if (str.getCode()==200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }
}
