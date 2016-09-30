package redix.booxtown.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import redix.booxtown.R;
import redix.booxtown.custom.NotificationAccept;
import redix.booxtown.model.Book;
import redix.booxtown.model.Transaction;

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
    Transaction trans;

    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request for window feature action bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_confirm_accept);

        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);


        //infor
        TextView txt_menu_notification_infor2_phone = (TextView)findViewById(R.id.txt_menu_notification_infor2_phone);
        txt_menu_notification_infor2_phone.setVisibility(View.GONE);
        TextView txtTitle=(TextView)findViewById(R.id.txt_title);
        txtTitle.setText("Notifications");
        //infor
        ImageView img_menu = (ImageView)findViewById(R.id.img_menu);
        img_menu.setImageResource(R.drawable.btn_sign_in_back);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //end

        ImageView imv_nitification_infor3_phone = (ImageView)findViewById(R.id.imv_menu_notification_infor2);
        imv_nitification_infor3_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationAcceptActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        //end

        txt_author_info2=(TextView) findViewById(R.id.txt_author_info2);
        txt_user_hi=(TextView) findViewById(R.id.txt_user_hi);
        txt_title_book_sell_noti_accept=(TextView) findViewById(R.id.txt_title_book_sell_noti_accept);
        txt_author_book_sell_noti_accept=(TextView) findViewById(R.id.txt_author_book_sell_noti_accept);
        txt_title_book_buy_noti_accept=(TextView) findViewById(R.id.txt_title_book_buy_noti_accept);
        txt_author_book_buy_noti_accept=(TextView) findViewById(R.id.txt_author_book_buy_noti_accept);

        trans= (Transaction) getIntent().getSerializableExtra("trans");
        Book book= (Book) getIntent().getSerializableExtra("book");
        SharedPreferences pref = NotificationAcceptActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = pref.edit();
        String userName = pref.getString("username", null);
        txt_user_hi.setText("Hi "+ userName+",");

        txt_title_book_sell_noti_accept.setText(trans.getBook_name());
        txt_author_book_sell_noti_accept.setText(trans.getBook_author());
        txt_title_book_buy_noti_accept.setText(book.getTitle());
        txt_author_book_buy_noti_accept.setText(book.getAuthor());
        txt_author_info2.setText(trans.getUser_buy());
    }
}
