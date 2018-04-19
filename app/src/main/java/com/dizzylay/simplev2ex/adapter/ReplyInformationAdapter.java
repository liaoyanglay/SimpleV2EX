package com.dizzylay.simplev2ex.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dizzylay.simplev2ex.R;
import com.dizzylay.simplev2ex.javabean.ReplyInformation;

import java.util.List;

/**
 * Created by dizzylay on 2018/4/13.
 */
public class ReplyInformationAdapter extends RecyclerView.Adapter<ReplyInformationAdapter
        .ViewHolder> {

    private List<ReplyInformation> informationList;
    private OnItemClickListener onItemClickListener;

    public ReplyInformationAdapter(List<ReplyInformation> informationList) {
        this.informationList = informationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_reply, parent,
                false);
        ViewHolder holder = new ViewHolder(view);
//        holder.replyDetails.setOnClickListener(v -> {
//            onItemClickListener.onItemClick(v, (int) holder.itemView.getTag());
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReplyInformation information = informationList.get(position);
        holder.replyDetails.setText(information.getReplyDetails());
        holder.replyContent.setText(information.getReplyContent());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView replyDetails;
        TextView replyContent;

        ViewHolder(View itemView) {
            super(itemView);
            replyDetails = itemView.findViewById(R.id.reply_information);
            replyContent = itemView.findViewById(R.id.reply_content);
        }
    }
}
