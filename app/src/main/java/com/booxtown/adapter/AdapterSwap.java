package com.booxtown.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import com.booxtown.R;

import com.booxtown.model.BookSwap;

/**
 * Created by Administrator on 30/08/2016.
 */
public class AdapterSwap  extends BaseAdapter {
    private Context mContext;
    ArrayList<BookSwap> list;

    public ArrayList<BookSwap> getList() {
        return list;
    }

    public void setList(ArrayList<BookSwap> list) {
        this.list = list;
    }

    public AdapterSwap(Context c, ArrayList<BookSwap> listS) {
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
        final CheckBox ck= (CheckBox) convertView.findViewById(R.id.checkBox2);
        BookSwap book= list.get(position);
        txt_content.setText(book.getValue());
        ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ck.isChecked()) {
                    list.get(position).setIscheck(true);
                }
            }
        });


        return convertView;
    }

}
