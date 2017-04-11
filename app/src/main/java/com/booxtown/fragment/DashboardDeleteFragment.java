package com.booxtown.fragment;

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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.activity.SignIn_Activity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.CheckSession;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import com.booxtown.R;

import com.booxtown.controller.BookController;
import com.booxtown.controller.Information;
import com.booxtown.controller.UserController;
import com.booxtown.model.Book;
import com.booxtown.model.DashBoard;
import com.booxtown.model.User;

/**
 * Created by thuyetpham94 on 29/08/2016.
 */
public class DashboardDeleteFragment extends Fragment {
    ImageView img_menu_dashboard_bottom_status,img_menu,img_menu_component;
    Button btn_menu_dashboard_bottom_rate,btn_menu_dashboard_bottom_cancel;
    TextView textView_namebook_seller,textView_nameauthor_seller,textView_namebook_buyer,textView_nameauthor_buyer,title_menu;

    //user profile
    CircularImageView img_menu_dashboard_middle,imageView_username_rating;
    TextView textView_username_dashboard_middle,textView_phone_dashboard_middle,textView_with;
    RatingBar ratingBar_user_dashboard_middle;
    String img_username,username;
    ImageView img_free_listings;
    CircularImageView img_rank1_satus,img_rank2_satus,img_rank3_satus;;
    DashBoard dashBoard;
    User user;
    String userID="";
    //end

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment,container,false);
        init(view);

        img_menu_dashboard_bottom_status.setImageResource(R.drawable.myprofile_all_not);
        btn_menu_dashboard_bottom_rate.setVisibility(View.GONE);
        btn_menu_dashboard_bottom_cancel.setVisibility(View.GONE);
        dashBoard = (DashBoard)getArguments().getSerializable("dashboard");
        user = (User) getArguments().getSerializable("user");
        userID= getArguments().getString("user_id");

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
        if(userID.equals(dashBoard.getUser_buyer_id()+"")) {
            getUser getUser = new getUser(getContext(), dashBoard.getUser_seller_id());
            getUser.execute();
        }
        else{
            getUser getUser = new getUser(getContext(), dashBoard.getUser_buyer_id());
            getUser.execute();
        }
        if(dashBoard.getAction().equals("swap")){

            if(userID.equals(dashBoard.getUser_buyer_id())) {
                getBookByID getBookByID = new getBookByID(getContext(),String.valueOf(dashBoard.getBook_seller_id()));
                getBookByID.execute();
            }else{
                getBookByID getBookByID = new getBookByID(getContext(), dashBoard.getBook_swap_id()+"");
                getBookByID.execute();
                /*String[] listBookID=dashBoard.getBook_buyer_id().replace("_+_","_").split("_");
                if(listBookID.length>0) {
                    getBookByID getBookByID = new getBookByID(getContext(), String.valueOf(listBookID[listBookID.length-1]));
                    getBookByID.execute();
                }else {
                    getBookByID getBookByID = new getBookByID(getContext(), String.valueOf(dashBoard.getBook_buyer_id()));
                    getBookByID.execute();
                }*/
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
            //Picasso.with(getContext()).load(R.drawable.explore_btn_buy_active).into(img_free_listings);
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
            //Picasso.with(getContext()).load(R.drawable.explore_btn_free_active).into(img_free_listings);
            textView_with.setVisibility(View.GONE);
        }

        textView_namebook_seller.setText(dashBoard.getBook_seller());
        textView_nameauthor_seller.setText(dashBoard.getAuthor());
        return view;
    }

    public void init(View view){
        img_rank1_satus = (CircularImageView) view.findViewById(R.id.img_rank1_satus);
        img_rank2_satus = (CircularImageView) view.findViewById(R.id.img_rank2_satus);
        img_rank3_satus = (CircularImageView) view.findViewById(R.id.img_rank3_satus);

        textView_namebook_seller = (TextView)view.findViewById(R.id.textView_namebook_seller);
        textView_nameauthor_seller = (TextView)view.findViewById(R.id.textView_nameauthor_seller);
        textView_namebook_buyer = (TextView)view.findViewById(R.id.textView_namebook_buyer);
        textView_nameauthor_buyer = (TextView)view.findViewById(R.id.textView_nameauthor_buyer);

        img_menu_component = (ImageView)getActivity().findViewById(R.id.img_menu_component);
        title_menu = (TextView)getActivity().findViewById(R.id.txt_title);
        img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        btn_menu_dashboard_bottom_cancel = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_cancel);
        btn_menu_dashboard_bottom_rate = (Button)view.findViewById(R.id.btn_menu_dashboard_bottom_rate);
        img_menu_dashboard_bottom_status = (ImageView)view.findViewById(R.id.img_menu_dashboard_bottom_status);
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
                    }
                    else{
                        Picasso.with(getContext())
                                .load(R.mipmap.user_empty)
                                .into(img_menu_dashboard_middle);
                    }
                    textView_username_dashboard_middle.setText(user.get(0).getFirst_name());
                    //img_username = ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length());
                    username = user.get(0).getUsername();

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
                String error= e.getMessage();
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

}
