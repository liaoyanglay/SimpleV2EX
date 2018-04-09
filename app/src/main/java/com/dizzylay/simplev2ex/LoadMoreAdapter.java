package com.dizzylay.simplev2ex;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by dizzylay on 2018/4/9.
 */
public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter adapter;

    private final static int TYPE_ITEM = 1;
    private final static int TYPE_FOOTER = 2;

    public final static int LOADING = 0;
    public final static int LOADING_COMPLETE = 1;
    public final static int LOADING_END = 2;

    private int loadState = LOADING_COMPLETE;

    public LoadMoreAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_view,
                    parent, false);
            return new FootViewHolder(view);
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING:
                    footViewHolder.loadMore.setText("loading...");
                    footViewHolder.loadMore.setVisibility(View.VISIBLE);
                    break;
                case LOADING_COMPLETE:
                    footViewHolder.loadMore.setVisibility(View.INVISIBLE);
                    break;
                case LOADING_END:
                    footViewHolder.loadMore.setText("--已经到底啦--");
                    footViewHolder.loadMore.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        } else {
            adapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount() + 1;
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        TextView loadMore;

        public FootViewHolder(View itemView) {
            super(itemView);
            loadMore = itemView.findViewById(R.id.load_more);
        }
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
}
