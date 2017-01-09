package com.crocusgames.destinyinventorymanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serkan on 13/12/16.
 */

public class CharacterInfoSingleton {
    private static CharacterInfoSingleton mCharacterInfo = null;
    private static String mMembershipId;
    private static Long mMembershipType;
    private static List<CharacterInfoObject> mCharacterList;
    private static List<ItemCompleteObject> mInventoryList;
    private static List<ItemCompleteObject> mGrindList0;
    private static List<ItemCompleteObject> mGrindList1;
    private static List<ItemCompleteObject> mGrindList2;

    private static ItemCompleteObject mSelectedItem;
    private static int mSelectedCharacter;
    private static boolean isEquippedItem;
    private static boolean apiFinished;

    public CharacterInfoSingleton() {
        mCharacterList = new ArrayList<>();
        mMembershipId = "";
        mMembershipType = -5l;
        mInventoryList = new ArrayList<>();
        mGrindList0 = new ArrayList<>();
        mGrindList1 = new ArrayList<>();
        mGrindList2 = new ArrayList<>();
        mSelectedItem = null;
        mSelectedCharacter = -1;
        isEquippedItem = false;
    }

    public static CharacterInfoSingleton getInstance() {
        if (mCharacterInfo == null) {
            mCharacterInfo = new CharacterInfoSingleton();
        }
        return mCharacterInfo;
    }

    public void setSelectedItem(ItemCompleteObject itemCompleteObject) {
        mSelectedItem = itemCompleteObject;
    }

    public ItemCompleteObject getSelectedItem() {
        return mSelectedItem;
    }

    public Long getMembershipType() {
        return mMembershipType;
    }

    public void setMembershipType(Long mMembershipType) {
        CharacterInfoSingleton.mMembershipType = mMembershipType;
    }

    public void addToItemList(ItemCompleteObject itemCompleteObject) {
        mInventoryList.add(itemCompleteObject);
    }

    public List<ItemCompleteObject> getItemList() {
        return mInventoryList;
    }

    public void clearAllItemList() {
        mInventoryList.clear();
    }

    public void addToCharacterList(CharacterInfoObject characterInfoObject) {
        mCharacterList.add(characterInfoObject);
    }

    public List<CharacterInfoObject> getCharacterList() {
        return mCharacterList;
    }

    public void clearAllCharacterList() {
        mCharacterList.clear();
    }

    public String getMembershipId() {
        return mMembershipId;
    }

    public void setMembershipId(String mMembershipId) {
        CharacterInfoSingleton.mMembershipId = mMembershipId;
    }

    public int getSelectedCharacter() {
        return mSelectedCharacter;
    }

    public void setSelectedCharacter(int mSelectedCharacter) {
        CharacterInfoSingleton.mSelectedCharacter = mSelectedCharacter;
    }

    public boolean isEquippedItem() {
        return isEquippedItem;
    }

    public void setIsEquippedItem(boolean isEquippedItem) {
        CharacterInfoSingleton.isEquippedItem = isEquippedItem;
    }

    public boolean isApiFinished() {
        return apiFinished;
    }

    public void setApiFinished(boolean apiFinished) {
        CharacterInfoSingleton.apiFinished = apiFinished;
    }

    public List<ItemCompleteObject> getGrindList0() {
        return mGrindList0;
    }

    public void addToGrindList0(ItemCompleteObject itemCompleteObject) {
        mGrindList0.add(itemCompleteObject);
    }

    public void clearAllGrindList0() {
        mGrindList0.clear();
    }

    public List<ItemCompleteObject> getGrindList1() {
        return mGrindList1;
    }

    public List<ItemCompleteObject> getGrindList2() {
        return mGrindList2;
    }

    public void addToGrindList1(ItemCompleteObject itemCompleteObject) {
        mGrindList1.add(itemCompleteObject);
    }

    public void addToGrindList2(ItemCompleteObject itemCompleteObject) {
        mGrindList2.add(itemCompleteObject);
    }

    public void clearAllGrindList1() {
        mGrindList1.clear();
    }

    public void clearAllGrindList2() {
        mGrindList2.clear();
    }
}

