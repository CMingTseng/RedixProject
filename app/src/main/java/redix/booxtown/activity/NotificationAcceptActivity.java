package redix.booxtown.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.TransactionController;
import redix.booxtown.controller.UserController;
import redix.booxtown.custom.NotificationAccept;
import redix.booxtown.model.Book;
import redix.booxtown.model.Transaction;
import redix.booxtown.model.User;

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
    TextView txt_menu_notification_infor2_phone;
    TextView txtTitle;

    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;
    ImageView img_menu;
    CircularImageView imv_nitification_infor3_phone;
    String trans;
    RatingBar myRatingBar;
    ImageView img_comment_rank1,img_comment_rank2,img_comment_rank3;
    TextView menu_notification_title2;
    RelativeLayout include8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request for window feature action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_confirm_accept);
        init();
        //inor
        txtTitle.setText("Notifications");
        menu_notification_title2.setText("with book");
        menu_notification_title2.setTextColor(getResources().getColor(R.color.text_color_with));
        //infor
        img_menu.setImageResource(R.drawable.btn_sign_in_back);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //end

        imv_nitification_infor3_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationAcceptActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //end

        trans = getIntent().getStringExtra("trans_id");
        transAsync transAsync= new transAsync(NotificationAcceptActivity.this, trans);
        transAsync.execute();
    }

    public void init(){
        include8 = (RelativeLayout)findViewById(R.id.include8);
        menu_notification_title2 = (TextView)include8.findViewById(R.id.txt_menu_notification_title2);
        myRatingBar = (RatingBar)findViewById(R.id.rating_bar_comment_interact);

        img_comment_rank1 = (ImageView)findViewById(R.id.img_comment_rank1);
        img_comment_rank2 = (ImageView)findViewById(R.id.img_comment_rank2);
        img_comment_rank3 = (ImageView)findViewById(R.id.img_comment_rank3);

        txt_author_info2 = (TextView) findViewById(R.id.txt_author_info2);
        txt_user_hi = (TextView) findViewById(R.id.txt_user_hi);
        txt_title_book_sell_noti_accept = (TextView) findViewById(R.id.txt_title_book_sell_noti_accept);
        txt_author_book_sell_noti_accept = (TextView) findViewById(R.id.txt_author_book_sell_noti_accept);
        txt_title_book_buy_noti_accept = (TextView) findViewById(R.id.txt_title_book_buy_noti_accept);
        txt_author_book_buy_noti_accept = (TextView) findViewById(R.id.txt_author_book_buy_noti_accept);

        imv_nitification_infor3_phone = (CircularImageView) findViewById(R.id.imv_menu_notification_infor2);
        img_menu = (ImageView) findViewById(R.id.img_menu);

        txtTitle = (TextView) findViewById(R.id.txt_title);
        txt_menu_notification_infor2_phone = (TextView) findViewById(R.id.txt_menu_notification_infor2_phone);

        img_menu_bottom_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView) findViewById(R.id.img_menu_bottom_user);
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

                getUser1 getUser1 = new getUser1(NotificationAcceptActivity.this,transaction.getUser_buyer_id());
                getUser1.execute();

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

                    String userName = pref.getString("username", null);
                    txt_user_hi.setText("Hi " + userName + ",");

                    txt_title_book_sell_noti_accept.setText(trans.getBook_name());
                    txt_author_book_sell_noti_accept.setText(trans.getBook_author());
                    txt_title_book_buy_noti_accept.setText(list.get(0).getTitle());
                    txt_author_book_buy_noti_accept.setText(list.get(0).getAuthor());
                    dialog.dismiss();
                }
            } catch (Exception e) {
            }

        }
    }

    class getUser1 extends AsyncTask<Void,Void,List<User>>{

        Context context;
        int user_id;
        ProgressDialog progressDialog;
        public getUser1(Context context,int user_id){
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
                    txt_author_info2.setText(user.get(0).getFirst_name()+"");
                    txt_menu_notification_infor2_phone.setText("Phone :"+user.get(0).getPhone());
                    Picasso.with(context)
                            .load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+user.get(0).getUsername()+"&image="+user.get(0).getPhoto().substring(user.get(0).getUsername().length()+3,user.get(0).getPhoto().length()))
                            .error(R.drawable.user)
                            .into(imv_nitification_infor3_phone);

                    myRatingBar.setRating(user.get(0).getRating());
                    LayerDrawable stars = (LayerDrawable) myRatingBar.getProgressDrawable();
                    stars.getDrawable(2).setColorFilter(Color.rgb(255,2224,0), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(1).setColorFilter(context.getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                    DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),context.getResources().getColor(R.color.bg_rating));

                    //set rank
                    if(user.get(0).getContributor() == 0){
                        img_comment_rank1.setVisibility(View.VISIBLE);
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_one);
                        img_comment_rank1.setImageBitmap(btn1);

                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.conbitrutor_two);
                        img_comment_rank1.setImageBitmap(btn1);

                    }
                    if(user.get(0).getGoldenBook() == 0){
                        img_comment_rank2.setVisibility(View.GONE);
                    }else if(user.get(0).getGoldenBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.golden_book);
                        img_comment_rank2.setImageBitmap(btn1);
                        img_comment_rank2.setVisibility(View.VISIBLE);
                    }

                    if(user.get(0).getListBook() == 0){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.newbie);
                        img_comment_rank3.setImageBitmap(btn1);
                        img_comment_rank3.setVisibility(View.VISIBLE);
                    }else if(user.get(0).getListBook() == 1){
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bookworm);
                        img_comment_rank3.setImageBitmap(btn1);
                        img_comment_rank3.setVisibility(View.VISIBLE);
                    }else{
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(),R.drawable.bibliophile);
                        img_comment_rank3.setImageBitmap(btn1);

                        img_comment_rank3.setVisibility(View.VISIBLE);
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
}
