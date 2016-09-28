package redix.booxtown.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import redix.booxtown.R;
import redix.booxtown.activity.MainAllActivity;
import redix.booxtown.custom.Custom_Listview_faq;
import redix.booxtown.custom.MenuBottomCustom;

public class FaqFragment extends Fragment {
    public static String [] prgmNameList={"General","Sell","Swap","Buy","Donate"};
    private MenuBottomCustom bottomListings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.faq_fragment, container, false);

        //picaso
        ImageView imageView_search_faq = (ImageView)view.findViewById(R.id.imageView_search_faq);
        Picasso.with(getContext()).load(R.drawable.btn_locate_search).into(imageView_search_faq);
        //end
        RecyclerView listView_faq = (RecyclerView)view.findViewById(R.id.lv_content_faq);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(getContext());
        listView_faq.setLayoutManager(layoutManager);
        //set adapter
        Custom_Listview_faq custom_faq = new Custom_Listview_faq(getContext(),prgmNameList);
        listView_faq.setAdapter(custom_faq);
        return view;
    }
}
