package com.booxtown.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.DrawableCompat;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.Information;
import com.booxtown.controller.UserController;
import com.booxtown.model.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;

import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.controller.TransactionController;
import com.booxtown.custom.CustomListviewNotificationSwap;
import com.booxtown.model.Book;
import com.booxtown.model.Transaction;
import com.booxtown.model.User;

public class NotificationSwapActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user,img_menu_component,img_menu;
    ImageView imv_menu_notification_infor1,img_comment_rank1,img_comment_rank2,img_comment_rank3;

    TextView txt_userbuy_notification_swap;
    TextView txt_user_hi;
    TextView title_book_notification_swap;
    TextView description_notification_swap;
    TextView author_list_notification_swap;
    TextView txtTitle,textView_author_book;

    Button btn_notification_not_like;
    RatingBar myRatingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_swap);

        init();

        String trans_id= getIntent().getStringExtra("trans_id");
        transAsync transAsync= new transAsync(NotificationSwapActivity.this,trans_id);
        transAsync.execute();
        //infor
        imv_menu_notification_infor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationSwapActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //end
        img_menu_component.setVisibility(View.GONE);
        txtTitle.setText("Notifications");
        img_menu.setImageResource(R.drawable.btn_sign_in_back);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //end
        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);
    }

    public void init(){
        textView_author_book = (TextView)findViewById(R.id.textView_author_book);

        img_comment_rank1 = (ImageView)findViewById(R.id.img_comment_rank1);
        img_comment_rank2 = (ImageView)findViewById(R.id.img_comment_rank2);
        img_comment_rank3 = (ImageView)findViewById(R.id.img_comment_rank3);

        myRatingBar = (RatingBar)findViewById(R.id.myRatingBar);
        imv_menu_notification_infor1 = (ImageView)findViewById(R.id.imv_menu_notification_infor1);

        img_menu  = (ImageView)findViewById(R.id.img_menu);
        txtTitle=(TextView)findViewById(R.id.txt_title);
        img_menu_component = (ImageView)findViewById(R.id.img_menu_component);

        btn_notification_not_like= (Button)findViewById(R.id.btn_notification_not_like);

        txt_user_hi= (TextView) findViewById(R.id.txt_user_hi);
        title_book_notification_swap= (TextView) findViewById(R.id.title_book_notification_swap);
        description_notification_swap= (TextView) findViewById(R.id.description_notification_swap);
        author_list_notification_swap= (TextView) findViewById(R.id.author_list_notification_swap);

        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);

        txt_userbuy_notification_swap= (TextView) findViewById(R.id.txt_userbuy_notification_swap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent1.putExtra("key","1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent2.putExtra("key","2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent3.putExtra("key","3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent4.putExtra("key","4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(NotificationSwapActivity.this,MainAllActivity.class);
                intent5.putExtra("key","5");
                startActivity(intent5);
                break;

        }
    }

    class transAsync extends AsyncTask<String,Void,Transaction> {

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        String trans_id;
        public transAsync(Context context, String trans_id){
            this.context = context;
            this.trans_id=trans_id;
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
            if (transaction == null){
                dialog.dismiss();
            }else {
                btn_notification_not_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(NotificationSwapActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_notification_swap_button);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                        TextView description= (TextView) dialog.findViewById(R.id.txt_dialog_reject_swap);
                        description.setText("Are you sure you want reject "+ transaction.getUser_buy()+"'s swap request ?");
                        Button btn_dialog_notification_swap = (Button)dialog.findViewById(R.id.btn_dialog_notification_swap);
                        btn_dialog_notification_swap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                String firstName = pref.getString("firstname", "");


                                // send notifi user seller
                                List<Hashtable> listSeller = new ArrayList<>();
                                Notification notificationSeller = new Notification("Swap Request", transaction.getId()+"","1" );
                                Hashtable objSeller = ObjectCommon.ObjectDymanic(notificationSeller);
                                objSeller.put("user_id", transaction.getUser_seller_id());
                                objSeller.put("messages", "You rejected a Swap request");
                                listSeller.add(objSeller);
                                NotificationController controllerSeller = new NotificationController();
                                controllerSeller.sendNotification(listSeller);
                                // end

                                List<Hashtable> list = new ArrayList<>();
                                Notification notification = new Notification("Swap Request", transaction.getId()+"","3" );
                                Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                                obj.put("user_id", transaction.getUser_buyer_id());
                                obj.put("messages",firstName + " rejected your Swap request");
                                list.add(obj);
                                NotificationController controller = new NotificationController();
                                controller.sendNotification(list);

//                                Intent intent = new Intent(NotificationSwapActivity.this,Notification_Swap_Accept_Like.class);
 //                               startActivity(intent);
                                dialog.dismiss();
                            }
                        });

                        ImageView imageView =(ImageView)dialog.findViewById(R.id.imv_clode_dialog_not_like);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

                ListView listView = (ListView)findViewById(R.id.lv_notification_swap);
                listView.setAdapter(new CustomListviewNotificationSwap(NotificationSwapActivity.this, transaction.getBook(), trans_id, transaction.getBook_name(), transaction));
                SharedPreferences pref = NotificationSwapActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                String userName = pref.getString("username", null);
                String firstName = pref.getString("firstname", "");
                txt_user_hi.setText("Hi "+ firstName+",");

                title_book_notification_swap.setText(transaction.getBook_name());
                textView_author_book.setText(transaction.getBook_author());
                Spannable wordtoSpan1 = new SpannableString("and good like to swap with you. Choose a book from "+transaction.getUser_buy()+"'s swap list to complete the swap" );
                wordtoSpan1.setSpan(new ForegroundColorSpan(Color.RED),51, 53+ transaction.getUser_buy().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                description_notification_swap.setText(wordtoSpan1);
                author_list_notification_swap.setText((transaction.getUser_buy()+"'s").toUpperCase()+" swap list");
                getUser getUser = new getUser(NotificationSwapActivity.this,transaction.getUser_buyer_id());
                getUser.execute();
                dialog.dismiss();
            }
            super.onPostExecute(transaction);
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
            UserController userController = new UserController(context);
            return userController.getByUserId(user_id);
        }

        @Override
        protected void onPostExecute(List<User> user) {
            try {
                if (user.size() > 0){
                    txt_userbuy_notification_swap.setText(user.get(0).getFirst_name()+"");
                    if(user.get(0).getPhoto().length()>3) {
                        Picasso.with(context)
                                .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + user.get(0).getUsername() + "&image=" + user.get(0).getPhoto().substring(user.get(0).getUsername().length() + 3, user.get(0).getPhoto().length()))
                                .error(R.mipmap.user_empty)
                                .into(imv_menu_notification_infor1);
                    }else {
                        Picasso.with(context)
                                .load(R.mipmap.user_empty)
                                .into(imv_menu_notification_infor1);
                    }

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
