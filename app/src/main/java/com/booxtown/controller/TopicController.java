package com.booxtown.controller;

import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.model.Topic;
import com.booxtown.model.TopicResult;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.model.Result;

import retrofit2.Call;

/**
 * Created by thuyetpham94 on 16/09/2016.
 */
public class TopicController {
    private ServiceInterface service;
    Boolean success;
    List<Topic> list = new ArrayList<>();
    public TopicController(){
        service = ServiceGenerator.GetInstance();
    }

    public List<Topic> getalltopic(){
        Call<TopicResult> getall = service.topic_getall();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            TopicResult str = getall.execute().body();
            if (str.getCode()==200){
                return str.getTopic();
            }
        } catch (Exception ex) {
            return list;
        }
        return null;
    }


    public List<Topic> gettopicbyid(String id){
        Call<TopicResult> getTopTopic = service.gettopicbyid(id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            TopicResult str = getTopTopic.execute().body();
            if (str.getCode()==200){
                return str.getTopic();
            }
        } catch (Exception ex) {
            return list;
        }
        return null;
    }

    public List<Topic> getALllTopicTop(String session_id, int top, int from){
        Call<TopicResult> getTopTopic = service.topic_gettop(session_id, top, from);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            TopicResult str = getTopTopic.execute().body();
            if (str.getCode()==200){
                return str.getTopic();
            }
        } catch (Exception ex) {
            return list;
        }
        return null;
    }

    public Boolean changeStatusTopic(String session_id, int thread_id){
        Hashtable obj= new Hashtable();
        obj.put("session_id",session_id);
        obj.put("content","read");
        obj.put("topic_id",thread_id);
        Call<Result> changeStatusTopic = service.changeStatusTopic(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = changeStatusTopic.execute().body();
            if (str.getCode() == 200){
                success = true;
            }
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }

    public Boolean changeStatusUnreadTopic(String session_id, int thread_id){
        Hashtable obj= new Hashtable();
        obj.put("session_id",session_id);
        obj.put("content","read");
        obj.put("topic_id",thread_id);
        Call<Result> changeStatusTopic = service.changeStatusUnreadTopic(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = changeStatusTopic.execute().body();
            if (str.getCode() == 200){
                success = true;
            }
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }
}
