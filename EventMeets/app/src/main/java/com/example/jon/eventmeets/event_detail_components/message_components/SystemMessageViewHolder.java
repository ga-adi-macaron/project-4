package com.example.jon.eventmeets.event_detail_components.message_components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jon.eventmeets.R;

/**
 * Created by Jon on 1/4/2017.
 */

public class SystemMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView mMessage;

    public SystemMessageViewHolder(View itemView) {
        super(itemView);

        mMessage = (TextView)itemView.findViewById(R.id.system_chat_message);
    }
}
