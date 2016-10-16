package com.booxtown.controller;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by duong on 9/30/2016.
 */
public class MyFirebaseMessagingService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        Log.d("ahgdjhhshjhd","dsd___"+ FirebaseInstanceId.getInstance().getToken());
        super.onTokenRefresh();
    }
}
