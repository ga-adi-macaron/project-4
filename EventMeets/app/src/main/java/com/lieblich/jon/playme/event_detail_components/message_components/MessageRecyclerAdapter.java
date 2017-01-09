package com.lieblich.jon.playme.event_detail_components.message_components;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lieblich.jon.playme.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by Jon on 1/3/2017.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SelfMessageObject> mMessages;
    private String mName;

    public MessageRecyclerAdapter(List<SelfMessageObject> list, String name) {
        mMessages = list;
        mName = name;
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
        SelfMessageObject message = mMessages.get(position);
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 0:
                ((MessageViewHolder)holder).mMessageText.setText(message.getContent());
                ((MessageViewHolder)holder).mSenderName.setText(message.getSender());
                ((MessageViewHolder)holder).mSenderIcon.setText(""+message.getSender().toUpperCase().charAt(0));
                break;
            case 1:
                ((SystemMessageViewHolder)holder).mMessage.setText(message.getContent());
                break;
            case 2:
                ((SelfMessageViewHolder)holder).mContent.setText(message.getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mMessages.get(position).getSender().equals(mName)) {
            return 2;
        }else if(mMessages.get(position).getSender().equals("system")) {
            return 1;
        } else {
            return 0;
        }
    }
}
