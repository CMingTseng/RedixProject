package com.booxtown.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.booxtown.activity.UserProfileActivity;
import com.booxtown.api.ServiceGenerator;
import com.booxtown.model.CommentBook;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.booxtown.R;

/**
 * Created by Administrator on 28/08/2016.
 */
public class AdapterCommentBook extends RecyclerView.Adapter<AdapterCommentBook.HolderComment> {
    private Context mContext;
    private List<CommentBook> listComments;


    public AdapterCommentBook(Context c, List<CommentBook> listComments) {
        mContext = c;
        this.listComments = listComments;

    }

    @Override
    public HolderComment onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_commnents_interact, parent, false);
        return new HolderComment(itemView);
    }

    @Override
    public void onBindViewHolder(HolderComment hoder, int position) {
        final CommentBook Comments= listComments.get(position);

        hoder.img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,UserProfileActivity.class);
                intent.putExtra("user",Comments.getUser_id()+"");
                mContext.startActivity(intent);
            }
        });
        if(Comments.getPhoto().length()>3) {
            int index = Comments.getPhoto().indexOf("_+_");
            Picasso.with(mContext).load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + Comments.getPhoto().substring(0,index).trim() + "&image=" + Comments.getPhoto().substring(index + 3, Comments.getPhoto().length())).error(R.mipmap.user_empty).into(hoder.img_icon);
        }else{
            Bitmap btm = BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.user_empty);
            hoder.img_icon.setImageBitmap(btm);
        }

        try {
            hoder.txt_userName.setText(Comments.getFirst_name().substring(0, 1).toUpperCase() + Comments.getFirst_name().substring(1, Comments.getFirst_name().length()));
        } catch (Exception err){
            hoder.txt_userName.setText(Comments.getFirst_name());
        }
        hoder.txt_contents.setText(Comments.getContent());
        try {
            SharedPreferences pref = mContext.getSharedPreferences("MyPref", mContext.MODE_PRIVATE);
            String timeZone = pref.getString("timezone", null);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date dt= dateFormat.parse(Comments.getCreate_date());
            String intMonth = (String) android.text.format.DateFormat.format("MM", dt);
            String year = (String) android.text.format.DateFormat.format("yyyy", dt);
            String day = (String) android.text.format.DateFormat.format("dd", dt);
            String hour= (String) android.text.format.DateFormat.format("kk", dt);
            String min= (String) android.text.format.DateFormat.format("mm", dt);
            String second= (String) android.text.format.DateFormat.format("ss", dt);

            String date_post= year+":"+intMonth+":"+day+":"+hour+":"+min+":"+second;
            hoder.txt_datetime.setText(formatDatetime(date_post));
        } catch (Exception e) {
            hoder.txt_datetime.setText(Comments.getCreate_date());
        }

        hoder.myRatingBar.setRating(Comments.getRating());
        LayerDrawable stars = (LayerDrawable) hoder.myRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(247,180,0), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(mContext.getResources().getColor(R.color.color_text_hint), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(mContext.getResources().getColor(R.color.color_text_hint), PorterDuff.Mode.SRC_ATOP); // for half filled stars
        DrawableCompat.setTint(DrawableCompat.wrap(stars.getDrawable(1)),mContext.getResources().getColor(R.color.color_text_hint));
        //set rank
        if(Comments.getContributor() == 0){
            hoder.img_comment_rank1.setVisibility(View.VISIBLE);
            Bitmap btn1 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.conbitrutor_one);
            hoder.img_comment_rank1.setImageBitmap(btn1);
        }else{
            Bitmap btn1 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.conbitrutor_two);
            hoder.img_comment_rank1.setImageBitmap(btn1);
        }
        if(Comments.getGoldenBook() == 0){
            hoder.img_comment_rank2.setVisibility(View.GONE);
        }else if(Comments.getGoldenBook() == 1){
            Bitmap btn1 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.golden_book);
            hoder.img_comment_rank2.setImageBitmap(btn1);
            hoder.img_comment_rank2.setVisibility(View.VISIBLE);
        }
        if(Comments.getListBook() == 0){
            Bitmap btn1 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.newbie);
            hoder.img_comment_rank2.setImageBitmap(btn1);
            hoder.img_comment_rank2.setVisibility(View.VISIBLE);
        }else if(Comments.getListBook() == 1){
            Bitmap btn1 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.bookworm);
            hoder.img_comment_rank2.setImageBitmap(btn1);
            hoder.img_comment_rank2.setVisibility(View.VISIBLE);
        }else{
            Bitmap btn1 = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.bibliophile);
            hoder.img_comment_rank2.setImageBitmap(btn1);
            hoder.img_comment_rank2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    class HolderComment extends RecyclerView.ViewHolder{
        CircularImageView img_icon;
        TextView txt_userName;
        TextView txt_contents;
        TextView txt_datetime;
        RatingBar myRatingBar;
        ImageView img_comment_rank1,img_comment_rank2,img_comment_rank3;
        public HolderComment(View itemView) {
            super(itemView);
            img_icon=(CircularImageView) itemView.findViewById(R.id.icon_user_listing_detail);
            txt_userName=(TextView) itemView.findViewById(R.id.txt_user_comment);
            txt_contents=(TextView) itemView.findViewById(R.id.txt_content_thread_comments);
            txt_datetime=(TextView) itemView.findViewById(R.id.txt_date_thread_comment);
            myRatingBar = (RatingBar)itemView.findViewById(R.id.myRatingBar);
            img_comment_rank1 = (ImageView)itemView.findViewById(R.id.img_comment_rank1);
            img_comment_rank2 = (ImageView)itemView.findViewById(R.id.img_comment_rank2);
            img_comment_rank3 = (ImageView)itemView.findViewById(R.id.img_comment_rank3);
        }
    }
    public String formatDatetime(String input){
        String outPut="";
        String[] stringTmp=input.split(":");
        if(stringTmp[1].equals("01")){
            outPut="Jan";
        }else if(stringTmp[1].equals("02")){
            outPut="Feb";
        }else if(stringTmp[1].equals("03")){
            outPut="Mar";
        }else if(stringTmp[1].equals("04")){
            outPut="Apr";
        }else if(stringTmp[1].equals("05")){
            outPut="May";
        }else if(stringTmp[1].equals("06")){
            outPut="Jun";
        }else if(stringTmp[1].equals("07")){
            outPut="Jul";
        }else if(stringTmp[1].equals("08")){
            outPut="Aug";
        }else if(stringTmp[1].equals("09")){
            outPut="Sep";
        }else if(stringTmp[1].equals("10")){
            outPut="Oct";
        }else if(stringTmp[1].equals("11")){
            outPut="Nov";
        }else if(stringTmp[1].equals("12")){
            outPut="Dec";
        }

        outPut=outPut+" "+ stringTmp[2]+ " at ";

        if(stringTmp[3].equals("12")){
            outPut=outPut+ stringTmp[3]+" pm";
        }
        else if(stringTmp[3].equals("13")){
            outPut=outPut+ "1:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("14")){
            outPut=outPut+ "2:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("15")){
            outPut=outPut+ "3:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("16")){
            outPut=outPut+ "4:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("17")){
            outPut=outPut+ "5:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("18")){
            outPut=outPut+ "6:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("19")){
            outPut=outPut+"7:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("20")){
            outPut=outPut+"8:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("21")){
            outPut=outPut+ "9:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("22")){
            outPut=outPut+ "10:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("23")){
            outPut=outPut+ "11:"+ stringTmp[4] +" pm";
        }
        else if(stringTmp[3].equals("24")){
            outPut=outPut+ "0:"+ stringTmp[4] +" am";
        }
        else{
            outPut=outPut+ stringTmp[3]+":"+ stringTmp[4] +" am";
        }

        return outPut;
    }
}
