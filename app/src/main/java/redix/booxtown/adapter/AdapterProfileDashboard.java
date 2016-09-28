package redix.booxtown.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import redix.booxtown.R;

/**
 * Created by thuyetpham94 on 29/08/2016.
 */
public class AdapterProfileDashboard extends BaseAdapter {

    String [] result;
    int [] imageoffer;
    int [] imagestatus;
    Context context;
    private static LayoutInflater inflater=null;
    public AdapterProfileDashboard(Context context, String[] prgmNameList,int [] imageoffer,int [] imagestatus) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        this.context=context;
        this.imageoffer = imageoffer;
        this.imagestatus = imagestatus;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img_offer;
        ImageView img_status;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_listview_profile_dashboard, null);
        holder.tv=(TextView) rowView.findViewById(R.id.txt_listview_profiledashboard);
        holder.img_offer = (ImageView)rowView.findViewById(R.id.img_listview_profiledashboard_offer);
        holder.img_status = (ImageView)rowView.findViewById(R.id.img_listview_profiledashboard_status);
        holder.tv.setText(result[position]);
        holder.img_offer.setImageResource(imageoffer[position]);
        holder.img_status.setImageResource(imagestatus[position]);

        if(position %2==0){
            rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_myprofile_list_databroad));
        }else{
            rowView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_myprofile_list_databroad));
        }
        return rowView;
    }
}