package com.booxtown.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.booxtown.R;

public class ContactFragment extends Fragment {
    EditText editText_message;
    Button btn_send_message;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        editText_message = (EditText)view.findViewById(R.id.editText_message);
        btn_send_message = (Button)view.findViewById(R.id.btn_send_message);
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Send message success", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
