package redix.booxtown.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterExplore;
import redix.booxtown.adapter.AdapterInteractThreadDetails;
import redix.booxtown.adapter.AdapterSwap;
import redix.booxtown.adapter.CustomPagerAdapter;
import redix.booxtown.adapter.ListBookAdapter;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.NotificationController;
import redix.booxtown.controller.ObjectCommon;
import redix.booxtown.controller.TransactionController;
import redix.booxtown.controller.UserController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;
import redix.booxtown.model.BookSwap;
import redix.booxtown.model.Explore;
import redix.booxtown.model.InteractComment;
import redix.booxtown.model.Notification;

public class SwapActivity extends AppCompatActivity {

    ArrayList<BookSwap> listSwap = new ArrayList<>();
    ListView listView;
    AdapterSwap adapter;
    Book bookIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_book);
        //------------------------------------------------------------
        View view_menu_top = (View) findViewById(R.id.menu_top_swap);
        TextView txtTitle = (TextView) view_menu_top.findViewById(R.id.txt_title);
        txtTitle.setText("Swap");
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        ImageView img_component = (ImageView) findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.INVISIBLE);
        ImageView imageView_back = (ImageView) findViewById(R.id.img_menu);
        imageView_back.setImageResource(R.drawable.btn_sign_in_back);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView btn_Swap = (TextView) findViewById(R.id.btn_swap);
        TextView btn_add_book = (TextView) findViewById(R.id.btn_add_book);
        bookIntent = (Book) getIntent().getSerializableExtra("Book");

        btn_Swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(SwapActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custompopup_screen78);
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
                if(filterList.size()>0) {
                    btnbacktohome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences pref = SwapActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            String session_id = pref.getString("session_id", null);

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
                startActivity(intent);
            }
        });

        listingAsync listingAsync = new listingAsync(SwapActivity.this);
        SharedPreferences pref = SwapActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String session_id = pref.getString("session_id", null);
        listingAsync.execute(session_id);
    }

    public ArrayList<BookSwap> getFilteredList(List<BookSwap> bookList) {
        ArrayList<BookSwap> filterList = new ArrayList<>();
        for (BookSwap bw : bookList) {
            if (bw.ischeck()) {
                filterList.add(bw);
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
            listemp = new ArrayList<>();
            BookController bookController = new BookController();
            listemp = bookController.getAllBookById(strings[0]);
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
            if (books == null) {
                dialog.dismiss();
            } else {
                BookSwap book;
                for (int i = 0; i < books.size(); i++) {
                    book = new BookSwap();
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
            String transactionID = "";
            TransactionController transactionController = new TransactionController();
            transactionID = transactionController.transactionInsert(buyUserID, sellUserID, buyBookID, sellBookID, action,session_id);
            return transactionID;
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
        protected void onPostExecute(String transactionID) {
            if (transactionID == "") {
                dialog.dismiss();
            } else {
                List<Hashtable> list = new ArrayList<>();
                ArrayList<BookSwap> filterList = getFilteredList(adapter.getList());
                Notification notification = new Notification(filterList.get(0).getUser_name().toLowerCase()+" sent a swap request", "BTNotiSwapRequest", transactionID);
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", sellUserID);
                obj.put("messages", "Request Swap book by " + buyUserID);
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);
                dialog.dismiss();

                Intent iten = new Intent(SwapActivity.this, MainAllActivity.class);
                startActivity(iten);
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
            UserController userController = new UserController();
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
                SharedPreferences.Editor editor = pref.edit();
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
                //}
            } catch (Exception e) {
                String ssss = e.getMessage();
                Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }

}