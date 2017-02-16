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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.booxtown.activity.MenuActivity;
import com.booxtown.activity.SignIn_Activity;
import com.booxtown.activity.Upgrade;
import com.booxtown.adapter.AdapterExplore;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.BookController;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.GPSTracker;
import com.booxtown.controller.GetAllGenreAsync;
import com.booxtown.controller.Information;
import com.booxtown.controller.RangeSeekBar;
import com.booxtown.controller.UserController;
import com.booxtown.custom.CustomEdittext;
import com.booxtown.custom.CustomTabbarExplore;
import com.booxtown.custom.DrawableClickListener;
import com.booxtown.model.DayUsed;
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
    CustomEdittext editSearch;
    ImageView btn_search;

    //filter
    String proximity;
    private LinearLayout linear_all, linear_swap, linear_free, linear_cart;
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
    int chooseTab = 1;
    int minRangerSeekbar = 0;
    int maxRangerSeekbar = 0;
    double maxSeekbar = 0;
    RelativeLayout notiTrial, notiUpgrade;
    TextView txtNotifiTrial;
    boolean trial = false;
    float longitude = 0;
    float latitude = 0;
    boolean flagClosSearch=false;
    public TextView tab_all_count, tab_swap_count, tab_free_count, tab_cart_count;

    //end
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        notiTrial = (RelativeLayout) view.findViewById(R.id.notiTrial);
        notiUpgrade = (RelativeLayout) view.findViewById(R.id.notiUpgrade);
        txtNotifiTrial = (TextView) view.findViewById(R.id.txtNotifiTrial);
        notiTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Upgrade.class);
                startActivity(intent);
            }
        });
        notiUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Upgrade.class);
                startActivity(intent);
            }
        });

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String sessionID = pref.getString("session_id", null);

        int is_current_location = pref.getInt("is_current_location", 1);
        if (is_current_location == 1) {
            longitude = (float) new GPSTracker(getActivity()).getLongitude();
            latitude = (float) new GPSTracker(getActivity()).getLatitude();
        } else {
            longitude = Float.parseFloat(pref.getString("Longitude", (float) new GPSTracker(getActivity()).getLongitude() + ""));
            latitude = Float.parseFloat(pref.getString("Latitude", (float) new GPSTracker(getActivity()).getLatitude() + ""));
        }

        View view_tab = (View) view.findViewById(R.id.tab_explore);
        final CustomTabbarExplore tab_custom = new CustomTabbarExplore(view_tab, getActivity());
        linear_all = (LinearLayout) view_tab.findViewById(R.id.linear_all);
        linear_swap = (LinearLayout) view_tab.findViewById(R.id.linear_swap);
        linear_free = (LinearLayout) view_tab.findViewById(R.id.linear_free);
        linear_cart = (LinearLayout) view_tab.findViewById(R.id.linear_cart);
        tab_all_count = (TextView) view_tab.findViewById(R.id.tab_all_count);
        tab_cart_count = (TextView) view_tab.findViewById(R.id.tab_cart_count);
        tab_free_count = (TextView) view_tab.findViewById(R.id.tab_free_count);
        tab_swap_count = (TextView) view_tab.findViewById(R.id.tab_swap_count);


        GetDayUsed getDayUsed = new GetDayUsed(getContext(), sessionID);
        getDayUsed.execute();
        //------------------------------------------------------------------------------------

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
        editSearch = (CustomEdittext) view_search.findViewById(R.id.editSearch);
        btn_search = (ImageView) view_search.findViewById(R.id.btn_search);
        ImageView img_menu_component = (ImageView) getActivity().findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.VISIBLE);

        TextView txtTitle = (TextView) getActivity().findViewById(R.id.txt_title);
        txtTitle.setText("Locate");
        editSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editSearch.getText().toString().trim().length()>0) {
                    editSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.closes, 0);
                    flagClosSearch=true;
                }else {
                    editSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    flagClosSearch=false;
                }
                List<Book> list_books = new ArrayList<Book>();
                list_books.clear();
                for (int i = 0; i < listExplore.size(); i++) {
                    if (listExplore.get(i).getTitle().toLowerCase().contains(editSearch.getText().toString().toLowerCase())
                            || listExplore.get(i).getAuthor().toLowerCase().contains(editSearch.getText().toString().toLowerCase())
                            || listExplore.get(i).getGenre().toLowerCase().contains(editSearch.getText().toString().toLowerCase())
                            || listExplore.get(i).getUsername().toLowerCase().contains(editSearch.getText().toString().toLowerCase())
                            || listExplore.get(i).getHash_tag().toLowerCase().contains(editSearch.getText().toString().toLowerCase())
                            || listExplore.get(i).getCondition().toLowerCase().contains(editSearch.getText().toString().toLowerCase())
                            ) {
                        list_books.add(listExplore.get(i));
                    }
                }
                if (list_books.size() > 0) {
                    addMarkerSearch(list_books);
                }else{
                    addMarkerSearch(new ArrayList<Book>());
                }
            }
        });

        editSearch.setDrawableClickListener(new DrawableClickListener() {
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        //Do something here
                        break;
                    case RIGHT:
                        if(flagClosSearch) {
                            editSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            editSearch.setText("");
                            flagClosSearch = false;
                        }
                        break;
                    case TOP:
                        //Do something here
                        break;
                    case BOTTOM:
                        //Do something here
                        break;
                    default:
                        break;
                }
            }

        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainFragment.this);

        filterSort(view);


        linear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTab = 1;
                tab_custom.setDefault(1);
                ArrayList<String> listvalueGenre = new ArrayList<>();

                for (int k = 0; k < genre.size(); k++) {
                    if (genre.get(k).ischeck() == true) {
                        listvalueGenre.add(genre.get(k).getValue());
                    }
                }
                addMarker(filter(listvalueGenre));

            }
        });

        linear_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTab = 2;
                tab_custom.setDefault(2);
                ArrayList<String> listvalueGenre = new ArrayList<>();

                for (int k = 0; k < genre.size(); k++) {
                    if (genre.get(k).ischeck() == true) {
                        listvalueGenre.add(genre.get(k).getValue());
                    }
                }
                addMarker(filter(listvalueGenre));

            }
        });

        linear_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTab = 3;
                tab_custom.setDefault(3);
                ArrayList<String> listvalueGenre = new ArrayList<>();

                for (int k = 0; k < genre.size(); k++) {
                    if (genre.get(k).ischeck() == true) {
                        listvalueGenre.add(genre.get(k).getValue());
                    }
                }
                addMarker(filter(listvalueGenre));


            }
        });

        linear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTab = 4;
                tab_custom.setDefault(4);
                ArrayList<String> listvalueGenre = new ArrayList<>();

                for (int k = 0; k < genre.size(); k++) {
                    if (genre.get(k).ischeck() == true) {
                        listvalueGenre.add(genre.get(k).getValue());
                    }
                }
                addMarker(filter(listvalueGenre));

            }
        });

        //end
        return view;
    }

    public List<Book> filterExplore(int type) {
        List<Book> list = new ArrayList<>();
        try {
            if (type == 1) {
                list = listExplore;
            } else if (type == 2) {
                for (int i = 0; i < listExplore.size(); i++) {
                    if (listExplore.get(i).getAction().substring(0, 1).equals("1")) {
                        list.add(listExplore.get(i));
                    }
                }
            } else if (type == 3) {
                for (int i = 0; i < listExplore.size(); i++) {
                    if (listExplore.get(i).getAction().substring(2, 3).equals("1")) {
                        list.add(listExplore.get(i));
                    }
                }
            } else {
                for (int i = 0; i < listExplore.size(); i++) {
                    if (listExplore.get(i).getAction().substring(1, 2).equals("1")) {
                        list.add(listExplore.get(i));
                    }
                }
            }
        } catch (Exception err) {


        }


        return list;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("message", "This is my message to be reloaded");
        super.onSaveInstanceState(outState);
    }


    public List<Book> filter(List<String> filter) {
        lisfilter_temp = new ArrayList<>();
        listfilter = new ArrayList<>();
        //LatLng latLngSt = new LatLng(25.2446,55.3154);
        //LatLng latLngSt = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
        LatLng latLngSt = new LatLng(latitude, longitude);
        Double distance = Double.valueOf(Information.maxSeekbar);

        for (int i = 0; i < filterExplore(chooseTab).size(); i++) {
            String[] genrel = filterExplore(chooseTab).get(i).getGenre().split(";");
            if (filter.size() > 0) {
                for (int j = 0; j < genrel.length; j++) {
                    for (int f = 0; f < filter.size(); f++) {
                        if (genrel[j].contains(filter.get(f))) {
                            LatLng latLngEnd = new LatLng(filterExplore(chooseTab).get(i).getLocation_latitude(), filterExplore(chooseTab).get(i).getLocation_longitude());
                            if (CalculationByDistance(latLngSt, latLngEnd) <= distance) {
                                if (!listfilter.contains(filterExplore(chooseTab).get(i))) {
                                    listfilter.add(filterExplore(chooseTab).get(i));
                                }
                            }
                        }
                    }
                }

            } else {

                LatLng latLngEnd = new LatLng(filterExplore(chooseTab).get(i).getLocation_latitude(), filterExplore(chooseTab).get(i).getLocation_longitude());
                if (CalculationByDistance(latLngSt, latLngEnd) <= distance) {
                    if (!listfilter.contains(filterExplore(chooseTab).get(i))) {
                        listfilter.add(filterExplore(chooseTab).get(i));
                    }
                }

            }
        }

        if (listfilter.size() != 0) {
            for (int i = 0; i < listfilter.size(); i++) {
                if (listfilter.get(i).getPrice() >= Float.valueOf(Information.minRager + "") &&
                        listfilter.get(i).getPrice() <= Float.valueOf(Information.maxRager + "")) {
                    lisfilter_temp.add(listfilter.get(i));
                }
            }
        }
        ReShowNumber(filter);
        return lisfilter_temp;
    }

    public void ReShowNumber(List<String> filter) {
        ArrayList<Book> lisfilter_temp = new ArrayList<>();
        ArrayList<Book> listfilter = new ArrayList<>();
        //LatLng latLngSt = new LatLng(25.2446,55.3154);
        //LatLng latLngSt = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
        LatLng latLngSt = new LatLng(latitude, longitude);
        Double distance = Double.valueOf(Information.maxSeekbar);

        for (int i = 0; i < listExplore.size(); i++) {
            String[] genrel = listExplore.get(i).getGenre().split(";");
            if (filter.size() > 0) {
                for (int j = 0; j < genrel.length; j++) {
                    for (int f = 0; f < filter.size(); f++) {
                        if (genrel[j].contains(filter.get(f))) {
                            LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(), listExplore.get(i).getLocation_longitude());
                            if (CalculationByDistance(latLngSt, latLngEnd) <= distance) {
                                if (!listfilter.contains(listExplore.get(i))) {
                                    listfilter.add(listExplore.get(i));
                                }
                            }
                        }
                    }
                }

            } else {

                LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(), listExplore.get(i).getLocation_longitude());
                if (CalculationByDistance(latLngSt, latLngEnd) <= distance) {
                    if (!listfilter.contains(listExplore)) {
                        listfilter.add(listExplore.get(i));
                    }
                }

            }
        }

        if (listfilter.size() != 0) {
            for (int i = 0; i < listfilter.size(); i++) {
                if (listfilter.get(i).getPrice() >= Float.valueOf(Information.minRager + "") &&
                        listfilter.get(i).getPrice() <= Float.valueOf(Information.maxRager + "")) {
                    lisfilter_temp.add(listfilter.get(i));
                }
            }
        }
        ShowNumberbook(lisfilter_temp);

    }

    public List<Book> filterStart() {

        lisfilter_temp = new ArrayList<>();
        listfilter = new ArrayList<>();
        //LatLng latLngSt = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
        //LatLng latLngSt = new LatLng(25.2446,55.3154);
        LatLng latLngSt = new LatLng(latitude, longitude);
        Double distance = Double.valueOf(Information.maxSeekbar);
        for (int i = 0; i < listExplore.size(); i++) {
            String[] genrel = listExplore.get(i).getGenre().split(";");

            LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(), listExplore.get(i).getLocation_longitude());
            double dss = CalculationByDistance(latLngSt, latLngEnd);
            if (CalculationByDistance(latLngSt, latLngEnd) <= distance) {
                if (!listfilter.contains(listExplore.get(i))) {
                    listfilter.add(listExplore.get(i));
                }
            }


        }

        if (listfilter.size() != 0) {
            for (int i = 0; i < listfilter.size(); i++) {
                if (listfilter.get(i).getPrice() >= Float.valueOf(Information.minRager + "") &&
                        listfilter.get(i).getPrice() <= Float.valueOf(Information.maxRager + "")) {
                    lisfilter_temp.add(listfilter.get(i));
                }
            }
        }
        try {
            if (filterList.get(0).getCheck() == true) {
                BookController bookController = new BookController(getActivity());
                Collections.sort(lisfilter_temp, bookController.distance);
                Information.nearDistance = true;
            } else if (filterList.get(1).getCheck() == true) {
                Collections.sort(lisfilter_temp, Book.priceasen);
                Information.priceLowtoHigh = true;
            } else if (filterList.get(2).getCheck() == true) {
                Collections.sort(lisfilter_temp, Book.pricedcen);
                Information.priceHightoLow = true;
            } else {
                Collections.sort(lisfilter_temp, Book.recently);
                Information.recently = true;
            }
        }catch (Exception err){

        }
        return lisfilter_temp;
    }

    public void ShowNumberbook(List<Book> lstBook) {
        int free = 0;
        int swap = 0;
        int buy = 0;

        for (int i = 0; i < lstBook.size(); i++) {
            if (lstBook.get(i).getAction().substring(0, 1).equals("1")) {
                swap++;
            }
            if (lstBook.get(i).getAction().substring(2, 3).equals("1")) {
                free++;
            }
            if (lstBook.get(i).getAction().substring(1, 2).equals("1")) {
                buy++;
            }
        }
        tab_all_count.setText("(" + lstBook.size() + ")");
        tab_swap_count.setText("(" + swap + ")");
        tab_free_count.setText("(" + free + ")");
        tab_cart_count.setText("(" + buy + ")");
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
        //Double kmInDec = Double.valueOf(newFormat+"");
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));

        return valueResult;
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


                TextView textView126=(TextView) dialog.findViewById(R.id.textView126);
                textView126.setText("Filter");

                TextView title_sort = (TextView) dialog.findViewById(R.id.title_sort);
                title_sort.setVisibility(View.GONE);

                ListView lv_dialog_filter = (ListView) dialog.findViewById(R.id.lv_dialog_filter);
                lv_dialog_filter.setVisibility(View.GONE);

                filterList = new ArrayList<>();
                for (int i = 0; i < prgmNameList1.length; i++) {
                    Filter filter = new Filter();
                    if (i == 0) {
                        filter.setTitle(prgmNameList1[i]);
                        filter.setCheck(Information.nearDistance);
                    } else if (i == 1) {
                        filter.setTitle(prgmNameList1[i]);
                        filter.setCheck(Information.priceLowtoHigh);
                    } else if (i == 2) {
                        filter.setTitle(prgmNameList1[i]);
                        filter.setCheck(Information.priceHightoLow);
                    } else if (i == 3) {
                        filter.setTitle(prgmNameList1[i]);
                        filter.setCheck(Information.recently);
                    }

                    filterList.add(filter);
                }
                adaper = new AdapterFilter(getActivity(), filterList);
                lv_dialog_filter.setAdapter(adaper);

                rangeSeekbar = (RangeSeekBar) dialog.findViewById(R.id.rangeSeekbar3);
                rangeSeekbar.setNotifyWhileDragging(true);
                rangeSeekbar.setSelectedMinValue(Information.minRager);
                rangeSeekbar.setSelectedMaxValue(Information.maxRager);
                rangeSeekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                        minRangerSeekbar = Integer.parseInt(minValue + "");
                        maxRangerSeekbar = Integer.parseInt(maxValue + "");
                        Information.minRager = minRangerSeekbar;
                        Information.maxRager = maxRangerSeekbar;
                    }
                });

                seekbar = (RangeSeekBar) dialog.findViewById(R.id.rangeSeekbar8);
                seekbar.setNotifyWhileDragging(true);
                seekbar.setSelectedMaxValue(Information.maxSeekbar);
                seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                        maxSeekbar = Double.parseDouble((maxValue + "").replace(" KM", ""));
                        Information.maxSeekbar = maxSeekbar;
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
                        ArrayList<String> listvalueGenre = new ArrayList<>();
                        String genreFilter = "";
                        for (int k = 0; k < genre.size(); k++) {
                            if (genre.get(k).ischeck() == true) {
                                listvalueGenre.add(genre.get(k).getValue());
                                genreFilter = genreFilter + genre.get(k).getValue() + ";";
                            }
                        }
                        if (genreFilter.length() > 0) {
                            genreFilter = genreFilter.substring(0, genreFilter.length() - 1);
                        }
                        Information.lstGenre = genre;
                        dialog.dismiss();

                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);

                        listingAsync listingAsync = new listingAsync(getContext(), pref.getString("session_id", ""), 0, 100, genreFilter);
                        listingAsync.execute();
                        //addMarkerSearch(filter(listvalueGenre));


                    }
                });
                final TextView tv_genralChoose = (TextView) dialog.findViewById(R.id.tv_genral);
                if (Information.lstGenre.size() > 0) {
                    String genreChoose = "";
                    for (int k = 0; k < Information.lstGenre.size(); k++) {
                        if (Information.lstGenre.get(k).ischeck() == true) {
                            genreChoose = genreChoose + Information.lstGenre.get(k).getValue() + ";";
                        }
                    }
                    if (genreChoose.length() > 0) {
                        tv_genralChoose.setText(genreChoose.substring(0, genreChoose.length() - 1));
                    } else {
                        tv_genralChoose.setText("Select genre");
                    }

                }
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
                        if (Information.lstGenre.size() > 0) {
                            rv_genre.setAdapter(new CustomListviewGenre(getContext(), Information.lstGenre));
                        } else {
                            rv_genre.setAdapter(new CustomListviewGenre(getContext(), genre));
                        }
                        dialog.show();

                        Button button_spiner_genre = (Button) dialog.findViewById(R.id.button_spiner_genre);
                        button_spiner_genre.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String genreChoose = "";
                                for (int k = 0; k < genre.size(); k++) {
                                    if (genre.get(k).ischeck() == true) {
                                        genreChoose = genreChoose + genre.get(k).getValue() + ";";
                                    }
                                }
                                if(genreChoose.length()>0){
                                    tv_genralChoose.setText(genreChoose.substring(0,genreChoose.length()-1));
                                }else{
                                    tv_genralChoose.setText("Select genre");
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
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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
            String err = e.getMessage();
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

        GetDayUsed getDayUsed = new GetDayUsed(getContext(), session_id);
        getDayUsed.execute();


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
            img_map_main = (CircularImageView) myContentsView.findViewById(R.id.img_map_main);
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
                if (books.getUser_photo().length() > 3) {
                    int index = books.getUser_photo().indexOf("_+_");
                    Picasso.with(getContext())
                            .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + books.getUser_photo().substring(0, index).trim() + "&image=" + books.getUser_photo().substring(index + 3, books.getUser_photo().length()))
                            .placeholder(R.mipmap.user_empty).
                            into(img_map_main);
                } else {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.user_empty);
                    img_map_main.setImageBitmap(bm);
                }

                txt_author_marker.setText("by " + books.getAuthor());
                txt_user_marker.setText(books.getUsername());
            } catch (Exception e) {
            }

            if(books.getRating()<0.1){
                ratingBar_marker.setVisibility(View.INVISIBLE);
            }else {
                ratingBar_marker.setRating(books.getRating());
                LayerDrawable stars = (LayerDrawable) ratingBar_marker.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.rgb(249, 242, 0), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.dot_light_screen1), PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.dot_light_screen1), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)), getResources().getColor(R.color.dot_light_screen1));
            }
            //stars.getColorFilter();
            char array[] = books.getAction().toCharArray();
            String swap = String.valueOf(array[0]);
            String free = String.valueOf(array[1]);
            String buy = String.valueOf(array[2]);


            String icon = IconMapController.iconExplorer(swap, free, buy);
            if (icon.equals("icon_swap")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_swap_not_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(), R.drawable.free_inactive);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.buy_inactive);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);

            }
            if (icon.equals("icon_free")) {

                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.swap_inactive);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_free_not_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.buy_inactive);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);


            }
            if (icon.equals("icon_buy")) {

                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.swap_inactive);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(), R.drawable.free_inactive);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_buy_not_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);

            }
            if (icon.equals("swapfree")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_swap_not_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_free_not_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.buy_inactive);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);

            }
            if (icon.equals("swapbuy")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_swap_not_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(), R.drawable.free_inactive);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_buy_not_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);

            }
            if (icon.equals("freebuy")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.swap_inactive);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_free_not_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_buy_not_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);

            }
            if (icon.equals("option")) {
                Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_swap_not_active);
                Bitmap bMap1 = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_free_not_active);
                Bitmap bMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.explore_btn_buy_not_active);
                img_swap_marker.setImageBitmap(bMap);
                img_free_marker.setImageBitmap(bMap1);
                img_buy_marker.setImageBitmap(bMap2);

            }
            return myContentsView;
        }
    }

    class listingAsync extends AsyncTask<String, Void, List<Book>> {

        Context context;
        ProgressDialog dialog;
        String session_id;
        int top, from;
        String genre;

        public listingAsync(Context context, String session_id, int from, int top, String genre) {
            this.context = context;
            this.session_id = session_id;
            this.top = top;
            this.from = from;
            this.genre = genre;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            try {
                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
                try {
                    CheckSession checkSession = new CheckSession();
                    boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
                    if (!check) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("session_id", null);
                        editor.putString("active", null);
                        editor.commit();
                        Intent intent = new Intent(getActivity(), SignIn_Activity.class);
                        getActivity().startActivity(intent);
                        this.cancel(true);
                    }
                } catch (Exception exx) {
                    Intent intent = new Intent(context, SignIn_Activity.class);
                    getActivity().startActivity(intent);
                    this.cancel(true);
                }
                BookController bookController = new BookController();

                return bookController.getAllBookInApp(0, 1000, 10, longitude, latitude, genre, "", pref.getString("session_id", null), 0, 10000, 0);
                //return bookController.getAllBookInApp(0,1000,10,Float.parseFloat("55.3154"),Float.parseFloat("25.2446"),"","",pref.getString("session_id", null),0,10000,0);
            }
            catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final List<Book> books) {
            try {
                if (books == null) {

                } else {
                    listExplore = books;
                    if(!trial) {
                        listExplore = books;
                    }
                    else{
                        ArrayList<Book> lisfilter_tempss = new ArrayList<>();
                        for (int i=0; i<books.size();i++){
                            if(books.get(i).getAction().substring(2, 3).equals("1")) {
                                lisfilter_tempss.add(books.get(i));
                            }
                        }
                        listExplore = lisfilter_tempss;
                    }

                    listExplore = filterStart();
                    ShowNumberbook(listExplore);
                    // create marker
                    addMarker(listExplore);

                }
            } catch (Exception e) {
                String sss=e.getMessage();
            }
        }
    }

    class GetDayUsed extends AsyncTask<String, Void, DayUsed> {

        Context context;

        String session_id;


        public GetDayUsed(Context context, String session_id) {
            this.context = context;
            this.session_id = session_id;

        }

        @Override
        protected DayUsed doInBackground(String... strings) {
            try {
                CheckSession checkSession = new CheckSession();
                SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
                boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
                if (!check) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("session_id", null);
                    editor.putString("active", null);
                    editor.commit();
                    Intent intent = new Intent(context, SignIn_Activity.class);
                    context.startActivity(intent);
                    this.cancel(true);
                }
            } catch (Exception exx) {
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            UserController userController = new UserController(context);
            return userController.GetDayUsed(session_id);
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final DayUsed dayUsed) {
            try {
                if (dayUsed == null) {

                } else {
                    if (Integer.parseInt(dayUsed.getDayUsed()) > 14 && !dayUsed.getIs_active().equals("1")) {
                        notiTrial.setVisibility(View.GONE);
                        notiUpgrade.setVisibility(View.VISIBLE);
                    } else if (Integer.parseInt(dayUsed.getDayUsed()) <= 14 && !dayUsed.getIs_active().equals("1")) {
                        txtNotifiTrial.setText("Your free trial expires in " + (14 - Integer.parseInt(dayUsed.getDayUsed())) + " days");
                        notiTrial.setVisibility(View.VISIBLE);
                        notiUpgrade.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
            }

            String genreChoose = "";
            try {
                if(Information.lstGenre.size()>0) {
                    for (int k = 0; k < Information.lstGenre.size(); k++) {
                        if (Information.lstGenre.get(k).ischeck() == true) {
                            genreChoose = genreChoose + Information.lstGenre.get(k).getValue() + ";";

                        }
                    }
                    if(genreChoose.length()>0){
                        genreChoose=genreChoose.substring(0,genreChoose.length()-1);
                    }
                }
            }catch (Exception err){

            }
            listingAsync listingAsync = new listingAsync(getContext(), session_id, 0, 100, genreChoose);
            listingAsync.execute();
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
                        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, (int) getResources().getDimension(R.dimen.width_pin),
                                (int) getResources().getDimension(R.dimen.height_pin))));
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
            ShowNumberbook(books);
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
                        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon, (int) getResources().getDimension(R.dimen.width_pin),
                                (int) getResources().getDimension(R.dimen.height_pin))));
                    }
                    Marker m_marker = mMap.addMarker(marker);
                    mMarkersHashMap.put(m_marker, books.get(i));
                }
            }

        } catch (Exception e) {
        }
    }

    public class GetAllGenreAsync1 extends AsyncTask<Void, Void, List<GenreValue>> {
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
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if (!check) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id", null);
                editor.putString("active", null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            GenreController genreController = new GenreController();
            return genreController.getAllGenre();
        }

        @Override
        protected void onPostExecute(List<GenreValue> genreValues) {
            try {

                if (genreValues.size() > 0) {
//                list.add("All");
                    for (int i = 0; i < genreValues.size(); i++) {
                        Genre genrel = new Genre();
                        genrel.setValue(genreValues.get(i).getTitle());
                        genre.add(genrel);
                    }
                }
            } catch (Exception e) {

            }
            dialog.dismiss();
        }
    }
}
