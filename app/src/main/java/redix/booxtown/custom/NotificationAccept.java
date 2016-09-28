package redix.booxtown.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import redix.booxtown.R;

/**
 * Created by thuyetpham94 on 27/08/2016.
 */
public class NotificationAccept {
    private Context mContext;
    private Resources mResources;
    private RelativeLayout mRelativeLayout;
    private Button mBTN;
    private ImageView mImageView;
    private Bitmap mBitmap;
    public void accept(Context context, Resources mresource, Bitmap mBitmap,ImageView mImageView){
        mContext = context;

        // Get the Resources
        mResources = mresource;

        // Get the widgets reference from XML layout


        // Get the bitmap from drawable resources
        //mBitmap = BitmapFactory.decodeResource(mResources,R.drawable.img_temp1);

        // Display the bitmap in ImageView
        mImageView.setImageBitmap(mBitmap);

        // Set an image for ImageView
        mImageView.setImageBitmap(mBitmap);
        BorderImage borderImage = new BorderImage();
        RoundedBitmapDrawable drawable = borderImage.createRoundedBitmapDrawableWithBorder(mResources,mBitmap);
        mImageView.setImageDrawable(drawable);
    }
}
