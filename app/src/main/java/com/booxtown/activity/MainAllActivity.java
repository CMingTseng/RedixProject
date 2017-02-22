package com.booxtown.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.adapter.AdapterExplore;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.Information;
import com.booxtown.controller.UserController;
import com.booxtown.fragment.ExploreFragment;
import com.booxtown.fragment.TopicFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;


import com.booxtown.R;

import com.booxtown.fragment.ListingsFragment;
import com.booxtown.fragment.MainFragment;
import com.booxtown.fragment.MyProfileFragment;
import com.booxtown.fragment.WishboardFragment;
import com.booxtown.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 03/09/2016.
 */
public class MainAllActivity extends AppCompatActivity {
    public static Resources mResources;
    View view_top;
    public static TextView txtTitle;
    public static ImageView img_component;
    boolean flag;

    //-----------------------------
    static ImageView btn_location;
    static ImageView btn_commnet;
    static ImageView btn_camera;
    static ImageView btn_bag;
    static ImageView btn_user;
    //-----------------------------
    static TableRow tb_bottom;
    static ImageView img_menu;
    public static MainAllActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_all);
        mResources = getResources();

        try {
            initLayout();
            view_top = (View) findViewById(R.id.menu_top_all);
            txtTitle = (TextView) view_top.findViewById(R.id.txt_title);

            flag = true;
            Intent intent = getIntent();
            Splash_Activity.value = true;
            if (intent.getStringExtra("key") != null) {
                int i = Integer.parseInt(intent.getStringExtra("key"));
                if (i == 1) {
                    initLayout();
                    callFragment(new MainFragment());
                    txtTitle.setText("Locate");
                    setDefaut(1);
                } else if (i == 2) {
                    initLayout();
                    callFragment(new TopicFragment());
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("Interact");
                    setDefaut(2);
                }else if (i == 15) {
                    initLayout();
                    callFragment(new ListingsFragment());
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("Listings");
                    setDefaut(3);

                } else if (i == 3) {
                    /*initLayout();
                    callFragment(new ListingsFragment());
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("Listings");
                    setDefaut(3);*/
                    cameraIntent();
                } else if (i == 4) {
                    initLayout();
                    callFragment(new WishboardFragment());
                    img_component.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext()).load(R.drawable.btn_add_wishbroad).into(img_component);
                    txtTitle.setText("Wishboard");
                    setDefaut(4);
                } else if (i == 5) {
                    initLayout();
                    callFragment(new MyProfileFragment());
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("My Profile");
                    setDefaut(5);
                } else if (i == 6) {
                    int num_list = getIntent().getIntExtra("num_list", 0);
                    Bundle bundle = new Bundle();
                    bundle.putString("activity", "add");
                    bundle.putInt("num_list", num_list);
                    ListingCollectionActivity listingCollectionActivity = new ListingCollectionActivity();
                    listingCollectionActivity.setArguments(bundle);
                    callFragment(listingCollectionActivity);
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("Listings");
                    setDefaut(3);

                }
            } else {
                try {
                    Book book = (Book) getIntent().getSerializableExtra("item");
                    if (book == null) {
                        setDefaut(1);
                        callFragment(new MainFragment());
                    }
                } catch (Exception err) {

                }
            }
            Picasso.with(getApplicationContext()).load(R.drawable.btn_explore).into(img_component);
            img_component.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        callFragment(new ExploreFragment());
                        txtTitle.setText("Explore");
                        Picasso.with(getApplicationContext()).load(R.drawable.btn_location).into(img_component);
                        flag = false;
                    } else {
                        callFragment(new MainFragment());
                        txtTitle.setText("Locate");
                        Picasso.with(getApplicationContext()).load(R.drawable.btn_explore).into(img_component);
                        flag = true;
                    }
                }
            });

            img_menu = (ImageView) view_top.findViewById(R.id.img_menu);
            Picasso.with(MainAllActivity.this).load(R.drawable.btn_menu_locate).into(img_menu);
            img_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainAllActivity.this, MenuActivity.class);
                    startActivity(intent);

                }
            });


            //-------------------------------------------------------

            btn_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //btn_location.setImageResource(R.drawable.icon_menu_bottom_location);
                    int fragmentChoose = Information.FragmentChoose;
                    if (fragmentChoose == 1) {
                        SaveSetting(1);

                    } else {
                        Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
                        initLayout();
                        callFragment(new MainFragment());
                        img_component = (ImageView) view_top.findViewById(R.id.img_menu_component);
                        img_component.setVisibility(View.VISIBLE);
                        Picasso.with(getApplicationContext()).load(R.drawable.btn_explore).into(img_component);
                        //Glide.with(MainAllActivity.this).load(R.drawable.btn_explore).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_component);

                        txtTitle.setText("Locate");
                        setDefaut(1);
                    }
                }
            });

            btn_commnet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int fragmentChoose = Information.FragmentChoose;
                    if (fragmentChoose == 1) {
                        SaveSetting(2);

                    } else {
                        callFragment(new TopicFragment());
                        img_component.setVisibility(View.GONE);
                        txtTitle.setText("Interact");
                        setDefaut(2);
                    }
                }
            });

            btn_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*callFragment(new ListingsFragment());
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("Listings");
                    setDefaut(3);*/
                    int fragmentChoose = Information.FragmentChoose;
                    if (fragmentChoose == 1) {
                        SaveSetting(3);

                    } else {
                        cameraIntent();
                    }
                }
            });
            btn_bag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int fragmentChoose = Information.FragmentChoose;
                    if (fragmentChoose == 1) {
                        SaveSetting(4);

                    } else {
                        callFragment(new WishboardFragment());
                        img_component.setVisibility(View.VISIBLE);
                        Picasso.with(getApplicationContext()).load(R.drawable.btn_add_wishbroad).into(img_component);
                        txtTitle.setText("Wishboard");
                        setDefaut(4);
                    }

                }
            });
            btn_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int fragmentChoose = Information.FragmentChoose;
                    if (fragmentChoose == 1) {
                        SaveSetting(5);

                    } else {
                        callFragment(new MyProfileFragment());
                        img_component.setVisibility(View.GONE);
                        txtTitle.setText("My Profile");
                        setDefaut(5);
                    }
                }
            });

            tb_bottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sss = "";
                }
            });

            try {
                initLayout();
                Book book = (Book) getIntent().getSerializableExtra("item");
                String key = getIntent().getStringExtra("keyDetail");
                if (key.equals("1")) {
                    ListingsDetailActivity fragment = new ListingsDetailActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString(String.valueOf(R.string.valueListings), "5");
                    bundle.putSerializable("item", book);
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("Listings");
                    // setDefaut(3);
                }

            } catch (Exception exs) {

            }


            //-------------------------------------------------------
        } catch (Exception ex) {

        }
        UserID userID = new UserID(MainAllActivity.this);
        userID.execute();
    }

    private void cameraIntent() {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, 1);
        Intent intent = new Intent(MainAllActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    public TextView gettitle() {
        return txtTitle;
    }

    public void initLayout() {
        tb_bottom = (TableRow) findViewById(R.id.tb_bottom);
        view_top = (View) findViewById(R.id.menu_top_all);
        txtTitle = (TextView) view_top.findViewById(R.id.txt_title);
        img_component = (ImageView) view_top.findViewById(R.id.img_menu_component);
        view_top = (View) findViewById(R.id.menu_top_all);
        txtTitle = (TextView) view_top.findViewById(R.id.txt_title);
        //txtTitle.setText("Locate");
        flag = true;
        img_component = (ImageView) view_top.findViewById(R.id.img_menu_component);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_explore).into(img_component);
        img_component.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    callFragment(new ExploreFragment());
                    txtTitle.setText("Explore");
                    Picasso.with(getApplicationContext()).load(R.drawable.btn_location).into(img_component);
                    flag = false;
                } else {
                    callFragment(new MainFragment());
                    txtTitle.setText("Locate");
                    Picasso.with(getApplicationContext()).load(R.drawable.btn_explore).into(img_component);
                    flag = true;
                }
            }
        });
        btn_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        btn_commnet = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        btn_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        btn_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        btn_user = (ImageView) findViewById(R.id.img_menu_bottom_user);
    }

    public void SaveSetting(final int type){
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(MainAllActivity.this);
        builder1.setMessage("Do you want to save the changes made");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

                        String session_id = pref.getString("session_id", "");
                        updateProfile updateProfile = new updateProfile(MainAllActivity.this, session_id, Information.FragmentEmail,
                                Information.FragmentPhone, Information.FragmentDateTime, Information.FragmentBirthday, Information.FragmentPhoto, Information.FragmentFirst, Information.FragmentLast,type);
                        updateProfile.execute();

                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Information.FragmentChoose = 0;
                        if(type==1){
                            Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
                            initLayout();
                            callFragment(new MainFragment());
                            img_component = (ImageView) view_top.findViewById(R.id.img_menu_component);
                            img_component.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext()).load(R.drawable.btn_explore).into(img_component);
                            //Glide.with(MainAllActivity.this).load(R.drawable.btn_explore).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_component);

                            txtTitle.setText("Locate");
                            setDefaut(1);
                        }else if(type==2){
                            callFragment(new TopicFragment());
                            img_component.setVisibility(View.GONE);
                            txtTitle.setText("Interact");
                            setDefaut(2);
                        }else if(type==3){
                            cameraIntent();
                        }else if(type==4){
                            callFragment(new WishboardFragment());
                            img_component.setVisibility(View.VISIBLE);
                            Picasso.with(getApplicationContext()).load(R.drawable.btn_add_wishbroad).into(img_component);
                            txtTitle.setText("Wishboard");
                            setDefaut(4);
                        }else {
                            callFragment(new MyProfileFragment());
                            img_component.setVisibility(View.GONE);
                            txtTitle.setText("My Profile");
                            setDefaut(5);
                        }
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static MainAllActivity getSaleInstance() {
        return INSTANCE;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame_main_all, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void callFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    public void setDefaut(int i) {
        //set icon tab
        if (i == 0) {
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_profile_not_active).into(btn_user);
        } else if (i == 1) {
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_active).into(btn_location);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_profile_not_active).into(btn_user);
        } else if (i == 2) {
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_interact_active).into(btn_commnet);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_profile_not_active).into(btn_user);
        } else if (i == 3) {
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_listing_active).into(btn_camera);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_profile_not_active).into(btn_user);
        } else if (i == 4) {
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_wishbroad_active).into(btn_bag);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_profile_not_active).into(btn_user);
        } else {
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_interact_not_active).into(btn_commnet);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_not_active).into(btn_location);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_listing_not_active).into(btn_camera);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_wishbroad_not_active).into(btn_bag);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_locate_profile_active).into(btn_user);
        }

    }

    @Override
    public void onBackPressed() {

        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                moveTaskToBack(true);
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
    }

    public static TextView getTxtTitle() {
        return txtTitle;
    }

    public static void setTxtTitle(String title) {

        txtTitle.setText(title);
    }


    public static ImageView getImg_menu() {
        return img_menu;
    }


    public static void setImg_component() {
        img_component.setVisibility(View.VISIBLE);
        Picasso.with(getSaleInstance()).load(R.drawable.btn_location).into(img_component);
    }

    public static Bitmap getImageThumb() {
        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.abc);
        int size = (int) mResources.getDimension(R.dimen.size_knob);
        if (size >= 60) {
            size = 44;
        }
        if (size == 40) {
            size = 38;
        }
        Bitmap thumb = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(thumb);
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(0, 0, thumb.getWidth(), thumb.getHeight()), null);

        return thumb;
    }

    class UserID extends AsyncTask<String, Void, String> {
        Context context;

        public UserID(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            String session_id = pref.getString("session_id", null);
            UserController userController = new UserController(context);
            String user_id = userController.getUserID(session_id);
            return user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(final String user_ID) {
            try {
                if (!user_ID.equals("") || user_ID != null) {
                    SharedPreferences pref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("user_id", user_ID);
                    editor.commit();
                }
                //}
            } catch (Exception e) {
                String ssss = e.getMessage();
                // Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
            }
        }
    }

    class updateProfile extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;
        Context context;
        String email, phone, birthday, photo, session_id, first_name, last_name, date_time;
        int type;
        public updateProfile(Context context, String session_id, String email, String phone, String date_time, String birthday, String photo, String first_name, String last_name,int type) {
            this.context = context;
            this.session_id = session_id;
            this.email = email;
            this.phone = phone;
            this.birthday = birthday;
            this.photo = photo;
            this.first_name = first_name;
            this.last_name = last_name;
            this.date_time = date_time;
            this.type=type;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainAllActivity.this);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
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
            return userController.updateprofile(first_name, last_name, email, phone, birthday, photo, session_id);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                Information.FragmentChoose=0;
                dialog.dismiss();
                if(type==1){
                    Glide.with(MainAllActivity.this).load(R.drawable.btn_locate_active).diskCacheStrategy(DiskCacheStrategy.ALL).into(btn_location);
                    initLayout();
                    callFragment(new MainFragment());
                    img_component = (ImageView) view_top.findViewById(R.id.img_menu_component);
                    img_component.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext()).load(R.drawable.btn_explore).into(img_component);
                    //Glide.with(MainAllActivity.this).load(R.drawable.btn_explore).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_component);

                    txtTitle.setText("Locate");
                    setDefaut(1);
                }else if(type==2){
                    callFragment(new TopicFragment());
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("Interact");
                    setDefaut(2);
                }else if(type==3){
                    cameraIntent();
                }else if(type==4){
                    callFragment(new WishboardFragment());
                    img_component.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext()).load(R.drawable.btn_add_wishbroad).into(img_component);
                    txtTitle.setText("Wishboard");
                    setDefaut(4);
                }else {
                    callFragment(new MyProfileFragment());
                    img_component.setVisibility(View.GONE);
                    txtTitle.setText("My Profile");
                    setDefaut(5);
                }
                Toast.makeText(MainAllActivity.this, Information.noti_update_success, Toast.LENGTH_LONG).show();
            } else {
                dialog.dismiss();
                Toast.makeText(MainAllActivity.this, Information.noti_update_fail, Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
