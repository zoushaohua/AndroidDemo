package com.hik.dialyinterview.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.hik.dialyinterview.R;
import com.hik.dialyinterview.bean.DialyBean;
import com.hik.dialyinterview.db.DialyService;
import com.hik.dialyinterview.util.PreferenceUtil;
import com.hik.dialyinterview.view.ScaleInTopAnimator;
import com.hik.dialyinterview.view.onLoadMoreListener;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    List<DialyBean> dialyBeanList;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    private MyAdapter myAdapter;
    private LinearLayoutManager layoutManager;
    private Handler handler;
    PreferenceUtil _pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        _pref = PreferenceUtil.getInstance(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        dialyBeanList = DialyService.getInstance().loadAll();
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
                viewHolder.textView.setText(dialyBeanList.get(position).getTitle() + "：" + dialyBeanList.get(position).getContent());
                viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, DialyDetailActivity.class);
                        intent.putExtra("url", dialyBeanList.get(position).getUrl());
                        intent.putExtra("title", dialyBeanList.get(position).getContent());
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

    private class FootViewHolder extends RecyclerView.ViewHolder {
        ContentLoadingProgressBar contentLoadingProgressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            contentLoadingProgressBar = itemView.findViewById(R.id.pb_progress);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void jsoupData() {
        String url = "https://github.com/Moosphan/Android-Daily-Interview";
        try {
            dialyBeanList = new ArrayList<>();
            Document document = Jsoup.connect(url).get();
            Elements as = document.getElementsByTag("a");
            int t = 1;
            for (int i = as.size() - 1; i > 0; i--) {
                Element element = as.get(i);
                String href = element.attr("href");//获取链接的url值
                String text = element.text();//获取链接的标题
                if (href.startsWith("https://github.com/Moosphan/Android-Daily-Interview/issues/")) {//筛选以http开头的链接
                    System.out.println("面试题" + href + " " + text);
                    dialyBeanList.add(new DialyBean(UUID.randomUUID().toString(), "第" + t + "题", text, href));
                    if (DialyService.getInstance().queryByUrl(href) == null)
                        DialyService.getInstance().insertOrUpdate(new DialyBean(UUID.randomUUID().toString(), "第" + t + "题", text, href));
                    t++;
                }
            }

            _pref.setFirst(false);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, KotlinLearn.class));
        } else if (id == R.id.nav_gallery) {
            showDialog(true, "aaaa");
        } else if (id == R.id.nav_slideshow) {

            showDialog(false, "bbb");
        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    AlertDialog.Builder builder;

    private void showDialog(final boolean flag, final String str) {

        builder = new AlertDialog.Builder(this);
        builder.setTitle("11");
        builder.setMessage("1111");
        builder.setPositiveButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "" + flag, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("不好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "~" + flag, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("差", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "" + flag, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
