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

import static com.crocusgames.destinyinventorymanager.AppConstants.BUNGIE_NET_START_URL;

/**
 * Created by Serkan on 14/12/16.
 */

public class FragmentCharArmor extends Fragment {
    private View mView;
    private ItemCompleteObject mHelmetEquipped, mGauntletEquipped, mChestArmorEquipped, mLegArmorEquipped;
    private ItemCompleteObject mClassArmorEquipped, mArtifactEquipped;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chararmor_layout, container, false);
        mView = v;
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        if (characterInfo.isApiFinished()) {
            setFragmentCharArmor(view, characterInfo);
        }
    }

    public void setFragmentCharArmor(View view, final CharacterInfoSingleton characterInfo) {
        //HELMET

        //Dummy object is when api returns a classified object for some reason.
        ItemCompleteObject dummyObject = new ItemCompleteObject(null, null, null, AppConstants.MISSING_ICON_URL,
                null, null, null, null, -1, -1, null, -1, false, null);

        ImageView equippedHelmetIcon = (ImageView) view.findViewById(R.id.equipped_helmet);

        List<ItemCompleteObject> helmets = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Helmet") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                helmets.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Helmet") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mHelmetEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mHelmetEquipped == null) {
            mHelmetEquipped = dummyObject;
        }

        equippedHelmetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mHelmetEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mHelmetEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + helmets.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mHelmetEquipped.getIconUrl()).into(equippedHelmetIcon);
        if (mHelmetEquipped.isGridComplete()) {
            equippedHelmetIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedHelmetIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView helmetRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_helmets);

        LinearLayoutManager layoutManagerHelmet = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        helmetRecyclerView.setLayoutManager(layoutManagerHelmet);

        GridLayoutRecyclerAdapter adapterHelmet = new GridLayoutRecyclerAdapter(helmets,
                mHelmetEquipped, equippedHelmetIcon, mView);
        helmetRecyclerView.setAdapter(adapterHelmet);

        adapterHelmet.setHelmetEquippedListener(new GridLayoutRecyclerAdapter.HelmetEquippedListener() {
            @Override
            public void onHelmetEquipped(ItemCompleteObject equippedHelmetItem) {
                mHelmetEquipped = equippedHelmetItem;
            }
        });

        //GAUNTLETS SET UP!
        ImageView equippedGauntletIcon = (ImageView) view.findViewById(R.id.equipped_gauntlet);

        List<ItemCompleteObject> gauntlets = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Gauntlets") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                gauntlets.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Gauntlets") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mGauntletEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mGauntletEquipped == null) {
            mGauntletEquipped = dummyObject;
        }

        equippedGauntletIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mGauntletEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mGauntletEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + gauntlets.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mGauntletEquipped.getIconUrl()).into(equippedGauntletIcon);
        if (mGauntletEquipped.isGridComplete()) {
            equippedGauntletIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedGauntletIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView gauntletRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_gauntlets);

        LinearLayoutManager layoutManagerGauntlets = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        gauntletRecyclerView.setLayoutManager(layoutManagerGauntlets);

        GridLayoutRecyclerAdapter adapterGauntlets = new GridLayoutRecyclerAdapter(gauntlets,
                mGauntletEquipped, equippedGauntletIcon, mView);
        gauntletRecyclerView.setAdapter(adapterGauntlets);

        adapterGauntlets.setGauntletEquippedListener(new GridLayoutRecyclerAdapter.GauntletEquippedListener() {
            @Override
            public void onGauntletEquipped(ItemCompleteObject equippedGauntletItem) {
                mGauntletEquipped = equippedGauntletItem;
            }
        });


        //Chest Armor
        ImageView equippedChestArmorIcon = (ImageView) view.findViewById(R.id.equipped_chestArmor);

        List<ItemCompleteObject> chestArmors = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Chest Armor") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                chestArmors.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Chest Armor") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mChestArmorEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mChestArmorEquipped == null) {
            mChestArmorEquipped = dummyObject;
        }

        equippedChestArmorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mChestArmorEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mChestArmorEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + chestArmors.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mChestArmorEquipped.getIconUrl()).into(equippedChestArmorIcon);
        if (mChestArmorEquipped.isGridComplete()) {
            equippedChestArmorIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedChestArmorIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView chestArmorRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_chestArmors);

        LinearLayoutManager layoutManagerChestArmor = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        chestArmorRecyclerView.setLayoutManager(layoutManagerChestArmor);

        GridLayoutRecyclerAdapter adapterChestArmor = new GridLayoutRecyclerAdapter(chestArmors,
                mChestArmorEquipped, equippedChestArmorIcon, mView);
        chestArmorRecyclerView.setAdapter(adapterChestArmor);

        adapterChestArmor.setChestArmorEquippedListener(new GridLayoutRecyclerAdapter.ChestArmorEquippedListener() {
            @Override
            public void onChestArmorEquipped(ItemCompleteObject equippedChestArmorItem) {
                mChestArmorEquipped = equippedChestArmorItem;
            }
        });


        //Leg Armor Set Up!
        ImageView equippedLegArmorIcon = (ImageView) view.findViewById(R.id.equipped_legArmor);

        List<ItemCompleteObject> legArmors = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Leg Armor") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                legArmors.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Leg Armor") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mLegArmorEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mLegArmorEquipped == null) {
            mLegArmorEquipped = dummyObject;
        }

        equippedLegArmorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mLegArmorEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mLegArmorEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + legArmors.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mLegArmorEquipped.getIconUrl()).into(equippedLegArmorIcon);
        if (mLegArmorEquipped.isGridComplete()) {
            equippedLegArmorIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedLegArmorIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView legArmorRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_legArmors);

        LinearLayoutManager layoutManagerLegArmor = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        legArmorRecyclerView.setLayoutManager(layoutManagerLegArmor);

        GridLayoutRecyclerAdapter adapterLegArmor = new GridLayoutRecyclerAdapter(legArmors,
                mLegArmorEquipped, equippedLegArmorIcon, mView);
        legArmorRecyclerView.setAdapter(adapterLegArmor);

        adapterLegArmor.setLegArmorEquippedListener(new GridLayoutRecyclerAdapter.LegArmorEquippedListener() {
            @Override
            public void onLegArmorEquipped(ItemCompleteObject equippedLegArmor) {
                mLegArmorEquipped = equippedLegArmor;
            }
        });


        //Class Armor WEAPON SET UP!
        ImageView equippedClassArmorIcon = (ImageView) view.findViewById(R.id.equipped_classArmor);

        List<ItemCompleteObject> classArmors = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Class Armor") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                classArmors.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Class Armor") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mClassArmorEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mClassArmorEquipped == null) {
            mClassArmorEquipped = dummyObject;
        }

        equippedClassArmorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mClassArmorEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mClassArmorEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + classArmors.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mClassArmorEquipped.getIconUrl()).into(equippedClassArmorIcon);
        if (mClassArmorEquipped.isGridComplete()) {
            equippedClassArmorIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedClassArmorIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView classArmorRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_classArmors);

        LinearLayoutManager layoutManagerClassArmor = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        classArmorRecyclerView.setLayoutManager(layoutManagerClassArmor);

        GridLayoutRecyclerAdapter adapterClassArmor = new GridLayoutRecyclerAdapter(classArmors,
                mClassArmorEquipped, equippedClassArmorIcon, mView);
        classArmorRecyclerView.setAdapter(adapterClassArmor);

        adapterClassArmor.setClassArmorEquippedListener(new GridLayoutRecyclerAdapter.ClassArmorEquippedListener() {
            @Override
            public void onClassArmorEquipped(ItemCompleteObject equippedClassArmor) {
                mClassArmorEquipped = equippedClassArmor;
            }
        });

        //Artifact SET UP!
        ImageView equippedArtifactIcon = (ImageView) view.findViewById(R.id.equipped_artifact);

        List<ItemCompleteObject> artifacts = new ArrayList<>();

        for (int i = 0; i < characterInfo.getItemList().size(); i++) {
            if (characterInfo.getItemList().get(i).getBucketName().equals("Artifacts") &&
                    characterInfo.getItemList().get(i).getTransferStatus() != 1) {
                artifacts.add(characterInfo.getItemList().get(i));
            } else if (characterInfo.getItemList().get(i).getBucketName().equals("Artifacts") &&
                    characterInfo.getItemList().get(i).getTransferStatus() == 1) {
                mArtifactEquipped = characterInfo.getItemList().get(i);
            }
        }

        if (mArtifactEquipped == null) {
            mArtifactEquipped = dummyObject;
        }

        equippedArtifactIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mArtifactEquipped.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                    characterInfo.setIsEquippedItem(true);
                    characterInfo.setSelectedItem(mArtifactEquipped);
                    FragmentManager fm = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                    FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                    dialogFragment.show(fm, "Sample Fragment");
                }
            }
        });

        Log.d(AppConstants.TAG, "onViewCreated: " + artifacts.size());

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + mArtifactEquipped.getIconUrl()).into(equippedArtifactIcon);
        if (mArtifactEquipped.isGridComplete()) {
            equippedArtifactIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            equippedArtifactIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        RecyclerView artifactRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_artifacts);

        LinearLayoutManager layoutManagerArtifact = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        artifactRecyclerView.setLayoutManager(layoutManagerArtifact);

        GridLayoutRecyclerAdapter adapterArtifact = new GridLayoutRecyclerAdapter(artifacts,
                mArtifactEquipped, equippedArtifactIcon, mView);
        artifactRecyclerView.setAdapter(adapterArtifact);

        adapterArtifact.setArtifactEquippedListener(new GridLayoutRecyclerAdapter.ArtifactEquippedListener() {
            @Override
            public void onArtifactEquipped(ItemCompleteObject equippedArtifact) {
                mArtifactEquipped = equippedArtifact;
            }
        });

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), CharInvActivity.class);
                intent.putExtra(AppConstants.TAB_NUMBER, 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
