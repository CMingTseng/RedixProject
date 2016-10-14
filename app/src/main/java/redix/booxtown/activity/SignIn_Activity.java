package redix.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;


import java.util.Hashtable;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import redix.booxtown.R;
import redix.booxtown.controller.CheckNetwork;
import redix.booxtown.controller.DeleteTokenService;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.ObjectCommon;
import redix.booxtown.controller.UserController;
import redix.booxtown.model.Result;
import redix.booxtown.model.Thread;
import redix.booxtown.model.User;

public class SignIn_Activity extends AppCompatActivity implements View.OnClickListener{
Button mButtonForgotPass;
    EditText edt_username,edt_pass;
    TextView mButtonBackSignIn;
    ImageView mimgBack;
    String session_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edt_pass  = (EditText) findViewById(R.id.password_login);
        edt_username = (EditText) findViewById(R.id.username_login);
        mButtonForgotPass = (Button) findViewById(R.id.btn_forgotpass);
        mimgBack = (ImageView) findViewById(R.id.btn_back_sigin);
        mButtonBackSignIn = (TextView) findViewById(R.id.btn_sigin);
        //image
        ImageView icon_sign_in_home = (ImageView)findViewById(R.id.icon_sign_in_home);
        Picasso.with(getApplicationContext()).load(R.drawable.icon_sign_in_home).into(icon_sign_in_home);

        Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(mimgBack);
        //end

        Intent intent1 = new Intent(this, DeleteTokenService.class);
        startService(intent1);
        mimgBack.setOnClickListener(this);
        mButtonBackSignIn.setOnClickListener(this);
        mButtonForgotPass.setOnClickListener(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        session_id = pref.getString("session_id", null);
        if (session_id != null){
            Intent intent = new Intent(SignIn_Activity.this, MainAllActivity.class);
            startActivity(intent);
        }
        if (CheckNetwork.isOnline(SignIn_Activity.this) == false){
            Toast.makeText(getApplicationContext(), Information.checkNetwork,Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forgotpass:
                Intent itent = new Intent(SignIn_Activity.this,ForgotPassword_Activity.class);
                startActivity(itent);
                break;
            case R.id.btn_back_sigin:
                onBackPressed();
                break;
            case R.id.btn_sigin:
                if (edt_username.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),Information.noti_fill_username,Toast.LENGTH_SHORT).show();
                }else  if (edt_pass.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),Information.noti_fill_password,Toast.LENGTH_SHORT).show();
                }
                else if(CheckNetwork.isOnline(SignIn_Activity.this) == false){
                    Toast.makeText(getApplicationContext(), Information.checkNetwork,Toast.LENGTH_SHORT).show();
                }
                else {
                    SiginAsystask siginAsystask = new SiginAsystask();
                    siginAsystask.execute(edt_username.getText().toString(), edt_pass.getText().toString(), "iphonecuatung",session_id);
                }
            default:
                break;
        }
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
                UserController userController = new UserController(SignIn_Activity.this);
                String session_id = userController.checkLoginValidate(params[0], params[1], params[2], sessionId);
                return session_id;
            }catch (Exception ex){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SignIn_Activity.this);
            dialog.setMessage(Information.noti_dialog);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String aBoolean) {
            try {
                if (aBoolean != null) {
                    Intent intent = new Intent(SignIn_Activity.this, MainAllActivity.class);
                    startActivity(intent);
                    String session_id = aBoolean.toString();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("session_id", session_id);
                    editor.putString("username", edt_username.getText().toString());
                    UserInfoAsystask us= new UserInfoAsystask();
                    us.execute(session_id);
                    editor.commit();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), Information.noti_wrong_login, Toast.LENGTH_SHORT).show();
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
                UserController userController = new UserController(SignIn_Activity.this);
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

    @Override
    public void onBackPressed() {
        try {
            Intent iten = new Intent(SignIn_Activity.this, WelcomeActivity.class);
            startActivity(iten);
            finish();
        }catch (Exception e){}
    }
}
