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
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.GPSTracker;
import redix.booxtown.controller.GetAllGenreAsync;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.ResizeImage;
import redix.booxtown.controller.UploadFileController;

import redix.booxtown.custom.CustomListviewGenre;
import redix.booxtown.fragment.ListingsFragment;
import redix.booxtown.model.Book;
import redix.booxtown.model.Explore;
import redix.booxtown.model.Genre;
import redix.booxtown.model.ImageClick;

public class ListingCollectionActivity extends Fragment implements OnMapReadyCallback,View.OnClickListener {
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    ImageView btn_sellectimage, imagebook1, imagebook2, imagebook3, addtag;
    UploadFileController uploadFileController;
    Button btn_menu_editlist_delete, btn_menu_editlisting_update, btn_menu_listing_addbook;
    String username;
    ArrayList<Genre> genre;
    double latitude, longitude;
    EditText edt_tilte, edt_author, edt_tag, edt_editlisting_sell;
    TableRow row;
    CheckBox swap, free, sell;
    String session_id;
    float price;
    String condition;
    Uri mImageUri;
    String s;
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
    String listeditimage;
//    String[] genravalue = {"Architecture", "Business and Economics", "Boy,Mid and Spirit", "Children", "Computers and Technology",
//            "Crafts and Hobbies", "Education", "Family,Parenting and Relationships", "Fiction and Literature", "Food and Drink",
//            "Health and Fitness","History and Politics","Homes Gaedens and DIY","Humor and Comedy","Languages","Manuals and Guides"
//    };


    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    ArrayList<String> listTag;
    Book bookedit;
    TableRow tb_menu;
    ImageView imageView_back;
    SupportMapFragment mapFragment;
    String[] image;
    String imageOrigin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_listing_collection, container, false);


        //map view
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragment_map_editlisting);
        mapFragment.getMapAsync(this);
        //end

        edt_editlisting_sell = (EditText) v.findViewById(R.id.edt_editlisting_sell);
        swap = (CheckBox) v.findViewById(R.id.checkBox);
        sell = (CheckBox) v.findViewById(R.id.ck_sell_editlisting);
        free = (CheckBox) v.findViewById(R.id.checkBox3);

        edt_author = (EditText) v.findViewById(R.id.editText9);
        Spannable wordtoSpan1 = new SpannableString("Author *");
        wordtoSpan1.setSpan(new ForegroundColorSpan(Color.RED),7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        edt_author.setHint(wordtoSpan1);


        edt_tilte = (EditText) v.findViewById(R.id.editText8);
        Spannable wordtoSpan = new SpannableString("Book Title *");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 11, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        edt_tilte.setHint(wordtoSpan);

        edt_tag = (EditText) v.findViewById(R.id.editText10);
        addtag = (ImageView) v.findViewById(R.id.imageView33);
        addtag.setOnClickListener(this);
        tag1 = (TextView) v.findViewById(R.id.tag1);
        tag2 = (TextView) v.findViewById(R.id.tag2);
        tag3 = (TextView) v.findViewById(R.id.tag3);
        tag1.setOnClickListener(this);
        tag2.setOnClickListener(this);
        tag3.setOnClickListener(this);
        btn_menu_listing_addbook = (Button) v.findViewById(R.id.btn_menu_listing_addbook);
        btn_menu_editlist_delete = (Button) v.findViewById(R.id.btn_menu_editlist_delete);
        btn_menu_editlisting_update = (Button) v.findViewById(R.id.btn_menu_editlisting_update);
        btn_menu_editlist_delete.setOnClickListener(this);
        btn_menu_editlisting_update.setOnClickListener(this);
        row = (TableRow) v.findViewById(R.id.row_edit_book);
        s = getArguments().getString("activity");
        listTag = new ArrayList<>();
        genre = new ArrayList<>();
        for (int i = 0; i < GetAllGenreAsync.list.size(); i++) {
            Genre genrel = new Genre();
            genrel.setValue(GetAllGenreAsync.list.get(i));
            genre.add(genrel);
        }
        btn_sellectimage = (ImageView) v.findViewById(R.id.imageView32);
        //picaso
        Picasso.with(getContext()).load(R.drawable.btn_add).into(btn_sellectimage);
        //end
        btn_sellectimage.setOnClickListener(this);
        uploadFileController = new UploadFileController();
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        username = pref.getString("username", null);
        session_id = pref.getString("session_id", null);

        //end
        //spinner
        ImageView imageView = (ImageView) v.findViewById(R.id.img_menu_genre);
        Picasso.with(getContext()).load(R.drawable.btn_down).into(imageView);
        imagebook1 = (ImageView) v.findViewById(R.id.imageView29);
        imagebook2 = (ImageView) v.findViewById(R.id.imageView30);
        imagebook3 = (ImageView) v.findViewById(R.id.imageView31);
        seekbar = (SeekBar) v.findViewById(R.id.seekBar2);

        listUserName= new ArrayList<>();

//        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.abc);
//        Bitmap thumb=Bitmap.createBitmap(64,64, Bitmap.Config.ARGB_8888);
//        Canvas canvas=new Canvas(thumb);
//        canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
//                new Rect(0,0,thumb.getWidth(),thumb.getHeight()),null);
//        Drawable drawable = new BitmapDrawable(getResources(),thumb);
        //seekbar.setLeftThumbDrawable(drawable);

        //seekbar.setLeftThumbHighlightDrawable(drawable);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_genre);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final ListView listView_genre = (ListView) dialog.findViewById(R.id.listView_genre);
                final CustomListviewGenre adapter = new CustomListviewGenre(getActivity(), genre);
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

        TextView txt_view = (TextView) v.findViewById(R.id.txt_menu_genre1);
        txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_genre);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ListView listView_genre = (ListView) dialog.findViewById(R.id.listView_genre);
                if (s.equals("edit")) {
                    String[] separated = bookedit.getGenre().split(";");
                    for (int i = 0; i < genre.size(); i++) {
                        for (int j = 0; j < separated.length; j++) {
                            if (genre.get(i).getValue().equals(separated[j].trim())) {
                                genre.get(i).setIscheck(true);
                            }
                        }
                    }
                    listView_genre.setAdapter(new CustomListviewGenre(getActivity(), genre));
                } else {
                    listView_genre.setAdapter(new CustomListviewGenre(getActivity(), genre));
                }

                dialog.show();

                Button button_spiner_genre = (Button) dialog.findViewById(R.id.button_spiner_genre);
                button_spiner_genre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ImageView img_close_dialoggenre = (ImageView) dialog.findViewById(R.id.img_close_dialoggenre);
                Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(img_close_dialoggenre);
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
        final CheckBox checkBox = (CheckBox) v.findViewById(R.id.ck_sell_editlisting);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    edt_editlisting_sell.setVisibility(View.VISIBLE);

                } else {
                    edt_editlisting_sell.setVisibility(View.GONE);
                }
            }
        });
        //end

        btn_menu_listing_addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),ListingsFragment.class);
//                startActivity(intent);

                addbook();
//                Addbook addbook = new Addbook();
//                addbook.execute();
                addImages();
                uploaddata uploaddata  = new uploaddata();
                uploaddata.execute();
                MainAllActivity mainAllActivity = (MainAllActivity) getActivity();
                mainAllActivity.callFragment(new ListingsFragment());
            }
        });

        //change color tab
        TextView txt_add_book = (TextView) v.findViewById(R.id.txt_add_book1);
        txt_add_book.setTextColor(getResources().getColor(R.color.color_text));
        txt_add_book.setBackgroundColor(getResources().getColor(R.color.dot_light_screen1));

        TextView txt_my_listings = (TextView) v.findViewById(R.id.txt_my_listings1);
        txt_my_listings.setTextColor(getResources().getColor(R.color.dot_light_screen1));
        txt_my_listings.setBackgroundColor(getResources().getColor(R.color.color_text));

        txt_my_listings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainAllActivity main = (MainAllActivity) getActivity();
                main.callFragment(new ListingsFragment());
            }
        });

        txt_my_listings.setText("My listings"+"("+getArguments().getInt("num_list")+")");
        tb_menu = (TableRow) v.findViewById(R.id.tableRow5);
        imageView_back = (ImageView) getActivity().findViewById(R.id.img_menu);
        imageOrigin="";
        imgOne="";
        imgTwo="";
        imgThree="";

        if (s.equals("edit")) {
            btn_menu_listing_addbook.setVisibility(View.GONE);
            row.setVisibility(View.VISIBLE);
            bookedit = (Book) getArguments().getSerializable("bookedit");
            Log.d("boooook", String.valueOf(bookedit.getPhoto()));
            edt_author.setText(bookedit.getAuthor().toString());
            edt_tilte.setText(bookedit.getTitle().toString());
            String[] listtag = bookedit.getHash_tag().split(";");
            for (int i=0;i<listtag.length;i++){
                listTag.add(listtag[i]);
            }
            //settag();
            if (listtag.length==1){
                tag1.setText(listtag[0]);
            }else if (listtag.length==2){
                tag1.setText(listtag[0]+"");
                tag2.setText(listtag[1]);
            }else {
                tag1.setText(listtag[0]+"");
                tag2.setText(listtag[1]+"");
                tag3.setText(listtag[2]+"");
                addtag.setVisibility(View.GONE);
                edt_tag.setVisibility(View.GONE);
            }
//            edt_tag.setText(bookedit.getHash_tag().toString());
//                    tb_menu.setVisibility(View.GONE);

            Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(imageView_back);
            imageView_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callFragment(new ListingsFragment());
                }
            });

            char array[] = bookedit.getAction().toCharArray();
            if (String.valueOf(array[0]).contains("1")) {
                swap.setChecked(true);
            }
            if (String.valueOf(array[1]).contains("1")) {
                free.setChecked(true);
            }
            if (String.valueOf(array[2]).contains("1")) {
                sell.setChecked(true);
                edt_editlisting_sell.setVisibility(View.VISIBLE);
                if (bookedit.getPrice()!=0){
                    edt_editlisting_sell.setText(String.valueOf(bookedit.getPrice()));
                }
                else {
                    edt_editlisting_sell.setText(String.valueOf(0));
                }
            }

            if (Integer.valueOf(bookedit.getCondition())!=0){
                seekbar.setProgress(Integer.valueOf(bookedit.getCondition()));
            }
            imageOrigin=bookedit.getPhoto();
            image = bookedit.getPhoto().split(";");

            if (image.length>0) {
                if (image[0] != null) {
                    imgOne = image[0];
                }
            }
            if (image.length>1) {
                if (image[1] != null) {
                    imgTwo = image[1];
                }
            }
            if (image.length>2) {
                if (image[2] != null) {
                    imgThree = image[2];
                }
            }

            arrImage = new ArrayList<>();

            int index=0;
            if(image.length>0) {

                for (int i = 0; i < image.length; i++) {
                    index = image[i].indexOf("_+_");
                    if (index > 0 && image[i].length() > 3) {
                        String sss = image[i].substring(index + 3, image[i].length());
                        listUserName.add(image[i].substring(0,index));
                        arrImage.add(sss);
                    } else {
                        listUserName.add(image[i]);
                        arrImage.add(image[i]);
                    }
                }
                if (image.length != 0) {
                    if (arrImage.size() == 1) {
                        Picasso.with(getActivity()).
                                load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(0) + "&image=" + arrImage.get(0) + "").
                                into(imagebook1);
                    } else if (arrImage.size() == 2) {
                        Picasso.with(getActivity()).
                                load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(0) + "&image=" + arrImage.get(0) + "").
                                into(imagebook1);
                        Picasso.with(getActivity()).
                                load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(1) + "&image=" + arrImage.get(1) + "").
                                into(imagebook2);
                    } else {
                        //String tmp= ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + username + "&image=" + arrImage.get(0) + "";
                        Picasso.with(getActivity()).
                                load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(0) + "&image=" + arrImage.get(0) + "").
                                into(imagebook1);
                        Picasso.with(getActivity()).
                                load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(1) + "&image=" + arrImage.get(1) + "").
                                into(imagebook2);
                        Picasso.with(getActivity()).
                                load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(2) + "&image=" + arrImage.get(2) + "").
                                into(imagebook3);
                    }
                }
            }

        } else {
            btn_menu_listing_addbook.setVisibility(View.VISIBLE);
            row.setVisibility(View.GONE);
        }

        imagebook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numimageclick = 1;
                choseImage();
                if (s.equals("edit")) {
                    if (arrImage.size()>1){
                        arrImage.remove(0);
                    }
                }
            }
        });

        imagebook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numimageclick = 2;
                choseImage();
                if (s.equals("edit")) {
                    if (arrImage.size()>2){
                        arrImage.remove(1);
                    }
                }
            }
        });

        imagebook3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numimageclick = 3;
                choseImage();
                if (s.equals("edit")) {
                    if (arrImage.size()==3){
                        arrImage.remove(2);
                    }
                }
            }
        });
        addbook();
        return v;
    }


    public void callFragment(Fragment fragment) {
        FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addbook() {
        GPSTracker gps = new GPSTracker(getActivity());
        for (int i = 0; i < lisImmage.size(); i++) {
            try {
                long time = System.currentTimeMillis();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), lisImmage.get(i).getUri());
                Bitmap photoBitMap = Bitmap.createScaledBitmap(bitmap,250,270, true);
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
                    tag = tag + listTag.get(i).replace("|","") + ";";
                } else {
                    tag = tag + listTag.get(i).replace("|","");
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
        if (numclick!=0||numimageclick!=0){
            if (!s.equals("edit")){
                book.setPhoto(imagename);
            }else {
                String imageupdate = imgOne+";"+ imgTwo+";"+imgThree;
                book.setPhoto(imageupdate);
                book.setId(bookedit.getId());
            }
        }else {
            if (s.equals("edit")){
                book.setId(bookedit.getId());
                if(imageOrigin.equals("")) {
                    book.setPhoto(imageOrigin);
                }
            }
        }
        if (sell.isChecked()){
            if (edt_editlisting_sell.getText().toString().isEmpty()){
                Toast.makeText(getActivity(),"Price no fill",Toast.LENGTH_LONG).show();
                return;
            }else {
                price = Float.valueOf(edt_editlisting_sell.getText().toString());
                book.setPrice(price);
            }
        }
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
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager service = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
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
        marker.icon((BitmapDescriptorFactory.fromBitmap(ResizeImage.resizeMapIcons(getContext(),"icon_buy",110, 150))));
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
            case R.id.btn_menu_editlist_delete:
                showdialog();
                break;
            case R.id.btn_menu_editlisting_update:
                addbook();
                if (numclick!=0 || numimageclick!=0){
//                    Addbook addbook1 = new Addbook();
//                    addbook1.execute();
                    addImages();
                }
                editbook editbook = new editbook();
                editbook.execute();
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
        final Snackbar snackbar = Snackbar.make(getView(),"Do you want remove "+tag+"",Snackbar.LENGTH_INDEFINITE);
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

    public void showdialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("You want to delete this book?");
        alertDialogBuilder
                .setMessage("Click yes to delete!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deletebook deletebook =new deletebook();
                                deletebook.execute();
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void addTag() {
        listTag.add(edt_tag.getText().toString());
        edt_tag.setText("");
        settag();
    }

    public void settag(){
        if (listTag.size()==0){
            tag1.setText("");
            tag2.setText("");
            tag3.setText("");
        }
        else if (listTag.size()==1){
            tag1.setText(listTag.get(0));
            tag2.setText("");
            tag3.setText("");
        }else if(listTag.size()==2){
            tag1.setText(listTag.get(0));
            tag2.setText(listTag.get(1));
            tag3.setText("");
        }else {
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
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == getActivity().RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    mImageUri = data.getData();
//                    lisImmage.add(mImageUri);

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(mImageUri,
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
                            Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
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
            Picasso.with(getActivity()).load(mImageUri).into(imagebook1);
//            imagebook1.setImageURI(mImageUri);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgOne=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        } else if (numclick == 2) {
            Picasso.with(getActivity()).load(mImageUri).into(imagebook2);
//            imagebook2.setImageURI(mImageUri);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgTwo=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        } else if (numclick == 3) {
            Picasso.with(getActivity()).load(mImageUri).into(imagebook3);
//            imagebook3.setImageURI(mImageUri);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgThree=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        }

        if (numimageclick == 1) {
//            imagebook1.setImageURI(mImageUri);
//            lisImmage.remove(0);
            Picasso.with(getActivity()).load(mImageUri).into(imagebook1);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgOne=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        } else if (numimageclick == 2) {
//            imagebook2.setImageURI(mImageUri);
//            lisImmage.remove(1);
            Picasso.with(getActivity()).load(mImageUri).into(imagebook2);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgTwo=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        } else if (numimageclick == 3) {
//            imagebook3.setImageURI(mImageUri);
//            lisImmage.remove(2);
            Picasso.with(getActivity()).load(mImageUri).into(imagebook3);
            ImageClick imageClick= new ImageClick(mImageUri,username+"_+_"+String.valueOf(time)+getFileName(mImageUri));
            lisImmage.add(imageClick);
            imgThree=username+"_+_"+String.valueOf(time)+getFileName(mImageUri);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
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
    ArrayList<Uri> listImageTemp = new ArrayList<>();
    ArrayList<Bitmap> bmap = new ArrayList<>();

    public void addImages(){
        uploadFileController.uploadFile(bmap,listFileName);
    }

//    public class Addbook extends AsyncTask<Void, Void, Boolean> {
//
//        public ProgressDialog dialog;
//
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//
////            Thread thread = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    if (uploadFileController.uploadFile(bmap.get(0), "" + username + "::" + listFileName.get(0))) {
////                    }
////                }
////            });
////
////            Thread thread1 = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    if (uploadFileController.uploadFile(bmap.get(1), "" + username + "::" + listFileName.get(1)))
////                        ;
////                }
////            });
////
////            Thread thread2 = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    uploadFileController.uploadFile(bmap.get(2), "" + username + "::" + listFileName.get(2) + "");
////
////                }
////            });
//
//
//            return success;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            dialog = new ProgressDialog(getActivity());
//            dialog.setMessage("Please wait...");
//            dialog.setIndeterminate(true);
//            dialog.show();
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Boolean bolean) {
//            if (bolean == true) {
//                dialog.dismiss();
////                Toast.makeText(getActivity(),"Addbook Success",Toast.LENGTH_LONG).show();
//
//            } else {
//                dialog.dismiss();
////                Toast.makeText(getActivity(),"Addbook Faile",Toast.LENGTH_LONG).show();
//            }
//            super.onPostExecute(bolean);
//        }
//    }


    public class uploaddata extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            bookController = new BookController();
            success = bookController.addbook(book, session_id);
            return success;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean==true){
                callFragment(new ListingsFragment());
            }
            super.onPostExecute(aBoolean);
        }
    }


    public class editbook extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            BookController bookController = new BookController();
            Boolean sucess = bookController.updatebook(book, session_id);
            return sucess;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                dialog.dismiss();
                Toast.makeText(getActivity(),Information.noti_update_success, Toast.LENGTH_LONG).show();
                callFragment(new ListingsFragment());
            } else {
                dialog.dismiss();
                Toast.makeText(getActivity(),Information.noti_update_fail, Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aBoolean);
        }
    }

    public class deletebook extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            BookController bookController = new BookController();
            Boolean successs = bookController.deletebook(bookedit.getId());
            return successs;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean ==true){
                Toast.makeText(getActivity(),Information.noti_delete_success,Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getActivity(),Information.noti_delete_fail,Toast.LENGTH_LONG).show();
            }
        }
    }
}
