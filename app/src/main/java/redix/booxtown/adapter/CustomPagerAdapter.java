package redix.booxtown.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import redix.booxtown.R;
import redix.booxtown.api.ServiceGenerator;

/**
 * Created by Administrator on 29/08/2016.
 */
public class CustomPagerAdapter extends PagerAdapter {
    String[] url;
    Context mContext;
    String username;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context,String[] url,String username) {
        mContext = context;
        this.url = url;
        this.username = username;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return url.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        try {
            Glide.with(mContext)
                    .load(ServiceGenerator.API_BASE_URL+"booxtown/rest/getImage?username="+username+"&image="+url[position].substring(username.length()+3,url[position].length()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}