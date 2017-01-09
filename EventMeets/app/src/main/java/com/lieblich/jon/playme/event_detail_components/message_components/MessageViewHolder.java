package com.lieblich.jon.playme.event_detail_components.message_components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lieblich.jon.playme.R;

/**
 * Created by Jon on 1/3/2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView mMessageText;
    public TextView mSenderName;
    public TextView mSenderIcon;

    public MessageViewHolder(View itemView) {
        super(itemView);

        mMessageText = (TextView)itemView.findViewById(R.id.message_layout_text);
        mSenderIcon = (TextView) itemView.findViewById(R.id.user_first_letter);
        mSenderName = (TextView)itemView.findViewById(R.id.message_sender_name);
    }
}
