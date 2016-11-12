package com.booxtown.controller;

import android.os.AsyncTask;

/**
 * Created by thuyetpham94 on 12/11/2016.
 */
public class CheckSessionAync extends AsyncTask<String,Void,Boolean>{
    public static boolean check = true;
    String session_id;
    public CheckSessionAync(String session_id){
        this.session_id = session_id;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        CheckSession checkSession = new CheckSession();
        boolean check1 = checkSession.checkSession_id(session_id);
        check = check1;
        return check1;
    }
}
