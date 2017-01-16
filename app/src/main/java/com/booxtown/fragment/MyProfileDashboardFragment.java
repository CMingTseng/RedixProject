package com.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.booxtown.activity.SignIn_Activity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.Information;
import com.booxtown.model.Comment;
import com.booxtown.recyclerclick.RecyclerItemClickListener;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;
import com.booxtown.adapter.AdapterProfileDashboard;
import com.booxtown.controller.DashBoardController;
import com.booxtown.model.DashBoard;
import com.booxtown.model.User;

public class MyProfileDashboardFragment extends Fragment {

    TextView txt_username;
    RecyclerView lv_myprofile_dashboard;
    int user_id;
    List<DashBoard> dashBoards_new = new ArrayList<>();
    AdapterProfileDashboard adapterProfileDashboard;
    ImageView img_menu_component, img_menu, img_rank1, img_rank2, img_rank3;
    TextView title_menu;
    //private static RelativeLayout bottomLayout;
    boolean loading = true,
            isLoading = true;
    RatingBar ratingBar_userprofile;
    User user;
    CircularImageView imv_menu_profile;
    LinearLayoutManager linearLayoutManager;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    String trans_Id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_profile_dashboard_fragment, container, false);
        init(view);
        lv_myprofile_dashboard.setLayoutManager(linearLayoutManager);
        lv_myprofile_dashboard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        user = (User) getArguments().getSerializable("user");
        if (user.getPhoto().length() > 3) {
            int index = user.getPhoto().indexOf("_+_");
            Picasso.with(getContext())
                    .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + user.getPhoto().substring(0, index).trim() + "&image=" + user.getPhoto().substring(index + 3, user.getPhoto().length()))
                    .placeholder(R.mipmap.user_empty)
                    .into(imv_menu_profile);
        } else {
            Picasso.with(getContext())
                    .load(R.mipmap.user_empty)
                    .into(imv_menu_profile);
        }
        //set rank
        if (user.getContributor() == 0) {
            img_rank1.setVisibility(View.VISIBLE);
            Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.conbitrutor_one);
            img_rank1.setImageBitmap(btn1);

        } else {
            Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.conbitrutor_two);
            img_rank1.setImageBitmap(btn1);

        }
        if (user.getGoldenBook() == 0) {
            img_rank2.setVisibility(View.GONE);
        } else if (user.getGoldenBook() == 1) {
            Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.golden_book);
            img_rank2.setImageBitmap(btn1);
            img_rank2.setVisibility(View.VISIBLE);
        }

        if (user.getListBook() == 0) {
            Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.newbie);
            img_rank3.setImageBitmap(btn1);
            img_rank3.setVisibility(View.VISIBLE);
        } else if (user.getListBook() == 1) {
            Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.bookworm);
            img_rank3.setImageBitmap(btn1);
            img_rank3.setVisibility(View.VISIBLE);
        } else {
            Bitmap btn1 = BitmapFactory.decodeResource(getResources(), R.drawable.bibliophile);
            img_rank3.setImageBitmap(btn1);
            img_rank3.setVisibility(View.VISIBLE);
        }

        //rank
        ratingBar_userprofile.setRating(user.getRating());
        LayerDrawable stars = (LayerDrawable) ratingBar_userprofile.getProgressDrawable();

        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.rgb(249, 242, 0), PorterDuff.Mode.SRC_ATOP);
        // stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.bg_rating), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)), getResources().getColor(R.color.bg_rating));
        //end

        img_menu_component.setVisibility(View.GONE);
        title_menu.setText("My Profile");
        Picasso.with(getContext()).load(R.drawable.btn_sign_in_back).into(img_menu);
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final String session_id = pref.getString("session_id", null);
        final String user_name = pref.getString("firstname", null);
        user_id = Integer.valueOf(pref.getString("user_id", null));

        txt_username.setText(user_name);

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFragment(new MyProfileFragment());
            }
        });
        populatRecyclerView(user_id, session_id);
        implementScrollListener(session_id);

        return view;
    }

    public void init(View view) {
        linearLayoutManager = new LinearLayoutManager(getContext());
        imv_menu_profile = (CircularImageView) view.findViewById(R.id.imv_menu_profile);
        ratingBar_userprofile = (RatingBar) view.findViewById(R.id.ratingBar_userprofile);
        img_rank1 = (ImageView) view.findViewById(R.id.img_rank1);
        img_rank2 = (ImageView) view.findViewById(R.id.img_rank2);
        img_rank3 = (ImageView) view.findViewById(R.id.img_rank3);

        txt_username = (TextView) view.findViewById(R.id.txt_profile_username);
        img_menu = (ImageView) getActivity().findViewById(R.id.img_menu);
        lv_myprofile_dashboard = (RecyclerView) view.findViewById(R.id.lv_myprofile_dashboard);
        img_menu_component = (ImageView) getActivity().findViewById(R.id.img_menu_component);
        title_menu = (TextView) getActivity().findViewById(R.id.txt_title);
    }

    public void callFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    private void populatRecyclerView(final int user_id, String session_id) {
        getDashBoard getDashBoard = new getDashBoard(getContext(), session_id, 15, 0);
        getDashBoard.execute();
        if (dashBoards_new.size() == 0) {
            adapterProfileDashboard = new AdapterProfileDashboard(getActivity(), dashBoards_new, user_id);
            lv_myprofile_dashboard.setAdapter(adapterProfileDashboard);
        } else {
            adapterProfileDashboard.notifyDataSetChanged();
        }
        lv_myprofile_dashboard.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        DashBoard dashBoard = dashBoards_new.get(position);
                        if (dashBoard.getIs_done() == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("dashboard", dashBoard);
                            bundle.putSerializable("user", user);
                            bundle.putString("user_id", user_id + "");
                            DashboardStatusFragment fragment = new DashboardStatusFragment();
                            fragment.setArguments(bundle);
                            callFragment(fragment);
                        } else if (dashBoard.getIs_cancel() == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("dashboard", dashBoard);
                            bundle.putSerializable("user", user);
                            bundle.putString("user_id", user_id + "");
                            DashboardDeleteFragment fragment = new DashboardDeleteFragment();
                            fragment.setArguments(bundle);
                            callFragment(fragment);
                        } else if (dashBoard.getIs_done() == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("dashboard", dashBoard);
                            bundle.putSerializable("user", user);
                            bundle.putSerializable("trans", trans_Id);
                            bundle.putString("user_id", user_id + "");
                            DashboardStopFragment fragment = new DashboardStopFragment();
                            fragment.setArguments(bundle);
                            callFragment(fragment);
                        }
                    }
                }));
    }

    private int totalItemCount, lastVisibleItem;

    private void implementScrollListener(final String session_id) {
        lv_myprofile_dashboard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    DashBoard dashBoard_lv = dashBoards_new.get(dashBoards_new.size() - 1);
                    getDashBoard getDashBoard = new getDashBoard(getContext(), session_id, 15, dashBoard_lv.getId());
                    getDashBoard.execute();
                    // Do something
                    loading = true;
                }

            }
        });
    }


    class getDashBoard extends AsyncTask<Void, Void, List<DashBoard>> {

        Context context;
        String session_id;
        int top;
        int from;
        ProgressDialog dialog;

        public getDashBoard(Context context, String session_id, int top, int from) {
            this.context = context;
            this.session_id = session_id;
            this.top = top;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage(Information.noti_dialog);
            dialog.show();
        }

        @Override
        protected List<DashBoard> doInBackground(Void... voids) {
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
            DashBoardController dashBoardController = new DashBoardController();
            return dashBoardController.getDashBoard(session_id, top, from);
        }

        @Override
        protected void onPostExecute(final List<DashBoard> dashBoards) {
            try {
                if (dashBoards.size() > 0) {
                    dashBoards_new.addAll(dashBoards);
                    adapterProfileDashboard.notifyDataSetChanged();
                    trans_Id = dashBoards.get(0).getId() + "";
                    dialog.dismiss();
                    loading = false;
                } else {
                    dialog.dismiss();
                    loading = true;
                }
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }
}
