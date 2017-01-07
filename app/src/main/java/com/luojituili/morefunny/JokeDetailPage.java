package com.luojituili.morefunny;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import api.Thread;

/**
 * Created by sherlockhua on 2017/1/1.
 */

public class JokeDetailPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke_detail_page);

        Intent intent = getIntent();
        Thread thread = (Thread)intent.getSerializableExtra("thread");
        Log.e("thread", thread.getTitle());

        initView(thread);
    }

    public void initView(Thread thread) {

        ListView detailList = (ListView) findViewById(R.id.joke_detail_view);
        View convertView = LayoutInflater.from(this.getApplicationContext()).inflate(
                R.layout.joke_title_view, detailList,false);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.robot_title);
        String title = thread.getTitle();

        if (title.length() > 0 && !title.equalsIgnoreCase("null")) {
            titleTextView.setText(thread.getTitle());
            detailList.addHeaderView(convertView);
        } else {
            detailList.setPadding(0, 48, 0, 0);
        }

        JokeDetailListAdapter adapter = new JokeDetailListAdapter(
                JokeDetailPage.this.getApplicationContext(), detailList, thread);
        detailList.setAdapter(adapter);
    }

}
