package redix.booxtown.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import redix.booxtown.R;
import redix.booxtown.fragment.NotificationFragment;

public class Splash_Activity extends AppCompatActivity {

    public static boolean value = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
