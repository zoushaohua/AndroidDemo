package com.hik.dialyinterview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hik.dialyinterview.R;
import com.hik.dialyinterview.bean.DetailBean;
import com.hik.dialyinterview.db.DetailService;
import com.hik.dialyinterview.db.DialyService;
import com.hik.dialyinterview.util.PreferenceUtil;
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

public class DialyDetailActivity extends AppCompatActivity {
    String url;
    List<DetailBean> dialyBeanList;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    PreferenceUtil _pref;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        _pref = PreferenceUtil.getInstance(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url = getIntent().getStringExtra("url");
        init();
    }

    private void init() {
        myAdapter = new MyAdapter();
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

        // refreshLayout.setRefreshing(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new onLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //getData("loadMore");
                    }
                }, 3000);
            }
        });

        dialyBeanList = DetailService.getInstance().queryByUrl(url);
        if (dialyBeanList == null || dialyBeanList.size() == 0) {
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

    private void jsoupData() {
        try {
            Document document = Jsoup.connect(url).get();
            Elements as = document.getElementsByClass("d-block comment-body markdown-body  js-comment-body");
            Elements alt = document.getElementsByClass("participant-avatar");
            Log.d("size;", as.size() + "");
            Log.d("size;", alt.size() + "");
            dialyBeanList = new ArrayList<>();
            for (int i = 0; i < as.size(); i++) {
                Element element = as.get(i);
                String text = element.text();//获取链接的标题
                String name;
                if (alt.size() > i) {
                    Element element1 = alt.get(i);
                    name = element1.attr("href").replace("/", "");
                } else
                    name = "路人甲";

                dialyBeanList.add(new DetailBean(UUID.randomUUID().toString(), url, name, text));
                DetailBean detailBean = DetailService.getInstance().queryByText(url, text);
                if (detailBean == null)
                    DetailService.getInstance().insert(new DetailBean(UUID.randomUUID().toString(), url, name, text));
                int t = 1;
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

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_CONTENT = 0;//正常内容
        private final static int TYPE_FOOTER = 1;//加载View

        @Override
        public int getItemViewType(int position) {
            if (position == dialyBeanList.size()) {
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
                viewHolder.textView.setText(dialyBeanList.get(position).getName() + "：" + dialyBeanList.get(position).getContent());
            }
            layoutManager.getChildCount();
            layoutManager.getItemCount();
            layoutManager.findLastVisibleItemPosition();
        }


        @Override
        public int getItemCount() {
            if (dialyBeanList == null)
                return 0;
            return dialyBeanList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textItem);
        }
    }
}
