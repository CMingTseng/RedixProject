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
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.ListingsDetailActivity;
import redix.booxtown.activity.MenuActivity;
import redix.booxtown.adapter.AdapterFilter;
import redix.booxtown.adapter.ListBookAdapter;
import redix.booxtown.controller.BookController;
import redix.booxtown.controller.GPSTracker;
import redix.booxtown.controller.IconMapController;
import redix.booxtown.controller.NotificationController;
import redix.booxtown.custom.CustomSearch;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;
import redix.booxtown.model.Filter;
import redix.booxtown.model.Notification;

public class MainFragment extends Fragment implements GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback {
    private GoogleMap mMap;
    final int RQS_GooglePlayServices = 1;

    ListView lv;
    Context context;
    ImageView close_menu;
    public static int [] prgmImages={R.drawable.home,R.drawable.notification,R.drawable.faq,R.drawable.invited,R.drawable.rate,R.drawable.about,R.drawable.contact1,R.drawable.setting,R.drawable.logout,R.drawable.unsub};
    public static String [] prgmNameList={"Nearest distance","Price low to high","Price high to low","Recently added","Nearest distance","Price low to high","Price high to low","Recently added","Nearest distance","Price low to high"};
    public static String [] prgmNameList1={"Nearest distance","Price low to high","Price high to low","Recently added"};
    private MenuBottomCustom bottom;
    private LatLng latLngBounds;
    MarkerOptions marker;
    private HashMap<Marker, Book> mMarkersHashMap = new HashMap<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        ImageView img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MenuActivity.class);
                startActivity(intent);
            }
        });


        View view_search= view.findViewById(R.id.custom_search);
//        new CustomSearch(view_search,getActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainFragment.this);

        filterSort(view);

//        List<Notification> list= new ArrayList<>();
//        Notification notification= new Notification("22","Thuyết Exception");
//        list.add(notification);
//
//        NotificationController controller= new NotificationController();
//        controller.sendNotification(list);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void filterSort(View view){
        ImageView btn_filter_explore = (ImageView)view.findViewById(R.id.btn_filter_explore);
        Picasso.with(getContext()).load(R.drawable.btn_locate_filter).into(btn_filter_explore);

        ImageView btn_search = (ImageView)view.findViewById(R.id.btn_search);
        Picasso.with(getContext()).load(R.drawable.btn_locate_search).into(btn_search);
        btn_filter_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_filter_sort);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
                List<Filter> listft = new ArrayList<Filter>();
                for (int i = 0;i<prgmNameList1.length;i++){
                    Filter filter = new Filter();
                    filter.setCheck(false);
                    filter.setTitle(prgmNameList1[i]);
                    listft.add(filter);
                }
                ListView lv_dialog_filter = (ListView)dialog.findViewById(R.id.lv_dialog_filter);
                lv_dialog_filter.setAdapter(new AdapterFilter(getActivity(),listft));

                final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) dialog.findViewById(R.id.rangeSeekbar3);
                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.abc);
                Bitmap thumb=Bitmap.createBitmap(38,38, Bitmap.Config.ARGB_8888);
                Canvas canvas=new Canvas(thumb);
                canvas.drawBitmap(bitmap,new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),
                        new Rect(0,0,thumb.getWidth(),thumb.getHeight()),null);
                Drawable drawable = new BitmapDrawable(getResources(),thumb);
                rangeSeekbar.setLeftThumbDrawable(drawable);
                rangeSeekbar.setRightThumbDrawable(drawable);

                final TextView tvMin = (TextView) dialog.findViewById(R.id.txt_filter_rangemin);
                final TextView tvMax = (TextView) dialog.findViewById(R.id.txt_filter_rangemax);

                rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number minValue, Number maxValue) {
                        tvMin.setText(String.valueOf(minValue));
                        tvMax.setText(String.valueOf(maxValue));
                    }
                });

                final TextView txt_filter_proximity = (TextView)dialog.findViewById(R.id.txt_filter_proximity);
                final CrystalSeekbar seekbar = (CrystalSeekbar) dialog.findViewById(R.id.rangeSeekbar8);
                seekbar.setLeftThumbDrawable(drawable);


                seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number minValue) {
                        txt_filter_proximity.setText(String.valueOf(minValue)+" KM");
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
                        dialog.dismiss();
                    }
                });
                Spinner spinner2 = (Spinner) dialog.findViewById(R.id.spinner_dialog_filter);
                List<String> list = new ArrayList<String>();
                list.add("Nearest distance");
                list.add("list 2");
                list.add("list 3");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter);
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    public Bitmap resizeMapIcons(String icon,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(icon, "drawable", getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        // latitude and longitude
        SharedPreferences pref = getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = pref.edit();
        String session_id = pref.getString("session_id", null);
        listingAsync listingAsync = new listingAsync(getContext());
        listingAsync.execute(session_id);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(new GPSTracker(getActivity()).getLatitude(),new GPSTracker(getActivity()).getLongitude()),10));
        //mMap.setOnMapLongClickListener(this);
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        TextView txt_title_marker,txt_author_marker,txt_user_marker;
        RatingBar ratingBar_marker;
        ImageView img_swap_marker,img_free_marker,img_buy_marker;
        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.dialog_map_main, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            Book books = mMarkersHashMap.get(marker);
            txt_title_marker = (TextView)myContentsView.findViewById(R.id.txt_title_marker);
            txt_author_marker = (TextView)myContentsView.findViewById(R.id.txt_author_marker);
            txt_user_marker = (TextView)myContentsView.findViewById(R.id.txt_user_marker);
            ratingBar_marker = (RatingBar)myContentsView.findViewById(R.id.ratingBar_marker);
            img_swap_marker =(ImageView)myContentsView.findViewById(R.id.img_swap_marker);
            img_free_marker =(ImageView)myContentsView.findViewById(R.id.img_free_marker);
            img_buy_marker =(ImageView)myContentsView.findViewById(R.id.img_buy_marker);
            txt_title_marker.setText(books.getTitle());
            txt_author_marker.setText("By "+books.getAuthor());
            txt_user_marker.setText(books.getUsername());
            char array[] = books.getAction().toCharArray();
            String swap = String.valueOf(array[0]);
            String free = String.valueOf(array[1]);
            String buy = String.valueOf(array[2]);
            String icon = IconMapController.iconExplorer(swap,free,buy);
            if(icon.equals("icon_swap")){
                Picasso.with(getContext()).load(R.drawable.explore_btn_swap_active).into(img_swap_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_free_not_active).into(img_free_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_buy_not_active).into(img_buy_marker);
            }
            if(icon.equals("icon_free")){
                Picasso.with(getContext()).load(R.drawable.explore_btn_swap_not_active).into(img_swap_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_free_active).into(img_free_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_buy_not_active).into(img_buy_marker);
            }
            if(icon.equals("icon_buy")){
                Picasso.with(getContext()).load(R.drawable.explore_btn_swap_not_active).into(img_swap_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_free_not_active).into(img_free_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_buy_active).into(img_buy_marker);
            }
            if(icon.equals("swapfree")){
                Picasso.with(getContext()).load(R.drawable.explore_btn_swap_active).into(img_swap_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_free_active).into(img_free_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_buy_not_active).into(img_buy_marker);
            }
            if(icon.equals("swapbuy")){
                Picasso.with(getContext()).load(R.drawable.explore_btn_swap_active).into(img_swap_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_free_not_active).into(img_free_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_buy_active).into(img_buy_marker);
            }
            if(icon.equals("freebuy")){
                Picasso.with(getContext()).load(R.drawable.explore_btn_swap_not_active).into(img_swap_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_free_active).into(img_free_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_buy_active).into(img_buy_marker);
            }
            if(icon.equals("option")){
                Picasso.with(getContext()).load(R.drawable.explore_btn_free_active).into(img_free_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_buy_active).into(img_buy_marker);
                Picasso.with(getContext()).load(R.drawable.explore_btn_swap_active).into(img_swap_marker);
            }
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    class listingAsync extends AsyncTask<String,Void,List<Book>> {

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        public listingAsync(Context context){
            this.context = context;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            listemp = new ArrayList<>();
            BookController bookController = new BookController();
            listemp = bookController.getallbook();
            return listemp;
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
        protected void onPostExecute(final List<Book> books) {
            if (books == null){
                dialog.dismiss();
            }else {
                // create marker
                for(int i = 0;i<books.size();i++) {
                    marker = new MarkerOptions().position(new LatLng(books.get(i).getLocation_latitude(),books.get(i).getLocation_longitude())).title("Hello Maps");
                    latLngBounds = new LatLng(books.get(i).getLocation_latitude(),books.get(i).getLocation_longitude());
                    // Changing marker icon
                    char array[] = books.get(i).getAction().toCharArray();
                    String swap = String.valueOf(array[0]);
                    String free = String.valueOf(array[1]);
                    String buy = String.valueOf(array[2]);
                    String icon = IconMapController.icon(swap,free,buy);
                    final Book book = books.get(i);
                    if (icon!=null){
                        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon,110, 150)));
                        // adding marker
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                Bundle bundle = new Bundle();
                                bundle.putString(String.valueOf(R.string.valueListings),"1");
                                bundle.putSerializable("item",book);
                                FragmentTransaction transaction = manager.beginTransaction();
                                ListingsDetailActivity fra = new ListingsDetailActivity();
                                fra.setArguments(bundle);
                                transaction.replace(R.id.map,fra);
                                transaction.commit();
                            }
                        });
                    }
                    Marker m_marker = mMap.addMarker(marker);
                    mMarkersHashMap.put(m_marker,book);
                }
                dialog.dismiss();
            }
            super.onPostExecute(books);
        }
    }
}
