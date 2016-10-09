package redix.booxtown.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.controller.NotificationController;
import redix.booxtown.controller.ObjectCommon;
import redix.booxtown.controller.TransactionController;
import redix.booxtown.custom.CustomListviewNotificationSwap;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.custom.NotificationAccept;
import redix.booxtown.model.Book;
import redix.booxtown.model.Notification;
import redix.booxtown.model.Transaction;

public class NotificationSellActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;

    TextView txt_author_info3;
    TextView txt_user_hi;
    TextView txt_title_book_buy;
    TextView txt_author_book_buy;
    TextView txt_price_book_buy;

    boolean flag = true;
    boolean flagChoose = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_sell);

        img_menu_bottom_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView) findViewById(R.id.img_menu_bottom_user);

        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);

        txt_author_info3 = (TextView) findViewById(R.id.txt_author_info3);
        txt_user_hi = (TextView) findViewById(R.id.txt_user_hi);
        txt_title_book_buy = (TextView) findViewById(R.id.txt_title_book_buy);
        txt_author_book_buy = (TextView) findViewById(R.id.txt_author_book_buy);
        txt_price_book_buy = (TextView) findViewById(R.id.txt_price_book_buy);

        // lấy được list sách swap đẻ đổ vào listview
        final String trans_id = getIntent().getStringExtra("trans_id");
        transAsync transAsync = new transAsync(NotificationSellActivity.this, trans_id, 0);
        transAsync.execute();

        TextView txt_notification_infor3_phone = (TextView) findViewById(R.id.txt_notification_infor3_phone);
        txt_notification_infor3_phone.setVisibility(View.GONE);

        TextView txt_menu_notification_infor3_title = (TextView) findViewById(R.id.txt_menu_notification_infor3_title);
        txt_menu_notification_infor3_title.setText("wants to buy your book");


        //menu

        ImageView img_menu_component = (ImageView) findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.GONE);

        TextView txtTitle = (TextView) findViewById(R.id.txt_title);
        txtTitle.setText("Notifications");

        ImageView img_menu = (ImageView) findViewById(R.id.img_menu);
        img_menu.setImageResource(R.drawable.btn_sign_in_back);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //end

        //infor
        ImageView imv_nitification_infor3_phone = (ImageView) findViewById(R.id.imv_nitification_infor3_phone);
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
                Intent intent3 = new Intent(NotificationSellActivity.this, MainAllActivity.class);
                intent3.putExtra("key", "3");
                startActivity(intent3);
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

        public transAsync(Context context, String trans_id, int type) {
            this.context = context;
            this.trans_id = trans_id;
            this.type = type;
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
            if (transaction == null) {
                dialog.dismiss();
            } else {

                SharedPreferences pref = NotificationSellActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String session_id = pref.getString("session_id", null);
                String userName = pref.getString("username", null);
                txt_user_hi.setText("Hi " + userName + ",");

                txt_price_book_buy.setText("AED " + transaction.getBook_price());
                txt_author_info3.setText(transaction.getUser_buy() + "");
                txt_title_book_buy.setText(transaction.getBook_name());
                txt_author_book_buy.setText(transaction.getBook_author());

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
                } else if (type == 0) {
                    Button btn_menu_notification_dominic_accept = (Button) findViewById(R.id.btn_menu_notification_dominic_accept);
                    btn_menu_notification_dominic_accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (flag || (transaction.getIs_accept() == 0 && transaction.getIs_cancel() == 0 && transaction.getIs_reject() == 0)) {
                                Intent intent = new Intent(NotificationSellActivity.this, NotificationSellNoAccept.class);
                                intent.putExtra("trans_id", trans_id);
                                startActivity(intent);
                                flag = false;

                                final String trans_id = getIntent().getStringExtra("trans_id");
                                transAsync transAsync = new transAsync(NotificationSellActivity.this, trans_id, 1);
                                transAsync.execute();
                            } else {
                                Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    Button btn_menu_notification_dominic_reject = (Button) findViewById(R.id.btn_menu_notification_dominic_reject);
                    btn_menu_notification_dominic_reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (flag || (transaction.getIs_accept() == 0 && transaction.getIs_cancel() == 0 && transaction.getIs_reject() == 0)) {
                                final String trans_id = getIntent().getStringExtra("trans_id");
                                transAsync transAsync = new transAsync(NotificationSellActivity.this, trans_id, 2);
                                transAsync.execute();
                                flag = false;
                            } else {
                                Toast.makeText(NotificationSellActivity.this, "The transaction is done!", Toast.LENGTH_SHORT).show();
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
        ProgressDialog dialog;
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
            if (type == 1) {
                List<Hashtable> list = new ArrayList<>();
                Notification notification = new Notification("Accepted your request for buying book", trans.getId() + "", "7");
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", trans.getUser_buyer_id());
                obj.put("messages", "Accepted your request for buying book");
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);
                // end

                // send notifi user seller

                List<Hashtable> listSeller = new ArrayList<>();
                Notification notificationSeller = new Notification("You accepted for request buy your book", trans.getId() + "", "5");
                Hashtable objSeller = ObjectCommon.ObjectDymanic(notificationSeller);
                objSeller.put("user_id", trans.getUser_seller_id());
                objSeller.put("messages", "You accepted for request buy your book by");
                listSeller.add(objSeller);
                NotificationController controllerSeller = new NotificationController();
                controllerSeller.sendNotification(listSeller);
            } else if (type == 2) {
                List<Hashtable> list = new ArrayList<>();
                Notification notification = new Notification("Rejected your request for buying book", trans.getId() + "", "8");
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", trans.getUser_buyer_id());
                obj.put("messages", "Rejected your request for buying book");
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);
                // end

                // send notifi user seller

                List<Hashtable> listSeller = new ArrayList<>();
                Notification notificationSeller = new Notification("You reject a request buy your book", trans.getId() + "", "6");
                Hashtable objSeller = ObjectCommon.ObjectDymanic(notificationSeller);
                objSeller.put("user_id", trans.getUser_seller_id());
                objSeller.put("messages", "You reject a request buy your book");
                listSeller.add(objSeller);
                NotificationController controllerSeller = new NotificationController();
                controllerSeller.sendNotification(listSeller);
            }
            super.onPostExecute(transactionID);
        }
    }
}
