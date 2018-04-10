package com.dizzylay.simplev2ex;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dizzylay on 2018/4/9.
 */
public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter adapter;

    private View headerView = null;
    private View footerView = null;

    private final static int TYPE_HEADER = 0;
    private final static int TYPE_ITEM = 1;
    private final static int TYPE_FOOTER = 2;

    public final static int LOADING = 0;
    public final static int LOADING_COMPLETE = 1;
    public final static int LOADING_END = 2;

    private int loadState = LOADING_COMPLETE;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerView != null) {
            return TYPE_HEADER;
        } else if (footerView != null) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new FootViewHolder(headerView);
        } else if (viewType == TYPE_HEADER) {
            return new HeadViewHolder(footerView);
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
                    footViewHolder.loadMore.setVisibility(View.GONE);
                    break;
                case LOADING_END:
                    footViewHolder.loadMore.setText("--已经到底啦--");
                    footViewHolder.loadMore.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            return;
        } else  if (holder instanceof HeadViewHolder) {
            return;
        }
        adapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        if (headerView != null) {
            if (footerView != null) {
                return adapter.getItemCount() + 2;
            }
            return adapter.getItemCount() + 1;
        } else if (footerView != null) {
            return adapter.getItemCount() + 1;
        }
        return adapter.getItemCount();
    }

    public int getRealItemCount() {
        return adapter.getItemCount();
    }

    public void setHeaderView(View v) {
        this.headerView = v;
    }

    public void setFooterView(View v) {
        this.footerView = v;
    }

    public View getHeaderView() {
        return headerView;
    }

    public View getFooterView() {
        return footerView;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView title;
        TextView nodeTitle;
        TextView username;
        TextView time;

        public HeadViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_header);
            title = itemView.findViewById(R.id.title_header);
            nodeTitle = itemView.findViewById(R.id.node_title_header);
            username = itemView.findViewById(R.id.username_header);
            time = itemView.findViewById(R.id.time_header);
        }

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
