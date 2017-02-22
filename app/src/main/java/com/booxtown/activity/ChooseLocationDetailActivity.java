package com.booxtown.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.R;
import com.booxtown.controller.GPSTracker;
import com.booxtown.controller.IconMapController;
import com.booxtown.model.Book;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Administrator on 10/01/2017.
 */

public class ChooseLocationDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    MarkerOptions marker;
    private GoogleMap mMap;
    private ImageView imageView_back;
    TextView txt_title;
    LatLng latLng_new;
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listingdetail_location);
        gpsTracker = new GPSTracker(ChooseLocationDetailActivity.this);
        txt_title=(TextView) findViewById(R.id.txt_title);
        txt_title.setText("Location");
        imageView_back = (ImageView) findViewById(R.id.img_menu);
        Bitmap btm = BitmapFactory.decodeResource(getResources(),R.drawable.btn_sign_in_back);
        imageView_back.setImageBitmap(btm);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("lat",latLng_new.latitude+"");
                returnIntent.putExtra("longti",latLng_new.longitude+"");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail_location);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            latLng_new = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_new, 11));

            addMarker(latLng_new);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mMap.clear();
                    latLng_new=latLng;
                    addMarker(latLng_new);
                }
            });
        } catch (Exception e) {
        }
    }

    public void addMarker(LatLng latLng) {
        try {
            marker = new MarkerOptions().position(latLng).title("Hello");
            String icon = IconMapController.icon("0", "0", "0");
            if (icon != null) {
                marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(icon,(int)getResources().getDimension(R.dimen.width_pin),
                        (int)getResources().getDimension(R.dimen.height_pin))));
            }
            mMap.addMarker(marker);
        } catch (Exception e) {

        }
    }
    public Bitmap resizeMapIcons(String icon, int width, int height) {
        Bitmap imageBitmap;
        Bitmap resizedBitmap;
        try {
            imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(icon, "drawable", getPackageName()));
            resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
            return resizedBitmap;
        } catch (Exception e) {
        }
        return null;
    }


}
