package com.dizzylay.simplev2ex;

import android.net.Uri;
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
 * Created by liaoy on 2018/4/3.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<ListItem> itemList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        ImageView avatar;
        TextView title;
        TextView nodeTitle;
        TextView username;
        TextView replies;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            avatar = itemView.findViewById(R.id.avatar);
            title = itemView.findViewById(R.id.title);
            nodeTitle = itemView.findViewById(R.id.node_title);
            username = itemView.findViewById(R.id.username);
            replies = itemView.findViewById(R.id.replies);
        }
    }

    public ItemAdapter(List<ListItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,
                false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem item = itemList.get(position);
        holder.avatar.setImageURI(Uri.parse(item.getAvatar()));
        holder.title.setText(item.getTitle());
        holder.nodeTitle.setText(item.getNodeTitle());
        holder.username.setText(item.getUsername());
        holder.replies.setText(item.getReplies());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}