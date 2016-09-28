package redix.booxtown.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import redix.booxtown.controller.UserController;
import redix.booxtown.R;

public class ForgotPassword_Activity extends AppCompatActivity implements View.OnClickListener{
Button mButtonBackForgot;
    TextView mButtonSubmit;
    //SignIn_Activity signIn_activity;
    EditText edt_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        edt_email = (EditText) findViewById(R.id.email_forgot);
        mButtonBackForgot = (Button) findViewById(R.id.btn_back_forgot);
        //signIn_activity = (SignIn_Activity)getApplicationContext();
        mButtonSubmit = (TextView)  findViewById(R.id.submit_forgot);
        mButtonSubmit.setOnClickListener(this);
        mButtonBackForgot.setOnClickListener(this);
//        if (signIn_activity.isOnline() ==false){
//            Toast.makeText(getApplicationContext(), "Check network state please", Toast.LENGTH_LONG).show();
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back_forgot:
                onBackPressed();
                break;
            case R.id.submit_forgot:
                Forgotpassword forgotpassword = new Forgotpassword();
                forgotpassword.execute(edt_email.getText().toString());
                break;

            default:
                break;
        }
    }

    class Forgotpassword extends AsyncTask<String,Void,Boolean>{

        ProgressDialog dialog;

        //SigUp_Activity sigUp_activity = (SigUp_Activity)getApplicationContext();

        @Override
        protected Boolean doInBackground(String... params) {
            UserController userController = new UserController();
            boolean success = userController.forgetPassword(params[0]);

            return success;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ForgotPassword_Activity.this);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean == true){
                Toast.makeText(getApplicationContext(), "Check Email Please", Toast.LENGTH_LONG).show();

            }
            dialog.dismiss();
//            else if (sigUp_activity.checkEmail(edt_email.getText().toString()) == false){
//                Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_LONG).show();
//                dialog.dismiss();
//            }
        }
    }
}
