package redix.booxtown.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import redix.booxtown.R;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);
//        ImageView img_splash =(ImageView)findViewById(R.id.img_splash);
//        Picasso.with(getApplicationContext()).load(R.drawable.splash).into(img_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Activity.this,WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}
