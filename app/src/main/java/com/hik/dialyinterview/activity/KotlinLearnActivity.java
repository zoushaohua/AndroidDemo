package com.hik.dialyinterview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hik.dialyinterview.R;
import com.hik.dialyinterview.bean.KotlinBean;
import com.hik.dialyinterview.db.KotlinService;
import com.hik.dialyinterview.view.ScaleInTopAnimator;
import com.hik.dialyinterview.view.onLoadMoreListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KotlinLearnActivity extends AppCompatActivity {
    List<KotlinBean> kotlinBeanList;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        kotlinBeanList = KotlinService.getInstance().loadAll();
        init();

    }


    private void init() {
        myAdapter = new MyAdapter();
        handler = new Handler();
        layoutManager = new LinearLayoutManager(this);

        refreshLayout = findViewById(R.id.swiperefreshlayout);
        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new ScaleInTopAnimator());
        recyclerView.setAdapter(myAdapter);

        //设置下拉时圆圈的颜色（可以尤多种颜色拼成）
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light);
        //设置下拉时圆圈的背景颜色（这里设置成白色）
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        ;
                    }
                }, 3000);
            }
        });

        recyclerView.addOnScrollListener(new onLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //getData("loadMore");
                    }
                }, 3000);
            }
        });

        if (kotlinBeanList == null || kotlinBeanList.size() == 0) {
            refreshLayout.setRefreshing(true);
            getJsoup();
        } else {
            myAdapter.notifyDataSetChanged();
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
        }
    }

    private void getJsoup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsoupData();
            }
        }).start();
    }

    private void getData(final String type) {
        if ("refresh".equals(type)) {

        } else {

        }
        myAdapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        if ("refresh".equals(type)) {
            //  Toast.makeText(getApplicationContext(), "刷新完毕", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(getApplicationContext(), "加载完毕", Toast.LENGTH_SHORT).show();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_CONTENT = 0;//正常内容
        private final static int TYPE_FOOTER = 1;//加载View

        @Override
        public int getItemViewType(int position) {
            if (position == kotlinBeanList.size()) {
                return TYPE_FOOTER;
            }
            return TYPE_CONTENT;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //if (viewType==TYPE_FOOTER){
            //    View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_main_foot, parent, false);
            //     return new FootViewHolder(view);
            // }
            // else {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
            // }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {

            } else {
                MyViewHolder viewHolder = (MyViewHolder) holder;
                viewHolder.textView.setText("第" + (position + 1) + "章 " + kotlinBeanList.get(position).getTitle());
                viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(KotlinLearnActivity.this, KotlinLearnDetailActivity.class);
                        intent.putExtra("url", kotlinBeanList.get(position).getUrl());
                        intent.putExtra("title", kotlinBeanList.get(position).getTitle());
                        startActivity(intent);
                    }
                });
            }
            layoutManager.getChildCount();
            layoutManager.getItemCount();
            layoutManager.findLastVisibleItemPosition();
        }


        @Override
        public int getItemCount() {
            if (kotlinBeanList == null)
                return 0;
            return kotlinBeanList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textItem);
        }
    }

    private void jsoupData() {
        String url = "https://www.runoob.com/kotlin/kotlin-tutorial.html";
        try {
            kotlinBeanList = new ArrayList<>();
            Document document = Jsoup.connect(url).get();
            Elements as = document.getElementsByTag("a");

            int t = 1;
            for (int i = 0; i < as.size(); i++) {
                Element element = as.get(i);
                String href = element.attr("href");//获取链接的url值
                if (href.startsWith("/kotlin/kotlin-")) {
                    String text = element.attr("title");//获取链接的标题
                    kotlinBeanList.add(new KotlinBean(UUID.randomUUID().toString(), "kotlin " + text, href));
                    if (KotlinService.getInstance().queryByUrl(href) == null)
                        KotlinService.getInstance().insertOrUpdate(new KotlinBean(UUID.randomUUID().toString(), "kotlin " + text, href));
                    t++;
                }
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
                    myAdapter.notifyDataSetChanged();
                    if (refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(false);
                    }
                    break;
            }
        }
    };
}
