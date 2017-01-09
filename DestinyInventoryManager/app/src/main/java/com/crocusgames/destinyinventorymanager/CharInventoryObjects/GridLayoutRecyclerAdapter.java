package com.crocusgames.destinyinventorymanager.CharInventoryObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.ItemCompleteObject;
import com.crocusgames.destinyinventorymanager.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.crocusgames.destinyinventorymanager.AppConstants.ACCESS_TOKEN_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.AUTHORIZATION_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.BUNGIE_NET_START_URL;
import static com.crocusgames.destinyinventorymanager.AppConstants.TRANSFER_SINGLE_ITEM;
import static com.crocusgames.destinyinventorymanager.AppConstants.USER_PREFERENCES;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_NAME;
import static com.crocusgames.destinyinventorymanager.AppConstants.X_API_KEY_VALUE;

/**
 * Created by Serkan on 15/12/16.
 */

public class GridLayoutRecyclerAdapter extends RecyclerView.Adapter<GridLayoutViewHolder> {
    private List<ItemCompleteObject> mItemList;
    private Context mContext;
    private ItemCompleteObject mEquippedObject;
    private ImageView mEquippedImageIcon;
    private View mView;
    private CharacterInfoSingleton mCharacterInfo;
    private String mAccessToken;
    private PrimaryEquippedListener mPrimaryListener;
    private SpecialEquippedListener mSpecialListener;
    private HeavyEquippedListener mHeavyListener;
    private GhostEquippedListener mGhostListener;
    private HelmetEquippedListener mHelmetListener;
    private GauntletEquippedListener mGauntletListener;
    private ChestArmorEquippedListener mChestArmorListener;
    private LegArmorEquippedListener mLegArmorListener;
    private ClassArmorEquippedListener mClassArmorListener;
    private ArtifactEquippedListener mArtifactListener;
    private ShaderEquippedListener mShaderListener;
    private EmblemEquippedListener mEmblemListener;
    private ShipEquippedListener mShipListener;
    private SparrowEquippedListener mSparrowListener;
    private EmoteEquippedListener mEmoteListener;
    private SparrowHornEquippedListener mSparrowHornListener;

    public GridLayoutRecyclerAdapter(List<ItemCompleteObject> itemList, ItemCompleteObject
            equippedItemObject, ImageView equippedItemIcon, View view) {
        mItemList = itemList;
        mEquippedObject = equippedItemObject;
        mEquippedImageIcon = equippedItemIcon;
        mView = view;
    }

    //The following listeners are to notify the respective fragments of the equipped item change.
    public interface PrimaryEquippedListener {
        void onPrimaryWeaponEquipped(ItemCompleteObject equippedPrimaryItem);
    }

    public void setPrimaryEquippedListener(PrimaryEquippedListener listener) {
        mPrimaryListener = listener;
    }

    public interface SpecialEquippedListener {
        void onSpecialWeaponEquipped(ItemCompleteObject equippedSpecialItem);
    }

    public void setSpecialEquippedListener(SpecialEquippedListener listener) {
        mSpecialListener = listener;
    }

    public interface HeavyEquippedListener {
        void onHeavyWeaponEquipped(ItemCompleteObject equippedHeavyItem);
    }

    public void setHeavyEquippedListener(HeavyEquippedListener listener) {
        mHeavyListener = listener;
    }

    public interface GhostEquippedListener {
        void onGhostEquipped(ItemCompleteObject equippedGhostItem);
    }

    public void setGhostEquippedListener(GhostEquippedListener listener) {
        mGhostListener = listener;
    }

    public interface HelmetEquippedListener {
        void onHelmetEquipped(ItemCompleteObject equippedHelmetItem);
    }

    public void setHelmetEquippedListener(HelmetEquippedListener listener) {
        mHelmetListener = listener;
    }

    public interface GauntletEquippedListener {
        void onGauntletEquipped(ItemCompleteObject equippedGauntletItem);
    }

    public void setGauntletEquippedListener(GauntletEquippedListener listener) {
        mGauntletListener = listener;
    }

    public interface ChestArmorEquippedListener {
        void onChestArmorEquipped(ItemCompleteObject equippedChestArmorItem);
    }

    public void setChestArmorEquippedListener(ChestArmorEquippedListener listener) {
        mChestArmorListener = listener;
    }

    public interface LegArmorEquippedListener {
        void onLegArmorEquipped(ItemCompleteObject equippedLegArmor);
    }

    public void setLegArmorEquippedListener(LegArmorEquippedListener listener) {
        mLegArmorListener = listener;
    }

    public interface ClassArmorEquippedListener {
        void onClassArmorEquipped(ItemCompleteObject equippedClassArmor);
    }

    public void setClassArmorEquippedListener(ClassArmorEquippedListener listener) {
        mClassArmorListener = listener;
    }

    public interface ArtifactEquippedListener {
        void onArtifactEquipped(ItemCompleteObject equippedArtifact);
    }

    public void setArtifactEquippedListener(ArtifactEquippedListener listener) {
        mArtifactListener = listener;
    }

    public interface ShaderEquippedListener {
        void onShaderEquipped(ItemCompleteObject equippedShader);
    }

    public void setShaderEquippedListener(ShaderEquippedListener listener) {
        mShaderListener = listener;
    }

    public interface EmblemEquippedListener {
        void onEmblemEquipped(ItemCompleteObject equippedEmblem);
    }

    public void setEmblemEquippedListener(EmblemEquippedListener listener) {
        mEmblemListener = listener;
    }

    public interface ShipEquippedListener {
        void onShipEquipped(ItemCompleteObject equippedShip);
    }

    public void setShipEquippedListener(ShipEquippedListener listener) {
        mShipListener = listener;
    }

    public interface SparrowEquippedListener {
        void onSparrowEquipped(ItemCompleteObject equippedSparrow);
    }

    public void setSparrowEquippedListener(SparrowEquippedListener listener) {
        mSparrowListener = listener;
    }

    public interface EmoteEquippedListener {
        void onEmoteEquipped(ItemCompleteObject equippedEmote);
    }

    public void setEmoteEquippedListener(EmoteEquippedListener listener) {
        mEmoteListener = listener;
    }

    public interface SparrowHornEquippedListener {
        void onSparrowHornEquipped(ItemCompleteObject equippedSparrowHorn);
    }

    public void setSparrowHornEquippedListener(SparrowHornEquippedListener listener) {
        mSparrowHornListener = listener;
    }

    @Override
    public GridLayoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_recyclerviewadapter_layout, parent, false);
        mContext = view.getContext();
        return new GridLayoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GridLayoutViewHolder holder, final int position) {
        Picasso.with(mContext).load(BUNGIE_NET_START_URL + mItemList.get(position).getIconUrl()).into(holder.mItemIcon);
        if (mItemList.get(position).isGridComplete()) {
            holder.mItemIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            holder.mItemIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        String bucketName = mItemList.get(position).getBucketName();

        if (bucketName.equals("Materials") || bucketName.equals("Consumables")
                || bucketName.equals("Ornaments")) {
            holder.mItemQuantity.setText(mItemList.get(position).getQuantity().toString());
            holder.mItemQuantity.setVisibility(View.VISIBLE);
            holder.mItemQuantity.getBackground().setAlpha(200);
        }

        mCharacterInfo = CharacterInfoSingleton.getInstance();

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);

        mAccessToken = sharedPreferences.getString(ACCESS_TOKEN_NAME, "");

        holder.mItemIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Get the Object and Save it to Singleton
                mCharacterInfo.setSelectedItem(mItemList.get(holder.getAdapterPosition()));
                mCharacterInfo.setIsEquippedItem(false);

                FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                FragmentCharItemDetailDialog dialogFragment = new FragmentCharItemDetailDialog();
                dialogFragment.show(fm, "Sample Fragment");

                dialogFragment.setEquipButtonPressedListener(new FragmentCharItemDetailDialog.EquipButtonPressedListener() {
                    @Override
                    public void onEquipButtonPressed() {
                        Log.d(AppConstants.TAG, "onEquipButtonPressed: ");
                        equipItem(mItemList.get(holder.getAdapterPosition()).getItemId(),
                                mItemList.get(holder.getAdapterPosition()).getItemName(), holder.getAdapterPosition());
                    }
                });

                dialogFragment.setTransferButtonListener(new FragmentCharItemDetailDialog.TransferButtonPressedListener() {
                    @Override
                    public void onTransferButtonPressed(String characterId, String quantity) {
                        Log.d(AppConstants.TAG, "onTransferButtonPressed: " + characterId);
                        Log.d(AppConstants.TAG, "onTransferButtonPressed: " + quantity);
                        if (quantity.equals("")) {
                            quantity = String.valueOf(1);
                        }

                        if (characterId.equals(mCharacterInfo.getCharacterList().
                                get(mCharacterInfo.getSelectedCharacter()).getCharacterId())) {
                            transferToVault(holder.getAdapterPosition(), quantity, holder.mItemQuantity);
                        } else {
                            transferToOtherCharacter(characterId, holder.getAdapterPosition(), quantity,
                                    holder.mItemQuantity);
                        }
                    }
                });
            }
        });

        holder.mItemIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String bucketName = mItemList.get(holder.getAdapterPosition()).getBucketName();
                if (!bucketName.equals("Materials") && !bucketName.equals("Consumables") &&
                        !bucketName.equals("Ornaments")) {
                    equipItem(mItemList.get(holder.getAdapterPosition()).getItemId(),
                            mItemList.get(holder.getAdapterPosition()).getItemName(), holder.getAdapterPosition());
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void transferToVault(final int itemAdapterPosition, final String quantity,
                                final TextView itemQuantityText) {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        Log.d(AppConstants.TAG, "transferToVault: TEXT QUANTITY" + quantity);

        HashMap<String, String> params = new HashMap<>();
        params.put("itemReferenceHash", mItemList.get(itemAdapterPosition).getItemHash());
        params.put("stackSize", quantity);
        params.put("transferToVault", "true");
        params.put("itemId", mItemList.get(itemAdapterPosition).getItemId());
        params.put("characterId", mCharacterInfo.getCharacterList().
                get(mCharacterInfo.getSelectedCharacter()).getCharacterId());
        params.put("membershipType", mCharacterInfo.getMembershipType().toString());

        String url = TRANSFER_SINGLE_ITEM;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    String message = response.getString("Message");
                    if (message.equals("Ok")) {
                        message = mItemList.get(itemAdapterPosition).getItemName() +
                                " is successfully transferred.";
                        int quantityNumeric = Integer.parseInt(quantity);
                        if (mItemList.get(itemAdapterPosition).getQuantity() == quantityNumeric) {
                            itemQuantityText.setVisibility(View.INVISIBLE);
                            mItemList.remove(itemAdapterPosition);
                            notifyItemRemoved(itemAdapterPosition);
                        } else {

                            //Update the item quantity info
                            Integer itemRemaining = mItemList.get(itemAdapterPosition).getQuantity() - quantityNumeric;
                            itemQuantityText.setText(itemRemaining.toString());

                            mItemList.get(itemAdapterPosition).
                                    setQuantity(itemRemaining);
                        }
                    }

                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

//                    if (mView != null) {
//                        Snackbar.make(mView, message, Snackbar.LENGTH_LONG)
//                                .setAction("Undo", null).show();
//                    }

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
                params.put(AUTHORIZATION_NAME, "Bearer " + mAccessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void transferToOtherCharacter(final String otherCharacterId, final int itemAdapterPosition,
                                         final String quantity, final TextView itemQuantityText) {
        final String itemReferenceHash = mItemList.get(itemAdapterPosition).getItemHash();
        final String itemId = mItemList.get(itemAdapterPosition).getItemId();
        final String itemName = mItemList.get(itemAdapterPosition).getItemName();
        final ItemCompleteObject sentItem = mItemList.get(itemAdapterPosition);
        final Integer itemQuantity = mItemList.get(itemAdapterPosition).getQuantity();
        final String selectedCharacterId = mCharacterInfo.getCharacterList().
                get(mCharacterInfo.getSelectedCharacter()).getCharacterId();

        RequestQueue queue = Volley.newRequestQueue(mContext);

        HashMap<String, String> params = new HashMap<>();
        params.put("itemReferenceHash", itemReferenceHash);
        params.put("stackSize", quantity);
        params.put("transferToVault", "true");
        params.put("itemId", itemId);
        params.put("characterId", mCharacterInfo.getCharacterList().
                get(mCharacterInfo.getSelectedCharacter()).getCharacterId());
        params.put("membershipType", mCharacterInfo.getMembershipType().toString());

        String url = TRANSFER_SINGLE_ITEM;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    String message = response.getString("Message");
                    if (message.equals("Ok")) {
                        //Means that we successfully transferred the item to the vault.
                        int quantityNumeric = Integer.parseInt(quantity);
                        if (mItemList.get(itemAdapterPosition).getQuantity() == quantityNumeric) {
                            itemQuantityText.setVisibility(View.INVISIBLE);
                            mItemList.remove(itemAdapterPosition);
                            notifyItemRemoved(itemAdapterPosition);
                        } else {
                            //Update the item quantity info
                            Integer itemRemaining = mItemList.get(itemAdapterPosition).getQuantity() - quantityNumeric;
                            itemQuantityText.setText(itemRemaining.toString());

                            mItemList.get(itemAdapterPosition).
                                    setQuantity(itemRemaining);
                        }

                        //Now we are moving it to the other character.
                        RequestQueue queue = Volley.newRequestQueue(mContext);

                        HashMap<String, String> params = new HashMap<>();
                        params.put("itemReferenceHash", itemReferenceHash);
                        params.put("stackSize", quantity);
                        params.put("transferToVault", "false");
                        params.put("itemId", itemId);
                        params.put("characterId", otherCharacterId);
                        params.put("membershipType", mCharacterInfo.getMembershipType().toString());

                        String url = TRANSFER_SINGLE_ITEM;

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                                url, new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(AppConstants.TAG, "onResponse: " + response);
                                try {
                                    String message = response.getString("Message");
                                    if (message.equals("Ok")) {
                                        message = itemName +
                                                " is successfully transferred.";
                                    } else {
                                        //This means that the transfer to the other character
                                        // failed for some reason. We will get the item back in this case.

                                        RequestQueue queue = Volley.newRequestQueue(mContext);

                                        Log.d(AppConstants.TAG, "transferToVault: TEXT QUANTITY" + quantity);

                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("itemReferenceHash",itemReferenceHash);
                                        params.put("stackSize", quantity);
                                        params.put("transferToVault", "false");
                                        params.put("itemId", itemId);
                                        params.put("characterId", selectedCharacterId);
                                        params.put("membershipType", mCharacterInfo.getMembershipType().toString());

                                        String url = TRANSFER_SINGLE_ITEM;

                                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                                                url, new JSONObject(params), new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d(AppConstants.TAG, "onResponse: " + response);
                                                try {
                                                    String message = response.getString("Message");
                                                    if (message.equals("Ok")) {
                                                        message = "Not enough space at destination character. " + itemName +
                                                                " has been retrieved.";
                                                        int quantityNumeric = Integer.parseInt(quantity);

                                                        if (itemQuantity == quantityNumeric) {
                                                            mItemList.add(sentItem);
                                                            notifyItemInserted(mItemList.size());
                                                        } else {
                                                            for (int i = 0; i < mItemList.size(); i++) {
                                                                if (mItemList.get(i).getItemHash().equals(itemReferenceHash)) {
                                                                    Integer currentQuantity = mItemList.get(i).getQuantity();
                                                                    Integer finalQuantity = currentQuantity + quantityNumeric;
                                                                    mItemList.get(i).setQuantity(finalQuantity);
                                                                    itemQuantityText.setText(finalQuantity.toString());
                                                                }
                                                            }
                                                        }

                                                    }

                                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

//                                                    if (mView != null) {
//                                                        Snackbar.make(mView, message, Snackbar.LENGTH_LONG)
//                                                                .setAction("Undo", null).show();
//                                                    }

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
                                                params.put(AUTHORIZATION_NAME, "Bearer " + mAccessToken);
                                                return params;
                                            }
                                        };
                                        queue.add(request);

                                    }

                                    String str = itemName + " is successfully transferred.";
                                    if (message.equals(str)) {
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                    }

//                                    if (mView != null) {
//                                        String str = itemName + " is successfully transferred.";
//                                        if (message.equals(str)) {
//                                            Snackbar.make(mView, message, Snackbar.LENGTH_LONG)
//                                                    .setAction("Undo", null).show();
//                                        }
//                                    }

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
                                params.put(AUTHORIZATION_NAME, "Bearer " + mAccessToken);
                                return params;
                            }
                        };
                        queue.add(request);
                    } else {

                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

//                        if (mView != null) {
//                            Snackbar.make(mView, message, Snackbar.LENGTH_LONG)
//                                    .setAction("Undo", null).show();
//                        }
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
                params.put(AUTHORIZATION_NAME, "Bearer " + mAccessToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void equipItem(final String itemId, final String itemName, final int removePosition) {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        Log.d(AppConstants.TAG, "onClick: " + itemId);
        Log.d(AppConstants.TAG, "onClick: " + mCharacterInfo.getCharacterList().get(mCharacterInfo.getSelectedCharacter()).getCharacterId());
        Log.d(AppConstants.TAG, "onClick: " + mCharacterInfo.getMembershipType().toString());


        HashMap<String, String> params = new HashMap<>();
        params.put("itemId", itemId);
        params.put("characterId", mCharacterInfo.getCharacterList().get(mCharacterInfo.getSelectedCharacter()).getCharacterId());
        params.put("membershipType", mCharacterInfo.getMembershipType().toString());

        String url = AppConstants.EQUIP_SINGLE_ITEM_URL;

        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST,
                url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(AppConstants.TAG, "onResponse: " + response);
                try {
                    String message = response.getString("Message");

                    if (message.equals("Ok")) {
                        message =  itemName + " is successfully equipped.";
                        if (!mEquippedObject.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                            mItemList.add(mEquippedObject);
                            notifyItemInserted(mItemList.size());
                        }
                        mEquippedObject = mItemList.get(removePosition);
                        mItemList.remove(removePosition);
                        notifyItemRemoved(removePosition);
                        Picasso.with(mContext).load(BUNGIE_NET_START_URL + mEquippedObject.getIconUrl()).into(mEquippedImageIcon);
                        if (mEquippedObject.isGridComplete()) {
                            mEquippedImageIcon.setBackgroundResource(R.drawable.border_complete);
                        } else {
                            mEquippedImageIcon.setBackgroundResource(R.drawable.border_not_complete);
                        }

                        if (mEquippedObject.getBucketName().equals("Emblems") &&
                                !mEquippedObject.getIconUrl().equals(AppConstants.MISSING_ICON_URL)) {
                            mCharacterInfo.getCharacterList().get(mCharacterInfo.getSelectedCharacter())
                                    .setEmblemUrl(mEquippedObject.getIconUrl());
                            mCharacterInfo.getCharacterList().get(mCharacterInfo.getSelectedCharacter())
                                    .setBackgroundUrl(mEquippedObject.getBackgroundUrl());
                        }

                        String bucketName = mEquippedObject.getBucketName();
                        Log.d(AppConstants.TAG, "onResponse: " + bucketName);
                        switch (bucketName) {
                            case "Primary Weapons":
                                mPrimaryListener.onPrimaryWeaponEquipped(mEquippedObject);
                                break;
                            case "Special Weapons":
                                mSpecialListener.onSpecialWeaponEquipped(mEquippedObject);
                                break;
                            case "Heavy Weapons":
                                mHeavyListener.onHeavyWeaponEquipped(mEquippedObject);
                                break;
                            case "Ghost":
                                mGhostListener.onGhostEquipped(mEquippedObject);
                                break;
                            case "Helmet":
                                mHelmetListener.onHelmetEquipped(mEquippedObject);
                                break;
                            case "Gauntlets":
                                mGauntletListener.onGauntletEquipped(mEquippedObject);
                                break;
                            case "Chest Armor":
                                mChestArmorListener.onChestArmorEquipped(mEquippedObject);
                                break;
                            case "Leg Armor":
                                mLegArmorListener.onLegArmorEquipped(mEquippedObject);
                                break;
                            case "Class Armor":
                                mClassArmorListener.onClassArmorEquipped(mEquippedObject);
                                break;
                            case "Artifacts":
                                mArtifactListener.onArtifactEquipped(mEquippedObject);
                                break;
                            case "Shaders":
                                mShaderListener.onShaderEquipped(mEquippedObject);
                                break;
                            case "Emblems":
                                mEmblemListener.onEmblemEquipped(mEquippedObject);
                                break;
                            case "Ships":
                                mShipListener.onShipEquipped(mEquippedObject);
                                break;
                            case "Vehicle":
                                mSparrowListener.onSparrowEquipped(mEquippedObject);
                                break;
                            case "Emotes":
                                mEmoteListener.onEmoteEquipped(mEquippedObject);
                                break;
                            case "Sparrow Horn":
                                mSparrowHornListener.onSparrowHornEquipped(mEquippedObject);
                                break;
                        }
                    }

                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

//                    if (mView != null) {
//                        Snackbar.make(mView, message, Snackbar.LENGTH_LONG)
//                                .setAction("Undo", null).show();
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(AppConstants.TAG, "onErrorResponse: Failed because of connectivity/server" + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(AppConstants.X_API_KEY_NAME, AppConstants.X_API_KEY_VALUE);
                params.put(AUTHORIZATION_NAME, "Bearer " + mAccessToken);
                return params;
            }
        };
        queue.add(request);
    }

}
