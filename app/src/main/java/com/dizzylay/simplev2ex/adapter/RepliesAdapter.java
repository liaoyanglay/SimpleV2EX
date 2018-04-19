package com.dizzylay.simplev2ex.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dizzylay.simplev2ex.R;
import com.dizzylay.simplev2ex.javabean.RepliesItem;

import java.util.List;

/**
 * a
 * Name
 * Created by liaoy on 2018/4/6.
 */
public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ViewHolder> {

    private List<RepliesItem> repliesItemList;
    private OnItemClickListener mOnClickListener;

    public RepliesAdapter(List<RepliesItem> repliesItemList) {
        this.repliesItemList = repliesItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.replies_content,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.avatar.setOnClickListener(v -> mOnClickListener.onItemClick(v, (int) holder
                .itemView.getTag()));
        holder.username.setOnClickListener(v -> mOnClickListener.onItemClick(v, (int) holder
                .itemView.getTag()));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RepliesItem item = repliesItemList.get(position);
        holder.avatar.setImageBitmap(item.getAvatar());
        holder.username.setText(item.getUsername());
        holder.replyContent.setText(item.getReplyContent());
        holder.replyContent.setMovementMethod(LinkMovementMethod.getInstance());
        holder.replyTime.setText(item.getReplyTime());
        holder.position.setText(String.valueOf(position + 1));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return repliesItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView username;
        TextView replyContent;
        TextView replyTime;
        TextView position;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_reply);
            username = itemView.findViewById(R.id.username_reply);
            replyContent = itemView.findViewById(R.id.content_reply);
            replyTime = itemView.findViewById(R.id.time_reply);
            position = itemView.findViewById(R.id.position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
