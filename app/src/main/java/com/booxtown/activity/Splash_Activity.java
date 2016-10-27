package com.booxtown.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.fragment.NotificationFragment;
import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;
import com.booxtown.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

public class Splash_Activity extends AppCompatActivity {

    public static boolean value = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize sdk
        TwitterAuthConfig authConfig = new TwitterAuthConfig(this.getResources().getString(R.string.twitter_api_key),
                this.getResources().getString(R.string.twitter_secrate_key));
        Fabric.with(Splash_Activity.this, new Twitter(authConfig));
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_);
        try {
            ImageView img_splash =(ImageView)findViewById(R.id.img_splash);
            //Glide.with(getApplicationContext()).load(R.drawable.splash).into(img_splash);
            Picasso.with(Splash_Activity.this).load(R.drawable.splash_one).fit().into(img_splash);
            if (value == false) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Splash_Activity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3000);
            } else {
                HomeActivity homeActivity = (HomeActivity) getApplicationContext();
                homeActivity.callFragment(new NotificationFragment());
                TextView txtTitle = (TextView) findViewById(R.id.txt_title);
                txtTitle.setText("Notifications");
            }
        }catch (Exception e){
        }
    }
}
