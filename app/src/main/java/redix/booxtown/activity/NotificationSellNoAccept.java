package redix.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.SettingController;
import redix.booxtown.controller.TransactionController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.custom.NotificationAccept;
import redix.booxtown.model.Book;
import redix.booxtown.model.Setting;
import redix.booxtown.model.Transaction;

/**
 * Created by thuyetpham94 on 27/08/2016.
 */
public class NotificationSellNoAccept extends AppCompatActivity implements View.OnClickListener{
    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;

    TextView txt_user_hi;
    TextView txt_author_info3;
    TextView txt_title_book_buy_accept;
    TextView txt_author_book_buy_accept;
    TextView txt_notification_sell_accept_money;
    TextView txt_notification_dominic_time;

    TextView txt_menu_notification_title2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sell_accept2);

        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);

        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);

        txt_user_hi=(TextView) findViewById(R.id.txt_user_hi);
        txt_author_info3=(TextView) findViewById(R.id.txt_author_info3);
        txt_title_book_buy_accept=(TextView) findViewById(R.id.txt_title_book_buy_accept);
        txt_author_book_buy_accept=(TextView) findViewById(R.id.txt_author_book_buy_accept);
        txt_notification_sell_accept_money=(TextView) findViewById(R.id.txt_notification_sell_accept_money);
        txt_notification_dominic_time=(TextView) findViewById(R.id.txt_notification_dominic_time);
        txt_menu_notification_title2=(TextView) findViewById(R.id.txt_menu_notification_title2);
        txt_menu_notification_title2.setText("you accepted a request from");
        TextView txt_menu_notification_title2 = (TextView)findViewById(R.id.txt_menu_notification_title2);
        txt_menu_notification_title2.setVisibility(View.GONE);

        TextView txt_menu_notification_infor3_title = (TextView)findViewById(R.id.txt_menu_notification_infor3_title);
        txt_menu_notification_infor3_title.setText("accepted your request for buying");

        //menu

        ImageView img_menu_component = (ImageView)findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.GONE);

        TextView txtTitle=(TextView)findViewById(R.id.txt_title);
        txtTitle.setText("Notifications");

        ImageView img_menu = (ImageView)findViewById(R.id.img_menu);
        img_menu.setImageResource(R.drawable.btn_sign_in_back);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //end

        //infor
        ImageView imv_nitification_infor3_phone = (ImageView)findViewById(R.id.imv_nitification_infor3_phone);
        imv_nitification_infor3_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationSellNoAccept.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //bottom
        //--------------------------------------------------------------
        // lấy được list sách swap đẻ đổ vào listview
        String trans_id= getIntent().getStringExtra("trans_id");
        transAsync transAsync= new transAsync(NotificationSellNoAccept.this,trans_id);
        transAsync.execute();
        //---------------------------------------------------------------
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(NotificationSellNoAccept.this,MainAllActivity.class);
                intent1.putExtra("key","1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(NotificationSellNoAccept.this,MainAllActivity.class);
                intent2.putExtra("key","2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(NotificationSellNoAccept.this,MainAllActivity.class);
                intent3.putExtra("key","3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(NotificationSellNoAccept.this,MainAllActivity.class);
                intent4.putExtra("key","4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(NotificationSellNoAccept.this,MainAllActivity.class);
                intent5.putExtra("key","5");
                startActivity(intent5);
                break;

        }
    }
    class transAsync extends AsyncTask<String,Void,Transaction> {

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        String trans_id;
        public transAsync(Context context, String trans_id){
            this.context = context;
            this.trans_id=trans_id;
            listemp = new ArrayList<>();
        }

        @Override
        protected Transaction doInBackground(String... strings) {
            TransactionController bookController = new TransactionController();
            return bookController.getTransactionId(trans_id);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Transaction transaction) {
            if (transaction == null){
                dialog.dismiss();
            }else {
                SharedPreferences pref = NotificationSellNoAccept.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = pref.edit();
                String session_id = pref.getString("session_id", null);
                getSetting gt= new getSetting(NotificationSellNoAccept.this, transaction);
                gt.execute(session_id);
                dialog.dismiss();
            }
            super.onPostExecute(transaction);
        }
    }

    class getSetting extends AsyncTask<String,Void,List<Setting>>{

        Context context;
        ProgressDialog progressDialog;
        Transaction trans;
        public getSetting(Context context, Transaction trans){
            this.trans= trans;
            this.context=context;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<Setting> doInBackground(String... strings) {
            SettingController settingController = new SettingController();
            return settingController.getSettingByUserId(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Setting> settings) {
            try {
                SharedPreferences pref = NotificationSellNoAccept.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = pref.edit();
                String userName = pref.getString("username", null);
                txt_user_hi.setText("Hi "+ userName+",");

                txt_notification_sell_accept_money.setText("AED "+trans.getBook_price());
                txt_author_info3.setText(trans.getUser_buy()+"");
                txt_title_book_buy_accept.setText(trans.getBook_name());
                txt_author_book_buy_accept.setText(trans.getBook_author());
                String []timeStart=settings.get(0).getTime_start().split(":");
                String timeS="";
                if(Integer.parseInt(timeStart[0])<=12){
                    timeS=timeStart[0]+":"+ timeStart[1]+ " AM";
                }else{
                    timeS=timeStart[0]+":"+ timeStart[1]+ " PM";
                }

                String []timeTo=settings.get(0).getTime_to().split(":");
                String timeT="";
                if(Integer.parseInt(timeTo[0])<=12){
                    timeT=timeTo[0]+":"+ timeTo[1]+ " AM";
                }else{
                    timeT=timeTo[0]+":"+ timeTo[1]+ " PM";
                }
                txt_notification_dominic_time.setText(timeS+"-"+timeT);
            }catch (Exception e){
            }
            progressDialog.dismiss();
        }
    }
}
