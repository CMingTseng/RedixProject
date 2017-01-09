package com.booxtown.fragment;

import android.app.Dialog;
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
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.activity.SignIn_Activity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.BookController;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.Information;
import com.booxtown.controller.TransactionController;
import com.booxtown.controller.UserController;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.booxtown.R;

import com.booxtown.model.Book;
import com.booxtown.model.DashBoard;
import com.booxtown.model.User;

public class DashboardStatusFragment extends Fragment {
    DashBoard dashBoard;
    ImageView img_menu;
    TextView txt_menu_dashboard_cancel;
    Button btn_menu_dashboard_bottom_cancel;
    ImageView img_menu_component;
    RadioButton radioButton2_dashboard,radioButton_dashboard;
    TextView title_menu;
    Button btn_menu_dashboard_bottom_rate;
    TextView textView_namebook_seller,textView_nameauthor_seller,textView_namebook_buyer,textView_nameauthor_buyer;

    //user profile
    CircularImageView img_menu_dashboard_middle,imageView_username_rating;
    TextView textView_username_dashboard_middle,textView_phone_dashboard_middle,textView_username_rating,textView_with;
    RatingBar ratingBar_user_dashboard_middle;
    ImageView img_free_listings;
    CircularImageView img_rank1_satus,img_rank2_satus,img_rank3_satus;
    String img_username,username;
    User user;
    int user_id;
    String userID="";
    //end

    //dialog rating
    RatingBar rating_promp,rating_cour,rating_quality;
    //end

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        init(view);
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final String session_id =  pref.getString("session_id", null);
        final String user_name = pref.getString("username",null);
        user_id = Integer.valueOf(pref.getString("user_id",null));
        dashBoard = (DashBoard)getArguments().getSerializable("dashboard");
        user = (User) getArguments().getSerializable("user");
        userID= getArguments().getString("user_id");
        //menu
        btn_menu_dashboard_bottom_cancel.setVisibility(View.GONE);
        txt_menu_dashboard_cancel.setVisibility(View.GONE);
        //menu

        img_menu.setImageResource(R.drawable.btn_sign_in_back);
        title_menu.setText("Dashboard");
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

        img_menu_component.setVisibility(View.GONE);
            btn_menu_dashboard_bottom_rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dashBoard.getUser_promp() != 0 || dashBoard.getUser_cour() != 0 || dashBoard.getUser_quality() !=0){
                        Toast.makeText(getContext(), Information.noti_tran_done,Toast.LENGTH_SHORT).show();
                    }else{
                    final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_dashboard_status);
//                    dialog.getWindow().setBackgroundDrawable();
                    dialog.show();

                        radioButton_dashboard = (RadioButton) dialog.findViewById(R.id.radioButton_dashboard);
                        radioButton2_dashboard = (RadioButton) dialog.findViewById(R.id.radioButton2_dashboard);

                    //int rating dialog
                    rating_promp = (RatingBar)dialog.findViewById(R.id.rating_promp);
                    rating_cour = (RatingBar)dialog.findViewById(R.id.rating_cour);
                    rating_quality = (RatingBar)dialog.findViewById(R.id.rating_quality);
                    LayerDrawable stars1 = (LayerDrawable) rating_promp.getProgressDrawable();
                    stars1.getDrawable(2).setColorFilter(Color.rgb(249,242,0), PorterDuff.Mode.SRC_ATOP);
                    stars1.getDrawable(0).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                    stars1.getDrawable(1).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                    DrawableCompat.setTint(DrawableCompat.wrap(stars1.getDrawable(1)),getResources().getColor(R.color.bg_rating));

                    LayerDrawable stars2 = (LayerDrawable) rating_cour.getProgressDrawable();
                    stars2.getDrawable(2).setColorFilter(Color.rgb(249,242,0), PorterDuff.Mode.SRC_ATOP);
                        stars2.getDrawable(0).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                        stars2.getDrawable(1).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                        DrawableCompat.setTint(DrawableCompat.wrap(stars2.getDrawable(1)),getResources().getColor(R.color.bg_rating));

                    LayerDrawable stars3 = (LayerDrawable) rating_quality.getProgressDrawable();
                    stars3.getDrawable(2).setColorFilter(Color.rgb(249,242,0), PorterDuff.Mode.SRC_ATOP);
                        stars3.getDrawable(0).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                        stars3.getDrawable(1).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                        DrawableCompat.setTint(DrawableCompat.wrap(stars3.getDrawable(1)),getResources().getColor(R.color.bg_rating));

                    imageView_username_rating = (CircularImageView)dialog.findViewById(R.id.imageView_username_rating);
                    textView_username_rating = (TextView)dialog.findViewById(R.id.textView_username_rating);
                    //end
                        if(img_username==null){
                            Picasso.with(getContext())
                                    .load(R.mipmap.user_empty)
                                    .into(imageView_username_rating);
                        }
                        else {
                            Picasso.with(getContext())
                                    .load(img_username)
                                    .into(imageView_username_rating);
                        }
                    textView_username_rating.setText(username);
                    Button btn_rate_dashboard_status = (Button)dialog.findViewById(R.id.btn_rate_dashboard_status);
                    btn_rate_dashboard_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (radioButton_dashboard.isChecked()){
                                int status_id = 0;
                                if(user_id == dashBoard.getUser_buyer_id())
                                {
                                    status_id = 1;
                                }
                                ratingAsync ratingAsync = new ratingAsync(getContext(),dashBoard.getId(),rating_promp.getRating(),
                                        rating_cour.getRating(),rating_quality.getRating(),status_id);
                                ratingAsync.execute();
                                dialog.dismiss();
                            }else {
                                    Toast.makeText(getActivity(),Information.noti_show_complete,Toast.LENGTH_LONG).show();
                                return;
                            }

                        }
                    });

                    ImageView imv_close_dialog_dashboard_status = (ImageView)dialog.findViewById(R.id.imv_close_dialog_dashboard_status);
                    imv_close_dialog_dashboard_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    }
                }
            });

        //end
        if(dashBoard.getAction().equals("swap")){
            if(userID.equals(dashBoard.getUser_buyer_id())) {
                getBookByID getBookByID = new getBookByID(getContext(), String.valueOf(dashBoard.getBook_seller_id()));
                getBookByID.execute();
            }else{
                String[] listBookID=dashBoard.getBook_buyer_id().replace("_+_","_").split("_");
                if(listBookID.length>0) {
                    getBookByID getBookByID = new getBookByID(getContext(), String.valueOf(listBookID[listBookID.length-1]));
                    getBookByID.execute();
                }else{
                    getBookByID getBookByID = new getBookByID(getContext(), String.valueOf(dashBoard.getBook_buyer_id()));
                    getBookByID.execute();
                }
            }
            textView_namebook_buyer.setVisibility(View.VISIBLE);
            textView_nameauthor_buyer.setVisibility(View.VISIBLE);
        }else if(dashBoard.getAction().equals("buy")){
            textView_namebook_buyer.setVisibility(View.GONE);
            textView_nameauthor_buyer.setVisibility(View.GONE);
            if(dashBoard.getUser_buyer_id()==Integer.parseInt(userID)) {
                Picasso.with(getContext()).load(R.drawable.buy_in).into(img_free_listings);
            }
            if (dashBoard.getUser_seller_id()==Integer.parseInt(userID)){
                Picasso.with(getContext()).load(R.drawable.buy_out).into(img_free_listings);
            }
            textView_with.setVisibility(View.GONE);
        }else if(dashBoard.getAction().equals("free")){
            textView_namebook_buyer.setVisibility(View.GONE);
            textView_nameauthor_buyer.setVisibility(View.GONE);
            if(dashBoard.getUser_buyer_id()==Integer.parseInt(userID)) {
                Picasso.with(getContext()).load(R.drawable.free_int).into(img_free_listings);
            }
            if (dashBoard.getUser_seller_id()==Integer.parseInt(userID)){
                Picasso.with(getContext()).load(R.drawable.free_out).into(img_free_listings);
            }

            textView_with.setVisibility(View.GONE);
        }
        if(userID.equals(dashBoard.getUser_buyer_id())) {
            getUser getUser = new getUser(getContext(),dashBoard.getUser_buyer_id());
            getUser.execute();
        }
        else{
            getUser getUser = new getUser(getContext(),dashBoard.getUser_seller_id());
            getUser.execute();
        }


        textView_namebook_seller.setText(dashBoard.getBook_seller());
        textView_nameauthor_seller.setText(dashBoard.getAuthor());

        return view;
    }

    public void init(View view){
        img_rank1_satus = (CircularImageView) view.findViewById(R.id.img_rank1_satus);
        img_rank2_satus = (CircularImageView)view.findViewById(R.id.img_rank2_satus);
        img_rank3_satus = (CircularImageView)view.findViewById(R.id.img_rank3_satus);

        img_free_listings = (ImageView)view.findViewById(R.id.img_free_listings);
        btn_menu_dashboard_bottom_rate = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_rate);
        title_menu = (TextView)getActivity().findViewById(R.id.txt_title);
        img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);
        txt_menu_dashboard_cancel = (TextView)view.findViewById(R.id.txt_menu_dashboard_cancel);
        btn_menu_dashboard_bottom_cancel = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_cancel);
        textView_namebook_seller = (TextView)view.findViewById(R.id.textView_namebook_seller);
        textView_nameauthor_seller = (TextView)view.findViewById(R.id.textView_nameauthor_seller);
        textView_namebook_buyer = (TextView)view.findViewById(R.id.textView_namebook_buyer);
        textView_nameauthor_buyer = (TextView)view.findViewById(R.id.textView_nameauthor_buyer);
        textView_with = (TextView)view.findViewById(R.id.textView_with);

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
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = ctx.getSharedPreferences("MyPref",ctx.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(ctx, SignIn_Activity.class);
                ctx.startActivity(intent);
                this.cancel(true);
            }
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
                    dialog.dismiss();
                }
            } catch (Exception e) {
                dialog.dismiss();
            }
            dialog.dismiss();

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
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",context.MODE_PRIVATE);
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
                    if(user.get(0).getPhoto().length()>3) {
                        int index = user.get(0).getPhoto().indexOf("_+_");
                        Picasso.with(context)
                                .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + user.get(0).getPhoto().substring(0,index).trim() + "&image=" + user.get(0).getPhoto().substring(index + 3, user.get(0).getPhoto().length()))
                                .placeholder(R.mipmap.user_empty)
                                .into(img_menu_dashboard_middle);
                        img_username =ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + user.get(0).getPhoto().substring(0,index).trim() + "&image=" + user.get(0).getPhoto().substring(index+ 3, user.get(0).getPhoto().length());

                    }
                    else{
                        Picasso.with(getContext())
                                .load(R.mipmap.user_empty)
                                .into(img_menu_dashboard_middle);

                    }
                    textView_username_dashboard_middle.setText(user.get(0).getUsername());
                    //img_username = ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length());
                    username = user.get(0).getUsername();

                    ratingBar_user_dashboard_middle.setRating(user.get(0).getRating());
                    LayerDrawable stars = (LayerDrawable) ratingBar_user_dashboard_middle.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.rgb(255,2224,0), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                    DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),context.getResources().getColor(R.color.bg_rating));

                    textView_phone_dashboard_middle.setText("Phone :"+user.get(0).getPhone());

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
                String error= e.getMessage();
            }
            progressDialog.dismiss();
        }
    }

    class ratingAsync extends AsyncTask<Void,Void,Boolean>{
        Context context;
        int trans_id,user_id;
        float user_promp,user_cour,user_quality;
        ProgressDialog progressDialog;

        public ratingAsync(Context context,int trans_id,float user_promp,float user_cour,float user_quality,int user_id){
            this.context = context;
            this.trans_id = trans_id;
            this.user_promp = user_promp;
            this.user_cour = user_cour;
            this.user_quality = user_quality;
            this.user_id = user_id;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            TransactionController transactionController = new TransactionController();
            return transactionController.updateRating(trans_id,user_promp,user_cour,user_quality,user_id);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),Information.noti_update_success, Toast.LENGTH_LONG).show();
            } else {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),Information.noti_update_fail, Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
    }
}
