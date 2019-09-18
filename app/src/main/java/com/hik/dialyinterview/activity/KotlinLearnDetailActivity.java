package com.hik.dialyinterview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hik.dialyinterview.R;
import com.hik.dialyinterview.bean.DetailBean;
import com.hik.dialyinterview.db.DetailService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class KotlinLearnDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    String url;
    TextView textItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kotlin_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url = "https://www.runoob.com" + getIntent().getStringExtra("url");
        textItem = (TextView) findViewById(R.id.textItem);

        getJsoup();
        int i;
    }


    private void getJsoup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsoupData();
            }
        }).start();
    }

    private void jsoupData() {
        try {
            Document document = Jsoup.connect(url).get();
            Log.d("aa", document.body().toString());
            Elements as = document.getElementsByClass("article-intro");
            for (int i = 0; i < as.size(); i++) {
                Element element = as.get(i);
                String text = element.text();//获取链接的标题
                Log.d("aa", text);
            }


            mHandler.sendEmptyMessage(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    break;
            }
        }
    };
}
