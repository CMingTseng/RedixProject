package com.booxtown.controller;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.R;
import com.booxtown.activity.Upgrade;
import com.booxtown.util.IabHelper;
import com.booxtown.util.IabResult;
import com.booxtown.util.Purchase;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 08/12/2016.
 */

public class CheckInAppPurchase {
    public Context ct;
    private IabHelper mHelper;
    private String base64EncodedPublicKey;
    private static String myproduct_id;
    private static int REQUEST_CODE = 10001;
    private Button btnBuy;
    private SharedPreferences preferences;
    private boolean isPurchase;
    public CheckInAppPurchase(Context ct){
        this.ct=ct;
        initInApp();
    }
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                if (result.getResponse() != IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {

                }

                return;
            }
        }
    };

    private void initInApp() {
        base64EncodedPublicKey = ct.getString(R.string.base64EncodedPublicKey);
        myproduct_id = ct.getString(R.string.testing);
        mHelper = new IabHelper(ct, base64EncodedPublicKey);
        mHelper.enableDebugLogging(false);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d("TAG", "In-app Billing setup failed: " + result);
                } else {
                    Log.d("TAG", "In-app Billing is set up OK");
                }

            }
        });
    }
}
