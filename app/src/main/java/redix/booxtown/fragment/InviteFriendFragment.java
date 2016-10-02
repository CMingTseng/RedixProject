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


import redix.booxtown.R;
import redix.booxtown.controller.Information;
import redix.booxtown.controller.InviteController;
import redix.booxtown.custom.Custom_invite;
import redix.booxtown.custom.MenuBottomCustom;

public class InviteFriendFragment extends Fragment {

    EditText editText_email_friend;
    Button imv_invite;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.invite_friend_fragment, container, false);
        init(view);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.lv_content_invite);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        imv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteAsync inviteAsync = new inviteAsync(getContext(),editText_email_friend.getText().toString());
                inviteAsync.execute();
            }
        });
        //set adapter
//        Custom_invite custom_invite = new Custom_invite(prgmNameList);
//        recyclerView.setAdapter(custom_invite);

        return view;
    }

    public void init(View view){
        editText_email_friend = (EditText)view.findViewById(R.id.editText_email_friend);
        imv_invite = (Button) view.findViewById(R.id.imv_invite);
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
