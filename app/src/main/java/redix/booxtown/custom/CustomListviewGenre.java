package redix.booxtown.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import redix.booxtown.R;
import redix.booxtown.model.Genre;

/**
 * Created by thuyetpham94 on 30/08/2016.
 */
public class CustomListviewGenre extends BaseAdapter {
    ArrayList<Genre> result;
    Context context;
    private static LayoutInflater inflater=null;
    public CustomListviewGenre(Context context, ArrayList<Genre> prgmNameList) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        this.context=context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
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
        CheckBox checkBox;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_dialog_genre, null);
        holder.tv=(TextView) rowView.findViewById(R.id.txt_custom_listview_genre);
        holder.checkBox = (CheckBox) rowView.findViewById(R.id.checkBox2);
        holder.tv.setText(result.get(position).getValue());
        holder.checkBox.setChecked(result.get(position).ischeck());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result.get(position).ischeck()==false){
                    result.get(position).setIscheck(true);
                }else {
                    result.get(position).setIscheck(false);
                }
            }
        });
        return rowView;
    }
}
