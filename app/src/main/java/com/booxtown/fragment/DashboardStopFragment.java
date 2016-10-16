package com.booxtown.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.BookController;
import com.booxtown.controller.Information;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.controller.TransactionController;
import com.booxtown.controller.UserController;
import com.booxtown.model.Book;
import com.booxtown.model.DashBoard;
import com.booxtown.model.Notification;
import com.booxtown.model.Transaction;
import com.booxtown.model.User;

/**
 * Created by thuyetpham94 on 29/08/2016.
 */
public class DashboardStopFragment extends Fragment {
    TextView txt_menu_dashboard_cancel,title_menu;
    TextView textView_namebook_seller,textView_nameauthor_seller,textView_namebook_buyer,textView_nameauthor_buyer;
    Button btn_menu_dashboard_bottom_rate,btn_menu_dashboard_bottom_cancel;
    ImageView img_menu_dashboard_bottom_status,img_menu,img_menu_component;

    //user profile
    CircularImageView img_menu_dashboard_middle;
    TextView textView_username_dashboard_middle,textView_phone_dashboard_middle,textView_with;
    RatingBar ratingBar_user_dashboard_middle;
    String img_username,username;
    ImageView img_free_listings;
    CircularImageView img_rank1_satus,img_rank2_satus,img_rank3_satus;
    DashBoard dashBoard;
    User user;
    String bookName="";
    String trans_id="";
    Transaction trans;
    //end
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        init(view);
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final String session_id = pref.getString("session_id", null);

        dashBoard = (DashBoard)getArguments().getSerializable("dashboard");
        user = (User)getArguments().getSerializable("user");
        trans_id=getArguments().getString("trans");
        transAsyncs transAsyncs= new transAsyncs(getContext(),trans_id);
        transAsyncs.execute();
        txt_menu_dashboard_cancel.setVisibility(View.GONE);
        btn_menu_dashboard_bottom_rate.setBackgroundResource(R.drawable.btn_xam);
        img_menu_dashboard_bottom_status.setImageResource(R.drawable.myprofile_not);


        btn_menu_dashboard_bottom_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_cancel_dashboard);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button btn_cancel_dialog_dashboard = (Button)dialog.findViewById(R.id.btn_cancel_dialog_dashboard);
                btn_cancel_dialog_dashboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("dashboard", dashBoard);
                        bundle.putSerializable("user", user);
                        DashboardDeleteFragment fragment= new DashboardDeleteFragment();
                        fragment.setArguments(bundle);
                        callFragment(fragment);

                        transactionChangeStatus updatestatus = new transactionChangeStatus(getContext(),session_id,
                                String.valueOf(dashBoard.getId()),"2",String.valueOf(dashBoard.getBook_swap_id()));
                        updatestatus.execute();
                        dialog.dismiss();
                    }
                });

                ImageView img_close_dialog_cancel_dashboard = (ImageView)dialog.findViewById(R.id.img_close_dialog_cancel_dashboard);
                img_close_dialog_cancel_dashboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        //menu
        img_menu.setImageResource(R.drawable.btn_sign_in_back);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                MyProfileDashboardFragment fragment= new MyProfileDashboardFragment();
                fragment.setArguments(bundle);
                callFragment(fragment);
            }
        });
        title_menu.setText("Dashboard");
        img_menu_component.setVisibility(View.GONE);
        getUser getUser = new getUser(getContext(),dashBoard.getUser_seller_id());
        getUser.execute();
        if(dashBoard.getAction().equals("swap")){
            getBookByID getBookByID = new getBookByID(getContext(),String.valueOf(dashBoard.getBook_swap_id()));
            getBookByID.execute();
            textView_namebook_buyer.setVisibility(View.VISIBLE);
            textView_nameauthor_buyer.setVisibility(View.VISIBLE);
        }else if(dashBoard.getAction().equals("buy")){
            textView_namebook_buyer.setVisibility(View.GONE);
            textView_nameauthor_buyer.setVisibility(View.GONE);
            Picasso.with(getContext()).load(R.drawable.explore_btn_buy_active).into(img_free_listings);
            textView_with.setVisibility(View.GONE);
        }else if(dashBoard.getAction().equals("free")){
            textView_namebook_buyer.setVisibility(View.GONE);
            textView_nameauthor_buyer.setVisibility(View.GONE);
            Picasso.with(getContext()).load(R.drawable.explore_btn_free_active).into(img_free_listings);
            textView_with.setVisibility(View.GONE);
        }

        textView_namebook_seller.setText(dashBoard.getBook_seller());
        textView_nameauthor_seller.setText(dashBoard.getAuthor());
        //end
        return view;
    }

    public void init(View view){
        img_rank1_satus = (CircularImageView) view.findViewById(R.id.img_rank1_satus);
        img_rank2_satus = (CircularImageView) view.findViewById(R.id.img_rank2_satus);
        img_rank3_satus = (CircularImageView) view.findViewById(R.id.img_rank3_satus);

        img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        title_menu = (TextView)getActivity().findViewById(R.id.txt_title);
        img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);

        txt_menu_dashboard_cancel = (TextView)view.findViewById(R.id.txt_menu_dashboard_cancel);
        btn_menu_dashboard_bottom_rate = (Button) view.findViewById(R.id.btn_menu_dashboard_bottom_rate);
        img_menu_dashboard_bottom_status = (ImageView)view.findViewById(R.id.img_menu_dashboard_bottom_status);
        btn_menu_dashboard_bottom_cancel = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_cancel);

        textView_namebook_seller = (TextView)view.findViewById(R.id.textView_namebook_seller);
        textView_nameauthor_seller = (TextView)view.findViewById(R.id.textView_nameauthor_seller);
        textView_namebook_buyer = (TextView)view.findViewById(R.id.textView_namebook_buyer);
        textView_nameauthor_buyer = (TextView)view.findViewById(R.id.textView_nameauthor_buyer);
        textView_with = (TextView)view.findViewById(R.id.textView_with);
        img_free_listings = (ImageView)view.findViewById(R.id.img_free_listings);

        //user profile
        img_menu_dashboard_middle = (CircularImageView)view.findViewById(R.id.img_menu_dashboard_middle);
        textView_username_dashboard_middle = (TextView)view.findViewById(R.id.textView_username_dashboard_middle);
        textView_phone_dashboard_middle = (TextView)view.findViewById(R.id.textView_phone_dashboard_middle);
        ratingBar_user_dashboard_middle = (RatingBar)view.findViewById(R.id.ratingBar_user_dashboard_middle);
        //end
    }

    public void callFragment(Fragment fragment ){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    class getUser extends AsyncTask<Void,Void,List<User>> {

        Context context;
        int user_id;
        ProgressDialog progressDialog;
        public getUser(Context context,int user_id){
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
            UserController userController = new UserController(context);
            return userController.getByUserId(user_id);
        }

        @Override
        protected void onPostExecute(List<User> user) {
            try {
                if (user.size() > 0){
                    Picasso.with(context)
                            .load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length()))
                            .error(R.drawable.user)
                            .into(img_menu_dashboard_middle);
                    textView_username_dashboard_middle.setText(user.get(0).getUsername());
                    img_username = ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length());
                    username = user.get(0).getUsername();

                    ratingBar_user_dashboard_middle.setRating(user.get(0).getRating());
                    ratingBar_user_dashboard_middle.setRating(user.get(0).getRating());
                    LayerDrawable stars = (LayerDrawable) ratingBar_user_dashboard_middle.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.rgb(255,2224,0), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                    DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),context.getResources().getColor(R.color.bg_rating));

                    textView_phone_dashboard_middle.setText(user.get(0).getPhone());

                    //set rank
                    if(user.get(0).getContributor() == 0){
                        img_rank1_satus.setVisibility(View.VISIBLE);
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_one);
                        img_rank1_satus.setImageBitmap(btn1);

                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_two);
                        img_rank1_satus.setImageBitmap(btn1);

                    }
                    if(user.get(0).getGoldenBook() == 0){
                        img_rank2_satus.setVisibility(View.GONE);
                    }else if(user.get(0).getGoldenBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.golden_book);
                        img_rank2_satus.setImageBitmap(btn1);
                        img_rank2_satus.setVisibility(View.VISIBLE);
                    }

                    if(user.get(0).getListBook() == 0){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.newbie);
                        img_rank3_satus.setImageBitmap(btn1);
                        img_rank3_satus.setVisibility(View.VISIBLE);
                    }else if(user.get(0).getListBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bookworm);
                        img_rank3_satus.setImageBitmap(btn1);
                        img_rank3_satus.setVisibility(View.VISIBLE);
                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bibliophile);
                        img_rank3_satus.setImageBitmap(btn1);

                        img_rank3_satus.setVisibility(View.VISIBLE);
                    }


                    progressDialog.dismiss();
                }else {
                    Toast.makeText(context,Information.noti_no_data,Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }catch (Exception e){

            }
            progressDialog.dismiss();
        }
    }

    class getBookByID extends AsyncTask<Void, Void, List<Book>> {
        String id;
        Context ctx;
        ProgressDialog dialog;
        public getBookByID(Context ctx,String id) {
            this.id = id;
            this.ctx = ctx;
        }

        @Override
        protected List<Book> doInBackground(Void... params) {
            BookController bookController = new BookController();
            return bookController.getBookByID(id);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ctx);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(List<Book> list) {
            try {
                if (list.size() > 0) {
                    textView_namebook_buyer.setText(list.get(0).getTitle()+"");
                    textView_nameauthor_buyer.setText(list.get(0).getAuthor());
                    bookName= list.get(0).getTitle();
                    dialog.dismiss();
                }
            } catch (Exception e) {
                dialog.dismiss();
            }
            dialog.dismiss();

        }
    }
    class transAsyncs extends AsyncTask<String, Void, Transaction> {

        Context context;
        ProgressDialog dialog;
        String trans_id;

        public transAsyncs(Context context, String trans_id) {
            this.context = context;
            this.trans_id = trans_id;
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
                trans= transaction;
            }
            super.onPostExecute(transaction);
        }
    }
    class transactionChangeStatus extends AsyncTask<Void, Void, String> {

        Context context;
        ProgressDialog dialog;
        Book book;
        String session_id, trans_id, status_id;
        String book_id;
        public transactionChangeStatus(Context context, String session_id, String trans_id, String status_id, String book_id) {
            this.context = context;
            this.session_id = session_id;
            this.trans_id = trans_id;
            this.status_id = status_id;
            this.book_id = book_id;
            this.book = book;
        }

        @Override
        protected String doInBackground(Void... voids) {
            TransactionController transactionController = new TransactionController();
            return transactionController.transactionUpdateStatus(session_id, trans_id, status_id, book_id);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String transactionID) {

            try {
                UserID us= new UserID(getContext(), trans);
                us.execute(session_id);

            }catch (Exception e){

            }
        }
    }

    class UserID extends AsyncTask<String,Void,String>{
        Context context;
        Transaction trans;

        public UserID(Context context,Transaction trans ){
            this.context=context;
            this.trans= trans;
        }
        @Override
        protected String doInBackground(String... strings) {
            UserController userController  = new UserController(context);
            String user_id = userController.getUserID(strings[0]);
            return user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String user_ID) {
            try {
                //if(!threads.getUser_id().equals(user_ID)) {
                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String firstName = pref.getString("firstname", "");

                List<Hashtable> list = new ArrayList<>();
                if(user_ID.equals(trans.getUser_buyer_id()+"")) {
                    Notification notification = new Notification("Cancel Transaction",trans.getId()+"", "12");
                    Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                    obj.put("user_id", trans.getUser_seller_id());
                    obj.put("messages",firstName+ " cancelled a transaction related to " + bookName);

                    list.add(obj);
                }else{
                    Notification notification = new Notification("Cancel Transaction", trans.getId()+"", "12");
                    Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                    obj.put("user_id", trans.getUser_buyer_id());
                    obj.put("messages", "You cancelled a transaction related to " + bookName);

                    list.add(obj);
                }

                NotificationController controller = new NotificationController();
                controller.sendNotification(list);

                //}
            }catch (Exception e){
                String ssss= e.getMessage();
               // Toast.makeText(context,"no data",Toast.LENGTH_LONG).show();
            }

        }
    }
}
