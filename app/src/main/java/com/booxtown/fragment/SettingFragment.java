package com.booxtown.fragment;

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

import com.booxtown.activity.SignIn_Activity;
import com.booxtown.controller.CheckSession;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import com.booxtown.R;
import com.booxtown.activity.MainAllActivity;
import com.booxtown.activity.MenuActivity;
import com.booxtown.controller.Information;
import com.booxtown.controller.ResizeImage;
import com.booxtown.controller.SettingController;
import com.booxtown.controller.UserController;
import com.booxtown.model.Setting;

public class SettingFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, View.OnClickListener {

    TextView besttime1, besttime2;
    public static String time1 = "", time2 = "";
    private SupportMapFragment mMapFragment;
    TextView txt_setting_besttime;
    GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    EditText editText_change_old, editText_change_new, editText_change_re;
    Button button_change_pass;
    Switch switch_setting_noti, switch_seting_location, switch_setting_besttime;
    private Dialog dialogtime;
    public int count = 0;
    public static int id_setting = 0, is_notification = 0, is_best_time = 0, is_current_location = 0;

    Setting setting_old;
    Setting setting_new;

    //menu bottom
    ImageView img_menu_bottom_location;
    ImageView img_menu_bottom_comment;
    ImageView img_menu_bottom_camera;
    ImageView img_menu_bottom_bag;
    ImageView img_menu_bottom_user;
    String session_id;
    TextView txtFindLocation;
    TextView textView18;
    //end
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        txt_setting_besttime = (TextView) view.findViewById(R.id.txt_setting_besttime);
        switch_setting_besttime = (Switch) view.findViewById(R.id.switch_setting_besttime);
        switch_setting_noti = (Switch) view.findViewById(R.id.switch_setting_noti);
        switch_seting_location = (Switch) view.findViewById(R.id.switch_seting_location);
        textView18=(TextView) view.findViewById(R.id.textView18);
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        session_id = pref.getString("session_id", null);

        getSetting setting = new getSetting(getContext());
        setting.execute(session_id);

        ImageView img_menu = (ImageView) getActivity().findViewById(R.id.img_menu);
        Picasso.with(getContext()).load(R.drawable.btn_menu_locate).into(img_menu);

        txtFindLocation = (TextView) view.findViewById(R.id.find_location);

        //switch notìication
        switch_setting_noti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    is_notification = 1;
                } else {
                    is_notification = 0;
                }
            }
        });
        //end

        ImageView imv_setting_pass = (ImageView) view.findViewById(R.id.imv_setting_editpass);
        textView18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_custom_editpass);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                editText_change_old = (EditText) dialog.findViewById(R.id.editText_change_old);
                editText_change_new = (EditText) dialog.findViewById(R.id.editText_change_new);
                editText_change_re = (EditText) dialog.findViewById(R.id.editText_change_re);
                button_change_pass = (Button) dialog.findViewById(R.id.button_change_pass);

                button_change_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText_change_new.getText().toString().equals(editText_change_re.getText().toString())) {
                            changePass pass = new changePass(getContext());
                            pass.execute(session_id, editText_change_old.getText().toString(),
                                    editText_change_new.getText().toString());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), Information.noti_match_pass, Toast.LENGTH_SHORT).show();
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
        imv_setting_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_custom_editpass);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                editText_change_old = (EditText) dialog.findViewById(R.id.editText_change_old);
                editText_change_new = (EditText) dialog.findViewById(R.id.editText_change_new);
                editText_change_re = (EditText) dialog.findViewById(R.id.editText_change_re);
                button_change_pass = (Button) dialog.findViewById(R.id.button_change_pass);

                button_change_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText_change_new.getText().toString().equals(editText_change_re.getText().toString())) {
                            changePass pass = new changePass(getContext());
                            pass.execute(session_id, editText_change_old.getText().toString(),
                                    editText_change_new.getText().toString());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), Information.noti_match_pass, Toast.LENGTH_SHORT).show();
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
                try {
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
                            if (!time1.equals("")) {

                                String[] time1Tmp = time1.split(":");
                                if (Integer.parseInt(time1Tmp[0]) < 12) {
                                    besttime1.setText(time1Tmp[0] + ":" + time1Tmp[1] + " AM");
                                } else {
                                    besttime1.setText(time1Tmp[0] + ":" + time1Tmp[1] + " PM");
                                }


                            }
                            if (!time2.equals("")) {

                                String[] time2Tmp = time2.split(":");
                                if (Integer.parseInt(time2Tmp[0]) < 12) {
                                    besttime2.setText(time2Tmp[0] + ":" + time2Tmp[1] + " AM");
                                } else {
                                    besttime2.setText(time2Tmp[0] + ":" + time2Tmp[1] + " PM");
                                }

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
                                            time1 = convertTime(i) + ":" + convertTime(i1) + " " + showTime(i, i1);
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
                                            time2 = convertTime(i) + ":" + convertTime(i1) + " " + showTime(i, i1);
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
                } catch (Exception ex) {
                    String sss= ex.getMessage();
                }
            }
        });

        //end
        mMapFragment = ((SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment));
        mMapFragment.getMapAsync(this);
        switch_seting_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    getActivity().getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
                    is_current_location = 0;
                    txtFindLocation.setVisibility(View.GONE);
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();
                    is_current_location = 1;
                }
            }
        });

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setting_new = new Setting(is_notification, is_best_time, is_current_location, time1, time2);
                    if (setting_old.getIs_notification() != setting_new.getIs_notification()
                            || setting_old.getIs_best_time() != setting_new.getIs_best_time()
                            || setting_old.getIs_current_location() != setting_new.getIs_current_location()
                            || !setting_old.getTime_start().equals(setting_new.getTime_start())
                            || !setting_old.getTime_to().equals(setting_new.getTime_to())) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage("Do you want to save setting ?");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        updateSetting update = new updateSetting(getContext(), session_id, id_setting, is_notification, is_best_time, is_current_location,
                                                time1, time2);
                                        update.execute();
                                        getSetting setting = new getSetting(getContext());
                                        setting.execute(session_id);
                                        dialog.cancel();
                                    }
                                });
                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    } else {
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                }
            }
        });

        //menu bottom

        img_menu_bottom_location = (ImageView) getActivity().findViewById(R.id.img_menu_bottom_location);
        img_menu_bottom_comment = (ImageView) getActivity().findViewById(R.id.img_menu_bottom_comment);
        img_menu_bottom_camera = (ImageView) getActivity().findViewById(R.id.img_menu_bottom_camera);
        img_menu_bottom_bag = (ImageView) getActivity().findViewById(R.id.img_menu_bottom_bag);
        img_menu_bottom_user = (ImageView) getActivity().findViewById(R.id.img_menu_bottom_user);

        img_menu_bottom_location.setOnClickListener(this);
        img_menu_bottom_comment.setOnClickListener(this);
        img_menu_bottom_camera.setOnClickListener(this);
        img_menu_bottom_bag.setOnClickListener(this);
        img_menu_bottom_user.setOnClickListener(this);
        //end
        return view;
    }

    public void saveSetting(Setting old, Setting new_setting, final String type) {
        if (old.getIs_notification() != new_setting.getIs_notification()
                || old.getIs_best_time() != new_setting.getIs_best_time()
                || old.getIs_current_location() != new_setting.getIs_current_location()
                || !old.getTime_start().equals(new_setting.getTime_start())
                || !old.getTime_to().equals(new_setting.getTime_to())) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Do you want to save setting ?");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            updateSetting update = new updateSetting(getContext(), session_id, id_setting, is_notification, is_best_time, is_current_location,
                                    time1, time2);
                            update.execute();
                            getSetting setting = new getSetting(getContext());
                            setting.execute(session_id);
                            dialog.cancel();
                        }
                    });
            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent1 = new Intent(getActivity(), MainAllActivity.class);
                            intent1.putExtra("key", type);
                            startActivity(intent1);
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            Intent intent1 = new Intent(getActivity(), MainAllActivity.class);
            intent1.putExtra("key", type);
            startActivity(intent1);
        }
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
        try {
            Location location;
            Boolean isGPSEnabled;
            Boolean isNetworkEnabled;
            mMap = googleMap;
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //        GPSTracker gpsTracker = new GPSTracker(getContext());
            //        addMaker(gpsTracker.getLocation());
            LocationManager service = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = service
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = service
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled) {
                location = service
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    addMaker(location);
                }

            }
            if (isNetworkEnabled) {
                location = service
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    addMaker(location);
                }
            }
        } catch (Exception e) {
        }
    }

    public void addMaker(Location location) {
        try {
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
            // Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromBitmap(ResizeImage.resizeMapIcons(getContext(), "location_default",(int)getResources().getDimension(R.dimen.width_pin),
                    (int)getResources().getDimension(R.dimen.height_pin))));
            // adding marker
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 8));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setTrafficEnabled(true);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu_bottom_location:
                setting_new = new Setting(is_notification, is_best_time, is_current_location, time1, time2);
                saveSetting(setting_old, setting_new, "1");
                break;
            case R.id.img_menu_bottom_comment:
                setting_new = new Setting(is_notification, is_best_time, is_current_location, time1, time2);
                saveSetting(setting_old, setting_new, "2");
                break;
            case R.id.img_menu_bottom_camera:
                setting_new = new Setting(is_notification, is_best_time, is_current_location, time1, time2);
                saveSetting(setting_old, setting_new, "3");
                break;
            case R.id.img_menu_bottom_bag:
                setting_new = new Setting(is_notification, is_best_time, is_current_location, time1, time2);
                saveSetting(setting_old, setting_new, "4");
                break;
            case R.id.img_menu_bottom_user:
                setting_new = new Setting(is_notification, is_best_time, is_current_location, time1, time2);
                saveSetting(setting_old, setting_new, "5");
                break;

        }
    }

    class changePass extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        Context context;

        public changePass(Context context) {
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
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            UserController userController = new UserController(context);
            return userController.changePassword(strings[0], strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true) {
                    Toast.makeText(context, Information.noti_change_pass, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, Information.noti_nochange_pass, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

            }
            dialog.dismiss();
        }
    }

    class getSetting extends AsyncTask<String, Void, List<Setting>> {

        Context context;
        ProgressDialog progressDialog;

        public getSetting(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(Information.noti_dialog);
            progressDialog.show();
        }

        @Override
        protected List<Setting> doInBackground(String... strings) {
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            SettingController settingController = new SettingController();
            return settingController.getSettingByUserId(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Setting> settings) {
            try {
                if (settings.size() > 0) {
                    if (settings.get(0).getIs_notification() == 1) {
                        switch_setting_noti.setChecked(true);
                    } else {
                        switch_setting_noti.setChecked(false);
                    }
                    if (settings.get(0).getIs_current_location() == 1) {
                        switch_seting_location.setChecked(true);
                        getActivity().getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
                    } else {
                        switch_seting_location.setChecked(false);
                        getActivity().getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();

                    }

                    if (settings.get(0).getIs_best_time() == 1) {
                        switch_setting_besttime.setChecked(true);


                        String[] time1Tmp = settings.get(0).getTime_start().split(":");
                        String time_start = "";
                        String time_end = "";
                        if (Integer.parseInt(time1Tmp[0]) < 12) {
                            time_start = time1Tmp[0] + ":" + time1Tmp[1] + " AM";
                        } else {
                            time_start = time1Tmp[0] + ":" + time1Tmp[1] + " PM";
                        }

                        String[] time2Tmp = settings.get(0).getTime_to().split(":");
                        if (Integer.parseInt(time2Tmp[0]) < 12) {
                            time_end = time2Tmp[0] + ":" + time2Tmp[1] + " AM";
                        } else {
                            time_end = time2Tmp[0] + ":" + time2Tmp[1] + " PM";
                        }
                        txt_setting_besttime.setText(time_start + " - " + time_end);
                        time1 = settings.get(0).getTime_start();
                        time2 = settings.get(0).getTime_to();

                    } else {
                        switch_setting_besttime.setChecked(false);
                        count=1;
                        time1 = "";
                        time2 = "";
                    }
                    id_setting = settings.get(0).getId();
                    is_notification = settings.get(0).getIs_notification();
                    is_current_location = settings.get(0).getIs_current_location();
                    if (is_current_location == 0) {
                        txtFindLocation.setVisibility(View.VISIBLE);
                    }
                    is_best_time = settings.get(0).getIs_best_time();
                    setting_old = new Setting(is_notification, is_best_time, is_current_location, time1, time2);
                } else {
                    Toast.makeText(context, Information.noti_no_data, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
            progressDialog.dismiss();
        }
    }

    class updateSetting extends AsyncTask<String, Void, Boolean> {

        Context context;
        ProgressDialog progressDialog;
        String session_id;
        int id_setting, is_notification, is_best_time, is_current_location;
        String time_start, time_to;

        public updateSetting(Context context, String session_id, int id_setting, int is_notification, int is_best_time, int is_current_location, String time_start, String time_to) {
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
            CheckSession checkSession = new CheckSession();
            SharedPreferences pref = context.getSharedPreferences("MyPref",context.MODE_PRIVATE);
            boolean check = checkSession.checkSession_id(pref.getString("session_id", null));
            if(!check){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("session_id",null);
                editor.commit();
                Intent intent = new Intent(context, SignIn_Activity.class);
                context.startActivity(intent);
                this.cancel(true);
            }
            SettingController settingController = new SettingController();
            return settingController.updateSetting(session_id, id_setting, is_notification, is_best_time, is_current_location, time_start, time_to);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if (aBoolean == true) {
                    Toast.makeText(context, Information.noti_update_setting, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, Information.noti_update_nosetting, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
            progressDialog.dismiss();
        }
    }
}

