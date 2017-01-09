package com.crocusgames.destinyinventorymanager.CharInventoryObjects;

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

import com.crocusgames.destinyinventorymanager.Activities.CharInvActivity;
import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.ItemCompleteObject;
import com.crocusgames.destinyinventorymanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serkan on 18/12/16.
 */

public class FragmentCharInventory extends Fragment {
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_charinv_layout, container, false);
        mView = v;
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        if (characterInfo.isApiFinished()) {
            setFragmentCharInventory(view, characterInfo);
        }
    }

    public void setFragmentCharInventory(View view, CharacterInfoSingleton characterInfo) {
        //Materials

        List<ItemCompleteObject> materialList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Materials")) {
                materialList.add(characterInfo.getItemList().get(i));
            }
        }

        Log.d(AppConstants.TAG, "Material list size: " + materialList.size());

        RecyclerView materialRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_materials);

        LinearLayoutManager layoutManagerMaterials = new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        materialRecyclerView.setLayoutManager(layoutManagerMaterials);

        GridLayoutRecyclerAdapter adapterMaterials = new GridLayoutRecyclerAdapter(materialList,
                null, null, mView);
        materialRecyclerView.setAdapter(adapterMaterials);

        //Consumables
        List<ItemCompleteObject> consumablesList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Consumables")) {
                consumablesList.add(characterInfo.getItemList().get(i));
            }
        }

        Log.d(AppConstants.TAG, "Material list size: " + consumablesList.size());

        RecyclerView consumablesRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_consumables);

        LinearLayoutManager layoutManagerConsumables = new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        consumablesRecyclerView.setLayoutManager(layoutManagerConsumables);

        GridLayoutRecyclerAdapter adapterConsumables = new GridLayoutRecyclerAdapter(consumablesList,
                null, null, mView);
        consumablesRecyclerView.setAdapter(adapterConsumables);


        //Ornaments
        List<ItemCompleteObject> ornamentsList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Ornaments")) {
                ornamentsList.add(characterInfo.getItemList().get(i));
            }
        }

        Log.d(AppConstants.TAG, "Material list size: " + ornamentsList.size());

        RecyclerView ornamentRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_ornaments);

        LinearLayoutManager layoutManagerOrnaments = new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        ornamentRecyclerView.setLayoutManager(layoutManagerOrnaments);

        GridLayoutRecyclerAdapter adapterOrnaments = new GridLayoutRecyclerAdapter(ornamentsList,
                null, null, mView);
        ornamentRecyclerView.setAdapter(adapterOrnaments);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), CharInvActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, 3);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
