package com.luojituili.morefunny;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import api.ReceiveThreadListHandler;
import api.RobotApi;
import api.Thread;


/**
 * Created by sherlockhua on 2016/10/30.
 */

public class JokePage extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    private SwipeRefreshLayout _swipeLayout;
    private JokeListAdapter _adapter;
    private String _categoryName;
    private int _categoryId;
    private RobotApi _robotApi = new RobotApi();

    public JokePage() {
    }

    public void setCategoryId(int categoryId) {
        _categoryId = categoryId;
    }

    public void setCategoryName(String name) {
        _categoryName = name;
    }

    public String getCategoryName() {

        return _categoryName;
    }

    public int getCategoryId() {
        return _categoryId;
    }


    private ReceiveThreadListHandler _handler = new ReceiveThreadListHandler() {
        public void onReceiveThreadList(int code, int catId, ArrayList<Thread> data){

            Log.e("catId", String.format("%d", catId));
            Log.e("count", String.format("%d", data.size()));
            for (int i = 0; i< data.size(); i++) {
                Log.e("threadId", String.format("%d", data.get(i).getThreadId()));
            }

            if (code == 200) {
                _adapter.appendThreadList(data);
            }

            _swipeLayout.setRefreshing(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_page, container, false);
        _swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        _swipeLayout.setOnRefreshListener(this);


        ListView jokeList = (ListView) view.findViewById(R.id.joke_view);
        _adapter = new JokeListAdapter(JokePage.this.getContext(), this, _swipeLayout, jokeList);
        jokeList.setAdapter(_adapter);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadData();
        }
    }

    @Override
    public  void onResume() {
        Log.e("jokepage", "resume");
        //loadData();
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.e("jokepage", "stop");
        super.onStop();
    }

    public void onRefresh() {
        _robotApi.getJokeList(1001, _categoryId, 10, _handler);
    }

    public void loadData() {
        if (_swipeLayout == null) {
            return;
        }

        _swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                _swipeLayout.setRefreshing(true);
            }
        });

        onRefresh();
    }
}
