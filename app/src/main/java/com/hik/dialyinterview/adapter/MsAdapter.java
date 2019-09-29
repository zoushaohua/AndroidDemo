package com.hik.dialyinterview.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hik.dialyinterview.R;
import com.hik.dialyinterview.activity.androidmianshi.MSDetailActivity;
import com.hik.dialyinterview.bean.MsBean;


public class MsAdapter extends RecyclerView.Adapter<MsAdapter.ViewHolder> {
    private MsBean[] mList;
    private Context mContext;

    public MsAdapter(Context context, MsBean[] list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MsBean item = mList[position];
        holder.textItem.setText(item.getTitle());

        holder.textItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MSDetailActivity.class);
                intent.putExtra("content", item.getContent());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textItem;

        public ViewHolder(View itemView) {
            super(itemView);
            textItem = (TextView) itemView.findViewById(R.id.textItem);
        }
    }

}
