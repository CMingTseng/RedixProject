package com.booxtown.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.autoviewpager.AutoScrollViewPager;
import com.booxtown.facebookSignIn.FacebookHelper;
import com.booxtown.facebookSignIn.FacebookResponse;
import com.booxtown.facebookSignIn.FacebookUser;
import com.booxtown.googleAuthSignin.GoogleAuthResponse;
import com.booxtown.googleAuthSignin.GoogleAuthUser;
import com.booxtown.googleAuthSignin.GoogleSignInHelper;
import com.booxtown.twitterSignIn.TwitterHelper;
import com.booxtown.twitterSignIn.TwitterResponse;
import com.booxtown.twitterSignIn.TwitterUser;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;

import com.booxtown.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleAuthResponse, FacebookResponse, TwitterResponse {

    private AutoScrollViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnsigup, btnsignin;
    private FacebookHelper mFbHelper;
    private GoogleSignInHelper mGAuthHelper;
    private TwitterHelper mTwitterHelper;
//    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTwitterHelper = new TwitterHelper(R.string.twitter_api_key,
                R.string.twitter_secrate_key,
                this,
                WelcomeActivity.this);
        //Google api initialization
        mGAuthHelper = new GoogleSignInHelper(this, null, this);
        //fb api initialization
        mFbHelper = new FacebookHelper(this,
                "id,name,email,gender,birthday,picture,cover",
                this);
        setContentView(R.layout.activity_welcome);
        //twitter initialization
        //end
        ImageView signin_fb = (ImageView) findViewById(R.id.signin_fb);
        Picasso.with(WelcomeActivity.this).load(R.mipmap.fb).into(signin_fb);
        ImageView signin_twitter = (ImageView) findViewById(R.id.signin_twitter);
        Picasso.with(WelcomeActivity.this).load(R.mipmap.twetter).into(signin_twitter);
        ImageView signin_google = (ImageView) findViewById(R.id.signin_google);
        Picasso.with(WelcomeActivity.this).load(R.mipmap.g).into(signin_google);

        signin_fb.setOnClickListener(this);
        signin_twitter.setOnClickListener(this);
        signin_google.setOnClickListener(this);
        //end
        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnsigup = (Button) findViewById(R.id.btn_sigup_wellcome);
        btnsignin = (Button) findViewById(R.id.btn_sigin_wellcome);

        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};
        addBottomDots(0);

        changeStatusBarColor();
        myViewPagerAdapter = new MyViewPagerAdapter(layouts);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnsigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itent = new Intent(WelcomeActivity.this, SigUp_Activity.class);
                startActivity(itent);
            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itent = new Intent(WelcomeActivity.this, SignIn_Activity.class);
                startActivity(itent);
            }
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String session_id = pref.getString("session_id", null);
        try {
            if (session_id != null) {
                Intent intent = new Intent(WelcomeActivity.this, MainAllActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(42);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    //	viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onFbSignInFail() {
        Toast.makeText(this, "Facebook sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbSignInSuccess() {
        Toast.makeText(this, "Facebook sign in success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {
        Toast.makeText(this, "Facebook user data: name= " + facebookUser.name + " email= " + facebookUser.email, Toast.LENGTH_SHORT).show();
        Log.d("Person name: ", facebookUser.name + "");
        Log.d("Person gender: ", facebookUser.gender + "");
        Log.d("Person email: ", facebookUser.email + "");
        Log.d("Person image: ", facebookUser.facebookID + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin_google:
                mGAuthHelper.performSignIn(this);
                break;
            case R.id.signin_fb:
                mFbHelper.performSignIn(this);
                break;
            case R.id.signin_twitter:
                mTwitterHelper.performSignIn();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mGHelper.disconnectApiClient();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle results
        mFbHelper.onActivityResult(requestCode, resultCode, data);
        mGAuthHelper.onActivityResult(requestCode, resultCode, data);
        mTwitterHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onTwitterError() {
        Toast.makeText(this, "Twitter sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTwitterSignIn(@NonNull String userId, @NonNull String userName) {
        Toast.makeText(this, " User id: " + userId + "\n user name" + userName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTwitterProfileReceived(TwitterUser user) {
        Toast.makeText(this, "Twitter user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        Toast.makeText(this, "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
        Log.d("Person name: ", user.name + "");
        Log.d("Person gender: ", user.id + "");
        Log.d("Person email: ", user.email + "");
        Log.d("Person image: ", user.photoUrl + "");
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        int[] layouts1;

        public MyViewPagerAdapter(int[] layouts) {
            this.layouts1 = layouts;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts1[position], container, false);
            ImageView iconbackground;
            if (position == 0) {
                iconbackground = (ImageView) view.findViewById(R.id.bg1);
                Picasso.with(WelcomeActivity.this).load(R.drawable.bg1ss).fit().into(iconbackground);
                ImageView imageView1_icon_booxtown_intro = (ImageView) view.findViewById(R.id.imageView1_icon_booxtown_intro);
                Picasso.with(getApplicationContext()).load(R.drawable.icon_booxtown_intro).into(imageView1_icon_booxtown_intro);
            } else if (position == 1) {
                iconbackground = (ImageView) view.findViewById(R.id.bg2);
                Picasso.with(WelcomeActivity.this).load(R.drawable.bg2ss).fit().into(iconbackground);

                ImageView imageView2_icon_booxtown_intro = (ImageView) view.findViewById(R.id.imageView2_icon_booxtown_intro);
                Picasso.with(getApplicationContext()).load(R.drawable.icon_booxtown_intro).into(imageView2_icon_booxtown_intro);
            } else if (position == 2) {
                iconbackground = (ImageView) view.findViewById(R.id.bg3);
                Picasso.with(WelcomeActivity.this).load(R.drawable.bg3s).fit().into(iconbackground);

                ImageView imageView3_icon_booxtown_intro = (ImageView) view.findViewById(R.id.imageView3_icon_booxtown_intro);
                Picasso.with(getApplicationContext()).load(R.drawable.icon_booxtown_intro).into(imageView3_icon_booxtown_intro);
            } else if (position == 3) {
                iconbackground = (ImageView) view.findViewById(R.id.bg4);
                Picasso.with(WelcomeActivity.this).load(R.drawable.bg4ss).fit().into(iconbackground);

                ImageView imageView4_icon_booxtown_intro = (ImageView) view.findViewById(R.id.imageView4_icon_booxtown_intro);
                Picasso.with(getApplicationContext()).load(R.drawable.icon_booxtown_intro).into(imageView4_icon_booxtown_intro);
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
