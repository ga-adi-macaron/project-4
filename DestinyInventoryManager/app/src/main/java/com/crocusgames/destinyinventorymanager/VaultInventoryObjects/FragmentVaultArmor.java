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

public class FragmentVaultArmor extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vaultarmor_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        if (characterInfo.isApiFinished()) {
            setFragmentVaultArmor(view, characterInfo);
        }

    }

    public void setFragmentVaultArmor(View view, CharacterInfoSingleton characterInfo) {
        final List<ItemCompleteObject> allItemsList = characterInfo.getItemList();

        //Helmet Set Up
        final List<ItemCompleteObject> helmetsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Helmet")) {
                helmetsList.add(allItemsList.get(i));
            }
        }

        RecyclerView helmetRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_helmets);

        LinearLayoutManager layoutManagerHelmets = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        helmetRecyclerView.setLayoutManager(layoutManagerHelmets);

        final VaultGridLayoutRecyclerAdapter helmetsAdapter = new VaultGridLayoutRecyclerAdapter(helmetsList);
        helmetRecyclerView.setAdapter(helmetsAdapter);

        //Gauntlets Set Up
        final List<ItemCompleteObject> gauntletsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Gauntlets")) {
                gauntletsList.add(allItemsList.get(i));
            }
        }

        RecyclerView gauntletsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_gauntlets);

        LinearLayoutManager layoutManagerGauntlets = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        gauntletsRecyclerView.setLayoutManager(layoutManagerGauntlets);

        final VaultGridLayoutRecyclerAdapter gauntletsAdapter = new VaultGridLayoutRecyclerAdapter(gauntletsList);
        gauntletsRecyclerView.setAdapter(gauntletsAdapter);

        //Chest Armor Set Up
        final List<ItemCompleteObject> chestArmorList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Chest Armor")) {
                chestArmorList.add(allItemsList.get(i));
            }
        }

        RecyclerView chestArmorRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_chestArmors);

        LinearLayoutManager layoutManagerChestArmor = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        chestArmorRecyclerView.setLayoutManager(layoutManagerChestArmor);

        final VaultGridLayoutRecyclerAdapter chestArmorAdapter = new VaultGridLayoutRecyclerAdapter(chestArmorList);
        chestArmorRecyclerView.setAdapter(chestArmorAdapter);


        //Leg Armor Set Up
        final List<ItemCompleteObject> legArmorList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Leg Armor")) {
                legArmorList.add(allItemsList.get(i));
            }
        }

        RecyclerView legArmorRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_LegArmor);

        LinearLayoutManager layoutManagerLegArmor = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        legArmorRecyclerView.setLayoutManager(layoutManagerLegArmor);

        final VaultGridLayoutRecyclerAdapter legArmorAdapter = new VaultGridLayoutRecyclerAdapter(legArmorList);
        legArmorRecyclerView.setAdapter(legArmorAdapter);


        //Class Armor Set Up
        final List<ItemCompleteObject> classArmorList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Class Armor")) {
                classArmorList.add(allItemsList.get(i));
            }
        }

        RecyclerView classArmorRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_classArmors);

        LinearLayoutManager layoutManagerClassArmor = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        classArmorRecyclerView.setLayoutManager(layoutManagerClassArmor);

        final VaultGridLayoutRecyclerAdapter classArmorAdapter = new VaultGridLayoutRecyclerAdapter(classArmorList);
        classArmorRecyclerView.setAdapter(classArmorAdapter);


        //Artifacts Set Up
        final List<ItemCompleteObject> artifactsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Artifacts")) {
                artifactsList.add(allItemsList.get(i));
            }
        }

        RecyclerView artifactsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_artifacts);

        LinearLayoutManager layoutManagerArtifacts = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        artifactsRecyclerView.setLayoutManager(layoutManagerArtifacts);

        final VaultGridLayoutRecyclerAdapter artifactsAdapter = new VaultGridLayoutRecyclerAdapter(artifactsList);
        artifactsRecyclerView.setAdapter(artifactsAdapter);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), VaultActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
