package redix.booxtown.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.GPSTracker;
import redix.booxtown.controller.GetAllGenreAsync;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.NotificationController;
import redix.booxtown.controller.ObjectCommon;
import redix.booxtown.controller.ResizeImage;
import redix.booxtown.controller.UploadFileController;

import redix.booxtown.custom.CustomListviewGenre;
import redix.booxtown.fragment.ListingsFragment;
import redix.booxtown.model.Book;
import redix.booxtown.model.Explore;
import redix.booxtown.model.Genre;
import redix.booxtown.model.ImageClick;
import redix.booxtown.model.Notification;

public class AddbookActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {
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
    ArrayList<String> arrImage,listUserName;
    SeekBar seekbar;
    //UserController userController;
    BookController bookController;
    boolean success;
    Book book;
    String titl;
    TextView tag1,tag2,tag3;
    public int numclick = 0;
    public int numimageclick = 0;
    public String imgOne, imgTwo, imgThree;

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    ArrayList<String> listTag;
    Book bookedit;
    TableRow tb_menu;
    ImageView imageView_back;
    SupportMapFragment mapFragment;
    String imageOrigin;
    int type=0;
    int user_id=0;
    String userName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_with_swap);
        try {
            bookedit = (Book) getIntent().getSerializableExtra("book");
            try{

                user_id=Integer.parseInt(getIntent().getStringExtra("user_id_respone"));
                userName=getIntent().getStringExtra("user_name_respone");

            }catch (Exception exx){

            }
            //map view
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_map_editlisting);
            mapFragment.getMapAsync(this);
            //end
            tbl_price_sell= (TableRow)findViewById(R.id.row_price_sell) ;
            edt_editlisting_sell = (EditText) findViewById(R.id.edt_editlisting_sell);
            swap = (CheckBox) findViewById(R.id.checkBox);
            sell = (CheckBox) findViewById(R.id.ck_sell_editlisting);
            free = (CheckBox) findViewById(R.id.checkBox3);

            edt_author = (EditText) findViewById(R.id.editText9);
            Spannable wordtoSpan1 = new SpannableString("Author *");
            wordtoSpan1.setSpan(new ForegroundColorSpan(Color.RED), 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edt_author.setHint(wordtoSpan1);

            try{
                type= Integer.parseInt(getIntent().getStringExtra("type"));
            }catch (Exception ex){

            }

            edt_tilte = (EditText) findViewById(R.id.editText8);
            Spannable wordtoSpan = new SpannableString("Book Title *");
            wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 11, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            edt_tilte.setHint(wordtoSpan);

            edt_tag = (EditText) findViewById(R.id.editText10);
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
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.abc);
            Bitmap thumb=Bitmap.createBitmap(44,44, Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(thumb);
            canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
                    new Rect(0,0,thumb.getWidth(),thumb.getHeight()),null);
            Drawable drawable = new BitmapDrawable(getResources(),thumb);
            seekbar.setThumb(drawable);
            TextView title=(TextView) findViewById(R.id.txt_title);
            title.setText("Add a book");


            ImageView imgBack=(ImageView) findViewById(R.id.img_menu);
            Picasso.with(getApplicationContext()).load(R.drawable.btn_sign_in_back).into(imgBack);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(type == 0){
                        Intent intent = new Intent(AddbookActivity.this,SwapActivity.class);
                        intent.putExtra("Book",bookedit);
                        startActivity(intent);
                        finish();
                    }else {
                        onBackPressed();
                    }
                }
            });

            listUserName = new ArrayList<>();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(AddbookActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_genre);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    ListView listView_genre = (ListView) dialog.findViewById(R.id.listView_genre);
                    CustomListviewGenre adapter = new CustomListviewGenre(AddbookActivity.this, genre);
                    listView_genre.setAdapter(adapter);
                    dialog.show();

                    Button button_spiner_genre = (Button) dialog.findViewById(R.id.button_spiner_genre);
                    button_spiner_genre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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

            TextView txt_view = (TextView) findViewById(R.id.txt_menu_genre1);
            txt_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(AddbookActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_genre);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    ListView listView_genre = (ListView) dialog.findViewById(R.id.listView_genre);
                    listView_genre.setAdapter(new CustomListviewGenre(AddbookActivity.this, genre));
                    dialog.show();

                    Button button_spiner_genre = (Button) dialog.findViewById(R.id.button_spiner_genre);
                    button_spiner_genre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
            //show edittext when check to sell
            final CheckBox checkBox = (CheckBox) findViewById(R.id.ck_sell_editlisting);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()) {
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
                    if(addbook(0)) {
                        addImages();
                        uploaddata uploaddata = new uploaddata();
                        uploaddata.execute();
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
                    choseImage();

                }
            });

            imagebook2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numimageclick = 2;
                    choseImage();

                }
            });

            imagebook3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numimageclick = 3;
                    choseImage();

                }
            });
            addbook(1);
        }catch (Exception e){
        }
    }

    public boolean addbook(int type) {

        if(edt_tilte.getText().toString().equals("")){
            if(type==0) {
                Toast.makeText(AddbookActivity.this, "Please enter valid book title", Toast.LENGTH_SHORT).show();
            }
            return  false;
        }
        else if(edt_author.getText().toString().equals("")){
            if(type==0) {
                Toast.makeText(AddbookActivity.this, "Please enter valid book author", Toast.LENGTH_SHORT).show();
            }
            return  false;
        }
        else {

            GPSTracker gps = new GPSTracker(AddbookActivity.this);
            for (int i = 0; i < lisImmage.size(); i++) {
                try {
                    long time = System.currentTimeMillis();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), lisImmage.get(i).getUri());
                    Bitmap photoBitMap = Bitmap.createScaledBitmap(bitmap, 250, 270, true);
                    bmap.add(photoBitMap);
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

//        seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number minValue) {
//                condition = String.valueOf(minValue);
//            }
//        });
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                //            int progress = 0;
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                progress = i;
                    //Toast.makeText(getContext(),"p111:"+i,Toast.LENGTH_LONG).show();
                    condition = String.valueOf(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    condition = String.valueOf(seekBar.getProgress());
                    //Toast.makeText(getContext(),"p"+progress,Toast.LENGTH_LONG).show();
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
            book.setLocation_latitude(Float.valueOf(String.valueOf(gps.getLatitude())));
            book.setLocation_longitude(Float.valueOf(String.valueOf(gps.getLongitude())));
            if (numclick != 0 || numimageclick != 0) {

                book.setPhoto(imagename);

            }
            if (sell.isChecked()) {
                if (edt_editlisting_sell.getText().toString().isEmpty()) {
                    Toast.makeText(AddbookActivity.this, "Please enter valid price", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    price = Float.valueOf(edt_editlisting_sell.getText().toString());
                    book.setPrice(price);
                }
            }
        }
        return true;
    }

    public String getAction() {
        String s = "";
        s += swap.isChecked() == true ? "1" : "0";
        s += free.isChecked() == true ? "1" : "0";
        s += sell.isChecked() == true ? "1" : "0";
        return s;
    }

    public String parseJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
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
        LocationManager service = (LocationManager)getSystemService(LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSEnabled){
            location = service
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                addMaker(location);
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
                addMaker(location);
            }
        }
    }
    public void addMaker(Location location){
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
        // Changing marker icon
        marker.icon((BitmapDescriptorFactory.fromBitmap(ResizeImage.resizeMapIcons(AddbookActivity.this,"icon_buy",110, 150))));
        // adding marker
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 20));
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
            case R.id.imageView32:
                numclick = numclick + 1;
                if (numclick > 3) {
                    numclick = 0;
                }
                if (lisImmage.size() < 3) {
                    choseImage();
                } else if (lisImmage.size() == 3) {
                    btn_sellectimage.setEnabled(false);
                }
                break;
            case R.id.imageView33:
                if (listTag.size() < 3) {
                    addTag();
                }
                if(listTag.size() == 3){
                    addtag.setVisibility(View.GONE);
                }
                break;
            case R.id.tag1:
                showSnack(tag1.getText().toString(),0);
                break;
            case R.id.tag2:
                showSnack(tag2.getText().toString(),1);
                break;
            case R.id.tag3:
                showSnack(tag3.getText().toString(),2);
                break;
        }
    }

    public void showSnack(String tag, final int position){
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"Do you want remove "+tag+"",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listTag.remove(position);
                settag();
                edt_tag.setVisibility(View.VISIBLE);
                addtag.setVisibility(View.VISIBLE);
                snackbar.dismiss();
            }
        }).show();
    }

    public void addTag() {
        listTag.add(edt_tag.getText().toString());
        edt_tag.setText("");
        settag();
    }


    public void settag(){
        if (listTag.size()==0){
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);
            tag1.setText("");
            tag2.setText("");
            tag3.setText("");
        }
        else if (listTag.size()==1){
            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);

            tag1.setText(listTag.get(0));
            tag2.setText("");
            tag3.setText("");
        }else if(listTag.size()==2){
            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.VISIBLE);
            tag3.setVisibility(View.GONE);


            tag1.setText(listTag.get(0));
            tag2.setText(listTag.get(1));
            tag3.setText("");
        }else {

            tag1.setVisibility(View.VISIBLE);
            tag2.setVisibility(View.VISIBLE);
            tag3.setVisibility(View.VISIBLE);
            tag1.setText(listTag.get(0));
            tag2.setText(listTag.get(1));
            tag3.setText(listTag.get(2));
            edt_tag.setVisibility(View.GONE);
        }
    }

    public void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_MULTIPLE);
    }

    ArrayList<ImageClick> lisImmage = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    mImageUri = data.getData();
//                    lisImmage.add(mImageUri);

                    // Get the cursor
                    Cursor cursor =getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();
                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
        long time = System.currentTimeMillis();
        if (numclick == 1) {
            Picasso.with(AddbookActivity.this).load(mImageUri).into(imagebook1);
//            imagebook1.setImageURI(mImageUri);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgOne=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        } else if (numclick == 2) {
            Picasso.with(AddbookActivity.this).load(mImageUri).into(imagebook2);
//            imagebook2.setImageURI(mImageUri);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgTwo=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        } else if (numclick == 3) {
            Picasso.with(AddbookActivity.this).load(mImageUri).into(imagebook3);
//            imagebook3.setImageURI(mImageUri);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgThree=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        }

        if (numimageclick == 1) {
//            imagebook1.setImageURI(mImageUri);
//            lisImmage.remove(0);
            Picasso.with(AddbookActivity.this).load(mImageUri).into(imagebook1);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgOne=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        } else if (numimageclick == 2) {
//            imagebook2.setImageURI(mImageUri);
//            lisImmage.remove(1);
            Picasso.with(AddbookActivity.this).load(mImageUri).into(imagebook2);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgTwo=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        } else if (numimageclick == 3) {
            Picasso.with(AddbookActivity.this).load(mImageUri).into(imagebook3);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgThree=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
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

    public void addImages(){
        uploadFileController.uploadFile(bmap,listFileName);
    }

    public class uploaddata extends AsyncTask<Void,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            bookController = new BookController();
            success = bookController.addbook(book, session_id).equals("")?false:true;
            return bookController.addbook(book, session_id);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if(!result.equals("")){
                    if(type == 0){
                        Intent intent = new Intent(AddbookActivity.this,SwapActivity.class);
                        intent.putExtra("Book",bookedit);
                        startActivity(intent);
                        finish();
                    }
                    else{

                        //Gửi thông báo sách cho người tìm
                    List<Hashtable> list = new ArrayList<>();
                    Notification notification = new Notification("Respone Wishboard",result , "14");
                    Hashtable obj = ObjectCommon.ObjectDymanic(notification);
                    obj.put("user_id", user_id+"");
                    obj.put("messages", userName + " send you a respone book");
                    list.add(obj);
                    NotificationController controller = new NotificationController();
                    controller.sendNotification(list);

                        onBackPressed();
                    }

                }
            }catch (Exception e){

            }
        }
    }
}
















//package redix.booxtown.activity;
//
//import android.Manifest;
//import android.app.Dialog;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TableRow;
//import android.widget.TextView;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.ArrayList;
//
//import redix.booxtown.R;
//import redix.booxtown.controller.GetAllGenreAsync;
//import redix.booxtown.custom.CustomListviewGenre;
//import redix.booxtown.custom.MenuBottomCustom;
//import redix.booxtown.model.Genre;
//
///**
// * Created by Administrator on 30/08/2016.
// */
//public class AddbookActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {
//    private GoogleMap mMap;
//    private SupportMapFragment mMapFragment;
//    private MenuBottomCustom bottomCustom;
//    ArrayList<Genre> genre;
////    String[] genvalue = {"Architecture","Business and Economics","Boy,Mid and Spirit","Children","Computers and Technology",
////            "Crafts and Hobbies","Education","Family,Parenting and Relationships","Fiction and Literature","Food and Drink"
////    };
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_book_with_swap);
//        genre = new ArrayList<>();
//        for (int i = 0; i< GetAllGenreAsync.list.size(); i++){
//            Genre genrel = new Genre();
//            genrel.setValue(GetAllGenreAsync.list.get(i));
//            genre.add(genrel);
//        }
//        Log.d("genkjkjk",String.valueOf(genre.size()));
//        //------------------------------------------------------------
//        View view_menu_top = (View) findViewById(R.id.menu_top_add_book_with_swap);
//        TextView txtTitle = (TextView) view_menu_top.findViewById(R.id.txt_title);
//        txtTitle.setText("Add book");
//        txtTitle.setGravity(Gravity.CENTER_VERTICAL);
//        ImageView img_component = (ImageView) findViewById(R.id.img_menu_component);
//        img_component.setVisibility(View.INVISIBLE);
//        ImageView imageView_back=(ImageView) findViewById(R.id.img_menu);
//        imageView_back.setImageResource(R.drawable.btn_sign_in_back);
//
//        imageView_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        //------------------------------------------------------------
//        View view=(View) findViewById(R.id.menu_bottom_add_book_swap);
//        bottomCustom=new MenuBottomCustom(view,this,3);
//        bottomCustom.setDefaut(3);
//
//        mMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map_editlisting));
//        mMapFragment.getMapAsync(AddbookActivity.this);
//        btnDelete();
//
//        //show edittext when check to sell
//        final CheckBox checkBox = (CheckBox)findViewById(R.id.ck_sell_editlisting);
//        final EditText edt_editlisting_sell = (EditText)findViewById(R.id.edt_editlisting_sell);
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkBox.isChecked()){
//                    edt_editlisting_sell.setVisibility(View.VISIBLE);
//
//                }else {
//                    edt_editlisting_sell.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        Button btn_menu_editlist_addbook = (Button)findViewById(R.id.btn_menu_listing_addbook);
//        btn_menu_editlist_addbook.setVisibility(View.VISIBLE);
//
//        TableRow row= (TableRow) findViewById(R.id.row_edit_book) ;
//        row.setVisibility(View.GONE);
//
//        btn_menu_editlist_addbook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               Intent intent = new Intent(getApplicationContext(),SwapActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        ImageView imageView=(ImageView) findViewById(R.id.img_menu_genre);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(AddbookActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.dialog_genre);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                ListView listView_genre=(ListView)dialog.findViewById(R.id.listView_genre);
//                listView_genre.setAdapter(new CustomListviewGenre(AddbookActivity.this,genre));
//                dialog.show();
//
//                Button button_spiner_genre = (Button)dialog.findViewById(R.id.button_spiner_genre);
//                button_spiner_genre.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                ImageView img_close_dialoggenre = (ImageView)dialog.findViewById(R.id.img_close_dialoggenre);
//                img_close_dialoggenre.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });
//        TextView txt_view = (TextView) findViewById(R.id.txt_menu_genre1);
//        txt_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(AddbookActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.dialog_genre);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                ListView listView_genre=(ListView)dialog.findViewById(R.id.listView_genre);
//                listView_genre.setAdapter(new CustomListviewGenre(AddbookActivity.this,genre));
//
//                dialog.show();
//
//                Button button_spiner_genre = (Button)dialog.findViewById(R.id.button_spiner_genre);
//                button_spiner_genre.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//                ImageView img_close_dialoggenre = (ImageView)dialog.findViewById(R.id.img_close_dialoggenre);
//                img_close_dialoggenre.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//
//            }
//        });
//
//
//    }
//
//    public void btnDelete(){
//        Button btn_menu_editlist_delete = (Button)findViewById(R.id.btn_menu_editlist_delete);
//        btn_menu_editlist_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog = new Dialog(AddbookActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.dialog_delete_editlisting);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//    }
//
//    @Override
//    public void onInfoWindowClick(Marker marker) {
//
//    }
//
//    @Override
//    public void onMapLongClick(LatLng latLng) {
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        Location location;
//        Boolean isGPSEnabled=false;
//        Boolean isNetworkEnabled = false;
//        mMap = googleMap;
//        if (Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission(AddbookActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(AddbookActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//        // getting GPS status
//        isGPSEnabled = service
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        isNetworkEnabled = service
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//
//        System.out.print("GPS:"+isGPSEnabled);
//        System.out.print("net:"+isNetworkEnabled);
//        if(isGPSEnabled){
//            location = service
//                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                addMaker(location);
//            }
//
//        }
//        if(isNetworkEnabled){
//            location = service
//                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (location != null) {
//                addMaker(location);
//            }
//        }
//    }
//    public void addMaker(Location location){
//        // create marker
//        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
//        // Changing marker icon
//        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_buy",110, 150)));
//        // adding marker
//        mMap.addMarker(marker);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 8));
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.getUiSettings().setAllGesturesEnabled(true);
//        mMap.setTrafficEnabled(true);
//    }
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        bottomCustom.setDefaut(3);
//    }
//
//    public Bitmap resizeMapIcons(String icon, int width, int height){
//        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(icon, "drawable", getApplication().getPackageName()));
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
//        return resizedBitmap;
//    }
//}
