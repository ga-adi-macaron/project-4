package com.crocusgames.destinyinventorymanager.CharInventoryObjects;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crocusgames.destinyinventorymanager.Activities.CharInvActivity;
import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.ItemCompleteObject;
import com.crocusgames.destinyinventorymanager.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.crocusgames.destinyinventorymanager.AppConstants.BUNGIE_NET_START_URL;

/**
 * Created by Serkan on 14/12/16.
 */

public class FragmentCharWeapons extends Fragment {
    private View mView;
    private ItemCompleteObject mPrimaryEquipped, mSpecialEquipped, mHeavyEquipped, mGhostEquipped;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_charweapons_layout, container, false);
        mView = v;
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        if (characterInfo.isApiFinished()) {
            setFragmentCharWeapons(view, characterInfo);
        }

//        try {
//            setFragmentCharWeapons(view, characterInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void setFragmentCharWeapons(View view, final CharacterInfoSingleton characterInfo) {
        //PRIMARY WEAPON SET UP!

        //Dummy object is when api returns a classified object for some reason.
        ItemCompleteObject dummyObject = new ItemCompleteObject(null, null, null, AppConstants.MISSING_ICON_URL,
                null, null, null, null, -1, -1, null, -1, false, null);

        ImageView equippedPrimaryIcon = (ImageView) view.findViewById(R.id.equipped_primary);

        List<ItemCompleteObject> primaryWeapons = new ArrayList<>();

        Log.d(TAG, "ITEMSLIST: " + characterInfo.getItemList().size());

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Primary Weapons") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                primaryWeapons.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Primary Weapons") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mPrimaryEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mPrimaryEquipped == null) {
            mPrimaryEquipped = dummyObject;
        }

        equippedPrimaryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mPrimaryEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mPrimaryEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mPrimaryEquipped.getIconUrl()).into(equippedPrimaryIcon);
        if (mPrimaryEquipped.isGridComplete()) {
            equippedPrimaryIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedPrimaryIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView primaryWeaponsAdapter = (RecyclerView) view.findViewById(R.id.recyclerview_primaryWeapons);

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        primaryWeaponsAdapter.setLayoutManager(layoutManager);

        GridLayoutRecyclerAdapter adapter = new GridLayoutRecyclerAdapter(primaryWeapons,
                mPrimaryEquipped, equippedPrimaryIcon, mView);
        primaryWeaponsAdapter.setAdapter(adapter);

        adapter.setPrimaryEquippedListener(new GridLayoutRecyclerAdapter.PrimaryEquippedListener() {
            @Override
            public void onPrimaryWeaponEquipped(ItemCompleteObject equippedPrimaryItem) {
                mPrimaryEquipped = equippedPrimaryItem;
            }
        });

        //SPECIAL WEAPON SET UP!
        ImageView equippedSpecialIcon = (ImageView) view.findViewById(R.id.equipped_special);

        List<ItemCompleteObject> specialWeapons = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Special Weapons") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                specialWeapons.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Special Weapons") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mSpecialEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mSpecialEquipped == null) {
            mSpecialEquipped = dummyObject;
        }

        equippedSpecialIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSpecialEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mSpecialEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + specialWeapons.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mSpecialEquipped.getIconUrl()).into(equippedSpecialIcon);
        if (mSpecialEquipped.isGridComplete()) {
            equippedSpecialIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedSpecialIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView specialWeaponsAdapter = (RecyclerView) view.findViewById(R.id.recyclerview_specialWeapons);

        LinearLayoutManager layoutManagerSpecial = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        specialWeaponsAdapter.setLayoutManager(layoutManagerSpecial);

        GridLayoutRecyclerAdapter adapterSpecial = new GridLayoutRecyclerAdapter(specialWeapons,
                mSpecialEquipped, equippedSpecialIcon, mView);
        specialWeaponsAdapter.setAdapter(adapterSpecial);

        adapterSpecial.setSpecialEquippedListener(new GridLayoutRecyclerAdapter.SpecialEquippedListener() {
            @Override
            public void onSpecialWeaponEquipped(ItemCompleteObject equippedSpecialItem) {
                mSpecialEquipped = equippedSpecialItem;
            }
        });


        //HEAVY WEAPON SET UP!
        ImageView equippedHeavyIcon = (ImageView) view.findViewById(R.id.equipped_heavy);

        List<ItemCompleteObject> heavyWeapons = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Heavy Weapons") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                heavyWeapons.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Heavy Weapons") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mHeavyEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mHeavyEquipped == null) {
            mHeavyEquipped = dummyObject;
        }

        equippedHeavyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mHeavyEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mHeavyEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + heavyWeapons.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mHeavyEquipped.getIconUrl()).into(equippedHeavyIcon);
        if (mHeavyEquipped.isGridComplete()) {
            equippedHeavyIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedHeavyIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView heavyWeaponsAdapter = (RecyclerView) view.findViewById(R.id.recyclerview_heavyWeapons);

        LinearLayoutManager layoutManagerHeavy = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        heavyWeaponsAdapter.setLayoutManager(layoutManagerHeavy);

        GridLayoutRecyclerAdapter adapterHeavy = new GridLayoutRecyclerAdapter(heavyWeapons,
                mHeavyEquipped, equippedHeavyIcon, mView);
        heavyWeaponsAdapter.setAdapter(adapterHeavy);

        adapterHeavy.setHeavyEquippedListener(new GridLayoutRecyclerAdapter.HeavyEquippedListener() {
            @Override
            public void onHeavyWeaponEquipped(ItemCompleteObject equippedHeavyItem) {
                mHeavyEquipped = equippedHeavyItem;
            }
        });

        //GHOST SET UP
        ImageView equippedGhostIcon = (ImageView) view.findViewById(R.id.equipped_ghost);

        List<ItemCompleteObject> ghosts = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Ghost") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                ghosts.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Ghost") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mGhostEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mGhostEquipped == null) {
            mGhostEquipped = dummyObject;
        }

        equippedGhostIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mGhostEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mGhostEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + ghosts.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mGhostEquipped.getIconUrl()).into(equippedGhostIcon);
        if (mGhostEquipped.isGridComplete()) {
            equippedGhostIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedGhostIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView ghostsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_ghosts);

        LinearLayoutManager layoutManagerGhost = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ghostsRecyclerView.setLayoutManager(layoutManagerGhost);

        GridLayoutRecyclerAdapter adapterGhost = new GridLayoutRecyclerAdapter(ghosts,
                mGhostEquipped, equippedGhostIcon, mView);
        ghostsRecyclerView.setAdapter(adapterGhost);

        adapterGhost.setGhostEquippedListener(new GridLayoutRecyclerAdapter.GhostEquippedListener() {
            @Override
            public void onGhostEquipped(ItemCompleteObject equippedGhostItem) {
                mGhostEquipped = equippedGhostItem;
            }
        });

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), CharInvActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                swipeLayout.setRefreshing(false);
            }
        });
    }
}


