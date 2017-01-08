package com.crocusgames.destinyinventorymanager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crocusgames.destinyinventorymanager.Activities.MainActivity;
import com.crocusgames.destinyinventorymanager.DatabaseObjects.SqlLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;
import static com.crocusgames.destinyinventorymanager.AppConstants.AUTHORIZATION_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.TRANSFER_SINGLE_ITEM;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_VALUE;

/**
 * Created by Serkan on 15/12/16.
 */

public class DestinyApiCallObject {
    private DestinyApiResponseListener mListener;
    private Context mContext;
    private int mCountChar0, mCountChar1, mCountChar2;

    public DestinyApiCallObject(Context context) {
        mListener = null;
        mContext = context;
    }

    public interface DestinyApiResponseListener {
        void onObjectReady();
    }

    public void setDestinyApiResponseListener(DestinyApiResponseListener listener) {
        mListener = listener;
    }

    public void getCharacterInventory(final String accessToken) {
        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
        String membershipType = characterInfo.getMembershipType().toString();
        String membershipId = characterInfo.getMembershipId();
        final String characterId = characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId();

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://www.bungie.net/Platform/Destiny/" + membershipType +
                "/Account/" + membershipId + "/Character/" + characterId +
                "/Inventory/Summary/?definitions=true";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    JSONObject responseInJson = response.getJSONObject("Response");
                    JSONObject data = responseInJson.getJSONObject("data");
                    JSONArray items = data.getJSONArray("items");

                    ArrayList<ItemPreDefinitionObject> itemHashList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemObject = items.getJSONObject(i);
                        String itemHash = itemObject.getString("itemHash");
                        String itemId = itemObject.getString("itemId");
                        String bucketHash = itemObject.getString("bucketHash");
                        Integer itemQuantity = itemObject.getInt("quantity");
                        Integer transferStatus = itemObject.getInt("transferStatus");
                        Boolean isGridComplete;
                        try {
                            isGridComplete = itemObject.getBoolean("isGridComplete");
                        } catch (Exception e) {
                            isGridComplete = false;
                        }
                        Integer itemLightLevel;
                        try {
                            JSONObject primaryStatus = itemObject.getJSONObject("primaryStat");
                            itemLightLevel = primaryStatus.getInt("value");
                        } catch (Exception e) {
                            itemLightLevel = -1;
                        }
                        itemHashList.add(new ItemPreDefinitionObject(bucketHash, itemHash, itemId,
                                itemQuantity, transferStatus, itemLightLevel, isGridComplete));
                    }

                    JSONObject definitions = responseInJson.getJSONObject("definitions");
                    JSONObject definitionItems = definitions.getJSONObject("items");
                    JSONObject buckets = definitions.getJSONObject("buckets");

                    characterInfo.clearAllItemList();

                    for (int i = 0; i < itemHashList.size(); i++) {
                        JSONObject itemInfoObject = definitionItems.getJSONObject(itemHashList.get(i).getItemHash());
                        JSONObject bucketInfoObject = buckets.getJSONObject(itemHashList.get(i).getBucketHash());
                        String itemName = itemInfoObject.getString("itemName");
                        String description;
                        try {
                            description = itemInfoObject.getString("itemDescription");
                        } catch (Exception e) {
                            description = "";
                        }
                        String iconUrl = itemInfoObject.getString("icon");

                        String secondaryIconUrl = itemInfoObject.getString("secondaryIcon");
                        String tierTypeName;
                        try {
                            tierTypeName = itemInfoObject.getString("tierTypeName");
                        } catch (Exception e) {
                            tierTypeName = "";
                        }
                        String itemTypeName = itemInfoObject.getString("itemTypeName");
                        String bucketName = bucketInfoObject.getString("bucketName");

                        characterInfo.addToItemList(new ItemCompleteObject(itemHashList.get(i).getBucketHash(),
                                bucketName, description, iconUrl, itemHashList.get(i).getItemHash(),
                                itemHashList.get(i).getItemId(), itemName, itemTypeName,
                                itemHashList.get(i).getLightLevel(), itemHashList.get(i).getQuantity(),
                                tierTypeName, itemHashList.get(i).getTransferStatus(), itemHashList.get(i).isGridComplete(), secondaryIconUrl));

                        Log.d(AppConstants.TAG, "onResponse: " + itemHashList.get(i).getItemHash() + " / " + itemName + " / " + itemTypeName +
                                " / " + tierTypeName + " / " + bucketName + " / " + itemHashList.get(i).getLightLevel()
                                + " / " + itemHashList.get(i).isGridComplete());
                    }

                    if (mListener != null) {
                        mListener.onObjectReady();
                    }

                    Log.d(AppConstants.TAG, "onResponse: " + characterInfo.getItemList().size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: " + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void getCharacterInventoryFromDb(final String accessToken) {
        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
        String membershipType = characterInfo.getMembershipType().toString();
        String membershipId = characterInfo.getMembershipId();
        final String characterId = characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId();

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://www.bungie.net/Platform/Destiny/" + membershipType +
                "/Account/" + membershipId + "/Character/" + characterId +
                "/Inventory/Summary/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    JSONObject responseInJson = response.getJSONObject("Response");
                    JSONObject data = responseInJson.getJSONObject("data");
                    JSONArray items = data.getJSONArray("items");

                    ArrayList<ItemPreDefinitionObject> itemHashList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemObject = items.getJSONObject(i);
                        String itemHash = itemObject.getString("itemHash");
                        String itemId = itemObject.getString("itemId");
                        String bucketHash = itemObject.getString("bucketHash");
                        Integer itemQuantity = itemObject.getInt("quantity");
                        Integer transferStatus = itemObject.getInt("transferStatus");
                        Boolean isGridComplete;
                        try {
                            isGridComplete = itemObject.getBoolean("isGridComplete");
                        } catch (Exception e) {
                            isGridComplete = false;
                        }
                        Integer itemLightLevel;
                        try {
                            JSONObject primaryStatus = itemObject.getJSONObject("primaryStat");
                            itemLightLevel = primaryStatus.getInt("value");
                        } catch (Exception e) {
                            itemLightLevel = -1;
                        }
                        itemHashList.add(new ItemPreDefinitionObject(bucketHash, itemHash, itemId,
                                itemQuantity, transferStatus, itemLightLevel, isGridComplete));
                    }

                    characterInfo.clearAllItemList();

                    for (int i = 0; i < itemHashList.size(); i++) {
                        String itemHashString = itemHashList.get(i).getItemHash();
                        String bucketHashString = itemHashList.get(i).getBucketHash();
                        Long itemHashLong = Long.parseLong(itemHashString);
                        Long bucketHashLong = Long.parseLong(bucketHashString);

                        JSONObject itemHashDefinition
                                = SqlLiteOpenHelper.getInstance(mContext).getItemDefinitionObject(itemHashLong);

                        JSONObject bucketHashDefinition
                                = SqlLiteOpenHelper.getInstance(mContext).getBucketDefinitionObject(bucketHashLong);

                        String itemName = "";
                        String description = "";
                        String iconUrl = "";
                        String secondaryIconUrl = "";
                        String tierTypeName = "";
                        String itemTypeName = "";
                        String bucketName = "";

                        itemName = itemHashDefinition.getString("itemName");
                        try {
                            description = itemHashDefinition.getString("itemDescription");
                        } catch (Exception e) {
                            description = "";
                        }
                        iconUrl = itemHashDefinition.getString("icon");
                        secondaryIconUrl = itemHashDefinition.getString("secondaryIcon");
                        try {
                            tierTypeName = itemHashDefinition.getString("tierTypeName");
                        } catch (Exception e) {
                            tierTypeName = "";
                        }
                        itemTypeName = itemHashDefinition.getString("itemTypeName");


                        bucketName = bucketHashDefinition.getString("bucketName");


                        //Log.d(AppConstants.TAG, "ITEMHASH " + itemHashString +" / " + itemHashDefinition);
                        //Log.d(AppConstants.TAG, "BUCKETHASH " + bucketHashString + " / " + bucketHashDefinition);

                        characterInfo.addToItemList(new ItemCompleteObject(itemHashList.get(i).getBucketHash(),
                                bucketName, description, iconUrl, itemHashList.get(i).getItemHash(),
                                itemHashList.get(i).getItemId(), itemName, itemTypeName,
                                itemHashList.get(i).getLightLevel(), itemHashList.get(i).getQuantity(),
                                tierTypeName, itemHashList.get(i).getTransferStatus(), itemHashList.get(i).isGridComplete(), secondaryIconUrl));

                        Log.d(AppConstants.TAG, "onResponse: " + itemName + " / " + itemTypeName +
                                " / " + tierTypeName + " / " + bucketName + " / " + itemHashList.get(i).getLightLevel()
                                + " / " + itemHashList.get(i).isGridComplete());

                    }

                    if (mListener != null) {
                        mListener.onObjectReady();
                    }

                    Log.d(AppConstants.TAG, "Size: " + characterInfo.getItemList().size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: " + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void getCharacterInventoryFromDbByList(final String accessToken) {
        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
        String membershipType = characterInfo.getMembershipType().toString();
        String membershipId = characterInfo.getMembershipId();
        final String characterId = characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId();

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://www.bungie.net/Platform/Destiny/" + membershipType +
                "/Account/" + membershipId + "/Character/" + characterId +
                "/Inventory/Summary/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    JSONObject responseInJson = response.getJSONObject("Response");
                    JSONObject data = responseInJson.getJSONObject("data");
                    JSONArray items = data.getJSONArray("items");

                    ArrayList<ItemPreDefinitionObject> itemHashList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemObject = items.getJSONObject(i);
                        String itemHash = itemObject.getString("itemHash");
                        String itemId = itemObject.getString("itemId");
                        String bucketHash = itemObject.getString("bucketHash");
                        Integer itemQuantity = itemObject.getInt("quantity");
                        Integer transferStatus = itemObject.getInt("transferStatus");
                        Boolean isGridComplete;
                        try {
                            isGridComplete = itemObject.getBoolean("isGridComplete");
                        } catch (Exception e) {
                            isGridComplete = false;
                        }
                        Integer itemLightLevel;
                        try {
                            JSONObject primaryStatus = itemObject.getJSONObject("primaryStat");
                            itemLightLevel = primaryStatus.getInt("value");
                        } catch (Exception e) {
                            itemLightLevel = -1;
                        }
                        itemHashList.add(new ItemPreDefinitionObject(bucketHash, itemHash, itemId,
                                itemQuantity, transferStatus, itemLightLevel, isGridComplete));
                    }

                    characterInfo.clearAllItemList();

                    List<JSONObject> itemHashObjectsList =
                            SqlLiteOpenHelper.getInstance(mContext).getItemDefinitionObjectByList(itemHashList);

                    List<JSONObject> bucketHashObjectsList
                            = SqlLiteOpenHelper.getInstance(mContext).getItemBucketDefinitionObjectByList(itemHashList);

                    for (int i = 0; i < itemHashObjectsList.size(); i++) {
                        JSONObject itemHashDefinition = itemHashObjectsList.get(i);
                        JSONObject bucketHashDefinition = bucketHashObjectsList.get(i);

                        String itemName = "";
                        String description = "";
                        String iconUrl = "";
                        String secondaryIconUrl = "";
                        String tierTypeName = "";
                        String itemTypeName = "";
                        String bucketName = "";

                        itemName = itemHashDefinition.getString("itemName");
                        try {
                            description = itemHashDefinition.getString("itemDescription");
                        } catch (Exception e) {
                            description = "";
                        }
                        iconUrl = itemHashDefinition.getString("icon");
                        secondaryIconUrl = itemHashDefinition.getString("secondaryIcon");
                        try {
                            tierTypeName = itemHashDefinition.getString("tierTypeName");
                        } catch (Exception e) {
                            tierTypeName = "";
                        }
                        itemTypeName = itemHashDefinition.getString("itemTypeName");


                        bucketName = bucketHashDefinition.getString("bucketName");


                        //Log.d(AppConstants.TAG, "ITEMHASH " + itemHashString +" / " + itemHashDefinition);
                        //Log.d(AppConstants.TAG, "BUCKETHASH " + bucketHashString + " / " + bucketHashDefinition);

                        characterInfo.addToItemList(new ItemCompleteObject(itemHashList.get(i).getBucketHash(),
                                bucketName, description, iconUrl, itemHashList.get(i).getItemHash(),
                                itemHashList.get(i).getItemId(), itemName, itemTypeName,
                                itemHashList.get(i).getLightLevel(), itemHashList.get(i).getQuantity(),
                                tierTypeName, itemHashList.get(i).getTransferStatus(), itemHashList.get(i).isGridComplete(), secondaryIconUrl));

                        Log.d(AppConstants.TAG, "onResponse: " + itemName + " / " + itemTypeName +
                                " / " + tierTypeName + " / " + bucketName + " / " + itemHashList.get(i).getLightLevel()
                                + " / " + itemHashList.get(i).isGridComplete());

                    }

                    if (mListener != null) {
                        mListener.onObjectReady();
                    }

                    Log.d(AppConstants.TAG, "Size: " + characterInfo.getItemList().size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: " + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void getCharacterInventoryFromDbAllAtOnce(final String accessToken) {

        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
        String membershipType = characterInfo.getMembershipType().toString();
        String membershipId = characterInfo.getMembershipId();
        final String characterId = characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId();

        characterInfo.clearAllItemList();

        characterInfo.setApiFinished(false);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://www.bungie.net/Platform/Destiny/" + membershipType +
                "/Account/" + membershipId + "/Character/" + characterId +
                "/Inventory/Summary/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    JSONObject responseInJson = response.getJSONObject("Response");
                    JSONObject data = responseInJson.getJSONObject("data");
                    JSONArray items = data.getJSONArray("items");

                    final ArrayList<ItemPreDefinitionObject> itemHashList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemObject = items.getJSONObject(i);
                        String itemHash = itemObject.getString("itemHash");
                        String itemId = itemObject.getString("itemId");
                        String bucketHash = itemObject.getString("bucketHash");
                        Log.d(TAG, "BucketHash: " + bucketHash);
                        Integer itemQuantity = itemObject.getInt("quantity");
                        Integer transferStatus = itemObject.getInt("transferStatus");
                        Boolean isGridComplete;
                        try {
                            isGridComplete = itemObject.getBoolean("isGridComplete");
                        } catch (Exception e) {
                            isGridComplete = false;
                        }
                        Integer itemLightLevel;
                        try {
                            JSONObject primaryStatus = itemObject.getJSONObject("primaryStat");
                            itemLightLevel = primaryStatus.getInt("value");
                        } catch (Exception e) {
                            itemLightLevel = -1;
                        }
                        itemHashList.add(new ItemPreDefinitionObject(bucketHash, itemHash, itemId,
                                itemQuantity, transferStatus, itemLightLevel, isGridComplete));
                    }

                    String itemHashCombined = "";
                    String bucketHashCombined = "";

                    for (int i = 0; i < itemHashList.size(); i++) {
                        if (i != itemHashList.size() - 1) {
                            itemHashCombined = itemHashCombined + itemHashList.get(i).getItemHash() + ", ";
                            bucketHashCombined = bucketHashCombined + itemHashList.get(i).getBucketHash() + ", ";
                        } else {
                            itemHashCombined = itemHashCombined + itemHashList.get(i).getItemHash();
                            bucketHashCombined = bucketHashCombined + itemHashList.get(i).getBucketHash();
                            Log.d(TAG, "onResponse: " + bucketHashCombined);
                        }
                    }

                    final String finalBucketHashCombined = bucketHashCombined;
                    AsyncTask<String, Void, HashMap<Long, JSONObject>> itemHashTask = new AsyncTask<String, Void, HashMap<Long, JSONObject>>() {
                        @Override
                        protected HashMap<Long, JSONObject> doInBackground(String... strings) {
                            HashMap<Long, JSONObject> itemHashObjectsList = null;
                            try {
                                itemHashObjectsList =
                                        SqlLiteOpenHelper.getInstance(mContext).getItemDefinitionObjectAllatOnce(strings[0]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return itemHashObjectsList;
                        }

                        @Override
                        protected void onPostExecute(final HashMap<Long, JSONObject> itemHashObjectsList) {
                            super.onPostExecute(itemHashObjectsList);

                            AsyncTask<String, Void, HashMap<Long, JSONObject>> bucketHashTask = new AsyncTask<String, Void, HashMap<Long, JSONObject>>() {
                                @Override
                                protected HashMap<Long, JSONObject> doInBackground(String... strings) {
                                    HashMap<Long, JSONObject> bucketHashObjectsList = null;
                                    try {
                                        bucketHashObjectsList = SqlLiteOpenHelper.getInstance(mContext).
                                                getBucketDefinitionObjectAllatOnce(strings[0]);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return bucketHashObjectsList;
                                }

                                @Override
                                protected void onPostExecute(HashMap<Long, JSONObject> bucketHashObjectsList) {
                                    super.onPostExecute(bucketHashObjectsList);

                                    Log.d(AppConstants.TAG, "ActualItemList: " + itemHashList.size());
                                    Log.d(AppConstants.TAG, "itemHashList from db: " + itemHashObjectsList.size());
                                    Log.d(AppConstants.TAG, "itemBucketList from db: " + bucketHashObjectsList.size());
                                    //Log.d(AppConstants.TAG, "onResponse: " + bucketHashObjectsList.get(215593132L));

                                    for (int i = 0; i < itemHashList.size(); i++) {
                                        String itemId = itemHashList.get(i).getItemId();
                                        Integer lightLevel = itemHashList.get(i).getLightLevel();
                                        Integer quantity = itemHashList.get(i).getQuantity();
                                        Integer transferStatus = itemHashList.get(i).getTransferStatus();
                                        Boolean isGridComplete = itemHashList.get(i).isGridComplete();
                                        Long bucketHash = Long.parseLong(itemHashList.get(i).getBucketHash());

                                        JSONObject itemHashDefinition = itemHashObjectsList.get(Long.parseLong(itemHashList.get(i).getItemHash()));
                                        Long itemHash = -1l;
                                        try {
                                            itemHash = itemHashDefinition.getLong("itemHash");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        JSONObject bucketHashDefinition = bucketHashObjectsList.get(bucketHash);
                                        Log.d(TAG, "onResponse: " + bucketHash + " / " + bucketHashDefinition);

                                        String itemName = "";
                                        String description = "";
                                        String iconUrl = "";
                                        String secondaryIconUrl = "";
                                        String tierTypeName = "";
                                        String itemTypeName = "";
                                        String bucketName = "";
                                        try {
                                            itemName = itemHashDefinition.getString("itemName");
                                            iconUrl = itemHashDefinition.getString("icon");
                                            secondaryIconUrl = itemHashDefinition.getString("secondaryIcon");
                                            itemTypeName = itemHashDefinition.getString("itemTypeName");
                                            bucketName = bucketHashDefinition.getString("bucketName");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            description = itemHashDefinition.getString("itemDescription");
                                        } catch (Exception e) {
                                            description = "";
                                        }
                                        try {
                                            tierTypeName = itemHashDefinition.getString("tierTypeName");
                                        } catch (Exception e) {
                                            tierTypeName = "";
                                        }
                                        //Log.d(AppConstants.TAG, "ITEMHASH " + itemHashString +" / " + itemHashDefinition);
                                        //Log.d(AppConstants.TAG, "BUCKETHASH " + bucketHashString + " / " + bucketHashDefinition);

                                        if (characterInfo.getItemList().size() < itemHashList.size()) {
                                            characterInfo.addToItemList(new ItemCompleteObject(bucketHash.toString(),
                                                    bucketName, description, iconUrl, itemHash.toString(), itemId, itemName,
                                                    itemTypeName, lightLevel, quantity,
                                                    tierTypeName, transferStatus, isGridComplete
                                                    , secondaryIconUrl));
                                        }

//                        Log.d(AppConstants.TAG, "onResponse: " + itemName + " / " + transferStatus + " / " + itemTypeName +
//                                " / " + tierTypeName + " / " + bucketName + " / " + lightLevel
//                                + " / " + isGridComplete + " / " + bucketHash);
                                    }

                                    Log.d(TAG, "ListVS: " + itemHashList.size());
                                    Log.d(TAG, "ListVS: " + characterInfo.getItemList().size());

                                    characterInfo.setApiFinished(true);

                                    if (mListener != null) {
                                        mListener.onObjectReady();
                                    }

                                    Log.d(AppConstants.TAG, "Double Size?: " + characterInfo.getItemList().size());
                                }
                            };
                            bucketHashTask.execute(finalBucketHashCombined);
                        }
                    };
                    itemHashTask.execute(itemHashCombined);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: " + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void getVaultInventoryFromDbAllAtOnce(final String accessToken) {
        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();
        String membershipType = characterInfo.getMembershipType().toString();
        characterInfo.clearAllItemList();

        if (membershipType.equals("-5")) {
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
        }

        Log.d(TAG, "getVaultInventoryFromDbAllAtOnce: Membership type" + membershipType);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://www.bungie.net/Platform/Destiny/" + membershipType +
                "/MyAccount/Vault/Summary/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    JSONObject responseInJson = response.getJSONObject("Response");
                    JSONObject data = responseInJson.getJSONObject("data");
                    JSONArray items = data.getJSONArray("items");

                    final ArrayList<ItemPreDefinitionObject> itemHashList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemObject = items.getJSONObject(i);
                        String itemHash = itemObject.getString("itemHash");
                        String itemId = itemObject.getString("itemId");
                        String bucketHash = itemObject.getString("bucketHash");
                        Integer itemQuantity = itemObject.getInt("quantity");
                        Integer transferStatus = itemObject.getInt("transferStatus");
                        Boolean isGridComplete;
                        try {
                            isGridComplete = itemObject.getBoolean("isGridComplete");
                        } catch (Exception e) {
                            isGridComplete = false;
                        }
                        Integer itemLightLevel;
                        try {
                            JSONObject primaryStatus = itemObject.getJSONObject("primaryStat");
                            itemLightLevel = primaryStatus.getInt("value");
                        } catch (Exception e) {
                            itemLightLevel = -1;
                        }
                        itemHashList.add(new ItemPreDefinitionObject(bucketHash, itemHash, itemId,
                                itemQuantity, transferStatus, itemLightLevel, isGridComplete));
                    }

                    String itemHashCombined = "";

                    for (int i = 0; i < itemHashList.size(); i++) {
                        if (i != itemHashList.size() - 1) {
                            itemHashCombined = itemHashCombined + itemHashList.get(i).getItemHash() + ", ";
                        } else {
                            itemHashCombined = itemHashCombined + itemHashList.get(i).getItemHash();
                        }
                    }

                    AsyncTask<String, Void, HashMap<Long, JSONObject> > itemHashTask = new AsyncTask<String, Void, HashMap<Long, JSONObject>>() {
                        @Override
                        protected HashMap<Long, JSONObject> doInBackground(String... strings) {
                            HashMap<Long, JSONObject> itemHashObjectsList = null;
                            try {
                                itemHashObjectsList = SqlLiteOpenHelper.getInstance(mContext).getItemDefinitionObjectAllatOnce(strings[0]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return itemHashObjectsList;
                        }

                        @Override
                        protected void onPostExecute(final HashMap<Long, JSONObject> itemHashObjectsList) {
                            super.onPostExecute(itemHashObjectsList);

                            ArrayList<String> bucketHashList = new ArrayList<>();

                            for (int i = 0; i < itemHashList.size(); i++) {
                                JSONObject itemDefinition = itemHashObjectsList.get(Long.parseLong(itemHashList.get(i).getItemHash()));
                                try {
                                    bucketHashList.add(itemDefinition.getString("bucketTypeHash"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            Log.d(TAG, "onResponse: " + bucketHashList.size());

                            String bucketHashCombined = "";

                            for (int i = 0; i < bucketHashList.size(); i++) {
                                if (i != bucketHashList.size() - 1) {
                                    bucketHashCombined = bucketHashCombined + bucketHashList.get(i) + ", ";
                                } else {
                                    bucketHashCombined = bucketHashCombined + bucketHashList.get(i);
                                }
                            }

                            AsyncTask<String, Void, HashMap<Long, JSONObject>> bucketListTask = new
                                    AsyncTask<String, Void, HashMap<Long, JSONObject>>() {
                                        @Override
                                        protected HashMap<Long, JSONObject> doInBackground(String... strings) {
                                            HashMap<Long, JSONObject> bucketHashObjectsList
                                                    = null;
                                            try {
                                                bucketHashObjectsList = SqlLiteOpenHelper.getInstance(mContext).getBucketDefinitionObjectAllatOnce(strings[0]);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            return bucketHashObjectsList;
                                        }

                                        @Override
                                        protected void onPostExecute(HashMap<Long, JSONObject> bucketHashObjectsList) {
                                            super.onPostExecute(bucketHashObjectsList);


                                            //Log.d(AppConstants.TAG, "ActualItemList: " + itemHashList.size());
                                            //Log.d(AppConstants.TAG, "itemHashList from db: " + itemHashObjectsList.size());
                                            //Log.d(AppConstants.TAG, "itemBucketList from db: " + bucketHashObjectsList.size());

                                            for (int i = 0; i < itemHashList.size(); i++) {

                                                String itemId = itemHashList.get(i).getItemId();
                                                Integer lightLevel = itemHashList.get(i).getLightLevel();
                                                Integer quantity = itemHashList.get(i).getQuantity();
                                                Integer transferStatus = itemHashList.get(i).getTransferStatus();
                                                Boolean isGridComplete = itemHashList.get(i).isGridComplete();

                                                JSONObject itemHashDefinition = itemHashObjectsList.get(Long.parseLong(itemHashList.get(i).getItemHash()));
                                                Long bucketHash = -1L;
                                                Long itemHash = -1L;
                                                try {
                                                    bucketHash = itemHashDefinition.getLong("bucketTypeHash");
                                                    itemHash = itemHashDefinition.getLong("itemHash");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                JSONObject bucketHashDefinition = bucketHashObjectsList.get(bucketHash);

                                                String itemName = "";
                                                String description = "";
                                                String iconUrl = "";
                                                String secondaryIconUrl = "";
                                                String tierTypeName = "";
                                                String itemTypeName = "";
                                                String bucketName = "";

                                                try {
                                                    itemName = itemHashDefinition.getString("itemName");
                                                    iconUrl = itemHashDefinition.getString("icon");
                                                    secondaryIconUrl = itemHashDefinition.getString("secondaryIcon");
                                                    itemTypeName = itemHashDefinition.getString("itemTypeName");
                                                    bucketName = bucketHashDefinition.getString("bucketName");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    description = itemHashDefinition.getString("itemDescription");
                                                } catch (Exception e) {
                                                    description = "";
                                                }
                                                try {
                                                    tierTypeName = itemHashDefinition.getString("tierTypeName");
                                                } catch (Exception e) {
                                                    tierTypeName = "";
                                                }

                                                //Log.d(AppConstants.TAG, "ITEMHASH " + itemHashString +" / " + itemHashDefinition);
                                                //Log.d(AppConstants.TAG, "BUCKETHASH " + bucketHashString + " / " + bucketHashDefinition);

                                                characterInfo.addToItemList(new ItemCompleteObject(bucketHash.toString(),
                                                        bucketName, description, iconUrl, itemHash.toString(), itemId, itemName,
                                                        itemTypeName, lightLevel, quantity, tierTypeName, transferStatus,
                                                        isGridComplete, secondaryIconUrl));

                                                Log.d(AppConstants.TAG, "onResponse: " + itemName + " / " + transferStatus + " / " + itemTypeName +
                                                        " / " + tierTypeName + " / " + bucketName + " / " + lightLevel
                                                        + " / " + isGridComplete + " / " + bucketHash);

                                            }

                                            if (mListener != null) {
                                                mListener.onObjectReady();
                                            }
                                            Log.d(AppConstants.TAG, "Size: " + characterInfo.getItemList().size());
                                        }
                                    };
                            bucketListTask.execute(bucketHashCombined);
                        }
                    };
                    itemHashTask.execute(itemHashCombined);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: " + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void getCharacterInventoryFromDbForGrindMode(final String accessToken, final String membershipId,
                                                        final String membershipType, final String characterId,
                                                        final int characterIndex) {

        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        switch (characterIndex) {
            case 0:
                characterInfo.clearAllGrindList0();
                break;
            case 1:
                characterInfo.clearAllGrindList1();
                break;
            case 2:
                characterInfo.clearAllGrindList2();
                break;
        }

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://www.bungie.net/Platform/Destiny/" + membershipType +
                "/Account/" + membershipId + "/Character/" + characterId +
                "/Inventory/Summary/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String status = "N/A";
                try {
                    status = response.getString("Message");
                    Log.d(TAG, "onResponse: " + status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject responseInJson = response.getJSONObject("Response");
                    JSONObject data = responseInJson.getJSONObject("data");
                    JSONArray items = data.getJSONArray("items");

                    final ArrayList<ItemPreDefinitionObject> itemHashList = new ArrayList<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject itemObject = items.getJSONObject(i);
                        String itemHash = itemObject.getString("itemHash");
                        String itemId = itemObject.getString("itemId");
                        String bucketHash = itemObject.getString("bucketHash");
                        Integer itemQuantity = itemObject.getInt("quantity");
                        Integer transferStatus = itemObject.getInt("transferStatus");
                        Boolean isGridComplete;
                        try {
                            isGridComplete = itemObject.getBoolean("isGridComplete");
                        } catch (Exception e) {
                            isGridComplete = false;
                        }
                        Integer itemLightLevel;
                        try {
                            JSONObject primaryStatus = itemObject.getJSONObject("primaryStat");
                            itemLightLevel = primaryStatus.getInt("value");
                        } catch (Exception e) {
                            itemLightLevel = -1;
                        }
                        itemHashList.add(new ItemPreDefinitionObject(bucketHash, itemHash, itemId,
                                itemQuantity, transferStatus, itemLightLevel, isGridComplete));
                    }

                    String itemHashCombined = "";
                    String bucketHashCombined = "";

                    for (int i = 0; i < itemHashList.size(); i++) {
                        if (i != itemHashList.size() - 1) {
                            itemHashCombined = itemHashCombined + itemHashList.get(i).getItemHash() + ", ";
                            bucketHashCombined = bucketHashCombined + itemHashList.get(i).getBucketHash() + ", ";
                        } else {
                            itemHashCombined = itemHashCombined + itemHashList.get(i).getItemHash();
                            bucketHashCombined = bucketHashCombined + itemHashList.get(i).getBucketHash();
                        }
                    }

                    final String finalBucketHashCombined = bucketHashCombined;
                    AsyncTask<String, Void, HashMap<Long, JSONObject>> itemHashTask = new AsyncTask<String, Void, HashMap<Long, JSONObject>>() {
                        @Override
                        protected HashMap<Long, JSONObject> doInBackground(String... strings) {
                            HashMap<Long, JSONObject> itemHashObjectsList = null;
                            try {
                                itemHashObjectsList =
                                        SqlLiteOpenHelper.getInstance(mContext).getItemDefinitionObjectAllatOnce(strings[0]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return itemHashObjectsList;
                        }

                        @Override
                        protected void onPostExecute(final HashMap<Long, JSONObject> itemHashObjectsList) {
                            super.onPostExecute(itemHashObjectsList);

                            AsyncTask<String, Void, HashMap<Long, JSONObject>> bucketHashTask = new AsyncTask<String, Void, HashMap<Long, JSONObject>>() {
                                @Override
                                protected HashMap<Long, JSONObject> doInBackground(String... strings) {
                                    HashMap<Long, JSONObject> bucketHashObjectsList = null;
                                    try {
                                        bucketHashObjectsList = SqlLiteOpenHelper.getInstance(mContext).
                                                getBucketDefinitionObjectAllatOnce(strings[0]);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return bucketHashObjectsList;
                                }

                                @Override
                                protected void onPostExecute(HashMap<Long, JSONObject> bucketHashObjectsList) {
                                    super.onPostExecute(bucketHashObjectsList);

//                                    Log.d(AppConstants.TAG, "ActualItemList: " + itemHashList.size());
//                                    Log.d(AppConstants.TAG, "itemHashList from db: " + itemHashObjectsList.size());
//                                    Log.d(AppConstants.TAG, "itemBucketList from db: " + bucketHashObjectsList.size());
                                    //Log.d(AppConstants.TAG, "onResponse: " + bucketHashObjectsList.get(215593132L));

                                    for (int i = 0; i < itemHashList.size(); i++) {

                                        String itemId = itemHashList.get(i).getItemId();
                                        Integer lightLevel = itemHashList.get(i).getLightLevel();
                                        Integer quantity = itemHashList.get(i).getQuantity();
                                        Integer transferStatus = itemHashList.get(i).getTransferStatus();
                                        Boolean isGridComplete = itemHashList.get(i).isGridComplete();
                                        Long bucketHash = Long.parseLong(itemHashList.get(i).getBucketHash());

                                        JSONObject itemHashDefinition = itemHashObjectsList.get(Long.parseLong(itemHashList.get(i).getItemHash()));
                                        Long itemHash = -1l;
                                        try {
                                            itemHash = itemHashDefinition.getLong("itemHash");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        JSONObject bucketHashDefinition = bucketHashObjectsList.get(bucketHash);
                                        //Log.d(TAG, "onResponse: " + bucketHash + " / " + bucketHashDefinition);

                                        String itemName = "";
                                        String description = "";
                                        String iconUrl = "";
                                        String secondaryIconUrl = "";
                                        String tierTypeName = "";
                                        String itemTypeName = "";
                                        String bucketName = "";
                                        try {
                                            itemName = itemHashDefinition.getString("itemName");
                                            iconUrl = itemHashDefinition.getString("icon");
                                            secondaryIconUrl = itemHashDefinition.getString("secondaryIcon");
                                            itemTypeName = itemHashDefinition.getString("itemTypeName");
                                            bucketName = bucketHashDefinition.getString("bucketName");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            description = itemHashDefinition.getString("itemDescription");
                                        } catch (Exception e) {
                                            description = "";
                                        }
                                        try {
                                            tierTypeName = itemHashDefinition.getString("tierTypeName");
                                        } catch (Exception e) {
                                            tierTypeName = "";
                                        }
                                        //Log.d(AppConstants.TAG, "ITEMHASH " + itemHashString +" / " + itemHashDefinition);
                                        //Log.d(AppConstants.TAG, "BUCKETHASH " + bucketHashString + " / " + bucketHashDefinition);

                                        switch (characterIndex) {
                                            case 0:
                                                characterInfo.addToGrindList0(new ItemCompleteObject(bucketHash.toString(),
                                                        bucketName, description, iconUrl, itemHash.toString(), itemId, itemName,
                                                        itemTypeName, lightLevel, quantity,
                                                        tierTypeName, transferStatus, isGridComplete
                                                        , secondaryIconUrl));
                                                break;
                                            case 1:
                                                characterInfo.addToGrindList1(new ItemCompleteObject(bucketHash.toString(),
                                                        bucketName, description, iconUrl, itemHash.toString(), itemId, itemName,
                                                        itemTypeName, lightLevel, quantity,
                                                        tierTypeName, transferStatus, isGridComplete
                                                        , secondaryIconUrl));
                                                break;
                                            case 2:
                                                characterInfo.addToGrindList2(new ItemCompleteObject(bucketHash.toString(),
                                                        bucketName, description, iconUrl, itemHash.toString(), itemId, itemName,
                                                        itemTypeName, lightLevel, quantity,
                                                        tierTypeName, transferStatus, isGridComplete
                                                        , secondaryIconUrl));
                                                break;
                                        }
                                    }

                                    switch (characterIndex) {
                                        case 0:
                                            final List<ItemCompleteObject> char0List = new ArrayList<>();
                                            for (int i = 0; i < characterInfo.getGrindList0().size(); i++) {
                                                if (characterInfo.getGrindList0().get(i).getItemTypeName().contains("Engram") &&
                                                        !characterInfo.getGrindList0().get(i).getBucketName().contains("Lost Items")) {
                                                    Log.d(TAG, "onPostExecute: " + characterInfo.getGrindList0().get(i).getItemTypeName());
                                                    Log.d(TAG, "onPostExecute: " + characterInfo.getGrindList0().get(i).getBucketName());
                                                    char0List.add(characterInfo.getGrindList0().get(i));
                                                }
                                            }

                                            mCountChar0 = 0;

                                            if (char0List.size() > 0) {
                                                new Timer().scheduleAtFixedRate(new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        Log.d(TAG, "run: transferring one item for char 0");
                                                        if (char0List.size() > 0 && mCountChar0 < char0List.size()) {
                                                            transferToVault(accessToken, char0List.get(mCountChar0).getItemHash(),
                                                                    char0List.get(mCountChar0).getItemId(), characterId, membershipType);
                                                            mCountChar0++;
                                                        } else if (mCountChar0 == char0List.size()) {
                                                            Log.d(TAG, "run: COMPLETED");
                                                            cancel();
                                                        }
                                                    }
                                                }, 0, 5000);
                                            }

                                            Log.d(AppConstants.TAG, "Size: " + characterInfo.getGrindList0().size());
                                            break;
                                        case 1:
                                            final List<ItemCompleteObject> char1List = new ArrayList<>();
                                            for (int i = 0; i < characterInfo.getGrindList1().size(); i++) {
                                                if (characterInfo.getGrindList1().get(i).getItemTypeName().contains("Engram") &&
                                                        !characterInfo.getGrindList1().get(i).getBucketName().contains("Lost Items")) {
                                                    Log.d(TAG, "onPostExecute: " + characterInfo.getGrindList1().get(i).getItemTypeName());
                                                    Log.d(TAG, "onPostExecute: " + characterInfo.getGrindList1().get(i).getBucketName());
                                                    char1List.add(characterInfo.getGrindList1().get(i));
                                                }

                                            }

                                            mCountChar1 = 0;

                                            if (char1List.size() > 0) {
                                                new Timer().scheduleAtFixedRate(new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        Log.d(TAG, "run: transferring one item for char1");
                                                        if (char1List.size() > 0 && mCountChar1 < char1List.size()) {
                                                            transferToVault(accessToken, char1List.get(mCountChar1).getItemHash(),
                                                                    char1List.get(mCountChar1).getItemId(), characterId, membershipType);
                                                            mCountChar1++;
                                                        } else if (mCountChar1 == char1List.size()) {
                                                            Log.d(TAG, "run: COMPLETED");
                                                            cancel();
                                                        }
                                                    }
                                                }, 0, 5000);
                                            }

                                            Log.d(AppConstants.TAG, "Size: " + characterInfo.getGrindList1().size());
                                            break;
                                        case 2:
                                            final List<ItemCompleteObject> char2List = new ArrayList<>();
                                            for (int i = 0; i < characterInfo.getGrindList2().size(); i++) {
                                                if (characterInfo.getGrindList2().get(i).getItemTypeName().contains("Engram") &&
                                                        !characterInfo.getGrindList2().get(i).getBucketName().contains("Lost Items")) {
                                                    Log.d(TAG, "onPostExecute: " + characterInfo.getGrindList2().get(i).getItemTypeName());
                                                    Log.d(TAG, "onPostExecute: " + characterInfo.getGrindList2().get(i).getBucketName());
                                                    char2List.add(characterInfo.getGrindList2().get(i));
                                                }
                                            }

                                            mCountChar2 = 0;

                                            if (char2List.size() > 0) {
                                                new Timer().scheduleAtFixedRate(new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        Log.d(TAG, "run: transferring one item for char2");
                                                        if (mCountChar2 < char2List.size()) {
                                                            transferToVault(accessToken, char2List.get(mCountChar2).getItemHash(),
                                                                    char2List.get(mCountChar2).getItemId(), characterId, membershipType);
                                                            mCountChar2++;
                                                        } else if (mCountChar2 == char2List.size()) {
                                                            Log.d(TAG, "run: COMPLETED");
                                                            cancel();
                                                        }
                                                    }
                                                }, 0, 5000);
                                            }

                                            Log.d(AppConstants.TAG, "Size: " + characterInfo.getGrindList2().size());
                                            break;
                                    }
                                }
                            };
                            bucketHashTask.execute(finalBucketHashCombined);
                        }
                    };
                    itemHashTask.execute(itemHashCombined);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: " + error);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void transferToVault(final String accessToken, String itemHash, String itemId,
                                String characterId, String membershipType) {

        RequestQueue queue = Volley.newRequestQueue(mContext);

        HashMap<String, String> params = new HashMap<>();
        params.put("itemReferenceHash", itemHash);
        params.put("stackSize", "1");
        params.put("transferToVault", "true");
        params.put("itemId", itemId);
        params.put("characterId", characterId);
        params.put("membershipType", membershipType);

        String url = TRANSFER_SINGLE_ITEM;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    String message = response.getString("Message");
                    if (message.equals("Ok")) {
                        Log.d(TAG, "onResponse: Sync transfer successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: Transfer failed b/c connectivity/server.");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(X_API_KEY_NAME, X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(request);
    }
}