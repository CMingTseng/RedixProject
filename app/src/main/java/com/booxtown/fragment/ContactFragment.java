package com.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.booxtown.R;
import com.booxtown.activity.HomeActivity;
import com.booxtown.activity.ListingsDetailActivity;
import com.booxtown.controller.BookController;
import com.booxtown.controller.Information;
import com.booxtown.model.Book;
import com.booxtown.model.Contact;

import java.util.List;

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
                String sss=editText_message.getText().toString().trim();
                if(editText_message.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(), "Please enter valid a message!", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    String session_id = pref.getString("session_id", null);
                    Contact contact= new Contact(session_id, editText_message.getText().toString().trim());
                    insertContact insertContact= new insertContact(getContext(),contact);
                    insertContact.execute();

                }
            }
        });
        return view;
    }
    class insertContact extends AsyncTask<Void, Void, Boolean> {
        Contact contact;
        Context ctx;
        ProgressDialog dialog;

        public insertContact(Context ctx, Contact contact) {
            this.contact = contact;
            this.ctx = ctx;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            BookController bookController = new BookController();
            return bookController.insertContact(contact);
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ctx);
            dialog.setMessage(Information.noti_dialog);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            try {
                if (flag) {
                    Toast.makeText(getContext(), "Send message success", Toast.LENGTH_SHORT).show();
                    editText_message.setText("");
                    dialog.dismiss();
                }
            } catch (Exception e) {
                dialog.dismiss();
            }
            dialog.dismiss();

        }
    }
}
