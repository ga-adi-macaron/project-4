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

public class FragmentVaultInventory extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vaultinv_layout, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        if (characterInfo.isApiFinished()) {
            setFragmentVaultInventory(view, characterInfo);
        }

    }

    public void setFragmentVaultInventory(View view, CharacterInfoSingleton characterInfo) {
        final List<ItemCompleteObject> allItemsList = characterInfo.getItemList();

        //Materials SET UP!
        final List<ItemCompleteObject> materialsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Materials")) {
                materialsList.add(allItemsList.get(i));
            }
        }

        RecyclerView materialsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_materials);

        LinearLayoutManager layoutManagerMaterials = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically()
            {
                return false;
            }
        };
        materialsRecyclerView.setLayoutManager(layoutManagerMaterials);

        final VaultGridLayoutRecyclerAdapter materialsAdapter = new VaultGridLayoutRecyclerAdapter(materialsList);
        materialsRecyclerView.setAdapter(materialsAdapter);


        //SPECIAL WEAPON SET UP
        final List<ItemCompleteObject> consumablesList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Consumables")) {
                consumablesList.add(allItemsList.get(i));
            }
        }

        RecyclerView consumablesRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_consumables);

        LinearLayoutManager layoutManagerConsumables = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        consumablesRecyclerView.setLayoutManager(layoutManagerConsumables);

        final VaultGridLayoutRecyclerAdapter consumablesAdapter = new VaultGridLayoutRecyclerAdapter(consumablesList);
        consumablesRecyclerView.setAdapter(consumablesAdapter);

        //Ornaments SET UP!

        final List<ItemCompleteObject> ornamentsList = new ArrayList<>();

        for (int i = 0; i < allItemsList.size(); i++) {
            String itemTypeName = allItemsList.get(i).getBucketName();
            if (itemTypeName.equals("Ornaments")) {
                ornamentsList.add(allItemsList.get(i));
            }
        }

        RecyclerView ornamentsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_ornaments);

        LinearLayoutManager layoutManagerOrnaments = new GridLayoutManager(getContext(), 6) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ornamentsRecyclerView.setLayoutManager(layoutManagerOrnaments);

        final VaultGridLayoutRecyclerAdapter ornamentsAdapter = new VaultGridLayoutRecyclerAdapter(ornamentsList);
        ornamentsRecyclerView.setAdapter(ornamentsAdapter);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), VaultActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, 3);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                swipeLayout.setRefreshing(false);
            }
        });

    }
}
