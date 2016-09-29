package redix.booxtown.controller;

import android.os.StrictMode;

import java.util.List;

import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.api.ServiceInterface;
import redix.booxtown.model.Genre;
import redix.booxtown.model.GenreValue;
import redix.booxtown.model.GenreValueResult;
import retrofit2.Call;

/**
 * Created by thuyetpham94 on 29/09/2016.
 */
public class GenreController {
    private ServiceInterface service;
    public GenreController(){
        service = ServiceGenerator.GetInstance();
    }

    public List<GenreValue> getAllGenre(){
        Call<GenreValueResult> getSetting1 = service.getAllGenre();
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            GenreValueResult str = getSetting1.execute().body();
            if (str.getCode()==200){
                return str.getGenre();
            }
        } catch (Exception ex) {
        }
        return null;
    }
}
