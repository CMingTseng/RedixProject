package redix.booxtown.controller;

import android.app.Activity;
import android.os.StrictMode;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.api.ServiceInterface;
import redix.booxtown.model.Book;
import redix.booxtown.model.Notification;
import redix.booxtown.model.NotificationResult;
import redix.booxtown.model.Result;
import retrofit2.Call;

/**
 * Created by Administrator on 26/09/2016.
 */
public class NotificationController {
    private ServiceInterface service;
    Boolean success;
    Activity mActivity;
    List<Notification> list = new ArrayList<>();
    public NotificationController(){
        service = ServiceGenerator.GetInstance();
    }

    public NotificationController(Activity mActivity) {
        this.mActivity = mActivity;
    }

     public boolean sendNotification(List<Hashtable> notifications){
        Hashtable obj = new Hashtable();
        obj.put("notification_list",notifications);
        try {
            Call<Result> status = service.sendNotification(obj);
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = status.execute().body();
            if (str.getCode() == 200){
                return true;
            }
            String s = "";
        } catch (Exception ex) {
            String s = ex.getMessage();
        }
        return false;
    }

    public List<Notification> getALllNotificationTop(String session_id, int top, int from){
        Call<NotificationResult> getTopNotification = service.getTopNotification(session_id, top, from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            NotificationResult str = getTopNotification.execute().body();
            if (str.getCode()==200){
                return str.getNotifi();
            }
        } catch (Exception ex) {
            return list;
        }
        return null;
    }

    public Boolean changeStatusNotification(String session_id, int thread_id){
        Hashtable obj= new Hashtable();
        obj.put("session_id",session_id);
        obj.put("notifi_id",thread_id);
        Call<Result> changeStatusNotifi = service.changeStatusNotification(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = changeStatusNotifi.execute().body();
            if (str.getCode() == 200){
                success = true;
            }
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }

    public Boolean changeStatusUnreadNotification(String session_id, int thread_id){
        Hashtable obj= new Hashtable();
        obj.put("session_id",session_id);
        obj.put("topic_id",thread_id);


        Call<Result> changeStatusNotifi = service.changeStatusUnreadNotification(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = changeStatusNotifi.execute().body();
            if (str.getCode() == 200){
                success = true;
            }
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }
}
