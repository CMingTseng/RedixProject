package com.booxtown.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booxtown.activity.MenuActivity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.BookController;
import com.booxtown.controller.GPSTracker;
import com.booxtown.controller.GetAllGenreAsync;
import com.booxtown.controller.Information;
import com.booxtown.controller.RangeSeekBar;
import com.booxtown.model.Genre;
import com.booxtown.model.GenreValue;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.booxtown.R;
import com.booxtown.activity.ListingsDetailActivity;
import com.booxtown.adapter.AdapterFilter;
import com.booxtown.controller.GenreController;
import com.booxtown.controller.IconMapController;
import com.booxtown.controller.MyFirebaseMessagingService;
import com.booxtown.custom.CustomListviewGenre;
import com.booxtown.model.Book;
import com.booxtown.model.Filter;

public class MainFragment extends Fragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {
    private GoogleMap mMap;
    public static String[] prgmNameList1 = {"Nearest distance", "Price low to high", "Price high to low", "Recently added"};
    private LatLng latLngBounds;
    MarkerOptions marker;
    private HashMap<Marker, Book> mMarkersHashMap = new HashMap<>();
    EditText editSearch;
    ImageView btn_search;

    //filter
    String proximity;
    private TextView tvMin, tvMax, txt_filter_proximity;
    private List<Filter> filterList;
    private AdapterFilter adaper;
    private RangeSeekBar rangeSeekbar;
    private RangeSeekBar seekbar;
    //private Spinner spinner2;
    private ArrayAdapter<String> dataAdapter;
    List<Book> lisfilter_temp;
    List<Book> listfilter;
    List<Book> listExplore;
    ArrayList<Genre> genre;

    int minRangerSeekbar=0;
    int maxRangerSeekbar=0;
    double maxSeekbar=0;

    //end
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        ImageView img_menu = (ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });
        genre = new ArrayList<>();
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
            Intent refreshTokenFirebase = new Intent(getActivity(), MyFirebaseMessagingService.class);
            getActivity().startService(refreshTokenFirebase);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GetAllGenreAsync getAllGenreAsync = new GetAllGenreAsync(getContext());
        getAllGenreAsync.execute();

        GetAllGenreAsync1 getAllGenreAsync1 = new GetAllGenreAsync1(getContext());
        getAllGenreAsync1.execute();

        listExplore = new ArrayList<>();

        View view_search = view.findViewById(R.id.custom_search);
        editSearch = (EditText) view_search.findViewById(R.id.editSearch);
        btn_search = (ImageView) view_search.findViewById(R.id.btn_search);
        ImageView img_menu_component = (ImageView) getActivity().findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.VISIBLE);

        TextView txtTitle = (TextView) getActivity().findViewById(R.id.txt_title);
        txtTitle.setText("Locate");

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                List<Book> list_books = new ArrayList<Book>();
                list_books.clear();
                for (int i = 0; i < listExplore.size(); i++) {
                    if (listExplore.get(i).getTitle().toLowerCase().contains(editSearch.getText().toString().toLowerCase())
                            || listExplore.get(i).getAuthor().toLowerCase().contains(editSearch.getText().toString().toLowerCase())) {
                        list_books.add(listExplore.get(i));
                    }
                }
                if (list_books.size() > 0) {
                    addMarkerSearch(list_books);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainFragment.this);

        filterSort(view);

        //end
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("message", "This is my message to be reloaded");
        super.onSaveInstanceState(outState);
    }



    public List<Book> filter(List<String> filter) {
        lisfilter_temp = new ArrayList<>();
        listfilter = new ArrayList<>();

        LatLng latLngSt = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
        Double distance = Double.valueOf(maxSeekbar);
        for (int i = 0; i < listExplore.size(); i++) {
            //String[] genrel = listExplore.get(i).getGenre().split(";");
            String[] genrel = listExplore.get(i).getGenre().split(";");
            for (int f = 0;f<filter.size();f++){
                if (filter.get(f).equals("All")){
                    LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(),listExplore.get(i).getLocation_longitude());
                    if (CalculationByDistance(latLngSt,latLngEnd)<=distance){
                        listfilter.add(listExplore.get(i));
                    }
                }
            }
            for (int j = 0;j<genrel.length;j++){
                for (int f = 0;f<filter.size();f++) {
                    if (genrel[j].contains(filter.get(f))) {
                        LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(), listExplore.get(i).getLocation_longitude());
                        if (CalculationByDistance(latLngSt, latLngEnd) <= distance) {
                            listfilter.add(listExplore.get(i));
                        }
                    }
                }
            }
        }

        if (listfilter.size() != 0) {
            for (int i = 0; i < listfilter.size(); i++) {
                if (listfilter.get(i).getPrice() >= Float.valueOf(minRangerSeekbar+"") &&
                        listfilter.get(i).getPrice() <= Float.valueOf(maxRangerSeekbar+"")) {
                    lisfilter_temp.add(listfilter.get(i));
                }
            }
        }

        if (filterList.get(0).getCheck() == true) {
            BookController bookController = new BookController(getActivity());
            Collections.sort(lisfilter_temp, bookController.distance);
        } else if (filterList.get(1).getCheck() == true) {
            Collections.sort(lisfilter_temp, Book.priceasen);
        } else if (filterList.get(2).getCheck() == true) {
            Collections.sort(lisfilter_temp, Book.pricedcen);
        } else {
            Collections.sort(lisfilter_temp, Book.recently);
        }
        return lisfilter_temp;
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        Double kmInDec = Double.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return kmInDec;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void filterSort(View view) {
        ImageView btn_filter_explore = (ImageView) view.findViewById(R.id.btn_filter_explore);
        Picasso.with(getContext()).load(R.drawable.btn_locate_filter).into(btn_filter_explore);

        ImageView btn_search = (ImageView) view.findViewById(R.id.btn_search);
        Picasso.with(getContext()).load(R.drawable.btn_locate_search).into(btn_search);
        btn_filter_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_filter_sort);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ListView lv_dialog_filter = (ListView) dialog.findViewById(R.id.lv_dialog_filter);
                filterList = new ArrayList<>();
                for (int i = 0; i < prgmNameList1.length; i++) {
                    Filter filter = new Filter();
                    filter.setTitle(prgmNameList1[i]);
                    filter.setCheck(false);
                    filterList.add(filter);
                }
                adaper = new AdapterFilter(getActivity(), filterList);
                lv_dialog_filter.setAdapter(adaper);

                rangeSeekbar = (RangeSeekBar) dialog.findViewById(R.id.rangeSeekbar3);
                rangeSeekbar.setNotifyWhileDragging(true);
                rangeSeekbar.setSelectedMaxValue(100);
                rangeSeekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                        minRangerSeekbar= Integer.parseInt(minValue+"");
                        maxRangerSeekbar= Integer.parseInt(maxValue+"");
                    }
                });
                /*rangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar3);

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.abc);
                Bitmap thumb = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(thumb);
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                        new Rect(0, 0, thumb.getWidth(), thumb.getHeight()), null);
                Drawable drawable = new BitmapDrawable(getResources(), thumb);
                rangeSeekbar.setLeftThumbDrawable(drawable);
                rangeSeekbar.setRightThumbDrawable(drawable);

                rangeSeekbar.setRight(0);


                tvMin = (TextView) dialog.findViewById(R.id.txt_filter_rangemin);
                tvMax = (TextView) dialog.findViewById(R.id.txt_filter_rangemax);

                rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number minValue, Number maxValue) {
                        tvMin.setText(String.valueOf(minValue));
                        if(String.valueOf(maxValue).equals("1")){
                            tvMax.setText("1000");
                        }else{
                            tvMax.setText(String.valueOf(maxValue));
                        }

                    }
                });*/
                /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.abc);
                Bitmap thumb = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(thumb);
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                        new Rect(0, 0, thumb.getWidth(), thumb.getHeight()), null);
                Drawable drawable = new BitmapDrawable(getResources(), thumb);
                txt_filter_proximity = (TextView) dialog.findViewById(R.id.txt_filter_proximity);
                seekbar = (CrystalSeekbar) dialog.findViewById(R.id.rangeSeekbar8);
                seekbar.setLeftThumbDrawable(drawable);
                seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number minValue) {
                        if(String.valueOf(minValue).equals("0")){
                            txt_filter_proximity.setText("10KM");
                            proximity = String.valueOf(minValue);
                        }else{
                            txt_filter_proximity.setText(String.valueOf(minValue) + "KM");
                            proximity = String.valueOf(minValue);
                        }
                    }
                });*/

                seekbar = (RangeSeekBar) dialog.findViewById(R.id.rangeSeekbar8);
                seekbar.setNotifyWhileDragging(true);
                seekbar.setSelectedMaxValue(3);
                seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                        maxSeekbar= Double.parseDouble((maxValue+"").replace(" KM",""));
                    }
                });

                ImageView imv_dialog_filter_close = (ImageView) dialog.findViewById(R.id.imv_dialog_filter_close);
                Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(imv_dialog_filter_close);
                imv_dialog_filter_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button btn_dialog_filter_submit = (Button) dialog.findViewById(R.id.btn_dialog_filter_submit);
                btn_dialog_filter_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        ArrayList<String> listvalueGenre = new ArrayList<>();
                        for (int k = 0; k < genre.size(); k++) {
                            if (genre.get(k).ischeck() == true) {
                                listvalueGenre.add(genre.get(k).getValue());
                            }
                        }
                        dialog.dismiss();
                        addMarkerSearch(filter(listvalueGenre));
                    }
                });

                RelativeLayout tv_genral = (RelativeLayout) dialog.findViewById(R.id.relaytive_genre);
                tv_genral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_genre);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        RecyclerView rv_genre = (RecyclerView) dialog.findViewById(R.id.listView_genre);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        rv_genre.setLayoutManager(linearLayoutManager);
                        rv_genre.setAdapter(new CustomListviewGenre(getContext(), genre));
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
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Book books = mMarkersHashMap.get(marker);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(String.valueOf(R.string.valueListings), "1");
        bundle.putSerializable("item", books);
        FragmentTransaction transaction = manager.beginTransaction();
        ListingsDetailActivity fra = new ListingsDetailActivity();
        fra.setArguments(bundle);
        transaction.replace(R.id.map, fra);
        transaction.commit();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    public Bitmap resizeMapIcons(String icon, int width, int height) {
        Bitmap imageBitmap;
        Bitmap resizedBitmap;
        try {
            imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(icon, "drawable", getActivity().getPackageName()));
            resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
            return resizedBitmap;
        } catch (Exception e) {
            String err= e.getMessage();
        }
        return null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        // latitude and longitude
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String session_id = pref.getString("session_id", null);
        listingAsync listingAsync = new listingAsync(getContext(),session_id,0,100);
        listingAsync.execute();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);
        GPSTracker gpsTracker = new GPSTracker(getContext());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 12));
        mMap.setOnInfoWindowClickListener(this);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        TextView txt_title_marker, txt_author_marker, txt_user_marker;
        RatingBar ratingBar_marker;
        ImageView img_swap_marker, img_free_marker, img_buy_marker;
        CircularImageView img_map_main;
        private final View myContentsView;

        MyInfoWindowAdapter() {

            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.dialog_map_main, null);
            txt_title_marker = (TextView) myContentsView.findViewById(R.id.txt_title_marker);
            txt_author_marker = (TextView) myContentsView.findViewById(R.id.txt_author_marker);
            txt_user_marker = (TextView) myContentsView.findViewById(R.id.txt_user_marker);
            ratingBar_marker = (RatingBar) myContentsView.findViewById(R.id.ratingBar_marker);
            img_swap_marker = (ImageView) myContentsView.findViewById(R.id.img_swap_marker);
            img_free_marker = (ImageView) myContentsView.findViewById(R.id.img_free_marker);
            img_buy_marker = (ImageView) myContentsView.findViewById(R.id.img_buy_marker);
            img_map_main = (CircularImageView)myContentsView.findViewById(R.id.img_map_main);
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;

        }

        @Override
        public View getInfoWindow(Marker marker) {
            Book books = mMarkersHashMap.get(marker);

            String title;
            try {
                if (books.getTitle().length() > 0) {
                    title = books.getTitle().substring(0, 1).toUpperCase() + books.getTitle().substring(1, books.getTitle().length());
                    txt_title_marker.setText(title);
                }
            } catch (Exception e) {
            }

            if (books.getUser_photo().length() > 3) {
                int index = books.getUser_photo().indexOf("_+_");
                Picasso.with(getContext())
                        .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" +books.getUsername() + "&image=" +books.getUser_photo().substring(index + 3, books.getUser_photo().length()))
                        .placeholder(R.mipmap.user_empty).
                        into(img_map_main);
            } else {
                Bitmap bm= BitmapFactory.decodeResource(getResources(),R.mipmap.user_empty);
                img_map_main.setImageBitmap(bm);
            }

            txt_author_marker.setText("by " + books.getAuthor());
            txt_user_marker.setText(books.getUsername());
            ratingBar_marker.setRating(books.getRating());
            LayerDrawable stars = (LayerDrawable) ratingBar_marker.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.rgb(249,242,0), PorterDuff.Mode.SRC_ATOP );
            stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.dot_light_screen1), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.dot_light_screen1), PorterDuff.Mode.SRC_ATOP); // for half filled stars
            DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),getResources().getColor(R.color.dot_light_screen1));

            //stars.getColorFilter();
            char array[] = books.getAction().toCharArray();
            String swap = String.valueOf(array[0]);
            String free = String.valueOf(array[1]);
            String buy = String.valueOf(array[2]);


            String icon = IconMapController.iconExplorer(swap, free, buy);
            if (icon.equals("icon_swap")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_swap_not_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_free_dis_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_buy_dis_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);


                /*img_swap_marker.setImageResource(R.drawable.explore_btn_swap_not_active);
                img_free_marker.setImageResource(R.drawable.explore_btn_free_dis_active);
                img_buy_marker.setImageResource(R.drawable.explore_btn_buy_dis_active);
*/
          /*      Glide.with(getContext()).load(R.drawable.explore_btn_swap_not_active).into(img_swap_marker);
                Glide.with(getContext()).load(R.drawable.explore_btn_free_dis_active).into(img_free_marker);
                Glide.with(getContext()).load(R.drawable.explore_btn_buy_dis_active).into(img_buy_marker);*/
            }
            if (icon.equals("icon_free")) {

                Bitmap bMap = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_swap_dis_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_free_not_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_buy_dis_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);

                /*img_swap_marker.setImageResource(R.drawable.explore_btn_swap_dis_active);
                img_free_marker.setImageResource(R.drawable.explore_btn_free_not_active);
                img_buy_marker.setImageResource(R.drawable.explore_btn_buy_dis_active);*/

//                Glide.with(getContext()).load(R.drawable.explore_btn_swap_dis_active).into(img_swap_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_free_not_active).into(img_free_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_buy_dis_active).into(img_buy_marker);
            }
            if (icon.equals("icon_buy")) {

                Bitmap bMap = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_swap_dis_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_free_dis_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_buy_not_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);

/*                img_swap_marker.setImageResource(R.drawable.explore_btn_swap_dis_active);
                img_free_marker.setImageResource(R.drawable.explore_btn_free_dis_active);
                img_buy_marker.setImageResource(R.drawable.explore_btn_buy_not_active);*/

//                Glide.with(getContext()).load(R.drawable.explore_btn_swap_dis_active).into(img_swap_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_free_dis_active).into(img_free_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_buy_not_active).into(img_buy_marker);
            }
            if (icon.equals("swapfree")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_swap_not_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_free_not_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_buy_dis_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);
               /* img_swap_marker.setImageResource(R.drawable.explore_btn_swap_not_active);
                img_free_marker.setImageResource(R.drawable.explore_btn_free_not_active);
                img_buy_marker.setImageResource(R.drawable.explore_btn_buy_dis_active);*/
//                Glide.with(getContext()).load(R.drawable.explore_btn_swap_not_active).into(img_swap_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_free_not_active).into(img_free_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_buy_dis_active).into(img_buy_marker);
            }
            if (icon.equals("swapbuy")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_swap_not_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_free_dis_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_buy_not_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);
                /*img_swap_marker.setImageResource(R.drawable.explore_btn_swap_not_active);
                img_free_marker.setImageResource(R.drawable.explore_btn_free_dis_active);
                img_buy_marker.setImageResource(R.drawable.explore_btn_buy_not_active);*/
//                Glide.with(getContext()).load(R.drawable.explore_btn_swap_not_active).into(img_swap_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_free_dis_active).into(img_free_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_buy_not_active).into(img_buy_marker);
            }
            if (icon.equals("freebuy")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_swap_dis_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_free_not_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_buy_not_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);
                /*img_swap_marker.setImageResource(R.drawable.explore_btn_swap_dis_active);
                img_free_marker.setImageResource(R.drawable.explore_btn_free_not_active);
                img_buy_marker.setImageResource(R.drawable.explore_btn_buy_not_active);*/
//                Glide.with(getContext()).load(R.drawable.explore_btn_swap_dis_active).into(img_swap_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_free_not_active).into(img_free_marker);
//                Glide.with(getContext()).load(R.drawable.explore_btn_buy_not_active).into(img_buy_marker);
            }
            if (icon.equals("option")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_swap_not_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_free_not_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(),R.drawable.explore_btn_buy_not_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);
                /*img_swap_marker.setImageResource(R.drawable.explore_btn_swap_not_active);
                img_free_marker.setImageResource(R.drawable.explore_btn_free_not_active);
                img_buy_marker.setImageResource(R.drawable.explore_btn_buy_not_active);*/

                /*Glide.with(getContext()).load(R.drawable.explore_btn_free_not_active).into(img_free_marker);
                Glide.with(getContext()).load(R.drawable.explore_btn_buy_not_active).into(img_buy_marker);
                Glide.with(getContext()).load(R.drawable.explore_btn_swap_not_active).into(img_swap_marker);*/
            }
            return myContentsView;
        }
    }

    class listingAsync extends AsyncTask<String, Void, List<Book>> {

        Context context;
        ProgressDialog dialog;
        String session_id;
        int top,from;
        public listingAsync(Context context,String session_id,int from,int top) {
            this.context = context;
            this.session_id = session_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            BookController bookController = new BookController();
            return bookController.book_gettop(session_id,from,top);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final List<Book> books) {
            try {
                if (books == null) {
                    dialog.dismiss();
                } else {
                    listExplore = books;
                    // create marker
                    addMarker(books);
                    dialog.dismiss();
                }
            } catch (Exception e) {
            }
        }
    }

    public void addMarker(final List<Book> books) {
        try {
            mMap.clear();
            if (books.size() > 0) {
                GPSTracker gpsTracker = new GPSTracker(getContext());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 12));
                for (int i = 0; i < books.size(); i++) {
                    marker = new MarkerOptions().position(new LatLng(books.get(i).getLocation_latitude(), books.get(i).getLocation_longitude())).title("Booxtown");
                    latLngBounds = new LatLng(books.get(i).getLocation_latitude(), books.get(i).getLocation_longitude());
                    // Changing marker icon
                    char array[] = books.get(i).getAction().toCharArray();
                    String swap = String.valueOf(array[0]);
                    String free = String.valueOf(array[1]);
                    String buy = String.valueOf(array[2]);
                    String icon = IconMapController.icon(swap, free, buy);
                    if (icon != null) {
                        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon,(int)getResources().getDimension(R.dimen.width_pin),
                                (int)getResources().getDimension(R.dimen.height_pin))));
                    }
                    Marker m_marker = mMap.addMarker(marker);
                    mMarkersHashMap.put(m_marker, books.get(i));
                }
            }
        } catch (Exception e) {

        }
    }

    public void addMarkerSearch(final List<Book> books) {
        try {
            mMap.clear();
            if (books.size() > 0) {
                LatLng latLng = new LatLng(books.get(0).getLocation_latitude(), books.get(0).getLocation_longitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                for (int i = 0; i < books.size(); i++) {
                    marker = new MarkerOptions().position(new LatLng(books.get(i).getLocation_latitude(), books.get(i).getLocation_longitude())).title("Booxtown");
                    latLngBounds = new LatLng(books.get(i).getLocation_latitude(), books.get(i).getLocation_longitude());
                    // Changing marker icon
                    char array[] = books.get(i).getAction().toCharArray();
                    String swap = String.valueOf(array[0]);
                    String free = String.valueOf(array[1]);
                    String buy = String.valueOf(array[2]);
                    String icon = IconMapController.icon(swap, free, buy);
                    if (icon != null) {
                        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon,(int)getResources().getDimension(R.dimen.width_pin),
                                (int)getResources().getDimension(R.dimen.height_pin))));
                    }
                    Marker m_marker = mMap.addMarker(marker);
                    mMarkersHashMap.put(m_marker, books.get(i));
                }
            }

        } catch (Exception e) {
        }
    }
    public class GetAllGenreAsync1 extends AsyncTask<Void,Void,List<GenreValue>>{
        public Context context;
        ProgressDialog dialog;
        public GetAllGenreAsync1(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected List<GenreValue> doInBackground(Void... voids) {
            GenreController genreController = new GenreController();
            return genreController.getAllGenre();
        }

        @Override
        protected void onPostExecute(List<GenreValue> genreValues) {
            try {

                if(genreValues.size() >0){
//                list.add("All");
                    for (int i=0;i<genreValues.size();i++){
                        Genre genrel = new Genre();
                        genrel.setValue(genreValues.get(i).getTitle());
                        genre.add(genrel);
                    }
                }
            }catch (Exception e){

            }
            dialog.dismiss();
        }
    }
}
