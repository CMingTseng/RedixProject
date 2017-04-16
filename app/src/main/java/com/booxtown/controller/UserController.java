package com.booxtown.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.api.ServiceInterface;
import com.booxtown.model.DayUsed;
import com.booxtown.model.UserResult;

import java.util.Hashtable;
import java.util.List;

import com.booxtown.model.Result;
import com.booxtown.model.User;

import retrofit2.Call;

/**
 * Created by duong on 9/5/2016.
 */
public class UserController {
    private ServiceInterface service;
    private Context ct;
    public UserController(Context ct){
        service = ServiceGenerator.GetInstance();
        this.ct= ct;
    }
    public UserController(){
        service = ServiceGenerator.GetInstance();

    }

    public String checkLoginValidate(String username, String password, String device_type,String session_id){
        try {
            Hashtable obj = new Hashtable();
            obj.put("username", username);
            obj.put("password", password);
            obj.put("device_type", device_type);
            obj.put("session_id", session_id);
            Call<Result> callService = service.login(obj);
            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                Result result = callService.execute().body();
                if (result.getCode() == 200) {
                    SharedPreferences pref = ct.getSharedPreferences("MyPref", ct.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("firstname", result.getDescription());
                    editor.putString("session_id", result.getSession_id());
                    editor.putString("active","active");
                    editor.commit();
                    return result.getCode()+"";
                }else{
                    return result.getCode()+"";
                }
            } catch (Exception ex) {
                String ss = ex.toString();
            }
            return "701";
        }catch (Exception exx){

            return "701";
        }
    }


    public boolean signUp(User user){
        Call<Result> callService = service.addUser(user);
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


    public boolean forgetPassword(String email) {
        Hashtable obj = new Hashtable();
        obj.put("email",email);
        Call<Result> status = service.forgotpassword(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = status.execute().body();
            if (str.getCode() == 200) {
                return true;
            }
        } catch (Exception ex) {

        }
        return false;
    }


    public boolean logout(String session_id){
        Hashtable obj = new Hashtable();
        obj.put("session_id",session_id);
        Call<Result> response = service.logout(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result rs = response.execute().body();
            if (rs.getCode() == 200){
                return true;
            }
        }catch (Exception e){
        }

        return false;
    }

    public List<User> getprofile(String session_id){
        Call<UserResult> profile = service.getprofile(session_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            UserResult str = profile.execute().body();
            if (str.getCode() == 200){
                return str.getUser();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public String getUserID(String session_id){
        Call<Result> profile = service.getuserID(session_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = profile.execute().body();
            if (str.getCode() == 200){
                return str.getSession_id();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public boolean changePassword(String session_id,String pwd_old,String pwd_new){
        Hashtable obj = new Hashtable();
        obj.put("session_id",session_id);
        obj.put("pwd_old",pwd_old);
        obj.put("pwd_new",pwd_new);
        Call<Result> profile = service.changepassword(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = profile.execute().body();
            if (str.getCode() == 200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public boolean activateUser(String session_id){
        Hashtable obj = new Hashtable();
        obj.put("session_id",session_id);

        Call<Result> profile = service.activateUser(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = profile.execute().body();
            if (str.getCode() == 200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public boolean buyInapp(String session_id){
        Hashtable obj = new Hashtable();
        obj.put("session_id",session_id);

        Call<Result> profile = service.buyInapp(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = profile.execute().body();
            if (str.getCode() == 200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public List<User> getByUserId(int user_id){
        Call<UserResult> profile = service.getProfileByUserId(user_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            UserResult str = profile.execute().body();
            if (str.getCode() == 200){
                return str.getUser();
            }
        } catch (Exception ex) {
            String exe = ex.getMessage();
        }
        return null;
    }

    public boolean updateprofile(String first_name,String last_name,String email,String phone,String birthday,String photo,String session_id){
        Hashtable obj = new Hashtable();
        obj.put("first_name",first_name);
        obj.put("last_name",last_name);
        obj.put("email",email);
        obj.put("phone",phone);
        obj.put("birthday",birthday);
        obj.put("photo",photo);
        obj.put("session_id",session_id);
        Call<Result> profile = service.updateprofile(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = profile.execute().body();
            if (str.getCode() == 200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public DayUsed GetDayUsed(String session_id){
        Call<DayUsed> profile = service.GetDayUsed(session_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            DayUsed str = profile.execute().body();
            if (str.getCode().equals("200")){
                return str;
            }
        } catch (Exception ex) {
            String exe = ex.getMessage();
        }
        return null;
    }

    public boolean EmailToActive(String email){
        Hashtable obj = new Hashtable();
        obj.put("email",email);

        Call<Result> active = service.EmailToActive(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = active.execute().body();
            if (str.getCode() == 200){
                return true;
            }

        } catch (Exception ex) {
        }
        return false;
    }

    public boolean deactivateUser(String session_id){
        Hashtable obj = new Hashtable();
        obj.put("session_id",session_id);

        Call<Result> active = service.deactivateUser(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = active.execute().body();
            if (str.getCode() == 200){
                return true;
            }

        } catch (Exception ex) {
        }
        return false;
    }
}
