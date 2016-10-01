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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.TransactionController;
import redix.booxtown.custom.NotificationAccept;
import redix.booxtown.model.Book;
import redix.booxtown.model.Transaction;

/**
 * Created by thuyetpham94 on 27/08/2016.
 */
public class NotificationAcceptActivity extends AppCompatActivity {

    TextView txt_author_info2;
    TextView txt_user_hi;
    TextView txt_title_book_sell_noti_accept;
    TextView txt_author_book_sell_noti_accept;
    TextView txt_title_book_buy_noti_accept;
    TextView txt_author_book_buy_noti_accept;


    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;

    String trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request for window feature action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_confirm_accept);

        img_menu_bottom_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView) findViewById(R.id.img_menu_bottom_user);


        //infor
        TextView txt_menu_notification_infor2_phone = (TextView) findViewById(R.id.txt_menu_notification_infor2_phone);
        txt_menu_notification_infor2_phone.setVisibility(View.GONE);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("Notifications");
        //infor
        ImageView img_menu = (ImageView) findViewById(R.id.img_menu);
        img_menu.setImageResource(R.drawable.btn_sign_in_back);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //end

        ImageView imv_nitification_infor3_phone = (ImageView) findViewById(R.id.imv_menu_notification_infor2);
        imv_nitification_infor3_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationAcceptActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //end

        txt_author_info2 = (TextView) findViewById(R.id.txt_author_info2);
        txt_user_hi = (TextView) findViewById(R.id.txt_user_hi);
        txt_title_book_sell_noti_accept = (TextView) findViewById(R.id.txt_title_book_sell_noti_accept);
        txt_author_book_sell_noti_accept = (TextView) findViewById(R.id.txt_author_book_sell_noti_accept);
        txt_title_book_buy_noti_accept = (TextView) findViewById(R.id.txt_title_book_buy_noti_accept);
        txt_author_book_buy_noti_accept = (TextView) findViewById(R.id.txt_author_book_buy_noti_accept);

        trans = getIntent().getStringExtra("trans_id");

        transAsync transAsync= new transAsync(NotificationAcceptActivity.this, trans);
        transAsync.execute();
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
                getBookByID getBookByID= new getBookByID(context, transaction);
                getBookByID.execute();

            }
            super.onPostExecute(transaction);
        }
    }

    class getBookByID extends AsyncTask<Void, Void, List<Book>> {
        Transaction trans;
        Context ctx;
        ProgressDialog dialog;
        public getBookByID(Context ctx, Transaction trans) {
            this.trans = trans;
            this.ctx = ctx;
        }

        @Override
        protected List<Book> doInBackground(Void... params) {
            BookController bookController = new BookController();

            return bookController.getBookByID(trans.getBook_swap_id());
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ctx);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Book> list) {
            try {
                if (list.size() > 0) {
                    SharedPreferences pref = NotificationAcceptActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    String userName = pref.getString("username", null);
                    txt_user_hi.setText("Hi " + userName + ",");

                    txt_title_book_sell_noti_accept.setText(trans.getBook_name());
                    txt_author_book_sell_noti_accept.setText(trans.getBook_author());
                    txt_title_book_buy_noti_accept.setText(list.get(0).getTitle());
                    txt_author_book_buy_noti_accept.setText(list.get(0).getAuthor());
                    txt_author_info2.setText(trans.getUser_buy());

                    dialog.dismiss();
                }
            } catch (Exception e) {
            }

        }
    }
}
