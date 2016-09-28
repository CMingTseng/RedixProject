package redix.booxtown.controller;

import android.os.StrictMode;

import java.util.Hashtable;
import java.util.List;

import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.api.ServiceInterface;
import redix.booxtown.model.Result;
import redix.booxtown.model.Setting;
import redix.booxtown.model.SettingResult;
import retrofit2.Call;

/**
 * Created by thuyetpham94 on 28/09/2016.
 */
public class SettingController {
    private ServiceInterface service;
    public SettingController(){
        service = ServiceGenerator.GetInstance();
    }

    public List<Setting> getSettingByUserId(String session_id){
        Call<SettingResult> getSetting1 = service.getSettingByUserId(session_id);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            SettingResult str = getSetting1.execute().body();
            if (str.getCode()==200){
                return str.getSetting();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public boolean updateSetting(String session_id,int id,int is_notification,int is_best_time,int is_current_location,
                                 String time_start,String time_to){
        Hashtable obj = new Hashtable();
        obj.put("session_id",session_id);
        obj.put("id",id);
        obj.put("is_notification",is_notification);
        obj.put("is_best_time",is_best_time);
        obj.put("is_current_location",is_current_location);
        obj.put("time_start",time_start);
        obj.put("time_to",time_to);
        Call<Result> getSetting1 = service.updateSetting(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = getSetting1.execute().body();
            if (str.getCode()==200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }
}
