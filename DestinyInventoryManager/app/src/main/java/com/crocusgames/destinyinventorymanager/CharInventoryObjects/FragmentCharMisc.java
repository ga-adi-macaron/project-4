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

public class FragmentCharMisc extends Fragment {
    private View mView;
    private ItemCompleteObject mShaderEquipped, mEmblemEquipped, mShipEquipped, mSparrowEquipped, mEmoteEquipped, mSparrowHornEquipped;
    private EmblemEquippedListener mEmblemListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_charmisc_layout, container, false);
        mView = v;
        return v;
    }

    public interface EmblemEquippedListener {
        void onEmblemEquipped(ItemCompleteObject equippedEmblem);
    }

    public void setEmblemEquippedListener(EmblemEquippedListener listener) {
        mEmblemListener = listener;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        if (characterInfo.isApiFinished()) {
            setFragmentCharMisc(view, characterInfo);
        }
    }

    public void setFragmentCharMisc(View view, final CharacterInfoSingleton characterInfo) {
        //SHADER

        //Dummy object is when api returns a classified object for some reason.
        ItemCompleteObject dummyObject = new ItemCompleteObject(null, null, null, AppConstants.MISSING_ICON_URL,
                null, null, null, null, -1, -1, null, -1, false, null);

        ImageView equippedShaderIcon = (ImageView) view.findViewById(R.id.equipped_shader);

        List<ItemCompleteObject> shadersList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Shaders")) {
                Log.d(AppConstants.TAG, "onViewCreated: " + characterInfo.getItemList().get(i).getItemName() +
                        " / " + characterInfo.getItemList().get(i).getTransferStatus());
            }
        }

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Shaders") &&
                    ((characterInfo.getItemList().get(i).getTransferStatus() == 0) ||
                            (characterInfo.getItemList().get(i).getTransferStatus() == 2))) {
                shadersList.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Shaders") &&
                    ((characterInfo.getItemList().get(i).getTransferStatus() == 3)
                            || (characterInfo.getItemList().get(i).getTransferStatus() == 1))) {
                mShaderEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mShaderEquipped == null) {
            mShaderEquipped = dummyObject;
        }

        equippedShaderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mShaderEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mShaderEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + shadersList.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mShaderEquipped.getIconUrl()).into(equippedShaderIcon);
        if (mShaderEquipped.isGridComplete()) {
            equippedShaderIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedShaderIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView shaderRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_shaders);

        LinearLayoutManager layoutManagerShader = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        shaderRecyclerView.setLayoutManager(layoutManagerShader);

        GridLayoutRecyclerAdapter shaderAdapter = new GridLayoutRecyclerAdapter(shadersList,
                mShaderEquipped, equippedShaderIcon, mView);
        shaderRecyclerView.setAdapter(shaderAdapter);

        shaderAdapter.setShaderEquippedListener(new GridLayoutRecyclerAdapter.ShaderEquippedListener() {
            @Override
            public void onShaderEquipped(ItemCompleteObject equippedShader) {
                mShaderEquipped = equippedShader;
            }
        });


        //EMBLEM
        ImageView equippedEmblemIcon = (ImageView) view.findViewById(R.id.equipped_emblem);

        List<ItemCompleteObject> emblemsList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Emblems") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                emblemsList.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Emblems") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mEmblemEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mEmblemEquipped == null) {
            mEmblemEquipped = dummyObject;
        }

        equippedEmblemIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEmblemEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mEmblemEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + emblemsList.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mEmblemEquipped.getIconUrl()).into(equippedEmblemIcon);
        if (mEmblemEquipped.isGridComplete()) {
            equippedEmblemIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedEmblemIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView emblemsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_emblems);

        LinearLayoutManager layoutManagerEmblems = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        emblemsRecyclerView.setLayoutManager(layoutManagerEmblems);

        GridLayoutRecyclerAdapter adapterEmblems = new GridLayoutRecyclerAdapter(emblemsList,
                mEmblemEquipped, equippedEmblemIcon, mView);
        emblemsRecyclerView.setAdapter(adapterEmblems);

        adapterEmblems.setEmblemEquippedListener(new GridLayoutRecyclerAdapter.EmblemEquippedListener() {
            @Override
            public void onEmblemEquipped(ItemCompleteObject equippedEmblem) {
                mEmblemEquipped = equippedEmblem;
                mEmblemListener.onEmblemEquipped(equippedEmblem);
            }
        });


        //SHIPS
        ImageView equippedShipIcon = (ImageView) view.findViewById(R.id.equipped_ship);

        List<ItemCompleteObject> shipsList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Ships") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                shipsList.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Ships") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mShipEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mShipEquipped == null) {
            mShipEquipped = dummyObject;
        }

        equippedShipIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mShipEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mShipEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + shipsList.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mShipEquipped.getIconUrl()).into(equippedShipIcon);
        if (mShipEquipped.isGridComplete()) {
            equippedShipIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedShipIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView shipsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_ships);

        LinearLayoutManager layoutManagerShips = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        shipsRecyclerView.setLayoutManager(layoutManagerShips);

        GridLayoutRecyclerAdapter adapterShips = new GridLayoutRecyclerAdapter(shipsList,
                mShipEquipped, equippedShipIcon, mView);
        shipsRecyclerView.setAdapter(adapterShips);

        adapterShips.setShipEquippedListener(new GridLayoutRecyclerAdapter.ShipEquippedListener() {
            @Override
            public void onShipEquipped(ItemCompleteObject equippedShip) {
                mShipEquipped = equippedShip;
            }
        });


        //Sparrow (Vehicle)
        ImageView equippedSparrowIcon = (ImageView) view.findViewById(R.id.equipped_sparrow);

        List<ItemCompleteObject> sparrowsList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Vehicle") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                sparrowsList.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Vehicle") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mSparrowEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mSparrowEquipped == null) {
            mSparrowEquipped = dummyObject;
        }

        equippedSparrowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSparrowEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mSparrowEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "SPARROWS: " + sparrowsList.size());

        for (int i = 0; i < sparrowsList.size(); i++) {
            Log.d(TAG, "SPARROWS: " + sparrowsList.get(i).getBucketName() + " / " +
            sparrowsList.get(i).getIconUrl() + " / " + sparrowsList.get(i).getItemName());
        }

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mSparrowEquipped.getIconUrl()).into(equippedSparrowIcon);
        if (mSparrowEquipped.isGridComplete()) {
            equippedSparrowIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedSparrowIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView sparrowRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_sparrows);

        LinearLayoutManager layoutManagerSparrow = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        sparrowRecyclerView.setLayoutManager(layoutManagerSparrow);

        GridLayoutRecyclerAdapter adapterSparrows = new GridLayoutRecyclerAdapter(sparrowsList,
                mSparrowEquipped, equippedSparrowIcon, mView);
        sparrowRecyclerView.setAdapter(adapterSparrows);

        adapterSparrows.setSparrowEquippedListener(new GridLayoutRecyclerAdapter.SparrowEquippedListener() {
            @Override
            public void onSparrowEquipped(ItemCompleteObject equippedSparrow) {
                mSparrowEquipped = equippedSparrow;
            }
        });

        //EMOTES
        ImageView equippedEmoteIcon = (ImageView) view.findViewById(R.id.equipped_emote);

        List<ItemCompleteObject> emotesList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Emotes") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                emotesList.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Emotes") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mEmoteEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mEmoteEquipped == null) {
            mEmoteEquipped = dummyObject;
        }

        equippedEmoteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEmoteEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mEmoteEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + emotesList.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mEmoteEquipped.getIconUrl()).into(equippedEmoteIcon);
        if (mEmoteEquipped.isGridComplete()) {
            equippedEmoteIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedEmoteIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView emotesRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_emotes);

        LinearLayoutManager layoutManagerEmotes = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        emotesRecyclerView.setLayoutManager(layoutManagerEmotes);

        GridLayoutRecyclerAdapter adapterEmotes = new GridLayoutRecyclerAdapter(emotesList,
                mEmoteEquipped, equippedEmoteIcon, mView);
        emotesRecyclerView.setAdapter(adapterEmotes);

        adapterEmotes.setEmoteEquippedListener(new GridLayoutRecyclerAdapter.EmoteEquippedListener() {
            @Override
            public void onEmoteEquipped(ItemCompleteObject equippedEmote) {
                mEmoteEquipped = equippedEmote;
            }
        });


        //Sparrow Horn
        ImageView equippedSparrowHorn = (ImageView) view.findViewById(R.id.equipped_sparrowHorn);

        List<ItemCompleteObject> sparrowHornsList = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Sparrow Horn") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                sparrowHornsList.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Sparrow Horn") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mSparrowHornEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mSparrowHornEquipped == null) {
            mSparrowHornEquipped = dummyObject;
        }

        equippedSparrowHorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSparrowHornEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mSparrowHornEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + sparrowHornsList.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mSparrowHornEquipped.getIconUrl()).into(equippedSparrowHorn);
        if (mSparrowHornEquipped.isGridComplete()) {
            equippedSparrowHorn.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedSparrowHorn.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView sparrowHornRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_sparrowHorns);

        LinearLayoutManager layoutManagerSparrowHorn = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        sparrowHornRecyclerView.setLayoutManager(layoutManagerSparrowHorn);

        GridLayoutRecyclerAdapter adapterSparrowHorns = new GridLayoutRecyclerAdapter(sparrowHornsList,
                mSparrowHornEquipped, equippedSparrowHorn, mView);
        sparrowHornRecyclerView.setAdapter(adapterSparrowHorns);

        adapterSparrowHorns.setSparrowHornEquippedListener(new GridLayoutRecyclerAdapter.SparrowHornEquippedListener() {
            @Override
            public void onSparrowHornEquipped(ItemCompleteObject equippedSparrowHorn) {
                mSparrowHornEquipped = equippedSparrowHorn;
            }
        });

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), CharInvActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, 2);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
