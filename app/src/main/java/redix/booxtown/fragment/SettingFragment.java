package redix.booxtown.fragment;

import android.Manifest;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.AlertDialog;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.MenuActivity;
import redix.booxtown.controller.GPSTracker;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.ResizeImage;
import redix.booxtown.controller.SettingController;
import redix.booxtown.controller.UserController;
import redix.booxtown.custom.MenuBottomCustom;
import redix.booxtown.model.Book;
import redix.booxtown.model.Setting;

public class SettingFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback{

    TextView besttime1, besttime2;
    public static String time1="",time2="";
    private SupportMapFragment mMapFragment;
    TextView txt_setting_besttime;
    GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    EditText editText_change_old,editText_change_new,editText_change_re;
    Button button_change_pass;
    Switch switch_setting_noti,switch_seting_location,switch_setting_besttime;
    private Dialog dialogtime;
    public int count = 0;
    public static int id_setting = 0,is_notification = 0,is_best_time = 0,is_current_location=0;
    Setting setting_old;
    Setting setting_new;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        txt_setting_besttime= (TextView)view.findViewById(R.id.txt_setting_besttime);
        switch_setting_besttime = (Switch) view.findViewById(R.id.switch_setting_besttime);
        switch_setting_noti = (Switch) view.findViewById(R.id.switch_setting_noti);
        switch_seting_location = (Switch) view.findViewById(R.id.switch_seting_location);

        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final String session_id = pref.getString("session_id", null);

        getSetting setting = new getSetting(getContext());
        setting.execute(session_id);

        ImageView img_menu = (ImageView)getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(img_menu);

        //switch notìication
        switch_setting_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    is_notification =1;
                }else{
                    is_notification = 0;
                }
            }
        });


        //end

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
                editText_change_re = (EditText)dialog.findViewById(R.id.editText_change_re);
                button_change_pass = (Button)dialog.findViewById(R.id.button_change_pass);

                button_change_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText_change_new.getText().toString().equals(editText_change_re.getText().toString())){
                            changePass pass = new changePass(getContext());
                            pass.execute(session_id,editText_change_old.getText().toString(),
                                    editText_change_new.getText().toString());
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getContext(),Information.noti_match_pass,Toast.LENGTH_SHORT).show();
                        }
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

        dialogtime = new Dialog(getActivity());
        dialogtime.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogtime.setContentView(R.layout.dialog_custom_seting_besttime);
        dialogtime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        switch_setting_besttime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == false) {
                    dialogtime.dismiss();
                    txt_setting_besttime.setVisibility(View.GONE);
                    is_best_time = 0;
                } else {
                    is_best_time = 1;
                    count += 1;
                    if (count > 1) {
                        dialogtime.show();
                        txt_setting_besttime.setVisibility(View.VISIBLE);
                        //chinh thơi gian
                        besttime1 = (TextView) dialogtime.findViewById(R.id.txt_seting_besttime1);
                        besttime2 = (TextView) dialogtime.findViewById(R.id.txt_seting_besttime2);
                        if(time1 != null){
                            besttime1.setText(time1);
                        }
                        if(time2 != null){
                            besttime2.setText(time2);
                        }
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
                        ImageView imageView = (ImageView) dialogtime.findViewById(R.id.imv_close_dialog_submit);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogtime.cancel();
                            }
                        });
                        Button btn_setingdialog_time = (Button) dialogtime.findViewById(R.id.btn_setingdialog_time);
                        btn_setingdialog_time.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                txt_setting_besttime.setText(besttime1.getText() + "  -  " + besttime2.getText());
                                dialogtime.dismiss();
                            }
                        });
                    }
                }
            }
        });

        //end
        mMapFragment = ((SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.fragment));
        mMapFragment.getMapAsync(this);
        switch_seting_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == false) {
                    getActivity().getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
                    is_current_location = 0;
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();
                    is_current_location = 1;
                }
            }
        });

        setting_new = new Setting(is_notification,is_best_time,is_current_location,time1,time2);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setting_old.equals(setting_new)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Do you want to save setting ?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    updateSetting update = new updateSetting(getContext(), session_id, id_setting, is_notification, is_best_time, is_current_location,
                                            besttime1.getText().toString(), besttime2.getText().toString());
                                    update.execute();
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else {
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    startActivity(intent);
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
        Boolean isGPSEnabled;
        Boolean isNetworkEnabled;
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        GPSTracker gpsTracker = new GPSTracker(getContext());
        addMaker(gpsTracker.getLocation());
//        LocationManager service = (LocationManager)getActivity().getSystemService(getContext().LOCATION_SERVICE);
//        // getting GPS status
//        isGPSEnabled = service
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//        isNetworkEnabled = service
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if(isGPSEnabled){
//            location = service
//                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                addMaker(location);
//            }
//
//        }
//        if(isNetworkEnabled){
//            location = service
//                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (location != null) {
//                addMaker(location);
//            }
//        }
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

    class changePass extends AsyncTask<String,Void,Boolean>{
        ProgressDialog dialog;
        Context context;

        public changePass(Context context){
            this.context = context;
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
        protected Boolean doInBackground(String... strings) {
            UserController userController = new UserController();
            return userController.changePassword(strings[0],strings[1],strings[2]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true){
                    Toast.makeText(context,Information.noti_change_pass,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,Information.noti_nochange_pass,Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
            dialog.dismiss();
        }
    }

    class getSetting extends AsyncTask<String,Void,List<Setting>>{

        Context context;
        ProgressDialog progressDialog;
        public getSetting(Context context){
            this.context=context;
        }
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<Setting> doInBackground(String... strings) {
            SettingController settingController = new SettingController();
            return settingController.getSettingByUserId(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Setting> settings) {
            try {
                if (settings.size()>0){
                    if(settings.get(0).getIs_notification() == 1){
                        switch_setting_noti.setChecked(true);
                    }else {
                        switch_setting_noti.setChecked(false);
                    }
                    if(settings.get(0).getIs_current_location() == 1){
                        switch_seting_location.setChecked(true);
                        getActivity().getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();
                    }else {
                        switch_seting_location.setChecked(false);
                        getActivity().getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
                    }

                    if (settings.get(0).getIs_best_time() == 1){
                        switch_setting_besttime.setChecked(true);
                        txt_setting_besttime.setText(settings.get(0).getTime_start() +" - "+ settings.get(0).getTime_to());
                        time1 = settings.get(0).getTime_start();
                        time2 = settings.get(0).getTime_to();
                    }else {
                        switch_setting_besttime.setChecked(false);
                        time1 = "";
                        time2 = "";
                    }
                    id_setting = settings.get(0).getId();
                    is_notification = settings.get(0).getIs_notification();
                    is_current_location = settings.get(0).getIs_current_location();
                    is_best_time = settings.get(0).getIs_best_time();
                    setting_old=new Setting(is_notification,is_best_time,is_current_location,time1,time2);
                }else {
                    Toast.makeText(context,Information.noti_no_data,Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
            }
            progressDialog.dismiss();
        }
    }

    class updateSetting extends AsyncTask<String,Void,Boolean>{

        Context context;
        ProgressDialog progressDialog;
        String session_id;
        int id_setting,is_notification,is_best_time,is_current_location;
        String time_start,time_to;

        public updateSetting(Context context,String session_id, int id_setting, int is_notification, int is_best_time, int is_current_location, String time_start, String time_to) {
            this.context = context;
            this.session_id = session_id;
            this.id_setting = id_setting;
            this.is_notification = is_notification;
            this.is_best_time = is_best_time;
            this.is_current_location = is_current_location;
            this.time_start = time_start;
            this.time_to = time_to;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            SettingController settingController = new SettingController();
            return settingController.updateSetting(session_id,id_setting,is_notification,is_best_time,is_current_location,time_start,time_to);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true){
                    Toast.makeText(context,Information.noti_update_setting,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,Information.noti_update_nosetting,Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
            }
            progressDialog.dismiss();
        }
    }
}

