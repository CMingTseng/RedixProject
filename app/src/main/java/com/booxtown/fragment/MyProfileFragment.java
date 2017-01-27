package com.booxtown.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.activity.MainAllActivity;
import com.booxtown.activity.MenuActivity;
import com.booxtown.activity.SignIn_Activity;
import com.booxtown.adapter.ListBookAdapter;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.BookController;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.GPSTracker;
import com.booxtown.controller.Information;
import com.booxtown.controller.UploadFileController;
import com.booxtown.controller.UserController;
import com.booxtown.controller.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.booxtown.R;

import com.booxtown.custom.CustomTabbarExplore;
import com.booxtown.model.Book;
import com.booxtown.model.User;

public class MyProfileFragment extends Fragment {
    private LinearLayout linear_all;
    private LinearLayout linear_swap;
    private LinearLayout linear_free;
    private LinearLayout linear_cart;
    List<Book> listEx = new ArrayList<>();
    GridView grid;
    ListBookAdapter adapter;
    CircularImageView imv_menu_profile;
    EditText txt_profile_phone, txt_profile_email;
    TextView txt_profile_username, txt_profile_birthday;
    String username, first_name, last_name;
    int user_id;
    TextView tab_all_count, tab_swap_count, tab_free_count, tab_cart_count;
    RatingBar ratingBar_userprofile;
    RecyclerView rView;
    GridLayoutManager gridLayoutManager;

    int PICK_IMAGE_MULTIPLE = 1;
    private int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap_profile;
    ImageView imageView_update_profile;
    ImageView img_rank1, img_rank2, img_rank3;
    String img_photo;
    String photoOrigin = "";
    boolean flagEdit = false;
    boolean dataChange = false;
    TextView myDashboard;
    UploadFileController uploadFileController;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;

    float longitude = 0;
    float latitude = 0;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    ImageView img_menu_personal_dashboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.my_profile_fragment, container, false);
        uploadFileController = new UploadFileController();

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int is_current_location = pref.getInt("is_current_location", 1);
        if (is_current_location == 1) {
            longitude = (float) new GPSTracker(getActivity()).getLongitude();
            latitude = (float) new GPSTracker(getActivity()).getLatitude();
        } else {
            longitude = Float.parseFloat(pref.getString("Longitude", (float) new GPSTracker(getActivity()).getLongitude() + ""));
            latitude = Float.parseFloat(pref.getString("Latitude", (float) new GPSTracker(getActivity()).getLatitude() + ""));
        }

        img_menu_personal_dashboard = (ImageView) view.findViewById(R.id.img_menu_personal_dashboard);
        imv_menu_profile = (CircularImageView) view.findViewById(R.id.imv_menu_profile);
        imv_menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        ImageView imageView_back = (ImageView) getActivity().findViewById(R.id.img_menu);
        Glide.with(getActivity()).load(R.drawable.btn_menu_locate).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView_back);
        TextView txt_title = (TextView) getActivity().findViewById(R.id.txt_title);
        txt_title.setText("My Profile");

        final String session_id = pref.getString("session_id", null);
        Profile profile = new Profile(getContext(), session_id);
        profile.execute();
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rView = (RecyclerView) view.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);

        //profile
        img_rank1 = (ImageView) view.findViewById(R.id.img_rank1);
        img_rank2 = (ImageView) view.findViewById(R.id.img_rank2);
        img_rank3 = (ImageView) view.findViewById(R.id.img_rank3);
        txt_profile_email = (EditText) view.findViewById(R.id.txt_profile_email);
        txt_profile_phone = (EditText) view.findViewById(R.id.txt_profile_phone);
        txt_profile_birthday = (TextView) view.findViewById(R.id.txt_profile_birthday);
        txt_profile_username = (TextView) view.findViewById(R.id.txt_profile_username);
        ratingBar_userprofile = (RatingBar) view.findViewById(R.id.ratingBar_userprofile);
        imageView_update_profile = (ImageView) view.findViewById(R.id.imageView_update_profile);
        myDashboard = (TextView) view.findViewById(R.id.textView87);
        //end


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });

//        grid=(GridView)view.findViewById(R.id.grid_myprofile);
        rView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        View view_tab = (View) view.findViewById(R.id.tab_myprofile);
        final CustomTabbarExplore tab_custom = new CustomTabbarExplore(view_tab, getActivity());
        linear_all = (LinearLayout) view_tab.findViewById(R.id.linear_all);
        linear_swap = (LinearLayout) view_tab.findViewById(R.id.linear_swap);
        linear_free = (LinearLayout) view_tab.findViewById(R.id.linear_free);
        linear_cart = (LinearLayout) view_tab.findViewById(R.id.linear_cart);

        tab_all_count = (TextView) view_tab.findViewById(R.id.tab_all_count);
        tab_swap_count = (TextView) view_tab.findViewById(R.id.tab_swap_count);
        tab_free_count = (TextView) view_tab.findViewById(R.id.tab_free_count);
        tab_cart_count = (TextView) view_tab.findViewById(R.id.tab_cart_count);

        linear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBookAdapter adapter = new ListBookAdapter(getActivity(), filterBook(1), 1, 1, 1);
//                grid=(GridView)view.findViewById(R.id.grid_myprofile);
                rView.setAdapter(adapter);
                tab_custom.setDefault(1);
            }
        });

        linear_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBookAdapter adapter = new ListBookAdapter(getActivity(), filterBook(2), 1, 1, 1);
//                grid=(GridView)view.findViewById(R.id.grid_myprofile);
                rView.setAdapter(adapter);
                tab_custom.setDefault(2);
            }
        });

        linear_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBookAdapter adapter = new ListBookAdapter(getActivity(), filterBook(3), 1, 1, 1);
//                grid=(GridView)view.findViewById(R.id.grid_myprofile);
                rView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                tab_custom.setDefault(3);
            }
        });

        linear_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ListBookAdapter adapter = new ListBookAdapter(getActivity(), filterBook(4), 1, 1, 1);
//                grid=(GridView)view.findViewById(R.id.grid_myprofile);
                rView.setAdapter(adapter);
                tab_custom.setDefault(4);
            }
        });
        Picasso.with(getContext()).load(R.drawable.btn_edit_profile).into(imageView_update_profile);
        //edit profile
        txt_profile_email.setEnabled(false);
        /*txt_profile_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Picasso.with(getContext()).load(R.drawable.ic_update_profile).into(imageView_update_profile);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });*/
        if (!flagEdit) {
            txt_profile_phone.setEnabled(false);
            txt_profile_birthday.setEnabled(false);
        }
        txt_profile_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (flagEdit) {
                    dataChange = true;
                    Picasso.with(getContext()).load(R.drawable.ic_update_profile).into(imageView_update_profile);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txt_profile_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagEdit) {
                    dataChange = true;
                    Picasso.with(getContext()).load(R.drawable.ic_update_profile).into(imageView_update_profile);
                    DialogFragment dialogfragment = new DatePickerDialogClass();
                    dialogfragment.show(getActivity().getFragmentManager(), "Date Time");
                }
            }
        });
        //end
        imageView_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flagEdit) {
                    Picasso.with(getContext()).load(R.drawable.ic_update_profile).into(imageView_update_profile);
                    txt_profile_phone.setEnabled(true);
                    txt_profile_birthday.setEnabled(true);
                    flagEdit = true;
                } else {

                    txt_profile_phone.setEnabled(false);
                    txt_profile_birthday.setEnabled(false);
                    flagEdit = false;
                    Picasso.with(getContext()).load(R.drawable.btn_edit_profile).into(imageView_update_profile);
                    try {
                        if (dataChange) {
                            ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                            bitmaps.add(bitmap_profile);
                            List<String> filename = new ArrayList<String>();
                            filename.add(username + "_+_" + img_photo);
                            if (img_photo != null) {
                                addImages(bitmaps, filename);

                                String[] birthDay = txt_profile_birthday.getText().toString().split("/");
                                String birthOfDay = birthDay[2] + "-" + birthDay[1] + "-" + birthDay[0] + " 00:00:00";

                                updateProfile updateProfile = new updateProfile(getContext(), session_id, txt_profile_email.getText().toString(),
                                        txt_profile_phone.getText().toString(), birthOfDay, username + "_+_" + img_photo, first_name, last_name);
                                updateProfile.execute();
                            } else {
                                String[] birthDay = txt_profile_birthday.getText().toString().split("/");
                                String birthOfDay = birthDay[2] + "-" + birthDay[1] + "-" + birthDay[0] + " 00:00:00";
                                updateProfile updateProfile = new updateProfile(getContext(), session_id, txt_profile_email.getText().toString(),
                                        txt_profile_phone.getText().toString(), birthOfDay, photoOrigin, first_name, last_name);
                                updateProfile.execute();
                            }
                        }
                    } catch (Exception e) {
                    }


                }
            }
        });
        MainAllActivity.getSaleInstance().setTxtTitle("My Profile");
        return view;
    }

    public boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    //------------------------------------------------------


    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return datepickerdialog;

        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView textview = (TextView) getActivity().findViewById(R.id.txt_profile_birthday);
            if (month < 10) {
                textview.setText(day + "/" + "0" + (month + 1) + "/" + year);
            } else {
                textview.setText(day + "/" + (month + 1) + "/" + year);
            }
        }
    }

    public List<Book> filterBook(int type) {
        List<Book> list = new ArrayList<>();
        if (type == 1) {
            list = listEx;
        } else if (type == 2) {
            for (int i = 0; i < listEx.size(); i++) {
                if (listEx.get(i).getAction().substring(0, 1).equals("1")) {
                    list.add(listEx.get(i));
                }
            }
        } else if (type == 3) {
            for (int i = 0; i < listEx.size(); i++) {
                if (listEx.get(i).getAction().substring(2, 3).equals("1")) {
                    list.add(listEx.get(i));
                }
            }
        } else {
            for (int i = 0; i < listEx.size(); i++) {
                if (listEx.get(i).getAction().substring(1, 2).equals("1")) {
                    list.add(listEx.get(i));
                }
            }
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

    class Profile extends AsyncTask<String, Void, List<User>> {
        Context context;
        String session_id;

        public Profile(Context context, String session_id) {
            this.context = context;
            this.session_id = session_id;
        }

        ProgressDialog dialog;

        @Override
        protected List<User> doInBackground(String... strings) {

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
            UserController userController = new UserController(context);
            List<User> profile = userController.getprofile(session_id);
            return profile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final List<User> userResult) {
            try {
                if (userResult.size() == 0) {
                    Toast.makeText(context, Information.noti_no_data, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                } else {
                    txt_profile_email.setText(userResult.get(0).getEmail());
                    txt_profile_phone.setText(userResult.get(0).getPhone());
                    String[] result = userResult.get(0).getBirthday().substring(0, 10).split("-");

                    txt_profile_birthday.setText(result[2] + "/" + result[1] + "/" + result[0]);
                    //txt_profile_username.setText(userResult.get(0).getUsername().substring(0,1).toUpperCase()+userResult.get(0).getUsername().substring(1,userResult.get(0).getUsername().length()));
                    txt_profile_username.setText(userResult.get(0).getFirst_name() + " " + userResult.get(0).getLast_name());
                    username = userResult.get(0).getUsername();
                    user_id = userResult.get(0).getUser_id();
                    first_name = userResult.get(0).getFirst_name();
                    last_name = userResult.get(0).getLast_name();
                    photoOrigin = userResult.get(0).getPhoto();
                    img_menu_personal_dashboard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MyProfileDashboardFragment profile = new MyProfileDashboardFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", userResult.get(0));
                            profile.setArguments(bundle);
                            callFragment(profile);

                        }
                    });
                    myDashboard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MyProfileDashboardFragment profile = new MyProfileDashboardFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", userResult.get(0));
                            profile.setArguments(bundle);
                            callFragment(profile);
                        }
                    });
                    //set rank
                    if (userResult.get(0).getContributor() == 0) {
                        img_rank1.setVisibility(View.VISIBLE);
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.conbitrutor_one);
                        img_rank1.setImageBitmap(btn1);

                    } else {
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.conbitrutor_two);
                        img_rank1.setImageBitmap(btn1);

                    }
                    if (userResult.get(0).getGoldenBook() == 0) {
                        img_rank2.setVisibility(View.GONE);
                    } else if (userResult.get(0).getGoldenBook() == 1) {
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.golden_book);
                        img_rank2.setImageBitmap(btn1);
                        img_rank2.setVisibility(View.VISIBLE);
                    }

                    if (userResult.get(0).getListBook() == 0) {
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.newbie);
                        img_rank3.setImageBitmap(btn1);
                        img_rank3.setVisibility(View.VISIBLE);
                    } else if (userResult.get(0).getListBook() == 1) {
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.bookworm);
                        img_rank3.setImageBitmap(btn1);
                        img_rank3.setVisibility(View.VISIBLE);
                    } else {
                        Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.bibliophile);
                        img_rank3.setImageBitmap(btn1);

                        img_rank3.setVisibility(View.VISIBLE);
                    }

                    //end
                    SharedPreferences pref2 = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref2.edit();
                    editor.putString("user_id", String.valueOf(user_id));
                    editor.commit();
                    int index = userResult.get(0).getPhoto().indexOf("_+_");
                    if (index > 3) {
                        Picasso.with(context)
                                .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + userResult.get(0).getPhoto().substring(0, index).trim() + "&image=" + userResult.get(0).getPhoto().substring(index + 3, userResult.get(0).getPhoto().length()))
                                .placeholder(R.mipmap.user_empty).into(imv_menu_profile);
                    } else {
                        Picasso.with(context)
                                .load(R.mipmap.user_empty)
                                .into(imv_menu_profile);
                    }
                    dialog.dismiss();

                    ratingBar_userprofile.setRating(userResult.get(0).getRating());
                    LayerDrawable stars = (LayerDrawable) ratingBar_userprofile.getProgressDrawable();

                    stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(2).setColorFilter(Color.rgb(249, 242, 0), PorterDuff.Mode.SRC_ATOP);
                    //stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
                    DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)), getResources().getColor(R.color.bg_rating));

                    //list book
                    listingAsync listingAsync = new listingAsync(getContext(), user_id);
                    listingAsync.execute(session_id);
                    //end
                }
                super.onPostExecute(userResult);
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }

    public void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Remove",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } else if (items[item].equals("Remove")) {
                    Remove();
                    Picasso.with(getContext()).load(R.drawable.ic_update_profile).into(imageView_update_profile);
                }

            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void Remove() {
        imv_menu_profile.setImageBitmap(null);
        imv_menu_profile.destroyDrawingCache();
        img_photo = "";
        bitmap_profile = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.mipmap.user_empty), 250, 270, true);
        Picasso.with(getActivity())
                .load(R.mipmap.user_empty)
                .into(imv_menu_profile);
    }

    public void addImages(ArrayList<Bitmap> bmap, List<String> listFileName) {
        uploadFileController.uploadFile(bmap, listFileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
                Picasso.with(getContext()).load(R.drawable.ic_update_profile).into(imageView_update_profile);
                flagEdit = true;
                dataChange=true;
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
                Picasso.with(getContext()).load(R.drawable.ic_update_profile).into(imageView_update_profile);
                flagEdit = true;
                dataChange=true;
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Uri uri = data.getData();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        int orientation = 0;
        if (thumbnail.getHeight() < thumbnail.getWidth()) {
            orientation = 90;
        } else {
            orientation = 0;
        }
        if (orientation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(),
                    thumbnail.getHeight(), matrix, true);
        } else
            thumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(),
                    thumbnail.getHeight(), true);
        bitmap_profile = Bitmap.createScaledBitmap(thumbnail, 250, 270, true);
        imv_menu_profile.setImageBitmap(bitmap_profile);
        long time = System.currentTimeMillis();
        img_photo = String.valueOf(time) + getFileName(uri);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = data.getData();
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                int orientation = 0;
                if (bm.getHeight() < bm.getWidth()) {
                    orientation = 90;
                } else {
                    orientation = 0;
                }
                if (orientation != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                            bm.getHeight(), matrix, true);
                } else
                    bm = Bitmap.createScaledBitmap(bm, bm.getWidth(),
                            bm.getHeight(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmap_profile = Bitmap.createScaledBitmap(bm, 250, 270, true);
        imv_menu_profile.setImageBitmap(bitmap_profile);
        long time = System.currentTimeMillis();
        img_photo = String.valueOf(time) + getFileName(uri);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    class listingAsync extends AsyncTask<String, Void, List<Book>> {

        Context context;
        ProgressDialog dialog;
        List<Book> listemp;
        int user_id;

        public listingAsync(Context context, int user_id) {
            this.context = context;
            this.user_id = user_id;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
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
            listemp = new ArrayList<>();
            BookController bookController = new BookController();
            //listemp = bookController.getAllBookById(context,strings[0]);
            listemp = bookController.getAllBookInApp(0, 100000, 10000, longitude, latitude, "", "", pref.getString("session_id", null), user_id, 10000, 0);
            return listemp;
        }

        @Override
        protected void onPreExecute() {
            /*dialog = new ProgressDialog(context);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();*/
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            try {
                if (books.size() > 0) {
                    adapter = new ListBookAdapter(getActivity(), books, 1, 1, 1);
                    rView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listEx = books;
                    tab_all_count.setText(" (" + filterBook(1).size() + ")");
                    tab_swap_count.setText(" (" + filterBook(2).size() + ")");
                    tab_free_count.setText(" (" + filterBook(3).size() + ")");
                    tab_cart_count.setText(" (" + filterBook(4).size() + ")");
                    //dialog.dismiss();
                } else {
                    tab_all_count.setText("(0)");
                    tab_swap_count.setText("(0)");
                    tab_free_count.setText("(0)");
                    tab_cart_count.setText("(0)");
                }
            } catch (Exception e) {
                Toast.makeText(context, Information.noti_no_data, Toast.LENGTH_LONG).show();
                //dialog.dismiss();
            }
            //dialog.dismiss();
        }
    }

    class updateProfile extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;
        Context context;
        String email, phone, birthday, photo, session_id, first_name, last_name;

        public updateProfile(Context context, String session_id, String email, String phone, String birthday, String photo, String first_name, String last_name) {
            this.context = context;
            this.session_id = session_id;
            this.email = email;
            this.phone = phone;
            this.birthday = birthday;
            this.photo = photo;
            this.first_name = first_name;
            this.last_name = last_name;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
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
            UserController userController = new UserController(context);
            return userController.updateprofile(first_name, last_name, email, phone, birthday, photo, session_id);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                dialog.dismiss();
                Toast.makeText(getActivity(), Information.noti_update_success, Toast.LENGTH_LONG).show();
                dataChange=false;
            } else {
                dialog.dismiss();
                Toast.makeText(getActivity(), Information.noti_update_fail, Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
