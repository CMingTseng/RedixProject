package redix.booxtown.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import redix.booxtown.R;
import redix.booxtown.controller.GetAllGenreAsync;
import redix.booxtown.custom.CustomListviewGenre;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.fragment.MyProfileFragment;
import redix.booxtown.model.Genre;

public class EditListingActivity extends AppCompatActivity implements LocationListener,OnMapReadyCallback,GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private MenuBottomCustom menu_bottom;

    private ImageView btn_location;
    private ImageView btn_commnet;
    private ImageView btn_camera;
    private ImageView btn_bag;
    private ImageView btn_user;

    ArrayList<Genre> genre;
//    String[] genravalue = {"Architecture","Business and Economics","Boy,Mid and Spirit","Children","Computers and Technology",
//            "Crafts and Hobbies","Education","Family,Parenting and Relationships","Fiction and Literature","Food and Drink"
//            ,"Family,Parenting and Relationships","Fiction and Literature","Food and Drink"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);
        genre = new ArrayList<>();
        //picaso
        ImageView img_add_image = (ImageView)findViewById(R.id.imageView32);
        Picasso.with(getApplicationContext()).load(R.drawable.btn_add).into(img_add_image);

        btn_location = (ImageView) findViewById(R.id.img_menu_bottom_location);
        btn_commnet = (ImageView) findViewById(R.id.img_menu_bottom_comment);
        btn_camera = (ImageView) findViewById(R.id.img_menu_bottom_camera);
        btn_bag = (ImageView) findViewById(R.id.img_menu_bottom_bag);
        btn_user = (ImageView) findViewById(R.id.img_menu_bottom_user);

        //sửa sau
        //end
        for (int i = 0; i< GetAllGenreAsync.list.size(); i++){
            Genre genrel = new Genre();
            genrel.setValue(GetAllGenreAsync.list.get(i));
            genre.add(genrel);
        }
        mMapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map_editlisting));
        mMapFragment.getMapAsync(EditListingActivity.this);
        btnDelete();

        //show edittext when check to sell
        final CheckBox checkBox = (CheckBox)findViewById(R.id.ck_sell_editlisting);
        final EditText edt_editlisting_sell = (EditText)findViewById(R.id.edt_editlisting_sell);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    edt_editlisting_sell.setVisibility(View.VISIBLE);

                }else {
                    edt_editlisting_sell.setVisibility(View.GONE);
                }
            }
        });

        Button btn_menu_editlist_delete = (Button)findViewById(R.id.btn_menu_editlist_delete);
        btn_menu_editlist_delete.setVisibility(View.VISIBLE);
        btn_menu_editlist_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EditListingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete_editlisting);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                //btn delete
                Button btn_delete_editlisting = (Button)dialog.findViewById(R.id.btn_delete_editlisting);
                btn_delete_editlisting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EditListingActivity.this,MyProfileFragment.class);
                        startActivity(intent);
                    }
                });

                ImageView imv_close_dialog_editlist = (ImageView)dialog.findViewById(R.id.imv_close_dialog_editlist);
                imv_close_dialog_editlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        Button btn_menu_editlisting_update = (Button)findViewById(R.id.btn_menu_editlisting_update);
        btn_menu_editlisting_update.setVisibility(View.VISIBLE);
        btn_menu_editlisting_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //menu
        ImageView img_menu_component = (ImageView)findViewById(R.id.img_menu_component);
        img_menu_component.setVisibility(View.GONE);

        TextView title_menu = (TextView)findViewById(R.id.txt_title);
        title_menu.setText("Edit Listing");

        ImageView menu = (ImageView)findViewById(R.id.img_menu);

        ImageView img_menu = (ImageView)findViewById(R.id.img_menu);
        img_menu.setImageResource(R.drawable.btn_sign_in_back);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //end

        //spinner
        ImageView imageView=(ImageView) findViewById(R.id.img_menu_genre);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EditListingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_genre);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ListView listView_genre=(ListView)dialog.findViewById(R.id.listView_genre);
                listView_genre.setAdapter(new CustomListviewGenre(EditListingActivity.this,genre));
                dialog.show();

                Button button_spiner_genre = (Button)dialog.findViewById(R.id.button_spiner_genre);
                button_spiner_genre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ImageView img_close_dialoggenre = (ImageView)dialog.findViewById(R.id.img_close_dialoggenre);
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
                final Dialog dialog = new Dialog(EditListingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_genre);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ListView listView_genre=(ListView)dialog.findViewById(R.id.listView_genre);
                listView_genre.setAdapter(new CustomListviewGenre(EditListingActivity.this,genre));
                dialog.show();

                Button button_spiner_genre = (Button)dialog.findViewById(R.id.button_spiner_genre);
                button_spiner_genre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                ImageView img_close_dialoggenre = (ImageView)dialog.findViewById(R.id.img_close_dialoggenre);
                img_close_dialoggenre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
        //end

        //--------------------------------------------------
//        View view_bottom = (View) findViewById(R.id.menu_bottom_myprofile);
//        menu_bottom=new MenuBottomCustom(view_bottom,this,0);
//        menu_bottom.setDefaut(0);
        //---------------------------------------------------------------
    }

    public void btnDelete(){
        Button btn_menu_editlist_delete = (Button)findViewById(R.id.btn_menu_editlist_delete);
        btn_menu_editlist_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EditListingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete_editlisting);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location location;
        Boolean isGPSEnabled=false;
        Boolean isNetworkEnabled = false;
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(EditListingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(EditListingActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        isNetworkEnabled = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        System.out.print("GPS:"+isGPSEnabled);
        System.out.print("net:"+isNetworkEnabled);
        if(isGPSEnabled){
            location = service
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                addMaker(location);
            }

        }
        if(isNetworkEnabled){
            location = service
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                addMaker(location);
            }
        }
    }
    public Bitmap resizeMapIcons(int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("icon_swap", "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
    public void addMaker(Location location){
        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
        // Changing marker icon
        marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(80,120)));
        // adding marker
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 8));
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setTrafficEnabled(true);
    }
}
