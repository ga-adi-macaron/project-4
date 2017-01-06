package com.example.jon.eventmeets.main_menu;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jon.eventmeets.R;
import com.example.jon.eventmeets.event_detail_components.message_components.ChatGroupActivity;
import com.example.jon.eventmeets.model.GameResultObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenuRecyclerAdapter extends RecyclerView.Adapter<MainMenuEventParentViewHolder>{
    private List<String> mEventList;
    private List<String> mNames;
    private Map<String, String> mChats;

    public MainMenuRecyclerAdapter(Map<String, String> list) {
        mChats = list;
        mNames = new ArrayList<>(mChats.values());
        mEventList = new ArrayList<>(mChats.keySet());
    }

    @Override
    public MainMenuEventParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainMenuEventParentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_menu_event_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final MainMenuEventParentViewHolder holder, int position) {
        String user = mNames.get(position);
        holder.mParentName.setText(user);
        holder.mUserInitial.setText(""+Character.toUpperCase(user.charAt(0)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mEventList.get(holder.getAdapterPosition());
                Intent intent = new Intent(holder.mParentName.getContext(), ChatGroupActivity.class);
                intent.putExtra("chatKey", id);
                holder.mParentName.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
