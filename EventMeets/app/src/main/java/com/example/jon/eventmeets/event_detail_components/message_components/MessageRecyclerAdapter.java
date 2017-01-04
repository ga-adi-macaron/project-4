package com.example.jon.eventmeets.event_detail_components.message_components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.jon.eventmeets.R;

import java.util.List;

/**
 * Created by Jon on 1/3/2017.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SelfMessageObject> mMessages;

    public MessageRecyclerAdapter(List<SelfMessageObject> list) {
        mMessages = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch(viewType) {
            case 0:
                return new MessageViewHolder(inflater.inflate(R.layout.message_layout, parent, false));
            case 1:
                return new SystemMessageViewHolder(inflater.inflate(R.layout.system_message_layout, parent, false));
            case 2:
                return new SelfMessageViewHolder(inflater.inflate(R.layout.self_message_layout, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mMessages.get(position) instanceof MessageObject) {
            return 0;
        } else if(mMessages.get(position) instanceof SystemMessageObject){
            return 1;
        } else {
            return 2;
        }
    }
}
