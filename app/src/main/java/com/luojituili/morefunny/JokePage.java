package com.luojituili.morefunny;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by sherlockhua on 2016/10/30.
 */

public class JokePage extends Fragment {

    public JokePage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.joke_page, container, false);
        View view = inflater.inflate(R.layout.fg_content, container, false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
        txt_content.setText("joke Page");
        //Log.e("HEHE", "1日狗");
        return view;
        //ListView jokeView = (ListView) view.findViewById(R.id.joke_view);

/*
        jokeView.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        if (Build.VERSION.SDK_INT >= 19) {
            jokeView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存
        }

        jokeView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        jokeView.loadUrl("http://www.baidu.com/");          //调用loadView方法为WebView加入链接
*/
       // return view;
    }
}
