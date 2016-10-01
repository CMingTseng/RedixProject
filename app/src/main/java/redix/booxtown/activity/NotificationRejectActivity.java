package redix.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.TransactionController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.custom.NotificationAccept;
import redix.booxtown.model.Book;
import redix.booxtown.model.Transaction;

public class NotificationRejectActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;

    TextView txt_author_info3;
    TextView txt_user_hi;
    TextView txt_book_sell_notifi_reject;
    TextView txt_book_author_sell_notifi_reject;
    TextView txt_book_buy_notifi_reject;
    TextView txt_book_author_buy_notifi_reject;
    String trans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_notification_reject);


        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);

        txt_author_info3=(TextView) findViewById(R.id.txt_author_info3);
        txt_user_hi= (TextView) findViewById(R.id.txt_user_hi) ;
        txt_book_sell_notifi_reject= (TextView) findViewById(R.id.txt_book_sell_notifi_reject) ;
        txt_book_author_sell_notifi_reject= (TextView) findViewById(R.id.txt_book_author_sell_notifi_reject) ;
        txt_book_buy_notifi_reject= (TextView) findViewById(R.id.txt_book_buy_notifi_reject) ;
        txt_book_author_buy_notifi_reject= (TextView) findViewById(R.id.txt_book_author_buy_notifi_reject) ;

        trans = getIntent().getStringExtra("trans_id");
        transAsync transAsync= new transAsync(NotificationRejectActivity.this, trans);
        transAsync.execute();


        TextView txt_menu_notification_title2 = (TextView)findViewById(R.id.txt_menu_notification_title2);
        txt_menu_notification_title2.setText("with your book");
        txt_menu_notification_title2.setTextColor(getResources().getColor(R.color.color_txt_menu_notification_title2));

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
                Intent intent = new Intent(NotificationRejectActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //--------------------------------------------------------------
        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);
        //---------------------------------------------------------------
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(NotificationRejectActivity.this,MainAllActivity.class);
                intent1.putExtra("key","1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(NotificationRejectActivity.this,MainAllActivity.class);
                intent2.putExtra("key","2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(NotificationRejectActivity.this,MainAllActivity.class);
                intent3.putExtra("key","3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(NotificationRejectActivity.this,MainAllActivity.class);
                intent4.putExtra("key","4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(NotificationRejectActivity.this,MainAllActivity.class);
                intent5.putExtra("key","5");
                startActivity(intent5);
                break;

        }
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
//                getBookByID getBookByID= new getBookByID(context, transaction);
//                getBookByID.execute();

            }
            super.onPostExecute(transaction);
        }
    }

//    class getBookByID extends AsyncTask<Void, Void, List<Book>> {
//        Transaction trans;
//        Context ctx;
//        ProgressDialog dialog;
//        public getBookByID(Context ctx, Transaction trans) {
//            this.trans = trans;
//            this.ctx = ctx;
//        }
//
//        @Override
//        protected List<Book> doInBackground(Void... params) {
//            BookController bookController = new BookController();
//
//            return bookController.getBookByID(trans.getBook_swap_id());
//        }
//
//        @Override
//        protected void onPreExecute() {
//            dialog = new ProgressDialog(ctx);
//            dialog.setMessage("Please wait...");
//            dialog.setIndeterminate(true);
//            dialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(List<Book> list) {
//            try {
//                if (list.size() > 0) {
//                    SharedPreferences pref = NotificationRejectActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor  = pref.edit();
//                    String userName = pref.getString("username", null);
//                    txt_user_hi.setText("Hi "+ userName+",");
//                    txt_book_sell_notifi_reject.setText(trans.getBook_name());
//                    txt_book_author_sell_notifi_reject.setText(trans.getBook_author());
//                    txt_book_buy_notifi_reject.setText(list.get(0).getTitle());
//                    txt_book_author_buy_notifi_reject.setText(list.get(0).getAuthor());
//                    txt_author_info3.setText(trans.getUser_sell());
//
//                    dialog.dismiss();
//                }
//            } catch (Exception e) {
//            }
//
//        }
//    }
}
