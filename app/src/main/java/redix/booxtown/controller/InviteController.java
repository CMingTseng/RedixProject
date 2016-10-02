package redix.booxtown.controller;

import android.os.StrictMode;

import java.util.Hashtable;

import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.api.ServiceInterface;
import redix.booxtown.model.Result;
import retrofit2.Call;

/**
 * Created by thuyetpham94 on 02/10/2016.
 */
public class InviteController {
    private ServiceInterface service;
    public InviteController(){
        service = ServiceGenerator.GetInstance();
    }

    public boolean inviteFriend(String email){
        Hashtable obj = new Hashtable();
        obj.put("email",email);
        Call<Result> invite = service.inviteFriend(obj);
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Result str = invite.execute().body();
            if (str.getCode()==200){
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }
}
