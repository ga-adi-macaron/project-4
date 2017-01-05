package com.example.jon.eventmeets.event_detail_components.message_components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jon.eventmeets.R;

/**
 * Created by Jon on 1/3/2017.
 */

public class SelfMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView mContent;

    public SelfMessageViewHolder(View itemView) {
        super(itemView);

        mContent = (TextView)itemView.findViewById(R.id.message_text);
    }
}
