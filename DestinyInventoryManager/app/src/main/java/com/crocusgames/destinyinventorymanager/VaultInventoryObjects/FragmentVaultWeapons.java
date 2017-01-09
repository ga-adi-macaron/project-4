package com.crocusgames.destinyinventorymanager.VaultInventoryObjects;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crocusgames.destinyinventorymanager.Activities.VaultActivity;
import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.ItemCompleteObject;
import com.crocusgames.destinyinventorymanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serkan on 23/12/16.
 */

public class FragmentVaultWeapons extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vaultweapons_layout, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //PRIMARY WEAPON SET UP!
        CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        if (characterInfo.isApiFinished()) {
            setFragmentVaultWeapons(view, characterInfo);
        }

    }

    public void setFragmentVaultWeapons(View view, CharacterInfoSingleton characterInfo) {
        final List<ItemCompleteObject> allItemsList = characterInfo.getItemList();
        Log.d(AppConstants.TAG, "Item List Size / VaultAct.: " + allItemsList.size());

        final List<ItemCompleteObject> primaryWeaponsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Primary Weapons")) {
                primaryWeaponsList.add(allItemsList.get(i));
            }
        }

        RecyclerView primaryWeaponRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_primaryWeapons);

        LinearLayoutManager layoutManagerPrimaryWeapons = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        primaryWeaponRecyclerView.setLayoutManager(layoutManagerPrimaryWeapons);

        final VaultGridLayoutRecyclerAdapter primaryWeaponsAdapter = new VaultGridLayoutRecyclerAdapter(primaryWeaponsList);
        primaryWeaponRecyclerView.setAdapter(primaryWeaponsAdapter);

        //SPECIAL WEAPON SET UP

        final List<ItemCompleteObject> specialWeaponsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Special Weapons")) {
                specialWeaponsList.add(allItemsList.get(i));
            }
        }

        RecyclerView specialWeaponRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_specialWeapons);

        LinearLayoutManager layoutManagerSpecialWeapons = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        specialWeaponRecyclerView.setLayoutManager(layoutManagerSpecialWeapons);

        final VaultGridLayoutRecyclerAdapter specialWeaponsAdapter = new VaultGridLayoutRecyclerAdapter(specialWeaponsList);
        specialWeaponRecyclerView.setAdapter(specialWeaponsAdapter);

        //SPECIAL WEAPON SET UP

        final List<ItemCompleteObject> heavyWeaponsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Heavy Weapons")) {
                heavyWeaponsList.add(allItemsList.get(i));
            }
        }

        RecyclerView heavyWeaponRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_heavyWeapons);

        LinearLayoutManager layoutManagerHeavyWeapons = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        heavyWeaponRecyclerView.setLayoutManager(layoutManagerHeavyWeapons);

        final VaultGridLayoutRecyclerAdapter heavyWeaponsAdapter = new VaultGridLayoutRecyclerAdapter(heavyWeaponsList);
        heavyWeaponRecyclerView.setAdapter(heavyWeaponsAdapter);

        //GHOST SET UP

        final List<ItemCompleteObject> ghostsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Ghost")) {
                ghostsList.add(allItemsList.get(i));
            }
        }

        RecyclerView ghostRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_ghosts);

        LinearLayoutManager layoutManagerGhost = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ghostRecyclerView.setLayoutManager(layoutManagerGhost);

        final VaultGridLayoutRecyclerAdapter ghostAdapter = new VaultGridLayoutRecyclerAdapter(ghostsList);
        ghostRecyclerView.setAdapter(ghostAdapter);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), VaultActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                swipeLayout.setRefreshing(false);
            }
        });

    }
}
