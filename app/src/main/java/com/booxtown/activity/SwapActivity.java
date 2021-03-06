package com.booxtown.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.adapter.AdapterSwap;
import com.booxtown.controller.BookController;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.GPSTracker;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.TransactionController;
import com.booxtown.controller.UserController;
import com.booxtown.model.Book;
import com.booxtown.model.BookSwap;
import com.booxtown.model.Notification;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;

import com.booxtown.controller.ObjectCommon;

public class SwapActivity extends AppCompatActivity {

    ArrayList<BookSwap> listSwap;
    ListView listView;
    AdapterSwap adapter;
    Book bookIntent;
    float longitude = 0;
    float latitude = 0;
    View view_menu_top;
    TextView txtTitle, btn_Swap, btn_add_book;
    ImageView img_component, imageView_back;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_book);
        //------------------------------------------------------------
        gpsTracker= new GPSTracker(SwapActivity.this);
        init();
        SharedPreferences pref = SwapActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final String session_id = pref.getString("session_id", null);
        txtTitle.setText("Swap");
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        img_component.setVisibility(View.INVISIBLE);
        imageView_back.setImageResource(R.drawable.btn_sign_in_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bookIntent = (Book) getIntent().getSerializableExtra("Book");
        btn_Swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(SwapActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custompopup_screen78);

                TextView txtTitle = (TextView) dialog.findViewById(R.id.tv_author);
                txtTitle.setText("Your request has been sent successfully. Let's wait for " + bookIntent.getUsername() + "'s reply");

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                ImageView btnclose = (ImageView) dialog.findViewById(R.id.close_popup);
                TextView btnbacktohome = (TextView) dialog.findViewById(R.id.backhome);
                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                ArrayList<BookSwap> filterList = getFilteredList(adapter.getList());
                if (filterList.size() > 0) {
                    btnbacktohome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserID us = new UserID(SwapActivity.this);
                            us.execute(session_id);
                        }
                    });
                }
            }
        });


        btn_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwapActivity.this, AddbookActivity.class);
                intent.putExtra("book", bookIntent);
                startActivity(intent);
                finish();
            }
        });

        int is_current_location = pref.getInt("is_current_location", 1);
        if (is_current_location == 1) {
            longitude = (float) gpsTracker.getLongitude();
            latitude = (float) gpsTracker.getLatitude();
        } else {
            longitude = Float.parseFloat(pref.getString("Longitude", (float) gpsTracker.getLongitude() + ""));
            latitude = Float.parseFloat(pref.getString("Latitude", (float) gpsTracker.getLatitude() + ""));
        }
        listingAsync listingAsync = new listingAsync(SwapActivity.this);
        listingAsync.execute(session_id);
    }

    public void init() {
        btn_add_book = (TextView) findViewById(R.id.btn_add_book);
        btn_Swap = (TextView) findViewById(R.id.btn_swap);
        imageView_back = (ImageView) findViewById(R.id.img_menu);
        img_component = (ImageView) findViewById(R.id.img_menu_component);
        view_menu_top = (View) findViewById(R.id.menu_top_swap);
        txtTitle = (TextView) view_menu_top.findViewById(R.id.txt_title);
    }

    public ArrayList<BookSwap> getFilteredList(List<BookSwap> bookList) {
        ArrayList<BookSwap> filterList = new ArrayList<>();
        if (bookList.get(0).ischeck()) {
            for (BookSwap bw : bookList) {
                filterList.add(bw);
            }
        } else {
            for (BookSwap bw : bookList) {
                if (bw.ischeck()) {
                    filterList.add(bw);
                }
            }
        }
        return filterList;
    }

    class listingAsync extends AsyncTask<String, Void, List<Book>> {
        Context context;
        ProgressDialog dialog;
        List<Book> listemp;

        public listingAsync(Context context) {
            this.context = context;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if (!check) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            listemp = new ArrayList<>();
            BookController bookController = new BookController();
            //listemp = bookController.getAllBookById(context,strings[0]);
            listemp = bookController.getAllBookInApp(0, 1000, 100, longitude, latitude, "", "", pref.getString("session_id", ""), Integer.parseInt(pref.getString("user_id", "0")), 10000, 0);
            return listemp;
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
        protected void onPostExecute(List<Book> books) {
            if (books == null || books.size() == 0) {
                final Dialog dialogs = new Dialog(SwapActivity.this);
                dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogs.setContentView(R.layout.dialog_addbook_swap);

                dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogs.show();

                TextView btn_addbook_dialog = (TextView) dialogs.findViewById(R.id.btn_addbook_dialog);
                btn_addbook_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SwapActivity.this, AddbookActivity.class);
                        intent.putExtra("book", bookIntent);
                        startActivity(intent);
                        dialogs.dismiss();
                        dialog.dismiss();
                        finish();
                    }
                });

                ImageView close_dialog = (ImageView) dialogs.findViewById(R.id.close_dialog);
                close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogs.dismiss();
                        dialog.dismiss();
                        finish();
                    }
                });

            } else {
                listSwap = new ArrayList<>();
                /*BookSwap bookSwap = new BookSwap();
                bookSwap.setIscheck(false);
                bookSwap.setValue("Any");
                bookSwap.setBook_id("0");
                bookSwap.setUser_name("0");
                listSwap.add(bookSwap);*/
                for (int i = 0; i < books.size(); i++) {
                    BookSwap book = new BookSwap();
                    book.setIscheck(false);
                    book.setValue(books.get(i).getTitle());
                    book.setBook_id(books.get(i).getId());
                    book.setUser_name(books.get(i).getUsername());
                    listSwap.add(book);
                }
                adapter = new AdapterSwap(SwapActivity.this, listSwap);
                listView = (ListView) findViewById(R.id.listView_swap);
                listView.setAdapter(adapter);
                dialog.dismiss();
            }

            super.onPostExecute(books);
        }
    }

    class transactionInsert extends AsyncTask<Void, Void, String> {

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        String session_id, buyUserID, sellUserID, buyBookID, sellBookID, action;

        public transactionInsert(Context context, String session_id, String buyUserID, String sellUserID, String buyBookID, String sellBookID, String action) {
            this.context = context;
            this.session_id = session_id;
            this.buyBookID = buyBookID;
            this.sellUserID = sellUserID;
            this.buyUserID = buyUserID;
            this.sellBookID = sellBookID;
            this.action = action;
        }

        @Override
        protected String doInBackground(Void... voids) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if (!check) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            String transactionID = "";
            TransactionController transactionController = new TransactionController();
            transactionID = transactionController.transactionInsert(buyUserID, sellUserID, buyBookID, sellBookID, action, session_id);
            return transactionID;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String transactionID) {
            if (transactionID == null) {

            } else if (transactionID.equals("isTrial")) {
                Toast.makeText(context, "Upgrade your membership", Toast.LENGTH_SHORT).show();
            } else {

                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String firstName = pref.getString("firstname", "");

                List<Hashtable> list = new ArrayList<>();
                ArrayList<BookSwap> filterList = getFilteredList(adapter.getList());
                Notification notification = new Notification("Swap Request", transactionID, "9");
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", sellUserID);
                obj.put("messages", firstName + " sent you a Swap request");
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);

                onBackPressed();
            }
            super.onPostExecute(transactionID);
        }
    }

    class UserID extends AsyncTask<String, Void, String> {
        Context context;

        public UserID(Context context) {
            this.context = context;
        }

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if (!check) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            UserController userController = new UserController(context);
            String user_id = userController.getUserID(strings[0]);
            return user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SwapActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String user_ID) {
            try {
                SharedPreferences pref = SwapActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String session_id = pref.getString("session_id", null);
                ArrayList<BookSwap> filterList = getFilteredList(adapter.getList());
                String buyBookID = "";
                for (int i = 0; i < filterList.size(); i++) {
                    if (i == 0) {
                        buyBookID = filterList.get(i).getBook_id();
                    } else {
                        buyBookID = buyBookID + "_+_" + filterList.get(i).getBook_id();
                    }
                }
                transactionInsert transactionInsert = new transactionInsert(SwapActivity.this, session_id, user_ID, bookIntent.getUser_id(), buyBookID, bookIntent.getId(), "swap");
                transactionInsert.execute();

                Intent intent = new Intent(SwapActivity.this, MainAllActivity.class);
                startActivity(intent);
                finish();

                //}
            } catch (Exception e) {
                String ssss = e.getMessage();
                // Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }

}