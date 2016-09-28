package redix.booxtown.fragment;

import android.Manifest;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

import redix.booxtown.R;
import redix.booxtown.controller.ResizeImage;
import redix.booxtown.custom.MenuBottomCustom;

public class SettingFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback{

        TextView besttime1, besttime2;
        private SupportMapFragment mMapFragment;
        Marker mMarker;
        private boolean flag = false;
        Geocoder geocoder;
        TextView txt_setting_besttime;
        SupportMapFragment mapFragment;
        GoogleMap mMap;
        MapView mMapView;
        private GoogleMap Map;
        Location location;
        /**
         * ATTENTION: This was auto-generated to implement the App Indexing API.
         * See https://g.co/AppIndexing/AndroidStudio for more information.
         */
        private GoogleApiClient client;
        EditText editText_change_old,editText_change_new,editText_change_re;
        Button button_change_pass;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.setting_fragment, container, false);
            txt_setting_besttime= (TextView)view.findViewById(R.id.txt_setting_besttime);
            ImageView imv_setting_pass = (ImageView)view.findViewById(R.id.imv_setting_editpass);
            imv_setting_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_custom_editpass);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    editText_change_old = (EditText)dialog.findViewById(R.id.editText_change_old);
                    editText_change_new = (EditText)dialog.findViewById(R.id.editText_change_new);
                    editText_change_old = (EditText)dialog.findViewById(R.id.editText_change_old);
                    button_change_pass = (Button)dialog.findViewById(R.id.button_change_pass);
                    button_change_pass.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    ImageView imageView = (ImageView) dialog.findViewById(R.id.imv_close_dialog_changepass);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });

            Switch switch_setting_editpass = (Switch) view.findViewById(R.id.switch_setting_besttime);
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_custom_seting_besttime);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            switch_setting_editpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b == false) {
                        dialog.dismiss();
                    } else {

                        dialog.show();

                        //chinh thơi gian

                        besttime1 = (TextView) dialog.findViewById(R.id.txt_seting_besttime1);
                        besttime2 = (TextView) dialog.findViewById(R.id.txt_seting_besttime2);

                        besttime1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Calendar mcurrentTime = Calendar.getInstance();
                                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                int minute = mcurrentTime.get(Calendar.MINUTE);
                                TimePickerDialog mTimePicker;
                                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                        besttime1.setText(convertTime(i) + ":" + convertTime(i1) + " " + showTime(i, i1));
                                    }
                                }, hour, minute, true);
                                mTimePicker.setTitle("");
                                mTimePicker.show();
                            }
                        });

                        besttime2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Calendar mcurrentTime = Calendar.getInstance();
                                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                int minute = mcurrentTime.get(Calendar.MINUTE);
                                TimePickerDialog mTimePicker;
                                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                        besttime2.setText(convertTime(i) + ":" + convertTime(i1) + " " + showTime(i, i1));
                                    }
                                }, hour, minute, true);
                                mTimePicker.setTitle("");
                                mTimePicker.show();
                            }
                        });

                        ImageView imageView = (ImageView) dialog.findViewById(R.id.imv_close_dialog_submit);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                        Button btn_setingdialog_time =(Button)dialog.findViewById(R.id.btn_setingdialog_time);
                        btn_setingdialog_time.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txt_setting_besttime.setText(besttime1.getText() +"  -  "+ besttime2.getText());
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });

            //map view
            mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.fragment);
            mapFragment.getMapAsync(this);
            //end
            Switch switch_location = (Switch)view. findViewById(R.id.switch_seting_location);
            mMapFragment = ((SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.fragment));
            mMapFragment.getMapAsync(this);
            switch_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b == false) {
                        getActivity().getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
                        getActivity().getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                    } else {
                        getActivity().getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();
                        getActivity().getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
                    }
                }
            });
            return view;
        }

        public String showTime(int hour, int min) {
            String format;
            if (hour == 0) {
                hour += 12;
                format = "AM";
            } else if (hour == 12) {
                format = "PM";
            } else if (hour > 12) {
                hour -= 12;
                format = "PM";
            } else {
                format = "AM";
            }
            return format;
        }

        public String convertTime(int hour) {
            if (hour < 10) {
                return "0" + hour;
            } else {
                return hour + "";
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Location location;
            Boolean isGPSEnabled=false;
            Boolean isNetworkEnabled = false;
            mMap = googleMap;
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return  ;
                }
                LocationManager service = (LocationManager)getActivity().getSystemService(getContext().LOCATION_SERVICE);
                // getting GPS status
                isGPSEnabled = service
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                isNetworkEnabled = service
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                System.out.print("GPS:"+isGPSEnabled);
                Toast.makeText(getActivity(),"ha"+isGPSEnabled +"net"+isNetworkEnabled,Toast.LENGTH_LONG).show();
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
        public void addMaker(Location location){
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
            // Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromBitmap(ResizeImage.resizeMapIcons(getContext(),"icon_buy",110, 150)));
            // adding marker
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 8));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setTrafficEnabled(true);
        }


    }

