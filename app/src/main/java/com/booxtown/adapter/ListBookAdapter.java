package com.booxtown.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;

import com.booxtown.activity.ListingCollectionActivity;
import com.booxtown.activity.ListingsDetailActivity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.model.Book;

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
    int type;
    String username;
    int back;

    public ListBookAdapter(Context c, List<Book> list_book,int type,int back) {
        mContext = c;
        this.listBook = list_book;
        this.originbook = list_book;
        this.type = type;
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
        final Book ex= listBook.get(position);
        username = pref.getString("username", null);
        String[] image = ex.getPhoto().split(";");
        if (image.length!=0){
            int index=image[0].indexOf("_+_");
            if(index>0 && image[0].length() >3 ) {
                String img = image[0].substring(index+3, image[0].length());
                Glide.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + image[0].substring(0,index) + "&image=" +  img  + "").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.blank_image).
                        into(hoder.img_book);
            }
            else{
                Glide.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + username + "&image=" +  image[0]  + "").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.blank_image).
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
            Picasso.with(mContext).load(R.drawable.listing_btn_buy).into(hoder.img_buy);
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


                String ss= ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.frame_main_all).getClass().getName().toString();
                int fragmentList=ss.lastIndexOf(".");
                if(ss.equals("ListingsFragment")) {
                    ListingsDetailActivity fragment = new ListingsDetailActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString(String.valueOf(R.string.valueListings), "3");
                    bundle.putInt("back", 2);
                    bundle.putSerializable("item", ex);
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }
                else{
                    ListingsDetailActivity fragment = new ListingsDetailActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString(String.valueOf(R.string.valueListings), "6");
                    bundle.putInt("back", 2);
                    bundle.putSerializable("item", ex);
                    fragment.setArguments(bundle);
                    callFragment(fragment);
                }
            }
        });


        hoder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListingCollectionActivity fragment = new ListingCollectionActivity();
                Bundle bundle = new Bundle();
                bundle.putString("activity","edit");
                if(back ==1){
                    bundle.putInt("back",1);
                }else{
                    bundle.putInt("back",2);
                }
                bundle.putSerializable("bookedit",ex);
                fragment.setArguments(bundle);
                callFragment(fragment);
            }
        });

        if(ex.getTitle().toString().length()>0) {
            hoder.txt_title_book.setText(ex.getTitle().toString().substring(0,1).toUpperCase()+ex.getTitle().toString().substring(1,ex.getTitle().toString().length()));
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

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // TODO Auto-generated method stub
//        LayoutInflater inflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final Book ex= listBook.get(position);
//        username = pref.getString("username", null);
//        String[] image = ex.getPhoto().split(";");
//
//        Hoder hoder = new Hoder();
//        convertView = inflater.inflate(R.layout.custom_gridview_listings, null);
//        hoder.txt_title_book = (TextView) convertView.findViewById(R.id.txt_title_book_listings);
//        hoder.txt_author_book = (TextView) convertView.findViewById(R.id.txt_author_book_listings);
//        hoder.txt_price_book=(TextView) convertView.findViewById(R.id.txt_price_listings);
//
//        hoder.img_book = (ImageView)convertView.findViewById(R.id.img_book);
//        hoder.img_swap = (ImageView)convertView.findViewById(R.id.img_explore_swap_listings);
//        hoder.img_free = (ImageView)convertView.findViewById(R.id.img_explore_free_listings);
//        hoder.img_buy = (ImageView)convertView.findViewById(R.id.img_explore_buy_listing);
//        hoder.img_edit = (ImageView)convertView.findViewById(R.id.img_listings_edit);
//        if (image.length!=0){
//            int index=image[0].indexOf("_+_");
//            if(index>0 && image[0].length() >3 ) {
//                String img = image[0].substring(index+3, image[0].length());
//                Glide.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + image[0].substring(0,index) + "&image=" +  img  + "").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.blank_image).
//                        into(hoder.img_book);
//            }
//            else{
//                Glide.with(mContext). load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username=" + username + "&image=" +  image[0]  + "").diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.blank_image).
//                        into(hoder.img_book);
//            }
//        }else {
//            Picasso.with(mContext).load(R.drawable.blank_image).into(hoder.img_book);
//        }
//        char array[]=ex.getAction().toCharArray();
//        if (ex.getPrice()!=0){
//            hoder.txt_price_book.setText("AED "+String.valueOf(ex.getPrice()));
//        }else{
//            hoder.txt_price_book.setVisibility(View.INVISIBLE);
//        }
//
//
//        if(String.valueOf(array[0]).contains("1")){
//            Picasso.with(mContext).load(R.drawable.explore_btn_swap_active).into(hoder.img_swap);
//        }
//        else {
//            Picasso.with(mContext).load(R.drawable.explore_btn_swap_dis_active).into(hoder.img_swap);
//
//        }
//        if(String.valueOf(array[1]).contains("1")){
//            Picasso.with(mContext).load(R.drawable.explore_btn_free_active).into(hoder.img_free);
//        }
//        else {
//            Picasso.with(mContext).load(R.drawable.explore_btn_free_dis_active).into(hoder.img_free);
//        }
//        if(String.valueOf(array[2]).contains("1")){
//            Picasso.with(mContext).load(R.drawable.listing_btn_buy).into(hoder.img_buy);
//        }
//        else {
//            Picasso.with(mContext).load(R.drawable.explore_btn_buy_dis_active).into(hoder.img_buy);
//        }
//        if (type==1){
//            Picasso.with(mContext).load(R.drawable.listing_btn_edit).into(hoder.img_edit);
//        }
//        else {
//            hoder.img_edit.setVisibility(View.GONE);
//        }
//
//            hoder.img_book.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ListingsDetailActivity fragment = new ListingsDetailActivity();
//                    Bundle bundle = new Bundle();
//                    bundle.putString(String.valueOf(R.string.valueListings),"3");
//                    bundle.putInt("back",2);
//                    bundle.putSerializable("item",ex);
//                    fragment.setArguments(bundle);
//                    callFragment(fragment);
//                }
//            });
//
//
//        hoder.img_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ListingCollectionActivity fragment = new ListingCollectionActivity();
//                Bundle bundle = new Bundle();
//                bundle.putString("activity","edit");
//                if(back ==1){
//                    bundle.putInt("back",1);
//                }else{
//                    bundle.putInt("back",2);
//                }
//                bundle.putSerializable("bookedit",ex);
//                fragment.setArguments(bundle);
//                callFragment(fragment);
//            }
//        });
//
//        if(ex.getTitle().toString().length()>0) {
//            hoder.txt_title_book.setText(ex.getTitle().toString().substring(0,1).toUpperCase()+ex.getTitle().toString().substring(1,ex.getTitle().toString().length()));
//        }
//
//        hoder.txt_author_book.setText("by "+ex.getAuthor().toString());
//
//
//        return convertView;
//    }
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

//    public class Hoder{
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
//
//    }

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
}
