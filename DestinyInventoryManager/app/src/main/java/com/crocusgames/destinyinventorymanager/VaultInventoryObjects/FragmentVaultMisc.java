package com.crocusgames.destinyinventorymanager.VaultInventoryObjects;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class FragmentVaultMisc extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vaultmisc_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        if (characterInfo.isApiFinished()) {
            setFragmentVaultMisc(view, characterInfo);
        }

    }

    public void setFragmentVaultMisc(View view, CharacterInfoSingleton characterInfo) {
        final List<ItemCompleteObject> allItemsList = characterInfo.getItemList();

        //Shaders Set Up
        final List<ItemCompleteObject> shadersList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Shaders")) {
                shadersList.add(allItemsList.get(i));
            }
        }

        RecyclerView shadersRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_shaders);

        LinearLayoutManager layoutManagerHelmets = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        shadersRecyclerView.setLayoutManager(layoutManagerHelmets);

        final VaultGridLayoutRecyclerAdapter shadersAdapter = new VaultGridLayoutRecyclerAdapter(shadersList);
        shadersRecyclerView.setAdapter(shadersAdapter);

        //Emblems Set Up
        final List<ItemCompleteObject> emblemsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Emblems")) {
                emblemsList.add(allItemsList.get(i));
            }
        }

        RecyclerView emblemsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_emblems);

        LinearLayoutManager layoutManagerEmblems = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        emblemsRecyclerView.setLayoutManager(layoutManagerEmblems);

        final VaultGridLayoutRecyclerAdapter emblemsAdapter = new VaultGridLayoutRecyclerAdapter(emblemsList);
        emblemsRecyclerView.setAdapter(emblemsAdapter);

        //Ships Armor Set Up
        final List<ItemCompleteObject> shipsArmorList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Ships")) {
                shipsArmorList.add(allItemsList.get(i));
            }
        }

        RecyclerView shipsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_ships);

        LinearLayoutManager layoutManagerShips = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        shipsRecyclerView.setLayoutManager(layoutManagerShips);

        final VaultGridLayoutRecyclerAdapter shipsAdapter = new VaultGridLayoutRecyclerAdapter(shipsArmorList);
        shipsRecyclerView.setAdapter(shipsAdapter);


        //Sparrows Armor Set Up
        final List<ItemCompleteObject> sparrowsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Vehicle")) {
                sparrowsList.add(allItemsList.get(i));
            }
        }

        RecyclerView sparrowsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_sparrows);

        LinearLayoutManager layoutManagerSparrows = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        sparrowsRecyclerView.setLayoutManager(layoutManagerSparrows);

        final VaultGridLayoutRecyclerAdapter sparrowsAdapter = new VaultGridLayoutRecyclerAdapter(sparrowsList);
        sparrowsRecyclerView.setAdapter(sparrowsAdapter);


        //Class Armor Set Up
        final List<ItemCompleteObject> emotesArmorList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Emotes")) {
                emotesArmorList.add(allItemsList.get(i));
            }
        }

        RecyclerView emotesRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_emotes);

        LinearLayoutManager layoutManagerEmotes = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        emotesRecyclerView.setLayoutManager(layoutManagerEmotes);

        final VaultGridLayoutRecyclerAdapter emotesAdapter = new VaultGridLayoutRecyclerAdapter(emotesArmorList);
        emotesRecyclerView.setAdapter(emotesAdapter);


        //Artifacts Set Up
        final List<ItemCompleteObject> sparrowHornsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Sparrow Horn")) {
                sparrowHornsList.add(allItemsList.get(i));
            }
        }

        RecyclerView sparrowHornsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_sparrowHorns);

        LinearLayoutManager layoutManagerSparrowHorns = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        sparrowHornsRecyclerView.setLayoutManager(layoutManagerSparrowHorns);

        final VaultGridLayoutRecyclerAdapter sparrowHorns = new VaultGridLayoutRecyclerAdapter(sparrowHornsList);
        sparrowHornsRecyclerView.setAdapter(sparrowHorns);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), VaultActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
