package redix.booxtown.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import redix.booxtown.R;
import redix.booxtown.activity.HomeActivity;
import redix.booxtown.activity.MenuActivity;
import redix.booxtown.custom.MenuBottomCustom;

public class RateFragment extends Fragment {
    Button btn_rate_now,btn_rate_later;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rate_fragment, container, false);
        btn_rate_now = (Button) view.findViewById(R.id.btn_rate_now);
        btn_rate_later = (Button) view.findViewById(R.id.btn_rate_later);

        btn_rate_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MenuActivity.class);
                startActivity(intent);
            }
        });

        btn_rate_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + getContext().getPackageName())));
                }
            }
        });
        return view;
    }
}
