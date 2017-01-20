package com.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.R;
import com.booxtown.controller.BookController;
import com.booxtown.controller.GPSTracker;
import com.booxtown.controller.Information;
import com.booxtown.controller.UserController;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 20/01/2017.
 */

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView mButtonBackForgot;
    TextView txt_resend,btn_proceed;
    String session_id, username,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        txt_resend=(TextView) findViewById(R.id.txt_resend);
        mButtonBackForgot=(ImageView) findViewById(R.id.btn_back_forgot);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(mButtonBackForgot);
        Spannable wordtoSpan1 = new SpannableString("Resend activation email");
        wordtoSpan1.setSpan(new ForegroundColorSpan(Color.RED), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_resend.setText(wordtoSpan1);



        session_id=getIntent().getStringExtra("session_id");
        username=getIntent().getStringExtra("username");
        pass=getIntent().getStringExtra("pass");

        btn_proceed=(TextView) findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SiginAsystask siginAsystask = new SiginAsystask();
                siginAsystask.execute(username, pass, "device",session_id);
            }
        });

        mButtonBackForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendActiveAsync sendActiveAsync= new SendActiveAsync(VerificationActivity.this, username,username,pass,session_id);
                sendActiveAsync.execute();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    class SiginAsystask extends AsyncTask<String,Void,String>{
        ProgressDialog dialog;
        String sessionId="";
        @Override
        protected String doInBackground(String... params) {
            try {
                java.lang.Thread.sleep(3000);
                session_id = FirebaseInstanceId.getInstance().getToken().toString();
                sessionId=session_id;
                UserController userController = new UserController(VerificationActivity.this);
                String code = userController.checkLoginValidate(params[0], params[1], params[2], sessionId);
                return code;
            }catch (Exception ex){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(VerificationActivity.this);
            dialog.setMessage(Information.noti_dialog);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String code) {
            try {
                if (code.equals("200")) {
                    Intent intent = new Intent(VerificationActivity.this, MainAllActivity.class);
                    startActivity(intent);
                    UserInfoAsystask us= new UserInfoAsystask();
                    us.execute(session_id);

                    dialog.dismiss();
                } else if(code.equals("703")) {
                    Toast.makeText(getApplicationContext(), "You need to activate your account first to proceed", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }catch (Exception e){
            }
        }
    }
    class UserInfoAsystask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                UserController userController = new UserController(VerificationActivity.this);
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

    class SendActiveAsync extends AsyncTask<String,Void,Boolean> {
        String email;
        ProgressDialog dialog;
        Context ct;
        String userName,pass,session_id;
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
            dialog = new ProgressDialog(VerificationActivity.this);
            dialog.setMessage(Information.noti_dialog);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                if (result) {
                    Toast.makeText(ct,"Resend activation email successfully!",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {

                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
}
