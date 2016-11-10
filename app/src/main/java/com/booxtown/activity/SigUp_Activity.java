package com.booxtown.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.controller.CheckNetwork;
import com.booxtown.controller.DeleteTokenService;
import com.booxtown.controller.Information;
import com.booxtown.controller.UserController;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.regex.Pattern;

import com.booxtown.R;
import com.booxtown.model.User;

public class SigUp_Activity extends AppCompatActivity implements View.OnClickListener{
    ImageView mButtonBackSigup;
    EditText edt_name,edt_firtname,edt_phone,edt_mail,edt_password,edt_confirmpass,edt_lastname;
    CheckBox checkSignup;
    EditText edt_birthday;
    String day="",moth="",year="";
    //String birthday;
    TextView signUp;
    String session_id;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //picaso
        ImageView icon_signup = (ImageView)findViewById(R.id.icon_sign_up_home);
        Picasso.with(SigUp_Activity.this).load(R.drawable.icon_sign_in_home).into(icon_signup);

        //end
        mButtonBackSigup = (ImageView) findViewById(R.id.btn_back_sigup);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(mButtonBackSigup);
        signUp = (TextView) findViewById(R.id.signup);
        signUp.setOnClickListener(this);
        mButtonBackSigup.setOnClickListener(this);
        edt_firtname = (EditText) findViewById(R.id.firstname);
        edt_lastname = (EditText) findViewById(R.id.lastname);
        edt_name = (EditText) findViewById(R.id.username);
        edt_mail = (EditText) findViewById(R.id.email);
        edt_phone = (EditText) findViewById(R.id.phone);
        edt_birthday = (EditText) findViewById(R.id.birthday);
        edt_password = (EditText) findViewById(R.id.password);
        edt_confirmpass = (EditText) findViewById(R.id.confirmpassword);
        checkSignup = (CheckBox) findViewById(R.id.checksignup);
        edt_birthday.setOnClickListener(this);
        Intent intent1 = new Intent(this, DeleteTokenService.class);
        startService(intent1);
        if (CheckNetwork.isOnline(SigUp_Activity.this) == false){
            Toast.makeText(getApplicationContext(), Information.checkNetwork, Toast.LENGTH_LONG).show();
        }
    }


    public boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup:
                /*ProgressDialog dialog = new ProgressDialog(SigUp_Activity.this);
                try {
                    dialog.setMessage(Information.noti_dialog);
                    dialog.show();
                    java.lang.Thread.sleep(3000);
                    session_id = FirebaseInstanceId.getInstance().getToken().toString();
                    dialog.dismiss();
                }catch (Exception e) {
                    dialog.dismiss();
                    Toast.makeText(SigUp_Activity.this, "Could not connect to server. Please try again", Toast.LENGTH_SHORT).show();
                }*/
                UserController userController = new UserController(SigUp_Activity.this);
                User user  = new User();
                String[] bod=edt_birthday.getText().toString().split("-");

                user.setBirthday(bod[2]+"-"+bod[1]+"-"+bod[0]);
                user.setEmail(edt_mail.getText().toString());
                user.setFirst_name(edt_firtname.getText().toString());
                user.setLast_name(edt_lastname.getText().toString());
                user.setPhone(edt_phone.getText().toString());
                user.setUsername(edt_name.getText().toString());
                user.setPassword(edt_password.getText().toString());
                user.setSession_id(session_id);
                if (edt_name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),Information.noti_fill_username,Toast.LENGTH_LONG).show();
                }else if(edt_lastname.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),Information.noti_fill_lastname,Toast.LENGTH_LONG).show();
                }else if(edt_firtname.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),Information.noti_fill_firstname,Toast.LENGTH_LONG).show();
                }else if(edt_phone.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),Information.noti_fill_phone,Toast.LENGTH_LONG).show();
                }else if(edt_birthday.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),Information.noti_fill_birthday,Toast.LENGTH_LONG).show();
                }else if(checkEmail(edt_mail.getText().toString()) == false){
                    Toast.makeText(getApplicationContext(),Information.noti_validate_email,Toast.LENGTH_LONG).show();
                }else if(edt_password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),Information.noti_fill_password,Toast.LENGTH_LONG).show();
                }else if(!edt_password.getText().toString().equals(edt_confirmpass.getText().toString())){
                    Toast.makeText(getApplicationContext(),Information.noti_match_pass,Toast.LENGTH_LONG).show();
                }else if (checkSignup.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), Information.noti_check_term, Toast.LENGTH_LONG).show();
                }else {
                    if (!CheckNetwork.isOnline(SigUp_Activity.this)){
                        Toast.makeText(getApplicationContext(), Information.checkNetwork, Toast.LENGTH_LONG).show();
                    }else{
                        SignupAsyntask signupAsyntask = new SignupAsyntask();
                        signupAsyntask.execute(user);
                    }
                }

                break;
            case R.id.btn_back_sigup:
                Intent intent = new Intent(SigUp_Activity.this,WelcomeActivity.class);
                startActivity(intent);

            case R.id.birthday:
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getFragmentManager(), "Date Time");
                break;
            default:
                break;
        }
    }

    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),this,year,month,day);
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            EditText textview = (EditText) getActivity().findViewById(R.id.birthday);
            textview.setText(day + "-" + (month+1) + "-" + year);
        }
    }

    public class SignupAsyntask extends AsyncTask<User,Void,Boolean>{

        ProgressDialog dialog;
        UserController userController;
        String sessionId="";
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
            userController = new UserController(SigUp_Activity.this);
            boolean success = userController.signUp(params[0]);
            return success;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(SigUp_Activity.this);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true) {
                    Intent intent = new Intent(SigUp_Activity.this, MainAllActivity.class);
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("session_id", session_id);
                    editor.putString("username", edt_mail.getText().toString());
                    editor.putString("firstname", edt_firtname.getText().toString());
                    editor.commit();
                    dialog.dismiss();
                } else if (aBoolean == false) {
                    Toast.makeText(getApplicationContext(), Information.noti_email_taken, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }catch (Exception e){}
        }
    }

    // Dismis keybroad
    public static void hideSoftKeyboard(SigUp_Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

}
