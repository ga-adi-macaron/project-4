package com.colinbradley.xboxoneutilitiesapp.profile_page.friends_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colinbradley.xboxoneutilitiesapp.R;

import java.util.List;

/**
 * Created by colinbradley on 12/19/16.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListViewHolder>{

    private List<Friend> mFriendsList;
    private OnItemSelectedListener mOnItemSelectedListener;
    private Context mContext;

    public FriendsListAdapter(List<Friend> friendsList, Context context, OnItemSelectedListener listener){
        mFriendsList = friendsList;
        mContext = context;
        mOnItemSelectedListener = listener;
    }

    public interface OnItemSelectedListener{
        void onItemSelected(int id);
    }

    @Override
    public FriendsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_rv_item, parent, false);
        return new FriendsListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendsListViewHolder holder, int position) {
        final Friend currentFriend = mFriendsList.get(position);

        holder.mGamertag.setText(mFriendsList.get(position).getGamertag());
        holder.bindImage(mFriendsList.get(position).getPicURL(), mContext);

        holder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemSelectedListener.onItemSelected(currentFriend.getXuid());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFriendsList.size();
    }
}
