package com.booxtown.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.R;
import com.booxtown.controller.IconMapController;
import com.booxtown.controller.ResizeImage;
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

public class LocationListingDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    MarkerOptions marker;
    private GoogleMap mMap;
    Book book;
    private ImageView imageView_back;
    TextView txt_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listingdetail_location);

        book = (Book) getIntent().getSerializableExtra("item");

        txt_title=(TextView) findViewById(R.id.txt_title);
        txt_title.setText("Location");
        imageView_back = (ImageView) findViewById(R.id.img_menu);
        Bitmap btm = BitmapFactory.decodeResource(getResources(),R.drawable.btn_sign_in_back);
        imageView_back.setImageBitmap(btm);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(book.getLocation_latitude(), book.getLocation_longitude()), 9));
            addMarker(book);
        } catch (Exception e) {
        }
    }
    public void addMarker(final Book books) {
        try {
            marker = new MarkerOptions().position(new LatLng(books.getLocation_latitude(), books.getLocation_longitude())).title(books.getTitle());
            // Changing marker icon
            char array[] = books.getAction().toCharArray();
            String swap = String.valueOf(array[0]);
            String buy = String.valueOf(array[2]);
            String free = String.valueOf(array[1]);
            String icon = IconMapController.icon(swap, free, buy);
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
