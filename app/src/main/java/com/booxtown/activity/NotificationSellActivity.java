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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.controller.CheckSession;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.Information;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.controller.TransactionController;
import com.booxtown.controller.UserController;
import com.booxtown.model.Book;
import com.booxtown.model.Notification;
import com.booxtown.model.Transaction;
import com.booxtown.model.User;

public class NotificationSellActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;
    ImageView img_menu_component;
    ImageView img_menu;
    CircularImageView imv_nitification_infor3_phone;

    TextView txt_author_info3;
    TextView txt_user_hi;
    TextView txt_title_book_buy;
    TextView txt_author_book_buy;
    TextView txt_price_book_buy;
    TextView txtTitle;

    boolean flag = true;
    boolean flagChoose = true;
    TextView txt_notification_infor3_phone;
    TextView txt_menu_notification_infor3_title;
    ImageView img_comment_rank1,img_comment_rank2,img_comment_rank3;
    String firstNameBuyer="";
    String firstNameSeller="";
    RatingBar ratingBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sell);
        init();

        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);

        // lấy được list sách swap đẻ đổ vào listview
        final String trans_id = getIntent().getStringExtra("trans_id");
        String keyOption= getIntent().getStringExtra("keyOption");
        transAsync transAsync = new transAsync(NotificationSellActivity.this, trans_id, 0,keyOption);
        transAsync.execute();

        txt_notification_infor3_phone.setVisibility(View.GONE);
        txt_menu_notification_infor3_title.setText("wants to buy your book");
        //menu
        img_menu_component.setVisibility(View.GONE);
        txtTitle.setText("Notifications");

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
                Intent intent = new Intent(NotificationSellActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //bottom
        //---------------------------------------------------------------
    }

    public void init(){

        img_comment_rank1 = (ImageView)findViewById(R.id.img_comment_rank1);
        img_comment_rank2 = (ImageView)findViewById(R.id.img_comment_rank2);
        img_comment_rank3 = (ImageView)findViewById(R.id.img_comment_rank3);

        ratingBar2 = (RatingBar)findViewById(R.id.ratingBar2);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        img_menu = (ImageView) findViewById(R.id.img_menu);
        imv_nitification_infor3_phone = (CircularImageView) findViewById(R.id.imv_nitification_infor3_phone);

        img_menu_component = (ImageView) findViewById(R.id.img_menu_component);
        txt_menu_notification_infor3_title = (TextView) findViewById(R.id.txt_menu_notification_infor3_title);

        txt_notification_infor3_phone = (TextView) findViewById(R.id.txt_notification_infor3_phone);
        txt_author_info3 = (TextView) findViewById(R.id.txt_author_info3);
        txt_user_hi = (TextView) findViewById(R.id.txt_user_hi);
        txt_title_book_buy = (TextView) findViewById(R.id.txt_title_book_buy);
        txt_author_book_buy = (TextView) findViewById(R.id.txt_author_book_buy);
        txt_price_book_buy = (TextView) findViewById(R.id.txt_price_book_buy);

        img_menu_bottom_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView) findViewById(R.id.img_menu_bottom_user);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(NotificationSellActivity.this, MainAllActivity.class);
                intent1.putExtra("key", "1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(NotificationSellActivity.this, MainAllActivity.class);
                intent2.putExtra("key", "2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                /*Intent intent3 = new Intent(NotificationSellActivity.this, MainAllActivity.class);
                intent3.putExtra("key", "3");
                startActivity(intent3);*/
                Intent intent= new Intent(NotificationSellActivity.this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(NotificationSellActivity.this, MainAllActivity.class);
                intent4.putExtra("key", "4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(NotificationSellActivity.this, MainAllActivity.class);
                intent5.putExtra("key", "5");
                startActivity(intent5);
                break;

        }
    }

    class transAsync extends AsyncTask<String, Void, Transaction> {

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        String trans_id;
        int type;
        String keyOption;
        public transAsync(Context context, String trans_id, int type,String keyOption) {
            this.context = context;
            this.trans_id = trans_id;
            this.type = type;
            listemp = new ArrayList<>();
            this.keyOption= keyOption;
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
            if (transaction == null) {
                dialog.dismiss();
            } else {

                SharedPreferences pref = NotificationSellActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String session_id = pref.getString("session_id", null);
                String userName = pref.getString("username", null);
                String firstName = pref.getString("firstname", "");
                txt_user_hi.setText("Hi " + firstName + ",");
                if(keyOption.equals("4")) {
                    txt_price_book_buy.setText("AED " + transaction.getBook_price());
                }else if(keyOption.equals("16")){
                    txt_price_book_buy.setVisibility(View.INVISIBLE);
                }
                txt_title_book_buy.setText(transaction.getBook_name());
                txt_author_book_buy.setText(transaction.getBook_author());

                Profile profile= new Profile(NotificationSellActivity.this, 0, transaction.getUser_buyer_id());
                profile.execute();

                Profile profile1= new Profile(NotificationSellActivity.this, 1, transaction.getUser_seller_id());
                profile1.execute();

                getUser getUser = new getUser(NotificationSellActivity.this,transaction.getUser_buyer_id());
                getUser.execute();
                if (type == 1) {
                    if (flagChoose) {
                        transactionChangeStatus transactionChangeStatus = new transactionChangeStatus(NotificationSellActivity.this, session_id, "1", transaction, 1);
                        transactionChangeStatus.execute();
                        flagChoose = false;
                    } else {
                        Toast.makeText(NotificationSellActivity.this, "You accepted this transaction", Toast.LENGTH_LONG).show();
                    }
                } else if (type == 2) {
                    if (flagChoose) {
                        transactionChangeStatus transactionChangeStatus = new transactionChangeStatus(NotificationSellActivity.this, session_id, "0", transaction, 2);
                        transactionChangeStatus.execute();
                        flagChoose = false;
                    } else {
                        Toast.makeText(NotificationSellActivity.this, "You rejected this transaction", Toast.LENGTH_LONG).show();
                    }

                }
                else if (type == 3) {
                    if (flagChoose) {
                        transactionChangeStatus transactionChangeStatus = new transactionChangeStatus(NotificationSellActivity.this, session_id, "1", transaction, 3);
                        transactionChangeStatus.execute();
                        flagChoose = false;
                    } else {
                        Toast.makeText(NotificationSellActivity.this, "You accepted this transaction", Toast.LENGTH_LONG).show();
                    }
                }
                else if (type == 4) {
                    if (flagChoose) {
                        transactionChangeStatus transactionChangeStatus = new transactionChangeStatus(NotificationSellActivity.this, session_id, "0", transaction, 4);
                        transactionChangeStatus.execute();
                        flagChoose = false;
                    } else {
                        Toast.makeText(NotificationSellActivity.this, "You rejected this transaction", Toast.LENGTH_LONG).show();
                    }
                }
                else if (type == 0) {
                    Button btn_menu_notification_dominic_accept = (Button) findViewById(R.id.btn_menu_notification_dominic_accept);
                    btn_menu_notification_dominic_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(keyOption.equals("4")) {
                                if ((transaction.getIs_accept() == 0 && transaction.getIs_cancel() == 0 && transaction.getIs_reject() == 0)) {
                                    if(flag) {
                                        Intent intent = new Intent(NotificationSellActivity.this, NotificationSellAccept.class);
                                        intent.putExtra("trans_id", trans_id + "");
                                        intent.putExtra("keyOption", "5");
                                        startActivity(intent);

                                        flag = false;

                                        final String trans_id = getIntent().getStringExtra("trans_id");
                                        transAsync transAsync = new transAsync(NotificationSellActivity.this, trans_id, 1, keyOption);
                                        transAsync.execute();
                                    }else{
                                        Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(keyOption.equals("16")) {
                                if ((transaction.getIs_accept() == 0 && transaction.getIs_cancel() == 0 && transaction.getIs_reject() == 0)) {
                                    if(flag) {
                                        flag = false;
                                        final String trans_id = getIntent().getStringExtra("trans_id");
                                        transAsync transAsync = new transAsync(NotificationSellActivity.this, trans_id, 3, keyOption);
                                        transAsync.execute();

                                        Intent intent = new Intent(NotificationSellActivity.this, NotificationSellAccept.class);
                                        intent.putExtra("trans_id", trans_id + "");
                                        intent.putExtra("keyOption", "17");
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });

                    Button btn_menu_notification_dominic_reject = (Button) findViewById(R.id.btn_menu_notification_dominic_reject);
                    btn_menu_notification_dominic_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (keyOption.equals("4")) {
                                if ((transaction.getIs_accept() == 0 && transaction.getIs_cancel() == 0 && transaction.getIs_reject() == 0)) {
                                    if(flag) {
                                        final String trans_id = getIntent().getStringExtra("trans_id");
                                        transAsync transAsync = new transAsync(NotificationSellActivity.this, trans_id, 2, keyOption);
                                        transAsync.execute();
                                        flag = false;

                                        Intent intent = new Intent(NotificationSellActivity.this, NotificationSellNoAccept.class);
                                        intent.putExtra("trans_id", trans_id + "");
                                        intent.putExtra("keyOption", "7");
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(keyOption.equals("16")) {
                                if ((transaction.getIs_accept() == 0 && transaction.getIs_cancel() == 0 && transaction.getIs_reject() == 0)) {
                                    if(flag) {
                                        final String trans_id = getIntent().getStringExtra("trans_id");
                                        transAsync transAsync = new transAsync(NotificationSellActivity.this, trans_id, 4, keyOption);
                                        transAsync.execute();
                                        flag = false;

                                        Intent intent = new Intent(NotificationSellActivity.this, NotificationSellNoAccept.class);
                                        intent.putExtra("trans_id", trans_id + "");
                                        intent.putExtra("keyOption", "19");
                                        startActivity(intent);

                                    }else {
                                        Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    });
                }

                dialog.dismiss();
            }
            super.onPostExecute(transaction);
        }
    }

    class transactionChangeStatus extends AsyncTask<Void, Void, String> {

        Context context;
        //ProgressDialog dialog;
        Transaction trans;
        String session_id, trans_id, status_id;
        int type;

        public transactionChangeStatus(Context context, String session_id, String status_id, Transaction trans, int type) {
            this.context = context;
            this.session_id = session_id;
            this.type = type;
            this.status_id = status_id;
            this.trans = trans;
        }

        @Override
        protected String doInBackground(Void... voids) {
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
            String transactionID = "";
            TransactionController transactionController = new TransactionController();
            transactionID = transactionController.transactionUpdateStatus(session_id, trans.getId() + "", status_id, "0");
            return transactionID;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String transactionID) {
            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            String firstName = pref.getString("firstname", "");
            if (type == 1) {

                List<Hashtable> list = new ArrayList<>();
                Notification notification = new Notification("Buy Request", trans.getId() + "", "7");
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", trans.getUser_buyer_id());
                obj.put("messages",firstName + " accepted your Buy request");
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);
                // end

                // send notifi user seller

                List<Hashtable> listSeller = new ArrayList<>();
                Notification notificationSeller = new Notification("Buy Request", trans.getId() + "", "5");
                Hashtable objSeller = ObjectCommon.ObjectDymanic(notificationSeller);
                objSeller.put("user_id", trans.getUser_seller_id());
                objSeller.put("messages", "You accepted a Buy request from "+ firstNameSeller );
                listSeller.add(objSeller);
                NotificationController controllerSeller = new NotificationController();
                controllerSeller.sendNotification(listSeller);

            } else if (type == 2) {
                List<Hashtable> list = new ArrayList<>();
                Notification notification = new Notification("Buy Request", trans.getId() + "", "8");
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", trans.getUser_buyer_id());
                obj.put("messages", firstName+" rejected your Buy request");
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);
                // end

                // send notifi user seller

                List<Hashtable> listSeller = new ArrayList<>();
                Notification notificationSeller = new Notification("Buy Request", trans.getId() + "", "6");
                Hashtable objSeller = ObjectCommon.ObjectDymanic(notificationSeller);
                objSeller.put("user_id", trans.getUser_seller_id());
                objSeller.put("messages", "You rejected a Buy request from "+ firstNameSeller);
                listSeller.add(objSeller);
                NotificationController controllerSeller = new NotificationController();
                controllerSeller.sendNotification(listSeller);
            }

            else if (type == 3) {
                List<Hashtable> list = new ArrayList<>();
                Notification notification = new Notification("Buy Request", trans.getId() + "", "19");
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", trans.getUser_buyer_id());
                obj.put("messages",firstName + " accepted your Buy request");
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);
                // end

                // send notifi user seller

                List<Hashtable> listSeller = new ArrayList<>();
                Notification notificationSeller = new Notification("Buy Request", trans.getId() + "", "17");
                Hashtable objSeller = ObjectCommon.ObjectDymanic(notificationSeller);
                objSeller.put("user_id", trans.getUser_seller_id());
                objSeller.put("messages", "You accepted a Buy request from "+ firstNameSeller );
                listSeller.add(objSeller);
                NotificationController controllerSeller = new NotificationController();
                controllerSeller.sendNotification(listSeller);
            }
            else if (type == 4) {
                List<Hashtable> list = new ArrayList<>();
                Notification notification = new Notification("Buy Request", trans.getId() + "", "20");
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", trans.getUser_buyer_id());
                obj.put("messages", firstName+" rejected your Buy request");
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);
                // end

                // send notifi user seller

                List<Hashtable> listSeller = new ArrayList<>();
                Notification notificationSeller = new Notification("Buy Request", trans.getId() + "", "18");
                Hashtable objSeller = ObjectCommon.ObjectDymanic(notificationSeller);
                objSeller.put("user_id", trans.getUser_seller_id());
                objSeller.put("messages", "You rejected a Buy request from "+ firstNameSeller);
                listSeller.add(objSeller);
                NotificationController controllerSeller = new NotificationController();
                controllerSeller.sendNotification(listSeller);
            }

            super.onPostExecute(transactionID);
        }
    }

    class getUser extends AsyncTask<Void,Void,List<User>>{

        Context context;
        int user_id;
        ProgressDialog progressDialog;
        public getUser(Context context,int user_id){
            this.context = context;
            this.user_id = user_id;
        }
        @Override
        protected void onPreExecute() {

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
                    txt_author_info3.setText(user.get(0).getFirst_name()+"");
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

                    ratingBar2.setRating(user.get(0).getRating());
                    LayerDrawable stars = (LayerDrawable) ratingBar2.getProgressDrawable();
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

                    //progressDialog.dismiss();
                }else {
                    //Toast.makeText(context,Information.noti_no_data,Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();
                }
            }catch (Exception e){

            }
            //progressDialog.dismiss();
        }
    }


    class Profile extends AsyncTask<String,Void,List<User>>{
        Context context;
        int type;
        int user_id;
        public Profile(Context context, int type, int user_id){
            this.context=context;
            this.type= type;
            this.user_id=user_id;
        }
        //ProgressDialog dialog;
        @Override
        protected List<User> doInBackground(String... strings) {
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
            UserController userController  = new UserController(context);
            List<User> profile = userController.getByUserId(user_id);
            return profile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(final List<User> userResult) {
            try {
                if(userResult.size() == 0){
                    //Toast.makeText(context,Information.noti_no_data,Toast.LENGTH_LONG).show();
                    //dialog.dismiss();
                }else {
                    if (type==0){
                        firstNameSeller = userResult.get(0).getFirst_name();
                    }else if(type==1){
                        firstNameBuyer = userResult.get(0).getFirst_name();
                    }
                }
                super.onPostExecute(userResult);
            }catch (Exception e){
            }

        }
    }

}
