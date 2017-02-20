package com.booxtown.fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.booxtown.adapter.AdapterExplore;
import com.booxtown.controller.AboutController;
import com.booxtown.controller.FaqController;
import com.booxtown.custom.CustomEdittext;
import com.booxtown.custom.Custom_Listview_faq;
import com.booxtown.custom.DrawableClickListener;
import com.booxtown.model.About;
import com.booxtown.model.Faq;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.booxtown.R;

public class FaqFragment extends Fragment {
    public static List<String> prgmNameList;
    CustomEdittext editSearch;
    ImageView imageView_search_faq;
    RecyclerView listView_faq;
    Custom_Listview_faq custom_faq;
    boolean flagClosSearch=false;
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

        FAQAsync faqAsync= new FAQAsync(getActivity());
        faqAsync.execute();

        return view;
    }
    public void init(View view){
        listView_faq = (RecyclerView)view.findViewById(R.id.lv_content_faq);
        imageView_search_faq = (ImageView)view.findViewById(R.id.imageView_search_faq);
        editSearch = (CustomEdittext)view.findViewById(R.id.editSearch);
        editSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }
    class FAQAsync extends AsyncTask<Void, Void, ArrayList<Faq>> {
        Context context;

        public FAQAsync(Context context) {
            this.context = context;

        }

        ProgressDialog dialog;

        @Override
        protected ArrayList<Faq> doInBackground(Void... pra) {
            FaqController userController = new FaqController();
            return  userController.getAllFAQ();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final ArrayList<Faq> faq) {
            try {
                prgmNameList = new ArrayList<>();
                if(faq.size()>0){
                    for(int i=0; i<faq.size(); i++){
                        if(!prgmNameList.contains(faq.get(i).getCategory_name())){
                            prgmNameList.add(faq.get(i).getCategory_name());
                        }
                    }
                }
                custom_faq = new Custom_Listview_faq(getContext(),prgmNameList,faq);
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
                        custom_faq = new Custom_Listview_faq(getContext(),array_faq,faq);
                        listView_faq.setAdapter(custom_faq);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(editSearch.getText().toString().trim().length()>0) {
                            editSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.closes, 0);
                            flagClosSearch=true;
                        }else {
                            editSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            flagClosSearch=false;
                        }
                    }
                });

                editSearch.setDrawableClickListener(new DrawableClickListener() {
                    public void onClick(DrawablePosition target) {
                        switch (target) {
                            case LEFT:
                                //Do something here
                                break;
                            case RIGHT:
                                if(flagClosSearch) {
                                    editSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                    editSearch.setText("");
                                    flagClosSearch = false;
                                }
                                break;
                            case TOP:
                                //Do something here
                                break;
                            case BOTTOM:
                                //Do something here
                                break;
                            default:
                                break;
                        }
                    }

                });

            } catch (Exception e) {
                String sss= e.getMessage();
                //Toast.makeText(context, "no data", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }
}
