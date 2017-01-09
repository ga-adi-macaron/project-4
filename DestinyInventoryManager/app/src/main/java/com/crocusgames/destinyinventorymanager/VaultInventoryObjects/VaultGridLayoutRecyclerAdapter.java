package com.crocusgames.destinyinventorymanager.VaultInventoryObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.crocusgames.destinyinventorymanager.CharInventoryObjects.FragmentCharItemDetailDialog;
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
 * Created by Serkan on 23/12/16.
 */

public class VaultGridLayoutRecyclerAdapter extends RecyclerView.Adapter<VaultGridLayoutRecyclerViewHolder> {
    private List<ItemCompleteObject> mItemList;
    private Context mContext;
    private CharacterInfoSingleton mCharacterInfo;
    private String mAccessToken;

    public VaultGridLayoutRecyclerAdapter(List<ItemCompleteObject> itemList) {
        mItemList = itemList;
    }

    @Override
    public VaultGridLayoutRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vault_grid_recyclerviewadapter_layout, parent, false);
        mContext = view.getContext();
        return new VaultGridLayoutRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VaultGridLayoutRecyclerViewHolder holder, int position) {
        mCharacterInfo = CharacterInfoSingleton.getInstance();

        Picasso.with(mContext).load(BUNGIE_NET_START_URL + mItemList.get(position).getIconUrl()).into(holder.mItemIcon);
        if (mItemList.get(position).isGridComplete()) {
            holder.mItemIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            holder.mItemIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        String bucketName = mItemList.get(position).getBucketName();
        String itemTypeName = mItemList.get(position).getItemTypeName();

        if (!itemTypeName.contains("Engram")) {
            if (bucketName.equals("Primary Weapons") || bucketName.equals("Special Weapons") ||
                    bucketName.equals("Heavy Weapons") || bucketName.equals("Ghost") ||
                    bucketName.equals("Helmet") || bucketName.equals("Gauntlets") ||
                    bucketName.equals("Gauntlets") || bucketName.equals("Chest Armor") ||
                    bucketName.equals("Leg Armor") || bucketName.equals("Class Armor") ||
                    bucketName.equals("Artifacts")) {
                holder.mItemLightLevel.setText(mItemList.get(position).getLightLevel().toString());
                holder.mItemLightLevel.setVisibility(View.VISIBLE);
            }
        }

        if (bucketName.equals("Materials") || bucketName.equals("Consumables")
                || bucketName.equals("Ornaments")) {
            holder.mItemQuantity.setText(mItemList.get(position).getQuantity().toString());
            holder.mItemQuantity.setVisibility(View.VISIBLE);
            holder.mItemQuantity.getBackground().setAlpha(200);
        }

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);

        mAccessToken = sharedPreferences.getString(ACCESS_TOKEN_NAME, "");

        holder.mItemIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCharacterInfo.setSelectedItem(mItemList.get(holder.getAdapterPosition()));

                FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                FragmentVaultItemDialog dialogFragment = new FragmentVaultItemDialog();
                dialogFragment.show(fm, "Sample Fragment");

                dialogFragment.setTransferButtonListener(new FragmentCharItemDetailDialog.TransferButtonPressedListener() {
                    @Override
                    public void onTransferButtonPressed(String characterId, String quantity) {
                        //Toast.makeText(mContext, characterId + " " + quantity, Toast.LENGTH_SHORT).show();

                        if (quantity.equals("")) {
                            quantity = String.valueOf(1);
                        }

                        transferToVault(holder.getAdapterPosition(), quantity,
                                holder.mItemQuantity, characterId);
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void transferToVault(final int itemAdapterPosition, final String quantity,
                                final TextView itemQuantityText, String characterId) {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        Log.d(AppConstants.TAG, "transferToVault: TEXT QUANTITY" + quantity);

        HashMap<String, String> params = new HashMap<>();
        params.put("itemReferenceHash", mItemList.get(itemAdapterPosition).getItemHash());
        params.put("stackSize", quantity);
        params.put("transferToVault", "false");
        params.put("itemId", mItemList.get(itemAdapterPosition).getItemId());
        params.put("characterId", characterId);
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
}
