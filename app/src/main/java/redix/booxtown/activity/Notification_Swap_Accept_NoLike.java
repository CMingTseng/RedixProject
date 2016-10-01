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
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class Notification_Swap_Accept_NoLike extends AppCompatActivity {
    private MenuBottomCustom bottomListings;


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
    String trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_swap_accept_no_like);

        TextView txt_menu_notification_infor2_phone = (TextView) findViewById(R.id.txt_menu_notification_infor2_phone);
        txt_menu_notification_infor2_phone.setVisibility(View.GONE);
        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("Notifications");
        //infor
        ImageView imv_nitification_infor3_phone = (ImageView) findViewById(R.id.imv_menu_notification_infor2);
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
        View view_bottom = (View) findViewById(R.id.menu_bottom_swapnolike);
        bottomListings = new MenuBottomCustom(view_bottom, this, 0);
        bottomListings.setDefaut(0);
        //---------------------------------------------------------------



        include27= (RelativeLayout) findViewById(R.id.include27);
        include27= (RelativeLayout) findViewById(R.id.include27);

        txt_menu_notification_title2_layout27 = (TextView) include27.findViewById(R.id.txt_menu_notification_title2);
        txt_menu_notification_title2_layout28 = (TextView) include28.findViewById(R.id.txt_menu_notification_title2);

        txt_user_hi = (TextView) findViewById(R.id.txt_user_hi);
        txt_title_book_sell_not_swap = (TextView) findViewById(R.id.txt_title_book_sell_not_swap);
        txt_author_book_sell_not_swap = (TextView) findViewById(R.id.txt_author_book_sell_not_swap);
        txt_title_book_buy_not_swap = (TextView) findViewById(R.id.txt_title_book_buy_not_swap);
        txt_author_book_buy_not_swap = (TextView) findViewById(R.id.txt_author_book_buy_not_swap);
        txt_author_info2 = (TextView) findViewById(R.id.txt_author_info2);

        txt_menu_notification_title2_layout27.setText("you rejected swapping your book");
        txt_menu_notification_title2_layout28.setText("with book");

        trans = getIntent().getStringExtra("trans_id");
        transAsync transAsync= new transAsync(Notification_Swap_Accept_NoLike.this, trans);
        transAsync.execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        bottomListings.setDefaut(0);
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
                    SharedPreferences pref = Notification_Swap_Accept_NoLike.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor  = pref.edit();
                    String userName = pref.getString("username", null);
                    txt_user_hi.setText("Hi "+ userName+",");

                    txt_title_book_sell_not_swap.setText(trans.getBook_name());
                    txt_author_book_sell_not_swap.setText(trans.getBook_author());
                    txt_title_book_buy_not_swap.setText(list.get(0).getTitle());
                    txt_author_book_buy_not_swap.setText(list.get(0).getAuthor());
                    txt_author_info2.setText(trans.getUser_sell());

                    dialog.dismiss();
                }
            } catch (Exception e) {
            }

        }
    }
}
