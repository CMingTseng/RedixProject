package redix.booxtown.controller;

import android.os.StrictMode;

import java.util.Hashtable;
import java.util.List;

import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.api.ServiceInterface;
import redix.booxtown.model.Comment;
import redix.booxtown.model.CommentResult;
import redix.booxtown.model.Result;
import retrofit2.Call;

/**
 * Created by thuyetpham94 on 17/09/2016.
 */
public class CommentController {
    private ServiceInterface service;
    public CommentController(){
        service = ServiceGenerator.GetInstance();
    }

    public List<Comment> getallcomment(String thread_id){
        Call<CommentResult> getall = service.getAllComment(thread_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            CommentResult str = getall.execute().body();
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
