package com.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.booxtown.controller.CheckSession;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.Information;
import com.booxtown.controller.SettingController;
import com.booxtown.controller.TransactionController;
import com.booxtown.controller.UserController;
import com.booxtown.model.Book;
import com.booxtown.model.Setting;
import com.booxtown.model.Transaction;
import com.booxtown.model.User;

/**
 * Created by thuyetpham94 on 28/08/2016.
 */
public class NotificationSellNoReject extends AppCompatActivity implements View.OnClickListener{
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
    TextView txt_notification_infor3_phone;
    TextView txt_menu_notification_infor3_title;
    TextView txt_notification_dominic_besttime;

    CircularImageView imv_nitification_infor3_phone;
    android.widget.RatingBar RatingBar;
    ImageView img_comment_rank1,img_comment_rank2,img_comment_rank3;
    String keyOption="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sell_accept2);

        init();

        // lấy được list sách swap đẻ đổ vào listview
        String trans_id= getIntent().getStringExtra("trans_id");
        keyOption=getIntent().getStringExtra("keyOption");
        transAsync transAsync= new transAsync(NotificationSellNoReject.this,trans_id);
        transAsync.execute();
        //---------------------------------------------------------------

        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);


        txt_menu_notification_title2.setText("Sorry!");


        txt_notification_infor3_phone.setVisibility(View.GONE);

        if(keyOption.equals("8")) {
            txt_menu_notification_infor3_title.setText("rejected your request for buying");
        }else if(keyOption.equals("20")){
            txt_menu_notification_infor3_title.setText("rejected your request to get the book");
        }


        txt_notification_dominic_besttime.setVisibility(View.GONE);


        txt_notification_dominic_time.setVisibility(View.GONE);

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

        imv_nitification_infor3_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationSellNoReject.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //bottom
        //--------------------------------------------------------------
        txt_user_hi=(TextView) findViewById(R.id.txt_user_hi);
        txt_author_info3=(TextView) findViewById(R.id.txt_author_info3);
        txt_title_book_buy_accept=(TextView) findViewById(R.id.txt_title_book_buy_accept);
        txt_author_book_buy_accept=(TextView) findViewById(R.id.txt_author_book_buy_accept);
        txt_notification_sell_accept_money=(TextView) findViewById(R.id.txt_notification_sell_accept_money);
        txt_notification_dominic_time=(TextView) findViewById(R.id.txt_notification_dominic_time);
        //---------------------------------------------------------------


    }

    public void init(){
        img_comment_rank1 = (ImageView)findViewById(R.id.img_comment_rank1);
        img_comment_rank2 = (ImageView)findViewById(R.id.img_comment_rank2);
        img_comment_rank3 = (ImageView)findViewById(R.id.img_comment_rank3);
        RatingBar = (RatingBar)findViewById(R.id.ratingBar2);

        imv_nitification_infor3_phone = (CircularImageView)findViewById(R.id.imv_nitification_infor3_phone);

        txt_notification_dominic_time = (TextView)findViewById(R.id.txt_notification_dominic_time);
        txt_notification_dominic_besttime = (TextView)findViewById(R.id.txt_notification_dominic_besttime);
        txt_menu_notification_infor3_title = (TextView)findViewById(R.id.txt_menu_notification_infor3_title);

        txt_notification_infor3_phone = (TextView)findViewById(R.id.txt_notification_infor3_phone);
        txt_menu_notification_title2 = (TextView)findViewById(R.id.txt_menu_notification_title2);
        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(NotificationSellNoReject.this,MainAllActivity.class);
                intent1.putExtra("key","1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(NotificationSellNoReject.this,MainAllActivity.class);
                intent2.putExtra("key","2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(NotificationSellNoReject.this,MainAllActivity.class);
                intent3.putExtra("key","3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(NotificationSellNoReject.this,MainAllActivity.class);
                intent4.putExtra("key","4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(NotificationSellNoReject.this,MainAllActivity.class);
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
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
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
                SharedPreferences pref = NotificationSellNoReject.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = pref.edit();
                String session_id = pref.getString("session_id", null);
                getSetting gt= new getSetting(NotificationSellNoReject.this, transaction);
                gt.execute(transaction.getSession_user_sell());

                getUser1 getUser1 = new getUser1(NotificationSellNoReject.this,transaction.getUser_seller_id());
                getUser1.execute();
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
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            SettingController settingController = new SettingController();
            return settingController.getSettingByUserId(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Setting> settings) {
            try {
                SharedPreferences pref = NotificationSellNoReject.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor  = pref.edit();
                String userName = pref.getString("username", null);
                String firstName = pref.getString("firstname", "");
                txt_user_hi.setText("Hi "+ firstName+",");


                if(keyOption.equals("8")) {
                    txt_notification_sell_accept_money.setText("AED "+trans.getBook_price());
                }else if(keyOption.equals("20")){
                    txt_notification_sell_accept_money.setVisibility(View.INVISIBLE);
                }
                txt_author_info3.setText(trans.getUser_sell()+"");
                txt_title_book_buy_accept.setText(trans.getBook_name());
                txt_author_book_buy_accept.setText(trans.getBook_author());
                String []timeStart=trans.getSeller_time_start().split(":");
                String timeS="";
                if(Integer.parseInt(timeStart[0])<=12){
                    timeS=timeStart[0]+":"+ timeStart[1]+ " ";
                }else{
                    timeS=timeStart[0]+":"+ timeStart[1]+ " ";
                }

                String []timeTo=trans.getSeller_time_to().split(":");
                String timeT="";
                if(Integer.parseInt(timeTo[0])<=12){
                    timeT=timeTo[0]+":"+ timeTo[1]+ " ";
                }else{
                    timeT=timeTo[0]+":"+ timeTo[1]+ " ";
                }
                txt_notification_dominic_time.setText(timeS+"-"+timeT);
            }catch (Exception e){
            }
            progressDialog.dismiss();
        }
    }

    class getUser1 extends AsyncTask<Void,Void,List<User>>{

        Context context;
        int user_id;
        ProgressDialog progressDialog;
        public getUser1(Context context,int user_id){
            this.context = context;
            this.user_id = user_id;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            UserController userController = new UserController(context);
            return userController.getByUserId(user_id);
        }

        @Override
        protected void onPostExecute(List<User> user) {
            try {
                if (user.size() > 0){
                    txt_author_info3.setText(user.get(0).getFirst_name());
                    if (user.get(0).getPhoto().length() > 3) {
                        int index =user.get(0).getPhoto().indexOf("_+_");
                    Picasso.with(context)
                            .load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getPhoto().substring(0,index).trim()+"&image="+user.get(0).getPhoto().substring(index+3,user.get(0).getPhoto().length()))
                            .error(R.mipmap.user_empty)
                            .into(imv_nitification_infor3_phone);
                    }else {
                        Picasso.with(context)
                                .load(R.mipmap.user_empty)
                                .into(imv_nitification_infor3_phone);
                    }
                    RatingBar.setRating(user.get(0).getRating());
                    LayerDrawable stars = (LayerDrawable) RatingBar.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.rgb(255,2224,0), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                    DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),context.getResources().getColor(R.color.bg_rating));

                    //set rank
                    if(user.get(0).getContributor() == 0){
                        img_comment_rank1.setVisibility(View.VISIBLE);
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_one);
                        img_comment_rank1.setImageBitmap(btn1);

                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_two);
                        img_comment_rank1.setImageBitmap(btn1);

                    }
                    if(user.get(0).getGoldenBook() == 0){
                        img_comment_rank2.setVisibility(View.GONE);
                    }else if(user.get(0).getGoldenBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.golden_book);
                        img_comment_rank2.setImageBitmap(btn1);
                        img_comment_rank2.setVisibility(View.VISIBLE);
                    }

                    if(user.get(0).getListBook() == 0){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.newbie);
                        img_comment_rank3.setImageBitmap(btn1);
                        img_comment_rank3.setVisibility(View.VISIBLE);
                    }else if(user.get(0).getListBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bookworm);
                        img_comment_rank3.setImageBitmap(btn1);
                        img_comment_rank3.setVisibility(View.VISIBLE);
                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bibliophile);
                        img_comment_rank3.setImageBitmap(btn1);
                        img_comment_rank3.setVisibility(View.VISIBLE);
                    }

                    progressDialog.dismiss();
                }else {
                    //Toast.makeText(context,Information.noti_no_data,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }catch (Exception e){

            }
            progressDialog.dismiss();
        }
    }
}
