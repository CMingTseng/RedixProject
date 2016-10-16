package com.booxtown.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.fragment.AboutFragment;
import com.booxtown.fragment.ContactFragment;
import com.booxtown.fragment.InviteFriendFragment;
import com.booxtown.fragment.RateFragment;
import com.booxtown.fragment.SettingFragment;
import com.squareup.picasso.Picasso;

import com.booxtown.R;
import com.booxtown.custom.MenuBottomCustom;
import com.booxtown.fragment.FaqFragment;
import com.booxtown.fragment.NotificationFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;
    TextView txtTitle;

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(TextView txtTitle) {
        this.txtTitle = txtTitle;
    }

    private MenuBottomCustom menu_bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);
        ImageView img_component=(ImageView) findViewById(R.id.img_menu_component);
        img_component.setVisibility(View.GONE);

        ImageView img_menu = (ImageView)findViewById(R.id.img_menu);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_menu_locate).into(img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        //--------------------------------------------------
        View view_bottom = (View)findViewById(R.id.menu_bottom);
        menu_bottom=new MenuBottomCustom(view_bottom,HomeActivity.this,0);
        menu_bottom.setDefaut(0);
        //---------------------------------------------------------------

        String i = intent.getStringExtra("position");
        int position = Integer.parseInt(i);
        if(position==1){
            callFragment(new NotificationFragment());
            txtTitle=(TextView)findViewById(R.id.txt_title);
            txtTitle.setText("Notifications");
        } else if(position == 2){
            callFragment(new FaqFragment());
            TextView txtTitle=(TextView)findViewById(R.id.txt_title);
            txtTitle.setText("FAQ");
        }else if(position == 3){
            callFragment(new InviteFriendFragment());
            TextView txtTitle=(TextView)findViewById(R.id.txt_title);
            txtTitle.setText("Invite Friends");
        }else if(position == 4){
            callFragment(new RateFragment());
            TextView txtTitle=(TextView)findViewById(R.id.txt_title);
            txtTitle.setText("Rate Booxtown");
        }else if(position == 5){
            callFragment(new AboutFragment());
            TextView txtTitle=(TextView)findViewById(R.id.txt_title);
            txtTitle.setText("About Boxtown");
        }else if(position == 6){
            callFragment(new ContactFragment());
            TextView txtTitle=(TextView)findViewById(R.id.txt_title);
            txtTitle.setText("Contact Booxtown");
        }else if(position == 7){
            callFragment(new SettingFragment());
            TextView txtTitle=(TextView)findViewById(R.id.txt_title);
            txtTitle.setText("Settings");
        }else if(position == 8){

        }

        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);
    }

    public void callFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.framlayout_home, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(HomeActivity.this,MainAllActivity.class);
                intent1.putExtra("key","1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(HomeActivity.this,MainAllActivity.class);
                intent2.putExtra("key","2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                Intent intent3 = new Intent(HomeActivity.this,MainAllActivity.class);
                intent3.putExtra("key","3");
                startActivity(intent3);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(HomeActivity.this,MainAllActivity.class);
                intent4.putExtra("key","4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(HomeActivity.this,MainAllActivity.class);
                intent5.putExtra("key","5");
                startActivity(intent5);
                break;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        menu_bottom.setDefaut(0);
    }
}
