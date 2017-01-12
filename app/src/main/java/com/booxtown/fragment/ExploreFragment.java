package com.booxtown.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.booxtown.activity.MenuActivity;
import com.booxtown.activity.SignIn_Activity;
import com.booxtown.activity.Upgrade;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.GetAllGenreAsync;
import com.booxtown.controller.Information;
import com.booxtown.controller.RangeSeekBar;
import com.booxtown.controller.UserController;
import com.booxtown.model.DayUsed;
import com.booxtown.model.Genre;
import com.booxtown.model.NumberBook;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.booxtown.R;

import com.booxtown.adapter.AdapterExplore;
import com.booxtown.adapter.AdapterFilter;
import com.booxtown.controller.BookController;
import com.booxtown.controller.GPSTracker;
import com.booxtown.custom.CustomListviewGenre;
import com.booxtown.custom.CustomSearch;
import com.booxtown.custom.CustomTabbarExplore;
import com.booxtown.model.Book;
import com.booxtown.model.Filter;

/**
 * Created by Administrator on 26/08/2016.
 */
public class ExploreFragment extends Fragment implements OnMapReadyCallback {
    private int type_filter = 0;

    private LinearLayout linear_all, linear_swap, linear_free, linear_cart;
    private AdapterFilter adaper;
    private List<Filter> filterList;
    private Spinner spinner2;
    private RangeSeekBar rangeSeekbar;
    private RangeSeekBar seekbar;
    List<Book> listfilter, listExplore = new ArrayList<>(), lisfilter_temp;
    String proximity, session_id;

    ArrayList<Genre> genre;
    private ArrayAdapter<String> dataAdapter;
    EditText editSearch;

    RecyclerView rView;
    AdapterExplore adapter_exploer;
    GridLayoutManager gridLayoutManager;
    private int previousTotal = 0, visibleThreshold = 5;
    boolean loading = true,
            isLoading = true;
    int total, swap, sell, free;
    public TextView tab_all_count, tab_swap_count, tab_free_count, tab_cart_count, tvMin, tvMax, txt_filter_proximity;
    List<Book> listbook = new ArrayList<>();

    int minRangerSeekbar = 0;
    int maxRangerSeekbar = 0;
    double maxSeekbar = 0;
    RelativeLayout notiTrial,notiUpgrade;
    TextView txtNotifiTrial;
    int chooseTab=0;
    boolean trial=false;
    float longitude=0;
    float latitude=0;
    //GridView grid;
    public static String[] prgmNameList1 = {"Nearest distance", "Price low to high", "Price high to low", "Recently added"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.explore_fragment, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        session_id = pref.getString("session_id", null);
        ImageView img_menu = (ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getActivity()).load(R.drawable.btn_menu_locate).into(img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });
        notiTrial= (RelativeLayout) view.findViewById(R.id.notiTrial);
        notiUpgrade= (RelativeLayout) view.findViewById(R.id.notiUpgrade);
        txtNotifiTrial=(TextView) view.findViewById(R.id.txtNotifiTrial);
        notiTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), Upgrade.class);
                startActivity(intent);
            }
        });
        notiUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(), Upgrade.class);
                startActivity(intent);
            }
        });

        longitude=(float) new GPSTracker(getActivity()).getLongitude();
        latitude=(float) new GPSTracker(getActivity()).getLatitude();
        //grid=(GridView)view.findViewById(R.id.gridView);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rView = (RecyclerView) view.findViewById(R.id.recycler_view);
        rView.setLayoutManager(gridLayoutManager);
        View view_search = (View) view.findViewById(R.id.explore_search);
        new CustomSearch(view_search, getActivity());

        TextView txtTitle = (TextView) getActivity().findViewById(R.id.txt_title);
        txtTitle.setText("Explore");
        type_filter = 0;
        total = 0;
        swap = 0;
        free = 0;
        sell = 0;


        //grid.setOnScrollListener(new EndlessScrollListener());
        filterSort(view);

        genre = new ArrayList<>();
        for (int i = 0; i < GetAllGenreAsync.list.size(); i++) {
            Genre genrel = new Genre();
            genrel.setValue(GetAllGenreAsync.list.get(i));
            genre.add(genrel);
        }
        //end-------------------------------------

        //---------------------------------------------------------------
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
        editSearch = (EditText) view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //adapter_exploer.getFilter().filter(s);
                //int sizeList= adapter_exploer.getListExplore().size();
                //ShowNumberbook(adapter_exploer.getListExplore());

            }

            @Override
            public void afterTextChanged(Editable s) {
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
                    //listExplore=list_books;
                    adapter_exploer.setListExplore(list_books);
                    ShowNumberbook(list_books);
                    AdapterExplore adapter = new AdapterExplore(getActivity(), filterExplore(chooseTab), 2, 0);
                    rView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        linear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTab=1;
                AdapterExplore adapter = new AdapterExplore(getActivity(), filterExplore(1), 2, 0);
                rView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tab_custom.setDefault(1);

            }
        });

        linear_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTab=2;
                AdapterExplore adapter = new AdapterExplore(getActivity(), filterExplore(2), 2, 0);
                rView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tab_custom.setDefault(2);

            }
        });

        linear_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTab=3;
                tab_custom.setDefault(3);
                AdapterExplore adapter = new AdapterExplore(getActivity(), filterExplore(3), 2, 0);
                rView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        linear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTab=4;
                AdapterExplore adapter = new AdapterExplore(getActivity(), filterExplore(4), 2, 0);
                rView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tab_custom.setDefault(4);

            }
        });
        GetNumberBook getNumberBook = new GetNumberBook(0);
        getNumberBook.execute();
        //populatRecyclerView(session_id);
        //implementScrollListener(session_id);


        GetDayUsed getDayUsed= new GetDayUsed(getContext(),session_id);
        getDayUsed.execute();
        //------------------------------------------------------------------------------------
        return view;
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

    public List<Book> filterExplore(int type) {
        List<Book> list = new ArrayList<>();
        try {
            if (type == 1) {
                list = adapter_exploer.getListExplore();
            } else if (type == 2) {
                for (int i = 0; i < adapter_exploer.getListExplore().size(); i++) {
                    if (adapter_exploer.getListExplore().get(i).getAction().substring(0, 1).equals("1")) {
                        list.add(adapter_exploer.getListExplore().get(i));
                    }
                }
            } else if (type == 3) {
                for (int i = 0; i < adapter_exploer.getListExplore().size(); i++) {
                    if (adapter_exploer.getListExplore().get(i).getAction().substring(2, 3).equals("1")) {
                        list.add(adapter_exploer.getListExplore().get(i));
                    }
                }
            } else {
                for (int i = 0; i < adapter_exploer.getListExplore().size(); i++) {
                    if (adapter_exploer.getListExplore().get(i).getAction().substring(1, 2).equals("1")) {
                        list.add(adapter_exploer.getListExplore().get(i));
                    }
                }
            }
        }catch (Exception err){


        }


        return list;
    }

    public void callFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
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
                    if(i==0){
                        filter.setTitle(prgmNameList1[i]);
                        filter.setCheck(Information.nearDistance);
                    }else if(i==1){
                        filter.setTitle(prgmNameList1[i]);
                        filter.setCheck(Information.priceLowtoHigh);
                    }
                    else if(i==2){
                        filter.setTitle(prgmNameList1[i]);
                        filter.setCheck(Information.priceHightoLow);
                    }
                    else if(i==3){
                        filter.setTitle(prgmNameList1[i]);
                        filter.setCheck(Information.recently);
                    }

                    filterList.add(filter);
                }
                adaper = new AdapterFilter(getActivity(), filterList);
                lv_dialog_filter.setAdapter(adaper);

                /*rangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar3);

                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.abc);
                Bitmap thumb=Bitmap.createBitmap(38,38, Bitmap.Config.ARGB_8888);
                Canvas canvas=new Canvas(thumb);
                canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
                        new Rect(0,0,thumb.getWidth(),thumb.getHeight()),null);
                Drawable drawable = new BitmapDrawable(getResources(),thumb);
                rangeSeekbar.setLeftThumbDrawable(drawable);
                rangeSeekbar.setRightThumbDrawable(drawable);

*//*

                tvMin = (TextView) dialog.findViewById(R.id.txt_filter_rangemin);
                tvMax = (TextView) dialog.findViewById(R.id.txt_filter_rangemax);
*//*

                rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number minValue, Number maxValue) {
                        *//*tvMin.setText(String.valueOf(minValue));
                        if(String.valueOf(maxValue).equals("1")){
                            tvMax.setText("1000");
                        }else{
                            tvMax.setText(String.valueOf(maxValue));
                        }*//*

                    }
                });*/

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

                        for (int k = 0; k < genre.size(); k++) {
                            if (genre.get(k).ischeck() == true) {
                                listvalueGenre.add(genre.get(k).getValue());
                            }
                        }
                        Information.lstGenre = genre;
                        dialog.dismiss();
                        filter(listvalueGenre);
                    }
                });
                spinner2 = (Spinner) dialog.findViewById(R.id.spinner_dialog_filter);

                dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, GetAllGenreAsync.list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter);
                final TextView tv_genralChoose=(TextView) dialog.findViewById(R.id.tv_genral);
                if(Information.lstGenre.size()>0){
                    String genreChoose="";
                    for (int k = 0; k < Information.lstGenre.size(); k++) {
                        if (Information.lstGenre.get(k).ischeck() == true) {
                            if(k<Information.lstGenre.size()-1) {
                                genreChoose = genreChoose +Information.lstGenre.get(k).getValue() +",";
                            }else{
                                genreChoose = genreChoose +Information.lstGenre.get(k).getValue() +"";
                            }
                        }
                    }
                    tv_genralChoose.setText(genreChoose);
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
                                String genreChoose="";
                                for (int k = 0; k < genre.size(); k++) {
                                    if (genre.get(k).ischeck() == true) {
                                        if(k==genre.size()-1) {
                                            genreChoose = genreChoose +genre.get(k).getValue() +"";
                                        }else{
                                            genreChoose = genreChoose +genre.get(k).getValue() +",";
                                        }
                                    }
                                }
                                tv_genralChoose.setText(genreChoose);
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

    public void filter(ArrayList<String> listvalueGenre) {

        lisfilter_temp = new ArrayList<>();
        listfilter = new ArrayList<>();
        LatLng latLngSt = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
        Double distance = Double.valueOf(Information.maxSeekbar);
        for (int i = 0; i < listExplore.size(); i++) {
            String[] genrel = listExplore.get(i).getGenre().split(";");
            if (listvalueGenre.size() > 0) {
                for (int j = 0; j < genrel.length; j++) {
                    for (int f = 0; f < listvalueGenre.size(); f++) {
                        if (genrel[j].contains(listvalueGenre.get(f))) {
                            LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(), listExplore.get(i).getLocation_longitude());
                            if (CalculationByDistance(latLngSt, latLngEnd) <= distance) {
                                if(!listfilter.contains(listExplore.get(i))) {
                                    listfilter.add(listExplore.get(i));
                                }
                            }
                        }
                    }
                }

            } else {
                LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(), listExplore.get(i).getLocation_longitude());
                if (CalculationByDistance(latLngSt, latLngEnd) <= distance) {
                    if(!listfilter.contains(listExplore.get(i))) {
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

        if (filterList.get(0).getCheck() == true) {
            BookController bookController = new BookController(getActivity());
            Collections.sort(lisfilter_temp, bookController.distance);
            Information.nearDistance=true;
        } else if (filterList.get(1).getCheck() == true) {
            Collections.sort(lisfilter_temp, Book.priceasen);
            Information.priceLowtoHigh=true;
        } else if (filterList.get(2).getCheck() == true) {
            Collections.sort(lisfilter_temp, Book.pricedcen);
            Information.priceHightoLow=true;
        } else {
            Collections.sort(lisfilter_temp, Book.recently);
            Information.recently=true;
        }
        ShowNumberbook(lisfilter_temp);
        adapter_exploer.setListExplore(lisfilter_temp);
        AdapterExplore adapter = new AdapterExplore(getActivity(), filterExplore(chooseTab), 2, 0);
        rView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public List<Book> filterStart() {
        try {
            lisfilter_temp = new ArrayList<>();
            listfilter = new ArrayList<>();
            LatLng latLngSt = new LatLng(new GPSTracker(getActivity()).getLatitude(), new GPSTracker(getActivity()).getLongitude());
            Double distance = Double.valueOf(Information.maxSeekbar);
            for (int i = 0; i < listExplore.size(); i++) {
                String[] genrel = listExplore.get(i).getGenre().split(";");

                LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(), listExplore.get(i).getLocation_longitude());
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
            return lisfilter_temp;
        }catch (Exception ex){
            String err= ex.getMessage();
            return lisfilter_temp;
        }
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
        return kmInDec;
    }

    private void populatRecyclerView(String session_id) {
        GetTopbook getbook = new GetTopbook(getContext(),session_id, 0, 20);
        getbook.execute();
        if (listExplore.size() == 0) {
            adapter_exploer = new AdapterExplore(getActivity(), listExplore, 2, 0);
            rView.setAdapter(adapter_exploer);
        } else {
            adapter_exploer.notifyDataSetChanged();
        }

    }

    private void implementScrollListener(final String session_id) {
        rView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = rView.getChildCount();
                int totalItemCount = gridLayoutManager.getItemCount();
                int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
                if (loading) {

                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold) && isLoading) {
                    // End has been reached
                    Book dashBoard_lv = listExplore.get(listExplore.size() - 1);
                    GetTopbook getbook = new GetTopbook(getContext(),session_id, Integer.parseInt(dashBoard_lv.getId()), 20);
                    getbook.execute();
                    // Do something
                    loading = true;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GPSTracker gpsTracker = new GPSTracker(getContext());

    }

    class GetDayUsed extends AsyncTask<String, Void,DayUsed> {

        Context context;

        String session_id;


        public GetDayUsed(Context context,String session_id) {
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
                    if(Integer.parseInt(dayUsed.getDayUsed())>14 && !dayUsed.getIs_active().equals("1")){
                        notiTrial.setVisibility(View.GONE);
                        notiUpgrade.setVisibility(View.VISIBLE);
                        trial=true;

                    }else if(Integer.parseInt(dayUsed.getDayUsed())<=14 && !dayUsed.getIs_active().equals("1")){
                        txtNotifiTrial.setText("Your free trial expires in "+(14-Integer.parseInt(dayUsed.getDayUsed()))+" days");
                        notiTrial.setVisibility(View.VISIBLE);
                        notiUpgrade.setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
            }

            GetTopbook getTopbook = new GetTopbook(getContext(),"", 0, 0);
            getTopbook.execute();
        }
    }

    public class GetTopbook extends AsyncTask<Void, Void, List<Book>> {
        String session_id;
        long from;
        long top;
        Context context;

        public GetTopbook(Context context,String session_id, long from, long top) {
            this.session_id = session_id;
            this.from = from;
            this.top = top;
            this.context = context;
        }

        @Override
        protected List<Book> doInBackground(Void... params) {
            try {
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
                BookController bookController = new BookController();
                //listbook= bookController.getallbook();

                listbook = bookController.getAllBookInApp(0,1000,10,longitude,latitude,"","",pref.getString("session_id", null),0,10000,0);
                return listbook;
            }catch (Exception exx){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Book> list) {
            try {
                if (list.size() > 0) {
                    //tab_all_count.setText("(" + list.size() + ")");
                    if(!trial) {
                        listExplore.addAll(list);
                    }
                    else{
                        ArrayList<Book> lisfilter_tempss = new ArrayList<>();
                        for (int i=0; i<list.size();i++){
                            if(list.get(i).getAction().substring(2, 3).equals("1")) {
                                lisfilter_tempss.add(list.get(i));
                            }
                        }
                        listExplore.addAll(lisfilter_tempss);
                    }
                    ShowNumberbook(filterStart());
                    adapter_exploer = new AdapterExplore(getActivity(), filterStart(), 2, 0);
                    rView.setAdapter(adapter_exploer);
                    adapter_exploer.setListExplore(filterStart());
                    isLoading = true;
                } else {
                    isLoading = false;
                }
            } catch (Exception e) {
                String errr= e.getMessage();
            }
        }
    }

    public class GetNumberBook extends AsyncTask<Void, Void, List<NumberBook>> {
        int user_id;

        public GetNumberBook(int user_id) {
            this.user_id = user_id;
        }

        @Override
        protected List<NumberBook> doInBackground(Void... params) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = getContext().getSharedPreferences("MyPref",getContext().MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(getContext(), SignIn_Activity.class);
                getContext().startActivity(intent);
                this.cancel(true);
            }
            BookController bookController = new BookController();

            return bookController.getNumberBook(user_id);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<NumberBook> list) {
            try {
                if (list.size() > 0) {

                    total = Integer.parseInt(list.get(0).getTotal());
                    sell = Integer.parseInt(list.get(0).getSell());
                    swap = Integer.parseInt(list.get(0).getSwap());
                    free = Integer.parseInt(list.get(0).getFree());


                } else {

                }
            } catch (Exception e) {

            }
        }
    }


}
