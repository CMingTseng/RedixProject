package com.booxtown.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.R;
import com.booxtown.util.IabHelper;
import com.booxtown.util.IabResult;
import com.booxtown.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;


public class Upgrade extends AppCompatActivity{
    private IabHelper mHelper;
    private String base64EncodedPublicKey;
    private static String myproduct_id;
    private static int REQUEST_CODE = 10001;
    private Button btnBuy;
    private SharedPreferences preferences;
    private boolean isPurchase=false;
    TextView btn_upgrade;
    ImageView btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        preferences = getSharedPreferences("inapppurchase", MODE_PRIVATE);
        btn_close=(ImageView) findViewById(R.id.btn_close_upgrade);
        btn_upgrade=(TextView) findViewById(R.id.btn_upgrade);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /*btn_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check upgrade
                //isPurchase = preferences.getBoolean("isPurchase", false);
                if (isPurchase) {
                    Toast.makeText(getApplicationContext(),
                            "You have already purchase", Toast.LENGTH_LONG)
                            .show();
                } else {
                    if (mHelper != null)
                        mHelper.flagEndAsync();
                    mHelper.launchPurchaseFlow(Upgrade.this, myproduct_id,
                            REQUEST_CODE, mPurchaseFinishedListener,
                            "mypurchasetoken");
                }
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        initInApp();
    }

    private void initInApp() {
        base64EncodedPublicKey = getString(R.string.base64EncodedPublicKey);
        myproduct_id = getString(R.string.testing);
        mHelper = new IabHelper(this, base64EncodedPublicKey);
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

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                if (result.getResponse() == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
                    // Already item purchased
                    alert("Already Purchased");
                    return;
                }
                // Handle error
                alertError("Error while purchase\n" + result.getMessage());
                return;
            } else if (purchase.getSku().equals(myproduct_id)) {
                // item purchased
                alert("Successfully Purchased");
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    alert("Successfully Purchased");

                } catch (JSONException e) {
                    alertError("Failed to parse purchase data.");
                    e.printStackTrace();
                }
            }
        }
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setTitle("In App Purchase");
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Is Purchased
                //SharedPreferences.Editor editor = preferences.edit();
                //editor.putBoolean("isPurchase", true);
                //editor.commit();
            }
        });
        bld.setCancelable(false);
        bld.create().show();
    }

    void alertError(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setTitle("In App Purchase");
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        bld.create().show();
    }

    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}