package com.luojituili.morefunny;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * Created by sherlockhua on 2016/10/30.
 */

public class JokePage extends Fragment {

    public JokePage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_page, container, false);
        ListView jokeList = (ListView) view.findViewById(R.id.joke_view);
        return view;

    }
}
