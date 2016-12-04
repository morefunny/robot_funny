package com.luojituili.morefunny;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
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
    private SimpleDiskCache _threadCache = null;
    private final String _configFileName = "update.index";
    private  int _curUpdateIndex = 0;
    private int _curHistoryIndex = 0;

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



    private void toast(String text) {
        Toast toast = Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return;
    }

    private ReceiveThreadListHandler _handler = new ReceiveThreadListHandler() {
        public void onReceiveThreadList(int code, int catId, ArrayList<Thread> data){

            Log.e("catId", String.format("%d", catId));
            Log.e("count", String.format("%d", data.size()));
            for (int i = 0; i< data.size(); i++) {
                Log.e("threadId", String.format("%d", data.get(i).getThreadId()));
            }

            if (code == 200) {

                try {
                    if (data.size() > 0) {
                        JokePage.this.toast(String.format("本次更新了%d个内容", data.size()));
                        _curUpdateIndex++;
                        _threadCache.putArrayList(getCacheKey(_curUpdateIndex), data);
                    } else {
                        JokePage.this.toast("没有发现新内容，休息一下吧！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                _adapter.addThreadList(data);
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

        try {
            _threadCache = SimpleDiskCache.getCache(this.getContext().getString(R.string.text_cache));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        jokeList.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    //判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        try {
                            _curHistoryIndex--;
                            if (_curHistoryIndex <= 0) {
                                return;
                            }

                            ArrayList<Thread> threadList = _threadCache.getArrayList(getCacheKey(_curHistoryIndex));
                            _adapter.appendThreadList(threadList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    int position = view.getFirstVisiblePosition();
                    _adapter.onItemVisible(position);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //loadData();
        }
    }

    public String getUpdateIndexKey() {
        return String.format("%d-index", _categoryId);
    }

    public String getCacheKey(int curIndex) {
        return String.format("%d-%d", _categoryId, curIndex);
    }

    @Override
    public  void onResume() {
        Log.e("jokepage", "resume");
        super.onResume();

        SharedPreferences sp = getContext().getSharedPreferences(_configFileName, Context.MODE_PRIVATE);
        _curUpdateIndex = sp.getInt(getUpdateIndexKey(), 0);
        if (_curUpdateIndex <= 0) {
            return;
        }

        _curHistoryIndex = _curUpdateIndex;
        try {
            ArrayList<Thread> threadList = _threadCache.getArrayList(getCacheKey(_curUpdateIndex));
            if (threadList.size() == 0) {
                return;
            }

            _adapter.appendThreadList(threadList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        Log.e("jokepage", "stop");
        SharedPreferences sp = getContext().getSharedPreferences(_configFileName, Context.MODE_PRIVATE);
        sp.edit().putInt(getUpdateIndexKey(), _curUpdateIndex).commit();
        super.onStop();
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    _robotApi.getJokeList(1001, _categoryId, 10, _handler);
                }
            }, 365
        );
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
