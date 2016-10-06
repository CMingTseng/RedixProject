package redix.booxtown.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.UserProfileActivity;
import redix.booxtown.api.ServiceGenerator;
import redix.booxtown.model.Comment;
import redix.booxtown.model.InteractComment;

/**
 * Created by Administrator on 28/08/2016.
 */
public class AdapterInteractThreadDetails extends BaseAdapter {
    private Context mContext;
    private List<Comment> listComments;


    public AdapterInteractThreadDetails(Context c, List<Comment> listComments) {
        mContext = c;
        this.listComments = listComments;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listComments.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Hoder hoder = new Hoder();

        final Comment Comments= listComments.get(position);
        convertView = inflater.inflate(R.layout.custom_commnents_interact, null);
        hoder.myRatingBar = (RatingBar)convertView.findViewById(R.id.myRatingBar);
        hoder.img_icon=(ImageView) convertView.findViewById(R.id.icon_user_listing_detail);
        hoder.img_rank_one=(ImageView) convertView.findViewById(R.id.img_comment_rank1);
        hoder.img_rank_two=(ImageView) convertView.findViewById(R.id.img_comment_rank2);
        hoder.img_rank_three=(ImageView) convertView.findViewById(R.id.img_comment_rank3);
        hoder.txt_userName=(TextView) convertView.findViewById(R.id.txt_user_comment);
        hoder.txt_contents=(TextView) convertView.findViewById(R.id.txt_content_thread_comments);
        hoder.txt_datetime=(TextView) convertView.findViewById(R.id.txt_date_thread_comment);
        hoder.img_comment_rank1 = (ImageView)convertView.findViewById(R.id.img_comment_rank1);
        hoder.img_comment_rank2 = (ImageView)convertView.findViewById(R.id.img_comment_rank2);
        hoder.img_comment_rank3 = (ImageView)convertView.findViewById(R.id.img_comment_rank3);
        SimpleDateFormat format = new SimpleDateFormat("MMM dd hh:mm");

        try {
            hoder.txt_datetime.setText(formatDatetime(Comments.getCreate_date().replaceAll("-",":").replace(" ",":")));
        } catch (Exception e) {
            hoder.txt_datetime.setText(Comments.getCreate_date());

        }

        hoder.img_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,UserProfileActivity.class);
                    intent.putExtra("user",Integer.parseInt(Comments.getUser_id()));
                    mContext.startActivity(intent);
                }
            });

        if(Comments.getPhoto().length()>3) {
            Picasso.with(mContext)
                    .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + Comments.getUsername() + "&image=" + Comments.getPhoto().substring(Comments.getUsername().length() + 3, Comments.getPhoto().length()))
                    .error(R.mipmap.user_empty)
                    .into(hoder.img_icon);
        }
        else
        {
            Picasso.with(mContext)
                    .load(ServiceGenerator.API_BASE_URL + "booxtown/rest/getImage?username=" + Comments.getUsername() + "&image=")
                    .error(R.mipmap.user_empty)
                    .into(hoder.img_icon);
        }

//            Resources mResources = mContext.getResources();
//            Bitmap mBitmap = BitmapFactory.decodeResource(mResources, R.drawable.icon_test);
//            NotificationAccept notificationAccept = new NotificationAccept();
//            notificationAccept.accept(mContext, mResources, mBitmap, img_icon);

//            if(interactComments.isRank_one()){
//                hoder.img_rank_one.setVisibility(View.VISIBLE);
//            }
//            else{
//                hoder.img_rank_one.setVisibility(View.INVISIBLE);
//            }
//
//            if(interactComments.isRank_two()){
//                hoder.img_rank_two.setVisibility(View.VISIBLE);
//            }
//            else{
//                hoder.img_rank_two.setVisibility(View.INVISIBLE);
//            }
//
//            if(interactComments.isRank_three()){
//                hoder.img_rank_three.setVisibility(View.VISIBLE);
//            }
//            else{
//                hoder.img_rank_three.setVisibility(View.INVISIBLE);
//            }
//
        hoder.txt_userName.setText(Comments.getUsername());
        hoder.txt_contents.setText(Comments.getContent());
        hoder.myRatingBar.setRating(Comments.getRating());
        return convertView;
    }

    public class Hoder{
        ImageView img_icon;
        ImageView img_rank_one;
        ImageView img_rank_two;
        ImageView img_rank_three;
        TextView txt_userName;
        TextView txt_contents;
        TextView txt_datetime;
        RatingBar myRatingBar;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
