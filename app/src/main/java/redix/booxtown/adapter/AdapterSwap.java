package redix.booxtown.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import redix.booxtown.model.BookSwap;
import redix.booxtown.model.Explore;

/**
 * Created by Administrator on 30/08/2016.
 */
public class AdapterSwap  extends BaseAdapter {
    private Context mContext;
    ArrayList<BookSwap> list;

    public AdapterSwap(Context c,ArrayList<BookSwap> listS) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_grid_swap, null);
        TextView txt_content=(TextView) convertView.findViewById(R.id.txt_content_swap_select);
        final ImageView img_check=(ImageView) convertView.findViewById(R.id.check_box_swap);
        BookSwap book= list.get(position);
        txt_content.setText(book.getValue());
        Drawable d = mContext.getResources().getDrawable(R.drawable.check_box_not);
        if(!book.ischeck()){
           img_check.setBackgroundDrawable(d);
        }
        img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable d_not = mContext.getResources().getDrawable(R.drawable.check_box_not);
                img_check.setBackgroundDrawable(d_not);
                list.get(position).setIscheck(true);
            }
        });

        return convertView;
    }

}
