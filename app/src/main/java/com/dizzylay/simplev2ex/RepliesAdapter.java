package com.dizzylay.simplev2ex;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * a
 * Name
 * Created by liaoy on 2018/4/6.
 */
public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ViewHolder> {

    private List<RepliesItem> repliesItemList;

    public RepliesAdapter(List<RepliesItem> repliesItemList) {
        this.repliesItemList = repliesItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.replies_content,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RepliesItem item = repliesItemList.get(position);
        holder.avatar.setImageBitmap(item.getAvatar());
        holder.username.setText(item.getUsername());
        holder.replyContent.setText(item.getReplyContent());
        holder.replyTime.setText(item.getReplyTime());
        holder.position.setText(position);
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
}
