package com.booxtown.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.booxtown.BuildConfig;
import com.booxtown.autoviewpager.AutoScrollViewPager;
import com.booxtown.controller.CheckNetwork;
import com.booxtown.controller.Information;
import com.booxtown.controller.UserController;
import com.booxtown.facebookSignIn.FacebookHelper;
import com.booxtown.facebookSignIn.FacebookResponse;
import com.booxtown.facebookSignIn.FacebookUser;
import com.booxtown.googleAuthSignin.GoogleAuthResponse;
import com.booxtown.googleAuthSignin.GoogleAuthUser;
import com.booxtown.googleAuthSignin.GoogleSignInHelper;
import com.booxtown.model.User;
import com.booxtown.twitterSignIn.TwitterHelper;
import com.booxtown.twitterSignIn.TwitterResponse;
import com.booxtown.twitterSignIn.TwitterUser;
import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import com.booxtown.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import test.jinesh.easypermissionslib.EasyPermission;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleAuthResponse, FacebookResponse, TwitterResponse,EasyPermission.OnPermissionResult {
    String session_id;
    private AutoScrollViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnsigup, btnsignin;
    private FacebookHelper mFbHelper;
    private GoogleSignInHelper mGAuthHelper;
    private TwitterHelper mTwitterHelper;
    ProgressDialog dialogTotal;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    static final Integer LOCATION = 0x1;
    static final Integer WRITE_EXST = 0x2;
    static final Integer READ_EXST = 0x3;
    static final Integer CAMERA = 0x4;

//    private PrefManager prefManager;

    EasyPermission easyPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (!Fabric.isInitialized()) {
                TwitterAuthConfig authConfig = new TwitterAuthConfig(this.getResources().getString(R.string.twitter_api_key),
                        this.getResources().getString(R.string.twitter_secrate_key));
                Fabric.with(WelcomeActivity .this, new Twitter(authConfig),new Digits(), new Crashlytics());
                //Fabric.with(this, new Crashlytics());
            }
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



        }catch (Exception err){

        }
        setContentView(R.layout.activity_welcome);


        //askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
       /* askForPermission(Manifest.permission.CAMERA,CAMERA);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);*/

        dialogTotal = new ProgressDialog(WelcomeActivity.this);
        dialogTotal.setMessage(Information.noti_dialog);
        dialogTotal.setIndeterminate(true);

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
        String active = pref.getString("active", null);
        try {
            if (session_id != null && active!=null) {
                Intent intent = new Intent(WelcomeActivity.this, MainAllActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
        }


        /*easyPermission = new EasyPermission();
        easyPermission.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);*/

        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
            }
        }
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    BuildConfig.APPLICATION_ID,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String ss=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
    }
    /*Check permission*/
    private boolean checkAndRequestPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int callPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int groupStorePermission = ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (callPhonePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (groupStorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission_group.STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                }
                break;
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        easyPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        *//*if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                case 1:
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    break;
                //Write external Storage
                case 2:
                    break;
                //Read External Storage
                case 3:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 4:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    break;

            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }*//*


    }*/

    @Override
    public void onPermissionResult(String permission, boolean isGranted) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (isGranted) {
                    easyPermission.requestPermission(WelcomeActivity.this,Manifest.permission.CAMERA);
                } else {
                    easyPermission.requestPermission(WelcomeActivity.this,Manifest.permission.CAMERA);
                }
                break;
            case Manifest.permission.CAMERA:
                if (isGranted) {
                    easyPermission.requestPermission(WelcomeActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    easyPermission.requestPermission(WelcomeActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (isGranted) {
                    easyPermission.requestPermission(WelcomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    easyPermission.requestPermission(WelcomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                break;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                if (isGranted) {

                } else {

                }
                break;
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(WelcomeActivity.this, permission)) {
                ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{permission}, requestCode);
            } else {

                ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {
        SiginAsystask siginAsystask= new SiginAsystask(facebookUser.name,facebookUser.facebookID,facebookUser.email);
        siginAsystask.execute();
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

    }

    @Override
    public void onTwitterProfileReceived(final TwitterUser user) {
        final TwitterSession session = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;
        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                String emailTiwtter= result.data.toString();
                SiginAsystask siginAsystask= new SiginAsystask(session.getUserName().toString(),session.getId()+"",emailTiwtter);
                siginAsystask.execute();

            }

            @Override
            public void failure(TwitterException exception) {
                String err= exception.getMessage();
                // Do something on failure
            }
        });

    }

    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        SiginAsystask siginAsystask= new SiginAsystask(user.name,user.id+"",user.email);
        siginAsystask.execute();
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

    class SiginAsystask extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        String sessionId="";

        String email,firstName,password;
        public SiginAsystask(String firstName, String password, String email){
            this.firstName=firstName;
            this.password=password;
            this.email= email;
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                java.lang.Thread.sleep(3000);
                session_id = FirebaseInstanceId.getInstance().getToken().toString();
                sessionId=session_id;
                UserController userController = new UserController(WelcomeActivity.this);
                String session_id = userController.checkLoginValidate(email, password, "iphone", sessionId);
                return session_id;
            }catch (Exception ex){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(WelcomeActivity.this);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String aBoolean) {
            try {
                if (aBoolean.equals("200")) {
                    Intent intent = new Intent(WelcomeActivity.this, MainAllActivity.class);
                    startActivity(intent);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username", firstName.toString());
                    editor.putString("firstname", firstName);
                    editor.commit();
                } else if(aBoolean.equals("701")){
                    // sign up
                    UserController userController = new UserController(WelcomeActivity.this);
                    User user  = new User();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    user.setBirthday(dateFormat.format(date));
                    user.setEmail(email);
                    user.setFirst_name(firstName);
                    user.setLast_name("");
                    user.setPhone("");
                    user.setUsername(email);
                    user.setPassword(password);
                    user.setSession_id(session_id);
                    if (!CheckNetwork.isOnline(WelcomeActivity.this)){
                        Toast.makeText(getApplicationContext(), Information.checkNetwork, Toast.LENGTH_LONG).show();
                    }else{
                        SignupAsyntask signupAsyntask = new SignupAsyntask(email,email,password);
                        signupAsyntask.execute(user);
                    }
                }else {
                    Intent intents= new Intent(WelcomeActivity.this, VerificationActivity.class);
                    intents.putExtra("username",email);
                    intents.putExtra("pass",password);
                    intents.putExtra("session_id",session_id);
                    startActivity(intents);
                    Toast.makeText(getApplicationContext(),"You need to activate your account first to proceed",Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
    class UserInfoAsystask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                UserController userController = new UserController(WelcomeActivity.this);
                String first_name = userController.getprofile(params[0]).get(0).getFirst_name();
                return first_name;
            }catch (Exception ex){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null) {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("firstname", result);
                    editor.commit();

                } else {

                }
            }catch (Exception e){
            }
        }
    }
    public class SignupAsyntask extends AsyncTask<User,Void,Boolean>{

        ProgressDialog dialog;
        UserController userController;
        String sessionId="";
        String username="";
        String firstName="";
        String pass;
        public SignupAsyntask(String username,String firstName,String pass){
            this.username= username;
            this.firstName=firstName;
            this.pass=pass;
        }
        @Override
        protected Boolean doInBackground(User... params) {

            try {
                Thread.sleep(3000);
                session_id = FirebaseInstanceId.getInstance().getToken().toString();
                sessionId=session_id;
                params[0].setSession_id(sessionId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            userController = new UserController(WelcomeActivity.this);
            boolean success = userController.signUp(params[0]);
            return success;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true) {
                    SendActiveAsync sendActiveAsync= new SendActiveAsync(WelcomeActivity.this,username,username,pass,session_id);
                    sendActiveAsync.execute();

                    //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("session_id", session_id);
                    editor.putString("username", username.toString());
                    editor.putString("firstname", firstName.toString());
                    editor.commit();

                } else if (aBoolean == false) {
                    Toast.makeText(getApplicationContext(), Information.noti_email_taken, Toast.LENGTH_LONG).show();

                }
            }catch (Exception e){}
        }
    }

    class SendActiveAsync extends AsyncTask<String,Void,Boolean> {
        String email;
        Context ct;
        String userName,pass,session_id;
        ProgressDialog dialog;
        public SendActiveAsync(Context ct,String email,String userName,String pass,String session_id){
            this.email=email;
            this.ct=ct;
            this.userName=userName;
            this.pass=pass;
            this.session_id=session_id;
        }
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                UserController userController = new UserController(ct);
                return  userController.EmailToActive(email);
            }catch (Exception ex){
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(WelcomeActivity.this);
            dialog.setMessage(Information.noti_dialog);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if (result) {
                    Intent intents= new Intent(WelcomeActivity.this, VerificationActivity.class);
                    intents.putExtra("username",userName);
                    intents.putExtra("pass",pass);
                    intents.putExtra("session_id",session_id);
                    startActivity(intents);
                    dialog.dismiss();
                } else {

                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }




}
