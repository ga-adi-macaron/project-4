package com.crocusgames.destinyinventorymanager;

/**
 * Created by Serkan on 15/12/16.
 */

public class ItemCompleteObject {
    private String mItemHash, mItemId, mBucketHash, mItemName, mDescription, mIconUrl, mTierTypeName, mItemTypeName, mBucketName, mBackgroundUrl;
    private Integer mQuantity, mTransferStatus, mLightLevel;
    private Boolean isGridComplete;

    public ItemCompleteObject(String bucketHash, String bucketName, String description, String iconUrl, String itemHash, String itemId, String itemName, String itemTypeName, Integer lightLevel, Integer quantity, String tierTypeName, Integer transferStatus, Boolean isGridComplete, String backgroundUrl) {
        mBucketHash = bucketHash;
        mBucketName = bucketName;
        mDescription = description;
        mIconUrl = iconUrl;
        mItemHash = itemHash;
        mItemId = itemId;
        mItemName = itemName;
        mItemTypeName = itemTypeName;
        mLightLevel = lightLevel;
        mQuantity = quantity;
        mTierTypeName = tierTypeName;
        mTransferStatus = transferStatus;
        this.isGridComplete = isGridComplete;
        mBackgroundUrl = backgroundUrl;
    }

    public String getBucketHash() {
        return mBucketHash;
    }

    public String getBucketName() {
        return mBucketName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public String getItemHash() {
        return mItemHash;
    }

    public String getItemId() {
        return mItemId;
    }

    public String getItemName() {
        return mItemName;
    }

    public String getItemTypeName() {
        return mItemTypeName;
    }

    public Integer getLightLevel() {
        return mLightLevel;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Integer quantity) {
        mQuantity = quantity;
    }

    public String getTierTypeName() {
        return mTierTypeName;
    }

    public Integer getTransferStatus() {
        return mTransferStatus;
    }

    public Boolean isGridComplete() {
        return isGridComplete;
    }

    public String getBackgroundUrl() {
        return mBackgroundUrl;
    }
}
