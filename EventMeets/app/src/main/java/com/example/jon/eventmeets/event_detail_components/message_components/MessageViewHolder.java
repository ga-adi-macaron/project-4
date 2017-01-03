package com.example.jon.eventmeets.event_detail_components.message_components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jon.eventmeets.R;

/**
 * Created by Jon on 1/3/2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView mMessageText;
    public TextView mSenderName;
    public ImageView mUserPhoto;

    public MessageViewHolder(View itemView) {
        super(itemView);

    }
}
