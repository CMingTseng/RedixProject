package com.booxtown.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.custom.Custom_Listview_faq;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;

public class FaqFragment extends Fragment {
    public static List<String> prgmNameList;
    TextView editSearch;
    ImageView imageView_search_faq;
    RecyclerView listView_faq;
    Custom_Listview_faq custom_faq;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.faq_fragment, container, false);
        init(view);
        //picaso
        Picasso.with(getContext()).load(R.drawable.btn_locate_search).into(imageView_search_faq);
        //end
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(getContext());
        listView_faq.setLayoutManager(layoutManager);
        //set adapter
        custom_faq = new Custom_Listview_faq(getContext(),prgmNameList);
        listView_faq.setAdapter(custom_faq);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> array_faq = new ArrayList<String>();
                for(int k = 0;k<prgmNameList.size();k++){
                    String faq = prgmNameList.get(k).toLowerCase();
                    String search = editSearch.getText().toString().toLowerCase();
                    if(faq.contains(search)){
                        array_faq.add(prgmNameList.get(k));
                    }
                }
                custom_faq = new Custom_Listview_faq(getContext(),array_faq);
                listView_faq.setAdapter(custom_faq);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }
    public void init(View view){
        listView_faq = (RecyclerView)view.findViewById(R.id.lv_content_faq);
        imageView_search_faq = (ImageView)view.findViewById(R.id.imageView_search_faq);
        editSearch = (EditText)view.findViewById(R.id.editSearch);
        prgmNameList = new ArrayList<>();
        prgmNameList.add("General");
        prgmNameList.add("Swap");
        prgmNameList.add("Buy");
        prgmNameList.add("Donate");
    }
}
