package com.booxtown.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booxtown.activity.MainAllActivity;
import com.booxtown.activity.SignIn_Activity;
import com.booxtown.controller.CheckSession;
import com.booxtown.controller.Information;
import com.booxtown.controller.UserController;
import com.booxtown.fragment.MainFragment;
import com.booxtown.fragment.MyProfileFragment;
import com.booxtown.fragment.TopicFragment;
import com.booxtown.fragment.WishboardFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;

import com.booxtown.activity.ListingCollectionActivity;
import com.booxtown.activity.ListingsDetailActivity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.model.Book;
import com.squareup.picasso.Target;

/**
 * Created by Administrator on 29/08/2016.
 */
public class ListBookAdapter extends RecyclerView.Adapter<ListBookAdapter.LisBookHolder>
        implements Filterable{
    private Context mContext;
    private List<Book> listBook;
    private List<Book> originbook;
    SharedPreferences pref;
    private ItemFilter mFilter = new ItemFilter();
    int type,typeColor;
    String username;
    int back;
    Book book;
    public ListBookAdapter(Context c, List<Book> list_book,int type, int typeColor,int back) {
        mContext = c;
        this.listBook = list_book;
        this.originbook = list_book;
        this.type = type;
        this.typeColor=typeColor;
        this.back = back;
        try {
            pref = mContext.getSharedPreferences("MyPref",mContext.MODE_PRIVATE);
        }catch (Exception e){

        }

    }

//    public class LisBookHolder extends RecyclerView.ViewHolder{
//
//        TextView txt_title_book ;
//        TextView txt_author_book ;
//        TextView txt_price_book;
//
//        ImageView img_book ;
//        ImageView img_swap;
//        ImageView img_free;
//        ImageView img_buy ;
//        ImageView img_edit ;
//        public LisBookHolder(View convertView) {
//            super(convertView);
//            txt_title_book = (TextView) convertView.findViewById(R.id.txt_title_book_listings);
//            txt_author_book = (TextView) convertView.findViewById(R.id.txt_author_book_listings);
//            txt_price_book=(TextView) convertView.findViewById(R.id.txt_price_listings);
//
//            img_book = (ImageView)convertView.findViewById(R.id.img_book);
//            img_swap = (ImageView)convertView.findViewById(R.id.img_explore_swap_listings);
//            img_free = (ImageView)convertView.findViewById(R.id.img_explore_free_listings);
//            img_buy = (ImageView)convertView.findViewById(R.id.img_explore_buy_listing);
//            img_edit = (ImageView)convertView.findViewById(R.id.img_listings_edit);
//        }
//    }


    @Override
    public LisBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gridview_listings, null);
        LisBookHolder rcv = new LisBookHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(LisBookHolder hoder, int position) {
        final LisBookHolder hoders=hoder;
        final Book ex= listBook.get(position);
        username = pref.getString("username", null);
        String[] image = ex.getPhoto().split(";");
        if (image.length!=0){
            int index=image[0].indexOf("_+_");
            if(index>0 && image[0].length() >3 ) {
                String img = image[0].substring(index+3, image[0].length());
                String imageLink= ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + image[0].substring(0,index) + "&image=" +  img  + "";
                try {
                    Picasso.with(mContext). load(imageLink).placeholder(R.drawable.blank_image).into(hoder.img_book);

                }catch (Exception exx){
                    String err= exx.getMessage();
                }
            }
            else{
                Picasso.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + username + "&image=" +  image[0]  + "").placeholder(R.drawable.blank_image).
                        into(hoder.img_book);
            }
        }else {
            Picasso.with(mContext).load(R.drawable.blank_image).into(hoder.img_book);
        }
        char array[]=ex.getAction().toCharArray();
        if (ex.getPrice()!=0){

            try {
                String priceBook=(ex.getPrice()+"");
                int index = priceBook.indexOf(".");
                if (Integer.parseInt(priceBook.substring(index+1,priceBook.length())) != 0) {
                    hoder.txt_price_book.setText("AED " + ex.getPrice());
                } else {
                    hoder.txt_price_book.setText("AED " + priceBook.substring(0, index));
                }
            }catch (Exception exx){

            }
        }else{
            hoder.txt_price_book.setVisibility(View.INVISIBLE);
        }


        if(String.valueOf(array[0]).contains("1")){
            Picasso.with(mContext).load(R.drawable.explore_btn_swap_active).into(hoder.img_swap);
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_swap_dis_active).into(hoder.img_swap);

        }
        if(String.valueOf(array[2]).contains("1")){
            Picasso.with(mContext).load(R.drawable.explore_btn_free_active).into(hoder.img_free);
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_free_dis_active).into(hoder.img_free);
        }
        if(String.valueOf(array[1]).contains("1")){
            if(typeColor==1) {
                Picasso.with(mContext).load(R.drawable.explore_btn_buy_active).into(hoder.img_buy);
            }else{
                Picasso.with(mContext).load(R.drawable.listing_btn_buy).into(hoder.img_buy);
            }
        }
        else {
            Picasso.with(mContext).load(R.drawable.explore_btn_buy_dis_active).into(hoder.img_buy);
        }
        if (type==1){
            Picasso.with(mContext).load(R.drawable.listing_btn_edit).into(hoder.img_edit);
        }
        else {
            hoder.img_edit.setVisibility(View.INVISIBLE);
        }

        hoder.img_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book=ex;
                if(Information.FragmentChoose==1){
                    SaveSetting(1);
                }else {

                    String ss = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.frame_main_all).getClass().getName().toString();
                    int fragmentList = ss.lastIndexOf(".");
                    if (ss.equals("com.booxtown.fragment.ListingsFragment")) {
                        ListingsDetailActivity fragment = new ListingsDetailActivity();
                        Bundle bundle = new Bundle();
                        bundle.putString(String.valueOf(R.string.valueListings), "3");
                        bundle.putInt("back", 2);
                        bundle.putSerializable("item", ex);
                        fragment.setArguments(bundle);
                        callFragment(fragment);
                    } else {
                        ListingsDetailActivity fragment = new ListingsDetailActivity();
                        Bundle bundle = new Bundle();
                        bundle.putString(String.valueOf(R.string.valueListings), "6");
                        bundle.putInt("back", 2);
                        bundle.putSerializable("item", ex);
                        fragment.setArguments(bundle);
                        callFragment(fragment);
                    }
                }
            }
        });


        hoder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book=ex;
                if(Information.FragmentChoose==1){
                    SaveSetting(2);
                }else {

                    ListingCollectionActivity fragment = new ListingCollectionActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("activity", "edit");
                    if (back == 1) {
                        bundle.putInt("back", 1);
                    } else {
                        bundle.putInt("back", 2);
                    }
                    bundle.putSerializable("bookedit", ex);
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }
            }
        });

        if(ex.getTitle().toString().length()>0) {
            hoder.txt_title_book.setText(ex.getTitle().toString().substring(0,1).toUpperCase()+ex.getTitle().toString().substring(1,ex.getTitle().toString().length()));
        }
        if(ex.getAuthor().toString().length()==0||ex.getAuthor().toString().isEmpty()||ex.getAuthor().toString().equals("")){
            hoder.txt_author_book.setVisibility(View.GONE);
        }
        hoder.txt_author_book.setText("by "+ex.getAuthor().toString());

    }


    public class LisBookHolder extends RecyclerView.ViewHolder{

        TextView txt_title_book ;
        TextView txt_author_book ;
        TextView txt_price_book;

        ImageView img_book ;
        ImageView img_swap;
        ImageView img_free;
        ImageView img_buy ;
        ImageView img_edit ;
        public LisBookHolder(View itemView) {
            super(itemView);
            txt_title_book = (TextView) itemView.findViewById(R.id.txt_title_book_listings);
            txt_author_book = (TextView) itemView.findViewById(R.id.txt_author_book_listings);
            txt_price_book=(TextView) itemView.findViewById(R.id.txt_price_listings);

            img_book = (ImageView)itemView.findViewById(R.id.img_book);
            img_swap = (ImageView)itemView.findViewById(R.id.img_explore_swap_listings);
            img_free = (ImageView)itemView.findViewById(R.id.img_explore_free_listings);
            img_buy = (ImageView)itemView.findViewById(R.id.img_explore_buy_listing);
            img_edit = (ImageView)itemView.findViewById(R.id.img_listings_edit);
        }
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemCount() {
        return listBook.size();
    }

    public void callFragment(Fragment fragment ){
        FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //Khi được goi, fragment truyền vào sẽ thay thế vào vị trí FrameLayout trong Activity chính
        transaction.replace(R.id.frame_main_all, fragment);
        transaction.commit();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<Book> filteredItems = new ArrayList<Book>();

                for(int i = 0, l = originbook.size(); i < l; i++)
                {
                    Book country = originbook.get(i);
                    if(country.toString().toLowerCase().contains(constraint))
                        filteredItems.add(country);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originbook;
                    result.count = originbook.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listBook = (ArrayList<Book>)results.values;
            notifyDataSetChanged();
//            for(int i = 0, l = listBook.size(); i < l; i++)
//                add(countryList.get(i));
//            notifyDataSetInvalidated();
        }
    }
    class GetWithHeight extends AsyncTask<Bitmap,Void,Bitmap> {

        Context context;
        String imageUrl;
        ImageView img_book;
        public GetWithHeight(Context context, String imageUrl,ImageView img_book){
            this.context = context;
            this.imageUrl=imageUrl;
            this.img_book=img_book;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... Bitmap) {
            Bitmap downloadedImage = null;
            try {
                downloadedImage = Picasso.with(context).load(imageUrl).get();
                return downloadedImage;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Bitmap downloadedImage) {
            try {
                int width = downloadedImage.getWidth();
                int height = downloadedImage.getHeight();
                if (width> height) {
                    img_book.setRotation(90f);
                    img_book.setImageBitmap(downloadedImage);
                    /*Picasso.with(mContext).load(imageUrl).rotate(90f).placeholder(R.drawable.blank_image).
                            into(img_book);*/
                } else {
                    img_book.setImageBitmap(downloadedImage);
                    /*Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.blank_image).
                            into(img_book);*/
                }
            }catch (Exception e){}
        }
    }

    public void SaveSetting(final int type){
        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want to save the changes made");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pref = mContext.getSharedPreferences("MyPref", mContext.MODE_PRIVATE);

                        String session_id = pref.getString("session_id", "");
                        updateProfile updateProfile = new updateProfile(mContext, session_id, Information.FragmentEmail,
                                Information.FragmentPhone, Information.FragmentDateTime, Information.FragmentBirthday, Information.FragmentPhoto, Information.FragmentFirst, Information.FragmentLast,type,Information.IsBirhtDay);
                        updateProfile.execute();

                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Information.FragmentChoose = 0;
                        if(type==1){
                            String ss = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.frame_main_all).getClass().getName().toString();
                            int fragmentList = ss.lastIndexOf(".");
                            if (ss.equals("com.booxtown.fragment.ListingsFragment")) {
                                ListingsDetailActivity fragment = new ListingsDetailActivity();
                                Bundle bundle = new Bundle();
                                bundle.putString(String.valueOf(R.string.valueListings), "3");
                                bundle.putInt("back", 2);
                                bundle.putSerializable("item", book);
                                fragment.setArguments(bundle);
                                callFragment(fragment);
                            } else {
                                ListingsDetailActivity fragment = new ListingsDetailActivity();
                                Bundle bundle = new Bundle();
                                bundle.putString(String.valueOf(R.string.valueListings), "6");
                                bundle.putInt("back", 2);
                                bundle.putSerializable("item", book);
                                fragment.setArguments(bundle);
                                callFragment(fragment);
                            }
                        }else {
                            ListingCollectionActivity fragment = new ListingCollectionActivity();
                            Bundle bundle = new Bundle();
                            bundle.putString("activity", "edit");
                            if (back == 1) {
                                bundle.putInt("back", 1);
                            } else {
                                bundle.putInt("back", 2);
                            }
                            bundle.putSerializable("bookedit", book);
                            fragment.setArguments(bundle);
                            callFragment(fragment);
                        }
                        dialog.cancel();
                    }
                });
        android.support.v7.app.AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    class updateProfile extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog dialog;
        Context context;
        String email, phone, birthday, photo, session_id, first_name, last_name, date_time;
        int type;
        int is_birthday;
        public updateProfile(Context context, String session_id, String email, String phone, String date_time, String birthday, String photo, String first_name, String last_name,int type, int is_birthday) {
            this.context = context;
            this.session_id = session_id;
            this.email = email;
            this.phone = phone;
            this.birthday = birthday;
            this.photo = photo;
            this.first_name = first_name;
            this.last_name = last_name;
            this.date_time = date_time;
            this.type=type;
            this.is_birthday=is_birthday;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
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
            return userController.updateprofile(first_name, last_name, email, phone, birthday, photo, session_id,is_birthday);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean == true) {
                Information.FragmentChoose=0;
                dialog.dismiss();
                if(type==1){
                    String ss = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.frame_main_all).getClass().getName().toString();
                    int fragmentList = ss.lastIndexOf(".");
                    if (ss.equals("com.booxtown.fragment.ListingsFragment")) {
                        ListingsDetailActivity fragment = new ListingsDetailActivity();
                        Bundle bundle = new Bundle();
                        bundle.putString(String.valueOf(R.string.valueListings), "3");
                        bundle.putInt("back", 2);
                        bundle.putSerializable("item", book);
                        fragment.setArguments(bundle);
                        callFragment(fragment);
                    } else {
                        ListingsDetailActivity fragment = new ListingsDetailActivity();
                        Bundle bundle = new Bundle();
                        bundle.putString(String.valueOf(R.string.valueListings), "6");
                        bundle.putInt("back", 2);
                        bundle.putSerializable("item", book);
                        fragment.setArguments(bundle);
                        callFragment(fragment);
                    }
                }else {
                    ListingCollectionActivity fragment = new ListingCollectionActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("activity", "edit");
                    if (back == 1) {
                        bundle.putInt("back", 1);
                    } else {
                        bundle.putInt("back", 2);
                    }
                    bundle.putSerializable("bookedit", book);
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }
                Toast.makeText(context, Information.noti_update_success, Toast.LENGTH_LONG).show();
            } else {
                dialog.dismiss();
                Toast.makeText(context, Information.noti_update_fail, Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
