package com.hik.dialyinterview.activity.androidmianshi;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hik.dialyinterview.BaseActivity;
import com.hik.dialyinterview.R;
import com.hik.dialyinterview.adapter.MsAdapter;
import com.hik.dialyinterview.bean.MsBean;
import com.hik.dialyinterview.view.BaseRecyclerView;
import com.hik.dialyinterview.view.LoadMoreListener;
import com.hik.dialyinterview.view.SpaceItemDecoration;

public class AndroidMSActivity extends BaseActivity {
    SwipeRefreshLayout mRefreshLayout;
    BaseRecyclerView mRecyclerView;
    MsBean[] list;
    MsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_mianshi);

        initView();
    }

    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRecyclerView = (BaseRecyclerView) findViewById(R.id.recycler_view);


        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(30));
        mRecyclerView.setCanloadMore(false);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        String[] titles = getResources().getStringArray(R.array.ms_title);
        String[] contents = getResources().getStringArray(R.array.ms_content);

        list = new MsBean[titles.length];
        for (int i = 0; i < titles.length; i++) {
            MsBean msBean = new MsBean();
            msBean.setTitle(titles[i]);
            msBean.setContent(contents[i]);
            list[i] = msBean;
        }

        adapter = new MsAdapter(this, list);
        mRecyclerView.setAdapter(adapter);

    }

}
