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
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.Information;
import com.booxtown.controller.TransactionController;
import com.booxtown.controller.UserController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;

import com.booxtown.model.Book;
import com.booxtown.model.Transaction;
import com.booxtown.model.User;

public class Notification_Swap_Accept_NoLike extends AppCompatActivity {

    TextView txt_menu_notification_title2_layout27;
    TextView txt_menu_notification_title2_layout28;

    TextView txt_user_hi;
    TextView txt_title_book_sell_not_swap;
    TextView txt_author_book_sell_not_swap;
    TextView txt_title_book_buy_not_swap;
    TextView txt_author_book_buy_not_swap;
    TextView txt_author_info2;

    RelativeLayout include27;
    RelativeLayout include28;
    String trans="";
    TextView txt_menu_notification_infor2_phone,txtTitle;
    ImageView imv_nitification_infor3_phone,img_comment_rank1,img_comment_rank2,img_comment_rank3;
    RatingBar rating_bar_comment_interact;

    ImageView img_menu,img_menu_component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_swap_accept_no_like);
        init();

        Bitmap btm = BitmapFactory.decodeResource(getResources(),R.drawable.btn_sign_in_back);
        img_menu.setImageBitmap(btm);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txt_menu_notification_infor2_phone.setVisibility(View.GONE);
        txtTitle.setText("Notifications");
        //infor
        imv_nitification_infor3_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Notification_Swap_Accept_NoLike.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //end
//bottom
        //--------------------------------------------------------------
        /*View view_bottom = (View) findViewById(R.id.menu_bottom_swapnolike);
        bottomListings = new MenuBottomCustom(view_bottom, this, 0);
        bottomListings.setDefaut(0);*/
        //---------------------------------------------------------------
        txt_menu_notification_title2_layout27.setText("you rejected swapping your book");
        txt_menu_notification_title2_layout28.setText("with book");

        trans = getIntent().getStringExtra("trans_id");
        transAsync transAsync= new transAsync(Notification_Swap_Accept_NoLike.this, trans);
        transAsync.execute();
    }


    public  void init(){
        img_menu = (ImageView)findViewById(R.id.img_menu);
        img_menu_component = (ImageView)findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.GONE);

        img_comment_rank1 = (ImageView)findViewById(R.id.img_comment_rank1);
        img_comment_rank2 = (ImageView)findViewById(R.id.img_comment_rank2);
        img_comment_rank3 = (ImageView)findViewById(R.id.img_comment_rank3);

        rating_bar_comment_interact = (RatingBar)findViewById(R.id.rating_bar_comment_interact);

        txt_user_hi = (TextView) findViewById(R.id.txt_user_hi);
        txt_title_book_sell_not_swap = (TextView) findViewById(R.id.txt_title_book_sell_not_swap);
        txt_author_book_sell_not_swap = (TextView) findViewById(R.id.txt_author_book_sell_not_swap);
        txt_title_book_buy_not_swap = (TextView) findViewById(R.id.txt_title_book_buy_not_swap);
        txt_title_book_buy_not_swap.setVisibility(View.GONE);
        txt_author_book_buy_not_swap = (TextView) findViewById(R.id.txt_author_book_buy_not_swap);
        txt_author_book_buy_not_swap.setVisibility(View.GONE);
        txt_author_info2 = (TextView) findViewById(R.id.txt_author_info2);

        include27= (RelativeLayout) findViewById(R.id.include27);
        include28= (RelativeLayout) findViewById(R.id.include28);
        include28.setVisibility(View.GONE);

        txt_menu_notification_title2_layout27 = (TextView) include27.findViewById(R.id.txt_menu_notification_title2);
        txt_menu_notification_title2_layout28 = (TextView) include28.findViewById(R.id.txt_menu_notification_title2);
        txt_menu_notification_title2_layout28.setVisibility(View.GONE);

        imv_nitification_infor3_phone = (ImageView) findViewById(R.id.imv_menu_notification_infor2);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txt_menu_notification_infor2_phone = (TextView) findViewById(R.id.txt_menu_notification_infor2_phone);
    }

    class transAsync extends AsyncTask<String, Void, Transaction> {
        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        String trans_id;

        public transAsync(Context context, String trans_id) {
            this.context = context;
            this.trans_id = trans_id;
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

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Transaction transaction) {
            if (transaction == null) {

            } else {
                txt_title_book_sell_not_swap.setText(transaction.getBook_name());
                txt_author_book_sell_not_swap.setText(transaction.getBook_author());

                getUser1 getUser1 = new getUser1(Notification_Swap_Accept_NoLike.this,transaction.getUser_buyer_id());
                getUser1.execute();

            }
            super.onPostExecute(transaction);
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
                    SharedPreferences pref = Notification_Swap_Accept_NoLike.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    String userName = pref.getString("username", null);
                    String firstName = pref.getString("firstname", "");
                    txt_user_hi.setText("Hi "+ firstName+",");
                    txt_author_info2.setText(user.get(0).getFirst_name());
                    if (user.get(0).getPhoto().length() > 3) {
                    Picasso.with(context)
                            .load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length()))
                            .error(R.mipmap.user_empty)
                            .into(imv_nitification_infor3_phone);
                    }else {
                        Picasso.with(context)
                                .load(R.mipmap.user_empty)
                                .into(imv_nitification_infor3_phone);
                    }

                    rating_bar_comment_interact.setRating(user.get(0).getRating());
                    LayerDrawable stars = (LayerDrawable) rating_bar_comment_interact.getProgressDrawable();
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
