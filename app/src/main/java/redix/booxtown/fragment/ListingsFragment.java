package redix.booxtown.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.AddbookActivity;
import redix.booxtown.activity.ListingCollectionActivity;
import redix.booxtown.activity.ListingsDetailActivity;
import redix.booxtown.activity.MainAllActivity;
import redix.booxtown.activity.MenuActivity;
import redix.booxtown.adapter.AdapterExplore;
import redix.booxtown.adapter.AdapterFilter;
import redix.booxtown.adapter.AdapterListings;
import redix.booxtown.adapter.ListBookAdapter;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.GPSTracker;
import redix.booxtown.controller.GetAllGenreAsync;
import redix.booxtown.controller.UserController;
import redix.booxtown.custom.CustomListviewGenre;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;
import redix.booxtown.model.Explore;
import redix.booxtown.model.Filter;
import redix.booxtown.model.Genre;

/**
 * Created by Administrator on 29/08/2016.
 */
public class ListingsFragment extends Fragment
{
    ArrayList<Book> listEx= new ArrayList<>();
    //GridView grid;
    Book book;
    private CrystalRangeSeekbar rangeSeekbar;
    private CrystalSeekbar seekbar;
    private AdapterFilter adaper;
    private  ArrayAdapter<String> dataAdapter;
    private Spinner spinner2;
    TextView txt_my_listings,tvMin,tvMax,txt_filter_proximity;
    private List<Filter> filterList;
    ListBookAdapter adapter_listbook;
    GridLayoutManager gridLayoutManager;
    RecyclerView rView;
    EditText editSearch;
    ArrayList<Genre> genre;
    public static int num_list;
    List<Book> lisfilter_temp,listfilter,listExplore = new ArrayList<>();
    public String proximity;
    public static TextView txt_add_book;
    public static String [] prgmNameList1={"Nearest distance","Price low to high","Price high to low","Recently added"};
    private int previousTotal = 0,visibleThreshold = 5;
    boolean loading = true,
            isLoading = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.listings_fragment, container, false);

        TextView title=(TextView) getActivity().findViewById(R.id.txt_title);
        title.setText("Listings");
        ImageView imageView_back=(ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(imageView_back);
        //grid=(GridView)view.findViewById(R.id.grid_view_listings);
        txt_my_listings = (TextView) view.findViewById(R.id.txt_my_listings);
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                startActivity(intent);
            }
        });
        editSearch = (EditText) view.findViewById(R.id.editSearch);
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        rView = (RecyclerView)view.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter_listbook.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ImageView btn_filter_explore = (ImageView)view.findViewById(R.id.btn_filter_explore);
        Picasso.with(getContext()).load(R.drawable.btn_locate_filter).into(btn_filter_explore);

        ImageView btn_search = (ImageView)view.findViewById(R.id.btn_search);
        Picasso.with(getContext()).load(R.drawable.btn_locate_search).into(btn_search);
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        String session_id = pref.getString("session_id", null);
        btn_filter_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_filter_sort);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ListView lv_dialog_filter = (ListView)dialog.findViewById(R.id.lv_dialog_filter);
                filterList = new ArrayList<>();
                for (int i =0;i<prgmNameList1.length;i++){
                    Filter filter = new Filter();
                    filter.setTitle(prgmNameList1[i]);
                    filter.setCheck(false);
                    filterList.add(filter);
                }
                adaper = new AdapterFilter(getActivity(),filterList);
                lv_dialog_filter.setAdapter(adaper);

                rangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar3);

                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.abc);
                Bitmap thumb=Bitmap.createBitmap(38,38, Bitmap.Config.ARGB_8888);
                Canvas canvas=new Canvas(thumb);
                canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
                        new Rect(0,0,thumb.getWidth(),thumb.getHeight()),null);
                Drawable drawable = new BitmapDrawable(getResources(),thumb);
                rangeSeekbar.setLeftThumbDrawable(drawable);
                rangeSeekbar.setRightThumbDrawable(drawable);


                tvMin = (TextView) dialog.findViewById(R.id.txt_filter_rangemin);
                tvMax = (TextView) dialog.findViewById(R.id.txt_filter_rangemax);

                rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number minValue, Number maxValue) {
                        tvMin.setText(String.valueOf(minValue));
                        tvMax.setText(String.valueOf(maxValue));
                    }
                });

                txt_filter_proximity = (TextView)dialog.findViewById(R.id.txt_filter_proximity);
                seekbar = (CrystalSeekbar) dialog.findViewById(R.id.rangeSeekbar8);
                seekbar.setLeftThumbDrawable(drawable);
                seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number minValue) {
                        txt_filter_proximity.setText(String.valueOf(minValue)+"KM");
                        proximity = String.valueOf(minValue);
                    }
                });

                ImageView imv_dialog_filter_close = (ImageView)dialog.findViewById(R.id.imv_dialog_filter_close);
                Picasso.with(getContext()).load(R.drawable.btn_close_filter).into(imv_dialog_filter_close);
                imv_dialog_filter_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button btn_dialog_filter_submit = (Button)dialog.findViewById(R.id.btn_dialog_filter_submit);
                btn_dialog_filter_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ArrayList<String> listvalueGenre = new ArrayList<>();
                        for (int k = 0; k < genre.size(); k++) {
                            if (genre.get(k).ischeck() == true) {
                                listvalueGenre.add(genre.get(k).getValue());
                            }
                        }
                        dialog.dismiss();
                        filter(listvalueGenre);
                    }
                });

                RelativeLayout tv_genral = (RelativeLayout) dialog.findViewById(R.id.relaytive_genre);
                tv_genral.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        genre = new ArrayList<>();
                        for (int i = 0; i < GetAllGenreAsync.list.size(); i++) {
                            Genre genrel = new Genre();
                            genrel.setValue(GetAllGenreAsync.list.get(i));
                            genre.add(genrel);
                        }
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_genre);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        ListView listView_genre = (ListView) dialog.findViewById(R.id.listView_genre);
                        listView_genre.setAdapter(new CustomListviewGenre(getContext(), genre));
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
                spinner2 = (Spinner) dialog.findViewById(R.id.spinner_dialog_filter);

                dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, GetAllGenreAsync.list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter);

            }
        });



        //------------------------------------------------------------
        //add book
        txt_add_book = (TextView)view.findViewById(R.id.txt_add_book);
        txt_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("activity","add");
                bundle.putInt("num_list",num_list);
                ListingCollectionActivity listingCollectionActivity = new ListingCollectionActivity();
                listingCollectionActivity.setArguments(bundle);
                callFragment(listingCollectionActivity);
            }
        });

        //--------------------------------------------------------------
        //change color tab

        TextView txt_my_listings = (TextView)view.findViewById(R.id.txt_my_listings);
        txt_my_listings.setTextColor(getResources().getColor(R.color.color_text));
        txt_my_listings.setBackgroundColor(getResources().getColor(R.color.dot_light_screen1));
        txt_add_book.setText("Add a book");
        txt_add_book.setTextColor(getResources().getColor(R.color.dot_light_screen1));
        txt_add_book.setBackgroundColor(getResources().getColor(R.color.color_text));
        //end
        MainAllActivity.setTxtTitle("Listings");

        populatRecyclerView(session_id);
        implementScrollListener(session_id);

        return view;
    }

    private void populatRecyclerView(String session_id) {
        listingAsync getbook = new listingAsync(getContext(),session_id,0,15);
        getbook.execute();
        if(listExplore.size() == 0){
            adapter_listbook = new ListBookAdapter(getActivity(), listExplore, 1,2);
            rView.setAdapter(adapter_listbook);
        }else {
            adapter_listbook.notifyDataSetChanged();
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
                    Book book = listExplore.get(listExplore.size()-1);
                    listingAsync getbook = new listingAsync(getContext(),session_id,Integer.valueOf(book.getId()),15);
                    getbook.execute();
                    // Do something
                    loading = true;
                }
            }
        });
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


    public void filter(ArrayList<String> listvalueGenre){
        lisfilter_temp = new ArrayList<>();
        listfilter = new ArrayList<>();
        LatLng latLngSt = new LatLng(new GPSTracker(getActivity()).getLatitude(),new GPSTracker(getActivity()).getLongitude());
        Double distance = Double.valueOf(proximity);


        for (int i = 0; i < listExplore.size();i++){
            String[] genrel = listExplore.get(i).getGenre().split(";");
            for (int f = 0;f<listvalueGenre.size();f++){
                if (listvalueGenre.get(f).equals("All")){
                    LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(),listExplore.get(i).getLocation_longitude());
                    if (CalculationByDistance(latLngSt,latLngEnd)<=distance){
                        listfilter.add(listExplore.get(i));
                    }
                }
            }

                for (int j = 0;j<genrel.length;j++){
                    for (int f = 0;f<listvalueGenre.size();f++){
                        if (genrel[j].contains(listvalueGenre.get(f))) {
                            LatLng latLngEnd = new LatLng(listExplore.get(i).getLocation_latitude(),listExplore.get(i).getLocation_longitude());
                            if (CalculationByDistance(latLngSt,latLngEnd)<=distance){
                                listfilter.add(listExplore.get(i));
                            }
                        }
                    }
            }
        }

        if (listfilter.size()!=0){
            for (int i = 0;i<listfilter.size();i++){
                if (listfilter.get(i).getPrice()>=Float.valueOf(tvMin.getText().toString()) &&
                        listfilter.get(i).getPrice()<= Float.valueOf(tvMax.getText().toString())){
                    lisfilter_temp.add(listfilter.get(i));
                }
            }
        }

        if (filterList.get(0).getCheck()== true){
            BookController bookController = new BookController(getActivity());
            Collections.sort(lisfilter_temp,bookController.distance);
        }
        else if (filterList.get(1).getCheck()== true){
            Collections.sort(lisfilter_temp,Book.priceasen);
        }
        else if (filterList.get(2).getCheck()== true){
            Collections.sort(lisfilter_temp,Book.pricedcen);
        }
        else{
            Collections.sort(lisfilter_temp,Book.recently);
        }
        ListBookAdapter adapter = new ListBookAdapter(getActivity(),lisfilter_temp,2,0);
        rView.setAdapter(adapter);
    }

    public void callFragment(Fragment fragment ){
        FragmentManager manager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    class listingAsync extends AsyncTask<String,Void,List<Book>>{

        Context context;
        ProgressDialog dialog;
        String session_id;
        int top,from;
        public listingAsync(Context context,String session_id,int from,int top){
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
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            try {
                if (books == null) {
                    dialog.dismiss();
                    isLoading = false;
                } else {
                    listExplore.addAll(books);
                    adapter_listbook.notifyDataSetChanged();
                    num_list = books.size();
                    txt_my_listings.setText("My listings" + "(" + String.valueOf(listExplore.size()) + ")");
                    dialog.dismiss();
                    isLoading = true;
                }
            }catch (Exception e){}
        }
    }
}

