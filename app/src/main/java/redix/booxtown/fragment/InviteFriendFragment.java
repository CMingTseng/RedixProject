package redix.booxtown.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import redix.booxtown.R;
import redix.booxtown.custom.Custom_invite;
import redix.booxtown.custom.MenuBottomCustom;

public class InviteFriendFragment extends Fragment {
    public static String [] prgmNameList={"Home","Notifications","FAQ","Invite friends"};
    private MenuBottomCustom bottomListings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.invite_friend_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.lv_content_invite);
        RecyclerView.LayoutManager  layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //set adapter
        Custom_invite custom_invite = new Custom_invite(prgmNameList);
        recyclerView.setAdapter(custom_invite);
        return view;
    }
}
