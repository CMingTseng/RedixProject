package redix.booxtown.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import redix.booxtown.R;
import redix.booxtown.activity.ListingsDetailActivity;
import redix.booxtown.model.Explore;

/**
 * Created by Administrator on 30/08/2016.
 */
public class AdapterSwap  extends BaseAdapter {
    private Context mContext;
    ArrayList<String> list;

    public AdapterSwap(Context c,ArrayList<String> listS) {
        mContext = c;
        this.list=listS;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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
//-----------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_grid_swap, null);
        TextView txt_content=(TextView) convertView.findViewById(R.id.txt_content_swap_select);
        ImageView img_check=(ImageView) convertView.findViewById(R.id.check_box_swap);
        if (position%2==0){
            img_check.setImageResource(R.drawable.check_box_not);
        }
        else{
            img_check.setImageResource(R.drawable.check_box);
        }
        txt_content.setText(list.get(position));
        img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

}
