package com.luojituili.morefunny;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sherlockhua on 2016/10/30.
 */

public class DiscoveryPage extends Fragment {


    public DiscoveryPage() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content, container, false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
        txt_content.setText("Picture Page");
        Log.e("HEHE", "1日狗");
        return view;
    }
}
