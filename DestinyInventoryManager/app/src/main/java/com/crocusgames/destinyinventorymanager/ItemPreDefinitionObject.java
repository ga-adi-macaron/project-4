package com.crocusgames.destinyinventorymanager;

/**
 * Created by Serkan on 14/12/16.
 */

public class ItemPreDefinitionObject {
    private String mItemHash, mItemId, mBucketHash;
    private Integer mQuantity, mTransferStatus, mLightLevel;
    private Boolean isGridComplete;

    public ItemPreDefinitionObject(String bucketHash, String itemHash, String itemId, Integer quantity,
                                   Integer transferStatus, Integer lightLevel, Boolean isGridComplete) {
        mBucketHash = bucketHash;
        mItemHash = itemHash;
        mItemId = itemId;
        mQuantity = quantity;
        mTransferStatus = transferStatus;
        mLightLevel = lightLevel;
        this.isGridComplete = isGridComplete;
    }

    public String getItemHash() {
        return mItemHash;
    }

    public String getItemId() {
        return mItemId;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public String getBucketHash() {
        return mBucketHash;
    }

    public Integer getTransferStatus() {
        return mTransferStatus;
    }

    public Integer getLightLevel() {
        return mLightLevel;
    }

    public Boolean isGridComplete() {
        return isGridComplete;
    }
}
