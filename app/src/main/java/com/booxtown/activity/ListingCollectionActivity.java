package com.booxtown.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.booxtown.adapter.AdapterExplore;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.BookController;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.GPSTracker;
import com.booxtown.controller.GetAllGenreAsync;
import com.booxtown.controller.Information;
import com.booxtown.controller.InputFilterMinMax;
import com.booxtown.controller.UploadFileController;
import com.booxtown.controller.Utility;
import com.booxtown.model.Genre;
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

import com.booxtown.R;

import com.booxtown.controller.IconMapController;
import com.booxtown.controller.ResizeImage;

import com.booxtown.custom.CustomListviewGenre;
import com.booxtown.fragment.ListingsFragment;
import com.booxtown.fragment.MyProfileFragment;
import com.booxtown.model.Book;
import com.booxtown.model.ImageClick;

public class ListingCollectionActivity extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    ImageView btn_sellectimage, imagebook1, imagebook2, imagebook3, addtag, imageView_back;
    UploadFileController uploadFileController;
    Button btn_menu_editlist_delete, btn_menu_editlisting_update, btn_menu_listing_addbook;
    TableRow tbl_price_sell;
    String username, session_id, condition, s, imgOne, imgTwo, imgThree, imageOrigin, titl;
    ArrayList<Genre> genre;
    double latitude, longitude;
    EditText edt_tilte, edt_author, edt_tag, edt_editlisting_sell;
    TableRow row;
    CheckBox swap, free, sell;
    float price;
    Uri mImageUri;
    ArrayList<String> arrImage, listUserName, listTag;
    SeekBar seekbar;
    BookController bookController;
    boolean success;
    Book book, bookedit;
    public int numclick = 0, numimageclick = 0;

    int PICK_IMAGE_MULTIPLE = 1, back;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    TableRow tb_menu;
    SupportMapFragment mapFragment;
    String[] image;
    TextView txt_add_book, txt_my_listings, tag1, tag2, tag3;
    int typeChooseImage = 0;
    String sChooseImage = "";
    LatLng latLng_new;
    boolean flagTag = false;
    RadioButton radioButton_current, radioButton_another;
    TextView txt_menu_genre1;
    Bitmap bitmaps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_listing_collection, container, false);

        //map view
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.fragment_map_editlisting);
        mapFragment.getMapAsync(this);

        radioButton_current = (RadioButton) v.findViewById(R.id.radioButton_current);
        radioButton_another = (RadioButton) v.findViewById(R.id.radioButton_another);

        //end
        tbl_price_sell = (TableRow) v.findViewById(R.id.row_price_sell);
        edt_editlisting_sell = (EditText) v.findViewById(R.id.edt_editlisting_sell);
        swap = (CheckBox) v.findViewById(R.id.checkBox);
        sell = (CheckBox) v.findViewById(R.id.ck_sell_editlisting);
        free = (CheckBox) v.findViewById(R.id.checkBox3);

        edt_author = (EditText) v.findViewById(R.id.editText9);
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

        edt_tilte = (EditText) v.findViewById(R.id.editText8);
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
        edt_tag = (EditText) v.findViewById(R.id.editText10);
        edt_tag.setVisibility(View.GONE);
        addtag = (ImageView) v.findViewById(R.id.imageView33);
        addtag.setOnClickListener(this);
        tag1 = (TextView) v.findViewById(R.id.tag1);
        tag2 = (TextView) v.findViewById(R.id.tag2);
        tag3 = (TextView) v.findViewById(R.id.tag3);

        tag1.setVisibility(View.GONE);
        tag2.setVisibility(View.GONE);
        tag3.setVisibility(View.GONE);

        tag1.setOnClickListener(this);
        tag2.setOnClickListener(this);
        tag3.setOnClickListener(this);
        imagebook1 = (ImageView) v.findViewById(R.id.imageView29);
        imagebook2 = (ImageView) v.findViewById(R.id.imageView30);
        imagebook3 = (ImageView) v.findViewById(R.id.imageView31);
        btn_menu_listing_addbook = (Button) v.findViewById(R.id.btn_menu_listing_addbook);
        btn_menu_editlist_delete = (Button) v.findViewById(R.id.btn_menu_editlist_delete);
        btn_menu_editlisting_update = (Button) v.findViewById(R.id.btn_menu_editlisting_update);
        btn_menu_editlist_delete.setOnClickListener(this);
        btn_menu_editlisting_update.setOnClickListener(this);
        row = (TableRow) v.findViewById(R.id.row_edit_book);
        s = getArguments().getString("activity");
        back = getArguments().getInt("back");

        try {
            SharedPreferences pref = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
            String ss = pref.getString("image", "");
            File imgFile = new File(pref.getString("image", ""));
            if (imgFile.exists()) {
                bitmaps = BitmapFactory.decodeFile(pref.getString("image", ""));

            }


        } catch (Exception err) {
            String sss = err.getMessage();
        }

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
        username = pref.getString("username", null);
        session_id = pref.getString("session_id", null);

        //end
        //spinner
        ImageView imageView = (ImageView) v.findViewById(R.id.img_menu_genre);
        Picasso.with(getContext()).load(R.drawable.btn_down).into(imageView);

        seekbar = (SeekBar) v.findViewById(R.id.seekBar2);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.abc);
        Bitmap thumb = Bitmap.createBitmap(46, 46, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(thumb);
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(0, 0, thumb.getWidth(), thumb.getHeight()), null);
        Drawable drawable = new BitmapDrawable(getResources(), thumb);
        seekbar.setThumb(drawable);
        seekbar.setProgress(50);
        listUserName = new ArrayList<>();
        final TextView txt_view = (TextView) v.findViewById(R.id.txt_menu_genre1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_genre);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                RecyclerView rv_genre = (RecyclerView) dialog.findViewById(R.id.listView_genre);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                rv_genre.setLayoutManager(linearLayoutManager);
                CustomListviewGenre adapter = new CustomListviewGenre(getActivity(), genre);
                dialog.show();

                if (s.equals("edit")) {
                    String[] separated = bookedit.getGenre().split(";");
                    for (int i = 0; i < genre.size(); i++) {
                        for (int j = 0; j < separated.length; j++) {
                            if (genre.get(i).getValue().equals(separated[j].trim())) {
                                genre.get(i).setIscheck(true);
                            }
                        }
                    }
                    rv_genre.setAdapter(new CustomListviewGenre(getActivity(), genre));
                } else {
                    rv_genre.setAdapter(new CustomListviewGenre(getActivity(), genre));
                }

                rv_genre.setAdapter(adapter);
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
                img_close_dialoggenre.setImageResource(R.drawable.btn_close_filter);
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
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_genre);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RecyclerView rv_genre = (RecyclerView) dialog.findViewById(R.id.listView_genre);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                rv_genre.setLayoutManager(linearLayoutManager);
                CustomListviewGenre adapter = new CustomListviewGenre(getActivity(), genre);
                dialog.show();

                if (s.equals("edit")) {
                    String[] separated = bookedit.getGenre().split(";");
                    for (int i = 0; i < genre.size(); i++) {
                        for (int j = 0; j < separated.length; j++) {
                            if (genre.get(i).getValue().equals(separated[j].trim())) {
                                genre.get(i).setIscheck(true);
                            }
                        }
                    }
                    rv_genre.setAdapter(new CustomListviewGenre(getActivity(), genre));
                } else {
                    rv_genre.setAdapter(new CustomListviewGenre(getActivity(), genre));
                }

                rv_genre.setAdapter(adapter);
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
//        CheckBox checkBox = (CheckBox) v.findViewById(R.id.ck_sell_editlisting);
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

        btn_menu_listing_addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkCheckBox()) {
                        if (addbook(0)) {
                            addImages();
                            uploaddata uploaddata = new uploaddata(getContext());
                            uploaddata.execute();
                            MainAllActivity mainAllActivity = (MainAllActivity) getActivity();
                            mainAllActivity.callFragment(new ListingsFragment());
                        }
                    }
                } catch (Exception exx) {

                }
            }
        });

        //change color tab
        txt_add_book = (TextView) v.findViewById(R.id.txt_add_book1);
        txt_add_book.setTextColor(getResources().getColor(R.color.color_text));
        txt_add_book.setBackgroundColor(getResources().getColor(R.color.dot_light_screen1));

        if (s.equals("edit")) {
            MainAllActivity.setTxtTitle("Edit Listing");
            txt_add_book.setText("Edit a book");

        }
        txt_my_listings = (TextView) v.findViewById(R.id.txt_my_listings1);
        txt_my_listings.setTextColor(getResources().getColor(R.color.dot_light_screen1));
        txt_my_listings.setBackgroundColor(getResources().getColor(R.color.color_text));

        txt_my_listings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainAllActivity main = (MainAllActivity) getActivity();
                main.callFragment(new ListingsFragment());
            }
        });
        if (getArguments().getInt("num_list") == 0) {
            txt_my_listings.setText("My listings" + "(" + ListingsFragment.num_list + ")");
        } else {
            txt_my_listings.setText("My listings" + "(" + getArguments().getInt("num_list") + ")");
        }
        tb_menu = (TableRow) v.findViewById(R.id.tableRow5);
        if (back == 1) {
            tb_menu.setVisibility(View.GONE);
        }
        imageView_back = (ImageView) getActivity().findViewById(R.id.img_menu);
        imageOrigin = "";
        imgOne = "";
        imgTwo = "";
        imgThree = "";

        if (s.equals("edit")) {
            btn_menu_listing_addbook.setVisibility(View.GONE);
            row.setVisibility(View.VISIBLE);
            bookedit = (Book) getArguments().getSerializable("bookedit");
            addMarkerChoice(new LatLng(bookedit.getLocation_latitude(), bookedit.getLocation_longitude()));
            edt_author.setText(bookedit.getAuthor().toString());
            edt_tilte.setText(bookedit.getTitle().toString());
            String[] separated = bookedit.getGenre().split(";");
            for (int i = 0; i < genre.size(); i++) {
                for (int j = 0; j < separated.length; j++) {
                    if (genre.get(i).getValue().equals(separated[j].trim())) {
                        genre.get(i).setIscheck(true);
                    }
                }
            }
            String[] listtag = bookedit.getHash_tag().split(";");
            for (int i = 0; i < listtag.length; i++) {
                listTag.add(listtag[i]);
            }
            //settag();
            if (listtag.length == 1) {
                tag1.setText(listtag[0]);
                tag1.setVisibility(View.VISIBLE);
            } else if (listtag.length == 2) {
                tag1.setText(listtag[0] + "");
                tag2.setText(listtag[1]);
                tag1.setVisibility(View.VISIBLE);
                tag2.setVisibility(View.VISIBLE);
            } else if (listtag.length == 2) {
                tag1.setText(listtag[0] + "");
                tag2.setText(listtag[1] + "");
                tag3.setText(listtag[2] + "");
                tag1.setVisibility(View.VISIBLE);
                tag2.setVisibility(View.VISIBLE);
                tag3.setVisibility(View.VISIBLE);

                addtag.setVisibility(View.GONE);
                edt_tag.setVisibility(View.GONE);
            }

            Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(imageView_back);
            imageView_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (back == 1) {
                        callFragment(new MyProfileFragment());
                    } else if (back == 3) {
                    } else {
                        callFragment(new ListingsFragment());
                        MainAllActivity.setTxtTitle("Listings");
                    }
                }
            });

            char array[] = bookedit.getAction().toCharArray();
            if (String.valueOf(array[0]).contains("1")) {
                swap.setChecked(true);
            }
            if (String.valueOf(array[2]).contains("1")) {
                free.setChecked(true);
            }
            if (String.valueOf(array[1]).contains("1")) {
                sell.setChecked(true);
                try {
                    if (bookedit.getPrice() > 0) {
                        tbl_price_sell.setVisibility(View.VISIBLE);
                    }
                } catch (Exception exx) {
                }
                edt_editlisting_sell.setVisibility(View.VISIBLE);
                if (bookedit.getPrice() != 0) {
                    edt_editlisting_sell.setText(String.valueOf((int) bookedit.getPrice() + ""));
                } else {
                    edt_editlisting_sell.setText(String.valueOf(0));
                }
            }

            if (Integer.valueOf(bookedit.getCondition()) != 0) {
                seekbar.setProgress(Integer.valueOf(bookedit.getCondition()));
            }
            imageOrigin = bookedit.getPhoto();
            image = bookedit.getPhoto().split(";");

            if (image.length > 0) {
                if (image[0] != null) {
                    imgOne = image[0];
                }
            }
            if (image.length > 1) {
                if (image[1] != null) {
                    imgTwo = image[1];
                }
            }
            if (image.length > 2) {
                if (image[2] != null) {
                    imgThree = image[2];
                }
            }

            arrImage = new ArrayList<>();
            int index = 0;
            if (image.length > 0) {
                for (int i = 0; i < image.length; i++) {
                    index = image[i].indexOf("_+_");
                    if (index > 0 && image[i].length() > 3) {
                        String sss = image[i].substring(index + 3, image[i].length());
                        listUserName.add(image[i].substring(0, index));
                        arrImage.add(sss);
                    } else {
                        listUserName.add(image[i]);
                        arrImage.add(image[i]);
                    }
                }
                if (image.length != 0) {
                    if (arrImage.size() == 1) {
                        final String imageLink = ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(0) + "&image=" + arrImage.get(0) + "";
                        sChooseImage ="1";
                        try {
                            GetWithHeight getWithHeight = new GetWithHeight(getContext(), imageLink, imagebook1);
                            getWithHeight.execute();

                        } catch (Exception exx) {
                            String err = exx.getMessage();
                        }

                    } else if (arrImage.size() == 2) {
                        final String imageLink = ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(0) + "&image=" + arrImage.get(0) + "";
                        final String imageLink2 = ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(1) + "&image=" + arrImage.get(1) + "";
                        sChooseImage = "12";
                        try {
                            GetWithHeight getWithHeight = new GetWithHeight(getContext(), imageLink, imagebook1);
                            getWithHeight.execute();

                            GetWithHeight getWithHeight2 = new GetWithHeight(getContext(), imageLink2, imagebook2);
                            getWithHeight2.execute();

                        } catch (Exception exx) {
                            String err = exx.getMessage();
                        }

                    } else {
                        final String imageLink = ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(0) + "&image=" + arrImage.get(0) + "";
                        final String imageLink2 = ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(1) + "&image=" + arrImage.get(1) + "";
                        final String imageLink3 = ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + listUserName.get(2) + "&image=" + arrImage.get(2) + "";
                        sChooseImage ="123";
                        try {
                            GetWithHeight getWithHeight = new GetWithHeight(getContext(), imageLink, imagebook1);
                            getWithHeight.execute();

                            GetWithHeight getWithHeight2 = new GetWithHeight(getContext(), imageLink2, imagebook2);
                            getWithHeight2.execute();

                            GetWithHeight getWithHeight3 = new GetWithHeight(getContext(), imageLink3, imagebook3);
                            getWithHeight3.execute();

                        } catch (Exception exx) {
                            String err = exx.getMessage();
                        }

                    }
                }
            }

        } else {
            btn_menu_listing_addbook.setVisibility(View.VISIBLE);
            row.setVisibility(View.GONE);


            int orientation = 0;
            if (bitmaps.getHeight() < bitmaps.getWidth()) {
                orientation = 90;
            } else {
                orientation = 0;
            }
            if (orientation != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                bitmaps = Bitmap.createBitmap(bitmaps, 0, 0, bitmaps.getWidth(),
                        bitmaps.getHeight(), matrix, true);
            } else
                bitmaps = Bitmap.createScaledBitmap(bitmaps, bitmaps.getWidth(),
                        bitmaps.getHeight(), true);
            mImageUri = getImageUri(getContext(), bitmaps);

            long time = System.currentTimeMillis();
            try {
                int width = bitmaps.getWidth();
                int height = bitmaps.getHeight();
                float scales = (float) height / (float) width;
                if (width > 250) {
                    Picasso.with(getActivity()).load(mImageUri).resize(250, (int) (250.0 * scales))
                            .centerInside().into(imagebook1);
                } else {
                    Picasso.with(getActivity()).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook1);
                }
                ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                lisImmage.add(imageClick);
                imgOne = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                sChooseImage = sChooseImage + "1";
            } catch (Exception errr) {
                String ssss = errr.getMessage();
            }
        }

        imagebook1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numimageclick = 1;
                typeChooseImage = 2;
                selectImage();
                if (s.equals("edit")) {
                    if (arrImage.size() > 1) {
                        arrImage.remove(0);
                    }
                }
            }
        });

        imagebook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numimageclick = 2;
                typeChooseImage = 2;
                selectImage();
                if (s.equals("edit")) {
                    if (arrImage.size() > 2) {
                        arrImage.remove(1);
                    }
                }
            }
        });

        imagebook3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numimageclick = 3;
                typeChooseImage = 2;
                selectImage();
                if (s.equals("edit")) {
                    if (arrImage.size() == 3) {
                        arrImage.remove(2);
                    }
                }
            }
        });

        try {
            if (latLng_new != null && !s.equals("edit")) {
                GPSTracker gpsTracker = new GPSTracker(getContext());
                latLng_new = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                addMarkerChoice(latLng_new);
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
                    if (latLng_new != null) {
                        GPSTracker gpsTracker = new GPSTracker(getContext());
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
        addbook(1);
        return v;
    }

    public boolean checkCheckBox() {
        final String swap1 = swap.isChecked() == true ? "1" : "0";
        final String sell1 = sell.isChecked() == true ? "1" : "0";
        final String free1 = free.isChecked() == true ? "1" : "0";
        if (free1.equals("1")) {
            if (sell1.equals("1") || swap1.equals("1")) {
                Toast.makeText(getContext(), Information.noti_show_choose_type_addbook, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (swap1.equals("0") && free1.equals("0") && sell1.equals("0")) {
            Toast.makeText(getContext(), Information.noti_show_choose_type_addbook_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (swap1.equals("1") || sell1.equals("1")) {
            if (free1.equals("1")) {
                return false;
            }
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


    public void callFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public boolean addbook(int type) {
        try {
            if (edt_tilte.getText().toString().equals("") || edt_tilte.getText().toString() == null) {
                if (type == 0) {
                    Toast.makeText(getContext(), "Please enter valid a book title", Toast.LENGTH_SHORT).show();
                }
                return false;
            } else if (edt_author.getText().toString().equals("") || edt_author.getText().toString() == null) {
                if (type == 0) {
                    Toast.makeText(getContext(), "Please enter valid a book author", Toast.LENGTH_SHORT).show();
                }
                return false;
            } else if ((listFileName == null || listFileName.size() == 0) && lisImmage.size() <= 0) {
                if (!s.equals("edit")) {
                    if (type == 0) {
                        Toast.makeText(getContext(), "You need to provide at least 1 image for this book", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }

            GPSTracker gps = new GPSTracker(getActivity());
            for (int i = 0; i < lisImmage.size(); i++) {
                try {
                    long time = System.currentTimeMillis();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), lisImmage.get(i).getUri());
                    if (bitmap.getWidth() > 250) {
                        float scale = (float) bitmap.getHeight() / (float) bitmap.getWidth();
                        Bitmap photoBitMap = Bitmap.createScaledBitmap(bitmap, 250, (int) (250.0 * scale), true);
                        bmap.add(photoBitMap);
                    } else {
                        Bitmap photoBitMap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
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
                        if (listFileName.get(i).length() > 0) {
                            imagename = imagename + listFileName.get(i) + ";";
                        }
                    } else {
                        if (listFileName.get(i).length() > 0) {
                            imagename = imagename + listFileName.get(i);
                        }
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
            try {
                book.setLocation_latitude((float) latLng_new.latitude);
                book.setLocation_longitude((float) latLng_new.longitude);
            } catch (Exception exx) {
                String err = exx.getMessage();
            }
            if (numclick != 0 || numimageclick != 0 || !imgOne.equals("")) {
                if (!s.equals("edit")) {
                    String imageupdate = "";
                    if (imgThree.trim().length() > 0) {
                        if (imgTwo.trim().length() > 0) {
                            imageupdate = imgOne.length() > 0 ? imgOne + ";" : "" + imgTwo + ";" + imgThree;
                        } else {
                            imageupdate = imgOne.length() > 0 ? imgOne + ";" : "" + imgThree;
                        }
                    } else {
                        if (imgTwo.trim().length() > 0) {
                            imageupdate = imgOne.length() > 0 ? imgOne + ";" : "" + imgTwo;
                        } else {
                            imageupdate = imgOne.length() > 0 ? imgOne + "" : "";
                        }
                    }
                    book.setPhoto(imagename + " ");
                    //book.setPhoto(imageupdate + " ");
                } else {
                    if (type == 0) {
                        String imageupdate = "";
                        if (imgThree.trim().length() > 0) {
                            if (imgTwo.trim().length() > 0) {
                                imageupdate = (imgOne.length() > 0 ? imgOne + ";" : "") + imgTwo + ";" + imgThree;
                            } else {
                                imageupdate = (imgOne.length() > 0 ? imgOne + ";" : "") + imgThree;
                            }
                        } else {
                            if (imgTwo.trim().length() > 0) {
                                imageupdate = (imgOne.length() > 0 ? imgOne + ";" : "") + imgTwo;
                            } else {
                                imageupdate = imgOne.length() > 0 ? imgOne + "" : "";
                            }
                        }
                        //String imageupdate = imgOne + ";" + imgTwo + ";" + imgThree;
                        book.setPhoto(imageupdate.trim() + " ");
                        book.setId(bookedit.getId());
                    }
                }
            } else {
                if (s.equals("edit")) {
                    if (type == 0) {
                        book.setId(bookedit.getId());
                        if (!imageOrigin.equals("")) {
                            book.setPhoto(imageOrigin + " ");
                        }
                    }
                }
            }
            if (sell.isChecked()) {
                if (edt_editlisting_sell.getText().toString().isEmpty() || edt_editlisting_sell.getText().toString().equals("0")) {
                    Toast.makeText(getActivity(), "Please enter valid a price", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    if (Integer.parseInt(edt_editlisting_sell.getText().toString()) <= 1000) {
                        price = Float.valueOf(edt_editlisting_sell.getText().toString());
                        book.setPrice(price);
                    } else {
                        Toast.makeText(getActivity(), "Price should not exceed 1000", Toast.LENGTH_LONG).show();
                        edt_editlisting_sell.requestFocus();
                        return false;
                    }

                }
            }

        } catch (Exception err) {
            return false;
        }
        return true;
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
        try {
            mMap = googleMap;
            s = getArguments().getString("activity");
            if (!s.equals("edit")) {
                GPSTracker gpsTracker = new GPSTracker(getContext());
                latLng_new = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                addMarkerChoice(latLng_new);
            } else if (s.equals("edit")) {
                bookedit = (Book) getArguments().getSerializable("bookedit");
                addMarkerChoice(new LatLng(bookedit.getLocation_latitude(), bookedit.getLocation_longitude()));
            }
        } catch (Exception e) {
        }
    }

    public void addMaker(Location location, String img) {
        try {
            mMap.clear();
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
            // Changing marker icon
            marker.icon((BitmapDescriptorFactory.fromBitmap(ResizeImage.resizeMapIcons(getContext(), img, (int) getResources().getDimension(R.dimen.width_pin),
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
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.btn_menu_editlist_delete:
                showdialog(1);
                break;
            case R.id.btn_menu_editlisting_update:
                if (checkCheckBox()) {
                    if (addbook(0)) {
                        if (numclick != 0 || numimageclick != 0) {
                            try {
                                addImages();
                            } catch (Exception exx) {

                            }
                        }
                        editbook editbook = new editbook();
                        editbook.execute();
                    }
                } else {
                    // Toast.makeText(getContext(),Information.noti_checkbox,Toast.LENGTH_SHORT).show();
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
                if (!tag1.getText().equals("")) {
                    showSnack(tag1.getText().toString(), 0);
                }
                break;
            case R.id.tag2:
                if (!tag2.getText().equals("")) {
                    showSnack(tag2.getText().toString(), 1);
                }
                break;
            case R.id.tag3:
                if (!tag3.getText().equals("")) {
                    showSnack(tag3.getText().toString(), 2);
                }
                break;
        }
    }

    public void showSnack(String tag, final int position) {
        final Snackbar snackbar = Snackbar.make(getView(), "Do you want remove " + tag + "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listTag.remove(position);
                settag();
                //edt_tag.setVisibility(View.VISIBLE);
                addtag.setVisibility(View.VISIBLE);
                snackbar.dismiss();
            }
        }).show();
    }

    public void showdialog(final int type) {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_confirm_book);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView txt_title_dialog = (TextView) dialog.findViewById(R.id.txt_title_dialog);

            Spannable wordtoSpan1 = new SpannableString("Are you sure you want to delete \n the book " + "\"" + bookedit.getTitle() + "\"" + "from your listings?");
            String ss = "Are you sure you want to delete \n the book " + "\"" + bookedit.getTitle().trim() + "\" from your listings?";
            int index = ss.indexOf(bookedit.getTitle().trim());
            wordtoSpan1.setSpan(new ForegroundColorSpan(Color.WHITE), index, bookedit.getTitle().trim().length() + index, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txt_title_dialog.setText(wordtoSpan1);

            TextView button_yes = (TextView) dialog.findViewById(R.id.btn_yes);
            button_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == 1) {
                        deletebook deletebook = new deletebook();
                        deletebook.execute();
                        dialog.dismiss();
                    }

                }
            });

            ImageView img_close_dialoggenre = (ImageView) dialog.findViewById(R.id.close_confirm_lising);
            img_close_dialoggenre.setImageResource(R.drawable.btn_close_filter);
            img_close_dialoggenre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
        }
    }

    public void addTag() {
        if (!flagTag) {
            edt_tag.setVisibility(View.VISIBLE);
            flagTag = true;
        } else {
            if (!edt_tag.getText().toString().trim().equals("") && !edt_tag.getText().toString().trim().contains(";")) {
                listTag.add(edt_tag.getText().toString());
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
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Remove",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

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
                } else if (items[item].equals("Remove")) {
                    Remove();
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
        //startActivityForResult(intent, REQUEST_CAMERA);
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra("keyChoose", 1);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void Remove() {
        if (typeChooseImage == 1) {
            if (!sChooseImage.contains("1")) {
                imagebook1.setImageBitmap(null);
                imagebook1.destroyDrawingCache();
                if (lisImmage.size() > 0)
                    lisImmage.remove(0);
                imgOne = "";
                sChooseImage = sChooseImage.replaceAll("1","");
            } else if (!sChooseImage.contains("2")) {
                imagebook2.setImageBitmap(null);
                imagebook2.destroyDrawingCache();
                if (lisImmage.size() > 1)
                    lisImmage.remove(1);
                imgTwo = "";
                sChooseImage = sChooseImage.replaceAll("2","");
            } else if (!sChooseImage.contains("3")) {
                imagebook3.setImageBitmap(null);
                imagebook3.destroyDrawingCache();
                if (lisImmage.size() > 2)
                    lisImmage.remove(2);
                imgThree = "";
                sChooseImage = sChooseImage.replaceAll("3","");
            }
        }else if(typeChooseImage == 2){
            if (numimageclick == 1) {
                imagebook1.setImageBitmap(null);
                imagebook1.destroyDrawingCache();
                if (lisImmage.size() > 0)
                    lisImmage.remove(0);
                imgOne = "";
                sChooseImage = sChooseImage.replaceAll("1","");
            } else if (numimageclick == 2) {
                imagebook2.setImageBitmap(null);
                imagebook2.destroyDrawingCache();
                if (lisImmage.size() > 1)
                    lisImmage.remove(1);
                imgTwo = "";
                sChooseImage = sChooseImage.replaceAll("2","");
            } else if (numimageclick == 3) {
                imagebook3.setImageBitmap(null);
                imagebook3.destroyDrawingCache();
                if (lisImmage.size() > 2)
                    lisImmage.remove(2);
                imgThree = "";
                sChooseImage = sChooseImage.replaceAll("3","");
            }
        }
    }

    ArrayList<ImageClick> lisImmage = new ArrayList<>();

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            Bitmap thumbnail = null;
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {
                    //thumbnail = (Bitmap) data.getExtras().get("data");
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
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
                            thumbnail = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
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
            mImageUri = getImageUri(getContext(), thumbnail);

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
            if (typeChooseImage == 1) {
                if (!sChooseImage.contains("1")) {
                    Picasso.with(getActivity()).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook1);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    if (lisImmage.size() >= 1) {
                        lisImmage.set(0, imageClick);
                    } else {
                        lisImmage.add(imageClick);
                    }
                    imgOne = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    if (!sChooseImage.contains("1")) {
                        sChooseImage = sChooseImage + "1";
                    }
                } else if (!sChooseImage.contains("2")) {
                    Picasso.with(getActivity()).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook2);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    if (lisImmage.size() >= 2) {
                        lisImmage.set(1, imageClick);
                    } else {
                        lisImmage.add(imageClick);
                    }
                    imgTwo = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    if (!sChooseImage.contains("2")) {
                        sChooseImage = sChooseImage + "2";
                    }
                } else if (!sChooseImage.contains("3")) {
                    Picasso.with(getActivity()).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook3);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    if (lisImmage.size() >= 3) {
                        lisImmage.set(2, imageClick);
                    } else {
                        lisImmage.add(imageClick);
                    }
                    imgThree = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    if (!sChooseImage.contains("3")) {
                        sChooseImage = sChooseImage + "3";
                    }
                }
            } else if (typeChooseImage == 2) {
                if (numimageclick == 1) {
                    Picasso.with(getActivity()).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook1);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    if (lisImmage.size() >= 1) {
                        lisImmage.set(0, imageClick);
                    } else {
                        lisImmage.add(imageClick);
                    }
                    imgOne = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    if (!sChooseImage.contains("1")) {
                        sChooseImage = sChooseImage + "1";
                    }
                } else if (numimageclick == 2) {
                    Picasso.with(getActivity()).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook2);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    if (lisImmage.size() >= 2) {
                        lisImmage.set(1, imageClick);
                    } else {
                        lisImmage.add(imageClick);
                    }
                    imgTwo = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);
                    if (!sChooseImage.contains("2")) {
                        sChooseImage = sChooseImage + "2";
                    }
                } else if (numimageclick == 3) {
                    Picasso.with(getActivity()).load(mImageUri).resize(width, height)
                            .centerInside().into(imagebook3);
                    ImageClick imageClick = new ImageClick(mImageUri, username + "_+_" + String.valueOf(time) + getFileName(mImageUri));
                    if (lisImmage.size() >= 3) {
                        lisImmage.set(2, imageClick);
                    } else {
                        lisImmage.add(imageClick);
                    }
                    imgThree = username + "_+_" + String.valueOf(time) + getFileName(mImageUri);

                    if (!sChooseImage.contains("3")) {
                        sChooseImage = sChooseImage + "3";
                    }
                }

            }
        } catch (Exception ex) {

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Point getDisplaySize(Display display) {
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
        } else {
            int width = display.getWidth();
            int height = display.getHeight();
            size = new Point(width, height);
        }

        return size;
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
    ArrayList<Bitmap> bmap = new ArrayList<>();

    public void addImages() {
        try {
            uploadFileController.uploadFile(bmap, listFileName);
        } catch (Exception exx) {

        }
    }

    public class uploaddata extends AsyncTask<Void, Void, String> {

        public Context context;

        public uploaddata(Context context) {
            this.context = context;
        }

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
                    SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("session_id", null);
                    editor.commit();
                    Intent intent = new Intent(context, SignIn_Activity.class);
                    context.startActivity(intent);
                    this.cancel(true);
                }
                bookController = new BookController();
                //success = bookController.addbook(book, session_id).equals("")? false: true;
                return bookController.addbook(book, session_id);
            } catch (Exception exx) {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("")) {
                if (back == 1) {
                    callFragment(new MyProfileFragment());
                } else {
                    //Toast.makeText(getActivity(),"Add Book Successful",Toast.LENGTH_SHORT).show();
                    MainAllActivity main = (MainAllActivity) getActivity();
//                    main.callFragment(new ListingsFragment());
                    MainAllActivity.setTxtTitle("Listings");

                }
            }
            super.onPostExecute(result);
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
                //Toast.makeText(getActivity(), Information.noti_update_success, Toast.LENGTH_LONG).show();
                if (back == 1) {
                    MainAllActivity main = (MainAllActivity) getActivity();
                    main.callFragment(new MyProfileFragment());

                } else {
                    MainAllActivity main = (MainAllActivity) getActivity();
                    main.callFragment(new ListingsFragment());
                    MainAllActivity.setTxtTitle("Listings");
                }
            } else {
                dialog.dismiss();
                //Toast.makeText(getActivity(), Information.noti_update_fail, Toast.LENGTH_LONG).show();
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
            if (aBoolean == true) {
                //Toast.makeText(getActivity(), Information.noti_delete_success, Toast.LENGTH_LONG).show();
                if (back == 1) {
                    callFragment(new MyProfileFragment());
                } else {
                    callFragment(new ListingsFragment());
                    MainAllActivity.setTxtTitle("Listings");
                }

//                MainAllActivity main = (MainAllActivity) getActivity();
//                main.callFragment(new ListingsFragment());
            } else {
                //Toast.makeText(getActivity(), Information.noti_delete_fail, Toast.LENGTH_LONG).show();
            }
        }
    }

    class GetWithHeight extends AsyncTask<Bitmap, Void, Bitmap> {

        Context context;
        String imageUrl;
        ImageView img_book;

        public GetWithHeight(Context context, String imageUrl, ImageView img_book) {
            this.context = context;
            this.imageUrl = imageUrl;
            this.img_book = img_book;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... Bitmap) {
            Bitmap downloadedImage = null;
            try {
                downloadedImage = Picasso.with(context).load(imageUrl).get();
                return downloadedImage;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap downloadedImage) {
            try {
                int width = downloadedImage.getWidth();
                int height = downloadedImage.getHeight();
                if (width > height) {
                    img_book.setRotation(90f);
                    img_book.setImageBitmap(downloadedImage);
                    /*Picasso.with(mContext).load(imageUrl).rotate(90f).placeholder(R.drawable.blank_image).
                            into(img_book);*/
                } else {
                    img_book.setImageBitmap(downloadedImage);
                    /*Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.blank_image).
                            into(img_book);*/
                }
            } catch (Exception e) {
            }
        }
    }
}
