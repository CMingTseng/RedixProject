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
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.CommentController;
import com.booxtown.controller.Information;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.controller.UserController;
import com.booxtown.controller.WishboardController;
import com.booxtown.model.Comment;
import com.booxtown.model.Notification;
import com.booxtown.model.Wishboard;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;
import com.booxtown.adapter.AdapterCommentBook;
import com.booxtown.model.CommentBook;
import com.booxtown.model.User;

/**
 * Created by Administrator on 30/08/2016.
 */
public class RespondActivity extends AppCompatActivity implements View.OnClickListener {
    AdapterCommentBook adapter;
    private RecyclerView rv_comment;
    LinearLayoutManager linearLayoutManager;
    List<CommentBook> arr_commet = new ArrayList<>();
    boolean loading = true,
            isLoading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 4;

    ImageView img_menu_bottom_location,img_menu_bottom_comment,img_menu_bottom_camera,img_menu_bottom_bag,img_menu_bottom_user;

    List<String> listUser = new ArrayList<>();
    Wishboard wishboard;
    CircularImageView photo_author_post;
    TextView txt_author_post,txt_title_book_respond,txt_author_book_post,txt_content_post,btn_add_book, txt_checkout_respond;
    View view;
    ImageView img_component,imageView_back,img_close_dialog_unsubcribe,img_rank1_respon,img_rank2_respon,img_rank3_respon;
    RatingBar ratingBar_respon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond);
        init();
        //--------------------------------------------------
        view = (View) findViewById(R.id.menu_top_respond);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);
        txtTitle.setText("Respond");
        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
        img_component.setVisibility(View.INVISIBLE);
        Bitmap btm = BitmapFactory.decodeResource(getResources(),R.drawable.btn_sign_in_back);
        imageView_back.setImageBitmap(btm);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        try {
            wishboard = (Wishboard) getIntent().getSerializableExtra("wishboard");
            if(wishboard.getPhoto().length()>3) {
                Picasso.with(RespondActivity.this).load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + wishboard.getUsername() + "&image=" + wishboard.getPhoto().substring(wishboard.getUsername().length() + 3, wishboard.getPhoto().length()))
                        .error(R.mipmap.user_empty)
                        .into(photo_author_post);
            }else{
                Bitmap btm2 = BitmapFactory.decodeResource(getResources(),R.mipmap.user_empty);
                photo_author_post.setImageBitmap(btm2);
            }
            if(wishboard.getUsername().length() >0){
                String a = wishboard.getUsername().substring(0,1).toUpperCase() + wishboard.getUsername().substring(1,wishboard.getUsername().length());
                txt_author_post.setText(a);
            }
            txt_title_book_respond.setText("Book: "+ wishboard.getTitle());
            txt_author_book_post.setText("Author: "+ wishboard.getAuthor());
            txt_content_post.setText(wishboard.getComment());
            getUser getUser = new getUser(RespondActivity.this,wishboard.getUser_id());
            getUser.execute();
        }catch (Exception ex){
            String err= ex.getMessage();
        }
        btn_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RespondActivity.this,AddbookActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("user_id_respone",wishboard.getUser_id());
                intent.putExtra("user_name_respone",wishboard.getUsername());
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
        img_close_dialog_unsubcribe= (ImageView) findViewById(R.id.img_close_dialog_unsubcribe);
        final EditText message= (EditText) findViewById(R.id.editText11) ;
        SharedPreferences pref = RespondActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final String session_id = pref.getString("session_id", null);
        img_close_dialog_unsubcribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message1 = message.getText().toString();
                if(message1 == null){

                }else{
                    message1 = message1.trim();
                    if(!message1.equals("")){
                        insertComment insertComment1 = new insertComment(RespondActivity.this);
                        insertComment1.execute(session_id,message.getText().toString(),wishboard.getId());
                        message.setText("");
                        CommentBook commentBook;
                        getComment comment;
                        if(arr_commet.size() == 0){
                            comment = new getComment(RespondActivity.this, wishboard.getId(), 15,0);
                        }else {
                            commentBook = arr_commet.get(arr_commet.size() - 1);
                            comment = new getComment(RespondActivity.this, wishboard.getId(), 15, commentBook.getId());
                        }
                        comment.execute();
                    }
                    else{
                        Toast.makeText(RespondActivity.this,Information.noti_show_comment_empty,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        populatRecyclerView(wishboard.getId());
        implementScrollListener(wishboard.getId());
    }

    public void init(){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
        rv_comment.setLayoutManager(linearLayoutManager);
        rv_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        ratingBar_respon = (RatingBar)findViewById(R.id.ratingBar_respon);
        img_rank1_respon = (ImageView)findViewById(R.id.img_rank1_respon);
        img_rank2_respon = (ImageView)findViewById(R.id.img_rank2_respon);
        img_rank3_respon = (ImageView)findViewById(R.id.img_rank3_respon);

        photo_author_post= (CircularImageView) findViewById(R.id.photo_author_post);

        photo_author_post=(CircularImageView) findViewById(R.id.photo_author_post);
        txt_author_post=(TextView) findViewById(R.id.txt_author_post) ;
        txt_title_book_respond=(TextView) findViewById(R.id.txt_title_book_respond) ;
        txt_author_book_post=(TextView) findViewById(R.id.txt_author_book_post) ;
        txt_content_post=(TextView) findViewById(R.id.txt_content_post) ;

        btn_add_book = (TextView) findViewById(R.id.txt_add_book_respond);
        txt_checkout_respond= (TextView) findViewById(R.id.txt_checkout_respond);
        txt_checkout_respond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sent message

                SharedPreferences pref = RespondActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String firstName = pref.getString("firstname", "");
                String session_id = pref.getString("session_id", null);

                UserID us = new UserID(RespondActivity.this);
                us.execute(session_id);


            }
        });

        imageView_back = (ImageView) findViewById(R.id.img_menu);
        img_component = (ImageView) findViewById(R.id.img_menu_component);

        img_menu_bottom_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView) findViewById(R.id.img_menu_bottom_user);
    }

    private void populatRecyclerView(String post_id) {
        getComment getcomment = new getComment(RespondActivity.this,post_id,15,0);
        getcomment.execute();
        if(arr_commet.size() == 0) {
            adapter = new AdapterCommentBook(RespondActivity.this, arr_commet);
            rv_comment.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }
    private int totalItemCount,lastVisibleItem;
    private void implementScrollListener(final String post_id) {
        rv_comment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = rv_comment.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                /*if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }*/
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    CommentBook commentBook = arr_commet.get(arr_commet.size() - 1);
                    getComment getcomment = new getComment(RespondActivity.this,post_id,15,commentBook.getId());
                    getcomment.execute();
                    // Do something
                    loading = true;
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent1.putExtra("key", "1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent2.putExtra("key", "2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent3.putExtra("key", "3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent4.putExtra("key", "4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent5.putExtra("key", "5");
                startActivity(intent5);
                break;

        }
    }

    class getComment extends AsyncTask<Void, Void, List<CommentBook>> {

        Context context;
        String post_id;
        int top, from;
        ProgressDialog progressDialog;

        public getComment(Context context, String post_id,int top,int from) {
            this.context = context;
            this.post_id = post_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<CommentBook> doInBackground(Void... voids) {
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
            WishboardController bookController = new WishboardController();
            return bookController.getCommnetWishboard(post_id,top,from);
        }

        @Override
        protected void onPostExecute(List<CommentBook> commentBooks) {
            try {
                if (commentBooks.size() > 0) {
                    arr_commet.addAll(commentBooks);
                    adapter.notifyDataSetChanged();
                    isLoading = true;
                    if (!listUser.contains(wishboard.getUser_id() + "")) {
                        listUser.add(wishboard.getUser_id() + "");
                    }
                    for (int i = 0; i < commentBooks.size(); i++) {
                        if (!listUser.contains(commentBooks.get(i).getUser_id() + "")) {
                            listUser.add(commentBooks.get(i).getUser_id() + "");
                        }
                    }
                    progressDialog.dismiss();
                } else {
                    isLoading = false;
                    progressDialog.dismiss();
                }
            } catch (Exception e) {

            }


            progressDialog.dismiss();
        }
    }

    class insertComment extends AsyncTask<String,Void,Boolean>{

        Context context;
        ProgressDialog dialog;
        public insertComment(Context context){
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("please waiting...");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            CommentController comment = new CommentController();
            return comment.insertComment(strings[0],strings[1],"0","0",strings[2]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if(aBoolean == true){
                    Toast.makeText(context,Information.noti_show_sent_comment,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    Toast.makeText(context,Information.noti_show_not_sent_comment,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }catch (Exception e){
                Toast.makeText(context,Information.noti_show_not_sent_comment,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
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
                    ratingBar_respon.setRating(user.get(0).getRating());
                    LayerDrawable stars = (LayerDrawable) ratingBar_respon.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.rgb(249,242,0), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                    DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),context.getResources().getColor(R.color.bg_rating));
                    //set rank
                    if(user.get(0).getContributor() == 0){
                        img_rank1_respon.setVisibility(View.VISIBLE);
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_one);
                        img_rank1_respon.setImageBitmap(btn1);
                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_two);
                        img_rank1_respon.setImageBitmap(btn1);
                    }
                    if(user.get(0).getGoldenBook() == 0){
                        img_rank2_respon.setVisibility(View.GONE);
                    }else if(user.get(0).getGoldenBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.golden_book);
                        img_rank2_respon.setImageBitmap(btn1);
                        img_rank2_respon.setVisibility(View.VISIBLE);
                    }
                    if(user.get(0).getListBook() == 0){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.newbie);
                        img_rank3_respon.setImageBitmap(btn1);
                        img_rank3_respon.setVisibility(View.VISIBLE);
                    }else if(user.get(0).getListBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bookworm);
                        img_rank3_respon.setImageBitmap(btn1);
                        img_rank3_respon.setVisibility(View.VISIBLE);
                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bibliophile);
                        img_rank3_respon.setImageBitmap(btn1);
                        img_rank3_respon.setVisibility(View.VISIBLE);
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
    class UserID extends AsyncTask<String, Void, String> {
        Context context;

        public UserID(Context context) {
            this.context = context;
        }

        ProgressDialog dialog;

        @Override
        protected String doInBackground(String... strings) {
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
                SharedPreferences pref = RespondActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                String firstName = pref.getString("firstname", "");
                List<Hashtable> list = new ArrayList<>();
                Notification notification = new Notification("Wishboard",user_ID , "15");
                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                obj.put("user_id", wishboard.getUser_id()+"");
                obj.put("messages", firstName + " suggested to check out his/her listings, in response to your post on Wishboard.");
                list.add(obj);
                NotificationController controller = new NotificationController();
                controller.sendNotification(list);

            } catch (Exception e) {
                String ssss = e.getMessage();

            }

        }
    }
}
