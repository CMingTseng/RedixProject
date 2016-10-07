package redix.booxtown.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import redix.booxtown.R;
import redix.booxtown.adapter.AdapterInvite;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.InviteController;
import redix.booxtown.custom.Custom_invite;
import redix.booxtown.custom.MenuBottomCustom;

public class InviteFriendFragment extends Fragment {

    EditText editText_email_friend;
    Button imv_invite;
    ImageView imv_search_invite;
    List<String> invite;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    String email="";
    AdapterInvite adapterInvite;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.invite_friend_fragment, container, false);
        init(view);
        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.lv_content_invite);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapterInvite = new AdapterInvite(getContext(),invite);
        recyclerView.setAdapter(adapterInvite);
        imv_search_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEmail(editText_email_friend.getText().toString())){
                    invite.add(editText_email_friend.getText().toString());
                    email +=editText_email_friend.getText().toString()+",";
                    adapterInvite = new AdapterInvite(getContext(),invite);
                    adapterInvite.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterInvite);
                    editText_email_friend.setText("");
                }else {
                    Toast.makeText(getContext(),Information.noti_enter_email,Toast.LENGTH_SHORT).show();
                }
            }
        });
        imv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!email.equals("")) {
                        email=email.substring(0,email.length()-2);
                        inviteAsync inviteAsync = new inviteAsync(getContext(), email);
                        inviteAsync.execute();
                        email = "";
                        invite.clear();
                        adapterInvite = new AdapterInvite(getContext(),invite);
                        adapterInvite.notifyDataSetChanged();
                        recyclerView.setAdapter(adapterInvite);
                    }
                    else if(email.equals("") && !editText_email_friend.getText().toString().equals("")){
                        if(checkEmail(editText_email_friend.getText().toString())) {
                            inviteAsync inviteAsync = new inviteAsync(getContext(), editText_email_friend.getText().toString());
                            inviteAsync.execute();
                            editText_email_friend.setText("");
                        }else{
                            Toast.makeText(getContext(),Information.noti_enter_email,Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(), Information.noti_enter_email, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){}
            }
        });
        return view;
    }

    public void init(View view){
        invite = new ArrayList<>();
        imv_search_invite = (ImageView)view.findViewById(R.id.imv_search_invite);
        editText_email_friend = (EditText)view.findViewById(R.id.editText_email_friend);
        imv_invite = (Button) view.findViewById(R.id.imv_invite);
    }

    public boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    class inviteAsync extends AsyncTask<Void,Void,Boolean>{

        Context context;
        String email;
        ProgressDialog dialog;
        public inviteAsync(Context context,String email){
            this.context = context;
            this.email = email;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage(Information.noti_dialog);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            InviteController inviteController = new InviteController();
            return inviteController.inviteFriend(email);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            try {
                if(aBoolean == true){
                    dialog.dismiss();
                    Toast.makeText(context,Information.noti_invite_success,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,Information.noti_invite_fail,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }catch (Exception e){

            }
            dialog.dismiss();
        }
    }
}
