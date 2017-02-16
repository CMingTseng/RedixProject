package com.booxtown.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.controller.CheckSession;
import com.booxtown.controller.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.booxtown.R;

import com.booxtown.controller.BookController;
import com.booxtown.controller.GPSTracker;
import com.booxtown.controller.GetAllGenreAsync;
import com.booxtown.controller.IconMapController;
import com.booxtown.controller.Information;
import com.booxtown.controller.NotificationController;
import com.booxtown.controller.ObjectCommon;
import com.booxtown.controller.ResizeImage;
import com.booxtown.controller.UploadFileController;

import com.booxtown.custom.CustomListviewGenre;
import com.booxtown.model.Book;
import com.booxtown.model.Genre;
import com.booxtown.model.ImageClick;
import com.booxtown.model.Notification;

public class AddbookActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    ImageView btn_sellectimage, imagebook1, imagebook2, imagebook3, addtag;
    UploadFileController uploadFileController;
    Button btn_menu_editlist_delete, btn_menu_editlisting_update, btn_menu_listing_addbook;
    String username;
    TableRow tbl_price_sell;
    ArrayList<Genre> genre;
    double latitude, longitude;
    EditText edt_tilte, edt_author, edt_tag, edt_editlisting_sell;
    TableRow row;
    CheckBox swap, free, sell;
    String session_id;
    float price;
    String condition;
    Uri mImageUri;
    ArrayList<String> arrImage, listUserName;
    SeekBar seekbar;
    //UserController userController;
    BookController bookController;
    boolean success;
    Book book;
    String titl;
    TextView tag1, tag2, tag3;
    public int numclick = 0;
    public int numimageclick = 0;
    public String imgOne, imgTwo, imgThree;

    int PICK_IMAGE_MULTIPLE = 1;
    //String imageEncoded;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    //List<String> imagesEncodedList;
    ArrayList<String> listTag;
    Book bookedit;
    TableRow tb_menu;
    ImageView imageView_back;
    SupportMapFragment mapFragment;
    String imageOrigin;
    int type = 0;
    int user_id = 0;
    String userName = "";
    LatLng latLng_new;

    int typeChooseImage = 0;
    String sChooseImage = "";
    boolean flagTag = false;
    RadioButton radioButton_current, radioButton_another;
    ImageView img_menu_bottom_location, img_menu_bottom_comment, img_menu_bottom_camera, img_menu_bottom_bag, img_menu_bottom_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_with_swap);
        radioButton_current = (RadioButton) findViewById(R.id.radioButton_current);
        radioButton_another = (RadioButton) findViewById(R.id.radioButton_another);

        img_menu_bottom_location = (ImageView)findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView)findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView)findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView)findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView)findViewById(R.id.img_menu_bottom_user);

        try {
            bookedit = (Book) getIntent().getSerializableExtra("book");
            try {
                user_id = Integer.parseInt(getIntent().getStringExtra("user_id_respone"));
                userName = getIntent().getStringExtra("user_name_respone");

            } catch (Exception exx) {

            }
            //map view
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_map_editlisting);
            mapFragment.getMapAsync(this);
            //end
            tbl_price_sell = (TableRow) findViewById(R.id.row_price_sell);
            edt_editlisting_sell = (EditText) findViewById(R.id.edt_editlisting_sell);
            swap = (CheckBox) findViewById(R.id.checkBox);
            sell = (CheckBox) findViewById(R.id.ck_sell_editlisting);
            free = (CheckBox) findViewById(R.id.checkBox3);

            edt_author = (EditText) findViewById(R.id.editText9);
            Spannable wordtoSpan1 = new SpannableString("Author *");
            wordtoSpan1.setSpan(new ForegroundColorSpan(Color.RED), 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edt_author.setHint(wordtoSpan1);
            edt_author.setFilters(new InputFilter[]{
                    new InputFilter() {
                        public CharSequence filter(CharSequence src, int start,
                                                   int end, Spanned dst, int dstart, int dend) {
                            if (src.equals("")) { // for backspace
                                return src;
                            }
                            if (src.toString().matches("[\\x00-\\x7F]+")) {
                                return src;
                            }
                            return "";
                        }
                    }
            });

            try {
                type = Integer.parseInt(getIntent().getStringExtra("type"));

            } catch (Exception ex) {

            }

            edt_tilte = (EditText) findViewById(R.id.editText8);
            Spannable wordtoSpan = new SpannableString("Book Title *");
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 11, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edt_tilte.setHint(wordtoSpan);
            edt_tilte.setFilters(new InputFilter[]{
                    new InputFilter() {
                        public CharSequence filter(CharSequence src, int start,
                                                   int end, Spanned dst, int dstart, int dend) {
                            if (src.equals("")) { // for backspace
                                return src;
                            }
                            if (src.toString().matches("[\\x00-\\x7F]+")) {
                                return src;
                            }
                            return "";
                        }
                    }
            });
            edt_tag = (EditText) findViewById(R.id.editText10);
            edt_tag.setVisibility(View.GONE);
            addtag = (ImageView) findViewById(R.id.imageView33);
            addtag.setOnClickListener(this);
            tag1 = (TextView) findViewById(R.id.tag1);
            tag2 = (TextView) findViewById(R.id.tag2);
            tag3 = (TextView) findViewById(R.id.tag3);

            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);

            tag1.setOnClickListener(this);
            tag2.setOnClickListener(this);
            tag3.setOnClickListener(this);
            btn_menu_listing_addbook = (Button) findViewById(R.id.btn_menu_listing_addbook);
            btn_menu_editlist_delete = (Button) findViewById(R.id.btn_menu_editlist_delete);
            btn_menu_editlisting_update = (Button) findViewById(R.id.btn_menu_editlisting_update);
            btn_menu_editlist_delete.setOnClickListener(this);
            btn_menu_editlisting_update.setOnClickListener(this);
            row = (TableRow) findViewById(R.id.row_edit_book);
            listTag = new ArrayList<>();
            genre = new ArrayList<>();
            for (int i = 0; i < GetAllGenreAsync.list.size(); i++) {
                Genre genrel = new Genre();
                genrel.setValue(GetAllGenreAsync.list.get(i));
                genre.add(genrel);
            }
            btn_sellectimage = (ImageView) findViewById(R.id.imageView32);
            //picaso
            Picasso.with(getApplicationContext()).load(R.drawable.btn_add).into(btn_sellectimage);
            //end
            btn_sellectimage.setOnClickListener(this);
            uploadFileController = new UploadFileController();
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            username = pref.getString("username", null);
            session_id = pref.getString("session_id", null);
            //end
            //spinner
            ImageView imageView = (ImageView) findViewById(R.id.img_menu_genre);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_down).into(imageView);
            imagebook1 = (ImageView) findViewById(R.id.imageView29);
            imagebook2 = (ImageView) findViewById(R.id.imageView30);
            imagebook3 = (ImageView) findViewById(R.id.imageView31);
            seekbar = (SeekBar) findViewById(R.id.seekBar2);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.abc);
            Bitmap thumb = Bitmap.createBitmap(46, 46, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(thumb);
            canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                    new Rect(0, 0, thumb.getWidth(), thumb.getHeight()), null);
            Drawable drawable = new BitmapDrawable(getResources(), thumb);
            seekbar.setThumb(drawable);
            TextView title = (TextView) findViewById(R.id.txt_title);
            title.setText("Add a book");

            ImageView imgBack = (ImageView) findViewById(R.id.img_menu);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(imgBack);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 0) {
                        Intent intent = new Intent(AddbookActivity.this, SwapActivity.class);
                        intent.putExtra("Book", bookedit);
                        startActivity(intent);
                        finish();
                    } else {
                        onBackPressed();
                    }
                }
            });

            listUserName = new ArrayList<>();
            final TextView txt_view = (TextView) findViewById(R.id.txt_menu_genre1);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(AddbookActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_genre);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    RecyclerView rv_genre = (RecyclerView) dialog.findViewById(R.id.listView_genre);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rv_genre.setLayoutManager(linearLayoutManager);
                    rv_genre.setAdapter(new CustomListviewGenre(getApplicationContext(), genre));
                    dialog.show();
                    Button button_spiner_genre = (Button) dialog.findViewById(R.id.button_spiner_genre);
                    button_spiner_genre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String genreResult = "";
                            int flag = 0;
                            for (int i = 0; i < genre.size(); i++) {
                                if (genre.get(i).ischeck() == true) {
                                    flag++;
                                    genreResult = genreResult + genre.get(i).getValue() + ",";
                                }
                            }
                            if (flag > 0) {
                                txt_view.setText(genreResult.substring(0, genreResult.length() - 1));
                            }
                            dialog.dismiss();
                        }
                    });

                    ImageView img_close_dialoggenre = (ImageView) dialog.findViewById(R.id.img_close_dialoggenre);
//                Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(img_close_dialoggenre);
                    img_close_dialoggenre.setImageResource(R.drawable.close_popup);
                    img_close_dialoggenre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }


            });


            txt_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(AddbookActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_genre);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    RecyclerView rv_genre = (RecyclerView) dialog.findViewById(R.id.listView_genre);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    rv_genre.setLayoutManager(linearLayoutManager);
                    rv_genre.setAdapter(new CustomListviewGenre(getApplicationContext(), genre));
                    dialog.show();

                    Button button_spiner_genre = (Button) dialog.findViewById(R.id.button_spiner_genre);
                    button_spiner_genre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String genreResult = "";
                            int flag = 0;
                            for (int i = 0; i < genre.size(); i++) {
                                if (genre.get(i).ischeck() == true) {
                                    flag++;
                                    genreResult = genreResult + genre.get(i).getValue() + ",";
                                }
                            }
                            if (flag > 0) {
                                txt_view.setText(genreResult.substring(0, genreResult.length() - 1));
                            }
                            dialog.dismiss();
                        }
                    });
                    ImageView img_close_dialoggenre = (ImageView) dialog.findViewById(R.id.img_close_dialoggenre);
                    Picasso.with(getApplicationContext()).load(R.drawable.btn_close_filter).into(img_close_dialoggenre);
                    img_close_dialoggenre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                }
            });
            //end
            sell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b == true) {
                        tbl_price_sell.setVisibility(View.VISIBLE);
                    } else {
                        tbl_price_sell.setVisibility(View.GONE);
                    }
                }
            });
            //end

            btn_menu_listing_addbook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (checkCheckBox()) {
                            if (addbook(0)) {
                                addImages();
                                uploaddata uploaddata = new uploaddata();
                                uploaddata.execute();
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            });
            tb_menu = (TableRow) findViewById(R.id.tableRow5);
            imageView_back = (ImageView) findViewById(R.id.img_menu);
            imageOrigin = "";
            imgOne = "";
            imgTwo = "";
            imgThree = "";


            btn_menu_listing_addbook.setVisibility(View.VISIBLE);
            row.setVisibility(View.GONE);

            imagebook1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numimageclick = 1;
                    typeChooseImage = 2;
                    selectImage();

                }
            });

            imagebook2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numimageclick = 2;
                    typeChooseImage = 2;
                    selectImage();

                }
            });

            imagebook3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numimageclick = 3;
                    typeChooseImage = 2;
                    selectImage();

                }
            });

            try {
                if (latLng_new == null) {
                    GPSTracker gpsTracker = new GPSTracker(AddbookActivity.this);
                    latLng_new = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                }
            } catch (Exception e) {
                latLng_new = new LatLng(0, 0);
            }
            radioButton_another.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            latLng_new = latLng;
                            addMarkerChoice(latLng);

                        }
                    });
                }
            });

            radioButton_current.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (latLng_new == null) {
                            GPSTracker gpsTracker = new GPSTracker(AddbookActivity.this);
                            latLng_new = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                            addMarkerChoice(latLng_new);
                        }
                    } catch (Exception e) {
                        latLng_new = new LatLng(0, 0);
                    }
                }
            });
            try {
                swap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkCheckBox()) {
                            addMarkerChoice(latLng_new);
                        } else {
                            swap.setChecked(false);
                            addMarkerChoice(latLng_new);
                        }
                    }
                });

                free.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkCheckBox()) {
                            addMarkerChoice(latLng_new);
                        } else {
                            free.setChecked(false);
                            addMarkerChoice(latLng_new);
                        }
                    }
                });

                sell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkCheckBox()) {
                            if (sell.isChecked()) {
                                tbl_price_sell.setVisibility(View.VISIBLE);
                                addMarkerChoice(latLng_new);
                            } else {
                                tbl_price_sell.setVisibility(View.GONE);
                                addMarkerChoice(latLng_new);
                            }

                        } else {
                            sell.setChecked(false);
                            addMarkerChoice(latLng_new);
                        }
                    }
                });
            } catch (Exception e) {
            }

            //--------------------------------------------------------------
            img_menu_bottom_location.setOnClickListener(this);
            img_menu_bottom_comment.setOnClickListener(this);
            img_menu_bottom_camera.setOnClickListener(this);
            img_menu_bottom_bag.setOnClickListener(this);
            img_menu_bottom_user.setOnClickListener(this);
            //---------------------------------------------------------------

            addbook(1);
            numimageclick = 1;
            typeChooseImage = 2;
            cameraIntent();

        } catch (Exception e) {
        }
    }

    public boolean addbook(int type) {

        if (edt_tilte.getText().toString().equals("")) {
            if (type == 0) {
                Toast.makeText(AddbookActivity.this, "Please enter valid a book title", Toast.LENGTH_SHORT).show();

                return false;
            }
        } else if (edt_author.getText().toString().equals("")) {
            if (type == 0) {
                Toast.makeText(AddbookActivity.this, "Please enter valid a book author", Toast.LENGTH_SHORT).show();
            }
            return false;
        } else if ((listFileName == null || listFileName.size() == 0) && lisImmage.size() <= 0) {
            if (type == 0) {
                Toast.makeText(AddbookActivity.this, "You need to provide at least 1 image for this book", Toast.LENGTH_SHORT).show();
            }
            return false;
        } else {

            GPSTracker gps = new GPSTracker(AddbookActivity.this);

            for (int i = 0; i < lisImmage.size(); i++) {
                try {
                    long time = System.currentTimeMillis();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), lisImmage.get(i).getUri());
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    float scales = (float) height / (float) width;
                    if (width > 250) {

                        Bitmap photoBitMap = Bitmap.createScaledBitmap(bitmap, 250, (int) (250.0 * scales), true);
                        bmap.add(photoBitMap);
                    } else {
                        Bitmap photoBitMap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                        bmap.add(photoBitMap);
                    }

                    listFileName.add(lisImmage.get(i).getKey());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ArrayList<String> listvalueGenre = new ArrayList<>();
            for (int i = 0; i < genre.size(); i++) {
                if (genre.get(i).ischeck() == true) {
                    listvalueGenre.add(genre.get(i).getValue());
                }
            }

            String auth = edt_author.getText().toString();
            titl = edt_tilte.getText().toString();
            String tag = "";
            if (listTag.size() != 0) {
                for (int i = 0; i < listTag.size(); i++) {
                    if (i != listTag.size() - 1) {
                        tag = tag + listTag.get(i).replace("|", "") + ";";
                    } else {
                        tag = tag + listTag.get(i).replace("|", "");
                    }
                }
            }

            String genrel = "";

            if (listvalueGenre.size() != 0) {
                for (int i = 0; i < listvalueGenre.size(); i++) {
                    if (i != listvalueGenre.size() - 1) {
                        genrel = genrel + listvalueGenre.get(i) + ";";
                    } else {
                        genrel = genrel + listvalueGenre.get(i);
                    }
                }
            }

            String action = getAction();
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    condition = String.valueOf(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    condition = String.valueOf(seekBar.getProgress());
                }
            });

            String imagename = "";
            if (listFileName.size() != 0) {
                for (int i = 0; i < listFileName.size(); i++) {
                    if (i != listFileName.size() - 1) {
                        imagename = imagename + listFileName.get(i) + ";";
                    } else {
                        imagename = imagename + listFileName.get(i);
                    }
                }
            }


            book = new Book();
            book.setAction(action);
            book.setAuthor(auth);
            book.setTitle(titl);
            book.setCondition(String.valueOf(seekbar.getProgress()));
            book.setGenre(genrel);
            book.setHash_tag(tag);
            book.setLocation_latitude((float) latLng_new.latitude);
            book.setLocation_longitude((float) latLng_new.longitude);
            if (numclick != 0 || numimageclick != 0) {
                book.setPhoto(imagename + " ");

            }
            if (sell.isChecked()) {
                if (edt_editlisting_sell.getText().toString().isEmpty()) {
                    Toast.makeText(AddbookActivity.this, "Please enter valid a book price", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    if (Integer.parseInt(edt_editlisting_sell.getText().toString()) <= 1000) {
                        price = Float.valueOf(edt_editlisting_sell.getText().toString());
                        book.setPrice(price);
                    } else {
                        Toast.makeText(AddbookActivity.this, "Price should not exceed 1000", Toast.LENGTH_LONG).show();
                        edt_editlisting_sell.requestFocus();
                        return false;
                    }
                }
            }

        }
        return true;
    }


    public boolean checkCheckBox() {
        final String swap1 = swap.isChecked() == true ? "1" : "0";
        final String sell1 = sell.isChecked() == true ? "1" : "0";
        final String free1 = free.isChecked() == true ? "1" : "0";
        if (free1.equals("1")) {
            if (sell1.equals("1") || swap1.equals("1")) {
                Toast.makeText(AddbookActivity.this, Information.noti_show_choose_type_addbook, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (swap1.equals("1") || sell1.equals("1")) {
            if (free1.equals("1")) {
                return false;
            }
        }
        if (swap1.equals("0") && free1.equals("0") && sell1.equals("0")) {
            Toast.makeText(AddbookActivity.this, Information.noti_show_choose_type_addbook_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (IconMapController.icon(swap1, sell1, free1) == "icon_3_option") {
            return false;
        }
        if (IconMapController.icon(swap1, sell1, free1) == "location_default") {
            return false;
        }
        return true;
    }

    public void addMarkerChoice(LatLng latLng) {
        try {
            final String swap1 = swap.isChecked() == true ? "1" : "0";
            final String sell1 = sell.isChecked() == true ? "1" : "0";
            final String free1 = free.isChecked() == true ? "1" : "0";
            Location location = new Location("you");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            addMaker(location, IconMapController.icon(swap1, sell1, free1));
            latLng_new = latLng;
        } catch (Exception e) {
        }
    }

    public void addMaker(Location location, String img) {
        try {
            mMap.clear();
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
            // Changing marker icon
            marker.icon((BitmapDescriptorFactory.fromBitmap(ResizeImage.resizeMapIcons(AddbookActivity.this, img, (int) getResources().getDimension(R.dimen.width_pin),
                    (int) getResources().getDimension(R.dimen.height_pin)))));
            // adding marker
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setTrafficEnabled(true);
        } catch (Exception e) {
        }
    }

    public String getAction() {
        String s = "";
        s += swap.isChecked() == true ? "1" : "0";
        s += sell.isChecked() == true ? "1" : "0";
        s += free.isChecked() == true ? "1" : "0";
        return s;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location location;
        Boolean isGPSEnabled = false;
        Boolean isNetworkEnabled = false;
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(AddbookActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(AddbookActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            location = service
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                addMarkerChoice(new LatLng(latitude, longitude));
            } else {
                addMarkerChoice(new LatLng(0, 0));
            }
        }
        if (isNetworkEnabled) {
            location = service
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                addMarkerChoice(new LatLng(latitude, longitude));
            } else {
                addMarkerChoice(new LatLng(0, 0));
            }
        }
    }

    public void addMaker(Location location) {
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
        // Changing marker icon
        marker.icon((BitmapDescriptorFactory.fromBitmap(ResizeImage.resizeMapIcons(AddbookActivity.this, "icon_buy", (int) getResources().getDimension(R.dimen.width_pin),
                (int) getResources().getDimension(R.dimen.height_pin)))));
        // adding marker
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 9));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_menu_bottom_location:
                Intent intent1 = new Intent(AddbookActivity.this, MainAllActivity.class);
                intent1.putExtra("key", "1");
                startActivity(intent1);
                break;
            case R.id.img_menu_bottom_comment:
                Intent intent2 = new Intent(AddbookActivity.this, MainAllActivity.class);
                intent2.putExtra("key", "2");
                startActivity(intent2);
                break;
            case R.id.img_menu_bottom_camera:
                /*Intent intent3 = new Intent(RespondActivity.this, MainAllActivity.class);
                intent3.putExtra("key", "3");
                startActivity(intent3);*/
                Intent intent = new Intent(AddbookActivity.this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.img_menu_bottom_bag:
                Intent intent4 = new Intent(AddbookActivity.this, MainAllActivity.class);
                intent4.putExtra("key", "4");
                startActivity(intent4);
                break;
            case R.id.img_menu_bottom_user:
                Intent intent5 = new Intent(AddbookActivity.this, MainAllActivity.class);
                intent5.putExtra("key", "5");
                startActivity(intent5);
                break;

            case R.id.imageView32:
                numclick = numclick + 1;
                typeChooseImage = 1;
                if (numclick > 3) {
                    numclick = 0;
                }
                if (lisImmage.size() < 3) {
                    selectImage();
                } else if (lisImmage.size() == 3) {
                    btn_sellectimage.setEnabled(false);
                }
                break;
            case R.id.imageView33:
                if (listTag.size() < 3) {
                    addTag();
                }
                if (listTag.size() == 3) {
                    addtag.setVisibility(View.GONE);
                }
                break;
            case R.id.tag1:
                if (!tag1.getText().toString().trim().equals("")) {
                    showSnack(tag1.getText().toString(), 0);
                }
                break;
            case R.id.tag2:
                if (!tag2.getText().toString().trim().equals("")) {
                    showSnack(tag2.getText().toString(), 1);
                }
                break;
            case R.id.tag3:
                if (!tag3.getText().toString().trim().equals("")) {
                    showSnack(tag3.getText().toString(), 2);
                }
                break;
        }
    }

    public void showSnack(String tag, final int position) {
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Do you want remove " + tag + "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listTag.remove(position);
                settag();
                addtag.setVisibility(View.VISIBLE);
                snackbar.dismiss();
            }
        }).show();
    }

    public void addTag() {
        if (!flagTag) {
            edt_tag.setVisibility(View.VISIBLE);
            flagTag = true;
        } else {
            if (!edt_tag.getText().toString().trim().equals("") && !edt_tag.getText().toString().trim().contains(";")) {
                listTag.add(edt_tag.getText().toString().trim());
                edt_tag.setText("");
                settag();
            }
            edt_tag.setVisibility(View.GONE);
            flagTag = false;
        }

    }


    public void settag() {
        if (listTag.size() == 0) {
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);
            tag1.setText("");
            tag2.setText("");
            tag3.setText("");
        } else if (listTag.size() == 1) {
            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);

            tag1.setText(listTag.get(0));
            tag2.setText("");
            tag3.setText("");
        } else if (listTag.size() == 2) {
            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.VISIBLE);
            tag3.setVisibility(View.GONE);


            tag1.setText(listTag.get(0));
            tag2.setText(listTag.get(1));
            tag3.setText("");
        } else {

            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.VISIBLE);
            tag3.setVisibility(View.VISIBLE);
            tag1.setText(listTag.get(0));
            tag2.setText(listTag.get(1));
            tag3.setText(listTag.get(2));
            edt_tag.setVisibility(View.GONE);
        }
    }


    //select image
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddbookActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddbookActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent(AddbookActivity.this, CameraActivity.class);
        intent.putExtra("keyChoose", 1);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void addImages(ArrayList<Bitmap> bmap, List<String> listFileName) {
        uploadFileController.uploadFile(bmap, listFileName);
    }

    ArrayList<ImageClick> lisImmage = new ArrayList<>();

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap thumbnail = null;
        try {

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {

                    //thumbnail = (Bitmap) data.getExtras().get("data");
                    SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                    String ss = pref.getString("image", "");
                    File imgFile = new File(pref.getString("image", ""));
                    if (imgFile.exists()) {
                        thumbnail = BitmapFactory.decodeFile(pref.getString("image", ""));

                    }

                    int orientation = 0;
                    if (thumbnail.getHeight() < thumbnail.getWidth()) {
                        orientation = 90;
                    } else {
                        orientation = 0;
                    }
                    if (orientation != 0) {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(orientation);
                        thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(),
                                thumbnail.getHeight(), matrix, true);
                    } else
                        thumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(),
                                thumbnail.getHeight(), true);
                } else if (requestCode == SELECT_FILE) {
                    if (data != null) {
                        try {
                            thumbnail = MediaStore.Images.Media.getBitmap(AddbookActivity.this.getContentResolver(), data.getData());
                            int orientation = 0;
                            if (thumbnail.getHeight() < thumbnail.getWidth()) {
                                orientation = 90;
                            } else {
                                orientation = 0;
                            }
                            if (orientation != 0) {
                                Matrix matrix = new Matrix();
                                matrix.postRotate(orientation);
                                thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(),
                                        thumbnail.getHeight(), matrix, true);
                            } else
                                thumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(),
                                        thumbnail.getHeight(), true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            mImageUri = getImageUri(AddbookActivity.this, thumbnail);

//            // When an Image is picked
//            if (resultCode == Activity.RESULT_OK) {
//                // Get the Image from data
//                if (data.getData() != null) {
//                    mImageUri = data.getData();
//                }
//            } else {
//                return;
//            }
        } catch (Exception e) {
            return;
        }
        long time = System.currentTimeMillis();
        try {
            int width = imagebook1.getWidth();
            int height = imagebook1.getHeight();
            if (width == 0) {
                width = thumbnail.getWidth();
                height = thumbnail.getHeight();
                float scales = (float) height / (float) width;
                if (width > 250) {
                    width = 250;
                    height = (int) (250.0 * scales);
                }
            }
            if (typeChooseImage == 1) {
                if (!sChooseImage.contains("1")) {
                    Picasso.with(AddbookActivity.this).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook1);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    lisImmage.add(imageClick);
                    imgOne = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    sChooseImage = sChooseImage + "1";
                } else if (!sChooseImage.contains("2")) {
                    Picasso.with(AddbookActivity.this).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook2);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    lisImmage.add(imageClick);
                    imgTwo = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    sChooseImage = sChooseImage + "2";
                } else if (!sChooseImage.contains("3")) {
                    Picasso.with(AddbookActivity.this).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook3);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    lisImmage.add(imageClick);
                    imgThree = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    sChooseImage = sChooseImage + "3";
                }
            } else {
                if (numimageclick == 1) {
                    Picasso.with(AddbookActivity.this).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook1);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    lisImmage.add(imageClick);
                    imgOne = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    if (!sChooseImage.contains("1")) {
                        sChooseImage = sChooseImage + "1";
                    }
                } else if (numimageclick == 2) {

                    Picasso.with(AddbookActivity.this).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook2);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    lisImmage.add(imageClick);
                    imgTwo = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    if (!sChooseImage.contains("2")) {
                        sChooseImage = sChooseImage + "2";
                    }
                } else if (numimageclick == 3) {
                    Picasso.with(AddbookActivity.this).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook3);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    lisImmage.add(imageClick);
                    imgThree = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);

                    if (!sChooseImage.contains("3")) {
                        sChooseImage = sChooseImage + "3";
                    }
                }
            }
        } catch (Exception exx) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    ArrayList<String> listFileName = new ArrayList<>();
    ArrayList<Bitmap> bmap = new ArrayList<>();

    public void addImages() {
        uploadFileController.uploadFile(bmap, listFileName);
    }

    public class uploaddata extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                CheckSession checkSession = new CheckSession();
                boolean check = checkSession.checkSession_id(session_id);
                if (!check) {
                    SharedPreferences pref = AddbookActivity.this.getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("session_id", null);
                    editor.putString("active", null);
                    editor.commit();
                    Intent intent = new Intent(AddbookActivity.this, SignIn_Activity.class);
                    startActivity(intent);
                    this.cancel(true);
                }

                bookController = new BookController();
                return bookController.addbook(book, session_id);
            } catch (Exception exx) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (!result.equals("")) {
                    Toast.makeText(AddbookActivity.this,Information.add_book_success,Toast.LENGTH_SHORT).show();

                    // Tạm thời rào lại vì liên quan đến approved
                    /*if (type == 0) {
                        Intent intent = new Intent(AddbookActivity.this, SwapActivity.class);
                        if (bookedit == null) {
                            intent.putExtra("Book", bookedit);
                        } else {
                            intent.putExtra("Book", book);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        //sent message
                        SharedPreferences pref = AddbookActivity.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        String firstName = pref.getString("firstname", "");

                        List<Hashtable> list = new ArrayList<>();
                        Notification notification = new Notification("Wishboard", result, "14");
                        Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                        obj.put("user_id", user_id + "");
                        obj.put("messages", firstName + " has added a book " + book.getTitle() + " in response to your post on Wishboard");
                        list.add(obj);
                        NotificationController controller = new NotificationController();
                        controller.sendNotification(list);

                        onBackPressed();
                    }*/

                }
            } catch (Exception e) {

            }
        }
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }*/
}


