package com.crocusgames.destinyinventorymanager.CharInventoryObjects;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.CharacterInfoObject;
import com.crocusgames.destinyinventorymanager.CharacterInfoSingleton;
import com.crocusgames.destinyinventorymanager.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import static com.crocusgames.destinyinventorymanager.AppConstants.BUNGIE_NET_START_URL;

/**
 * Created by Serkan on 16/12/16.
 */

public class FragmentCharItemDetailDialog extends android.support.v4.app.DialogFragment {
    private EquipButtonPressedListener mEquipListener;
    private TransferButtonPressedListener mTransferListener;

    public interface TransferButtonPressedListener {
        void onTransferButtonPressed(String characterId, String quantity);
    }

    public void setTransferButtonListener(TransferButtonPressedListener listener) {
        mTransferListener = listener;
    }

    public interface EquipButtonPressedListener {
        void onEquipButtonPressed();
    }

    public void setEquipButtonPressedListener(EquipButtonPressedListener listener) {
        mEquipListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_itemdetail_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final CharacterInfoSingleton characterInfo = CharacterInfoSingleton.getInstance();

        TextView itemName = (TextView) view.findViewById(R.id.title);
        TextView lightLevel = (TextView) view.findViewById(R.id.selectedItem_lightLevel);
        TextView itemTierType = (TextView) view.findViewById(R.id.selectedItem_itemTierType);
        TextView itemDescription = (TextView) view.findViewById(R.id.selectedItem_description);
        TextView transferTitle = (TextView) view.findViewById(R.id.transfer_title);
        TextView equipButton = (TextView) view.findViewById(R.id.button_equip);
        ImageView itemIcon = (ImageView) view.findViewById(R.id.icon);
        TextView otherChar1Class = (TextView) view.findViewById(R.id.transfer_class_name1);
        TextView otherChar2Class = (TextView) view.findViewById(R.id.transfer_class_name2);
        TextView otherChar1RaceGender = (TextView) view.findViewById(R.id.transfer_racegender_name1);
        TextView otherChar2RaceGender = (TextView) view.findViewById(R.id.transfer_racegender_name2);
        TextView otherChar1Level = (TextView) view.findViewById(R.id.transfer_level1);
        TextView otherChar2Level = (TextView) view.findViewById(R.id.transfer_level2);
        TextView otherChar1LightLevel = (TextView) view.findViewById(R.id.transfer_lightlevellevel1);
        TextView otherChar2LightLevel = (TextView) view.findViewById(R.id.transfer_lightlevellevel2);
        TextView buttonTransferToVault = (TextView) view.findViewById(R.id.button_transferToVault);
        TextView buttonDismiss = (TextView) view.findViewById(R.id.button_close);
        TextView buttonDismissOther = (TextView) view.findViewById(R.id.button_closeOther);
        TextView buttonTransferToVaultSmall = (TextView) view.findViewById(R.id.button_smallVault);
        ImageView otherChar1Emblem = (ImageView) view.findViewById(R.id.other_char1_emblem);
        ImageView otherChar2Emblem = (ImageView) view.findViewById(R.id.other_char2_emblem);
        final RelativeLayout otherChar1Banner = (RelativeLayout) view.findViewById(R.id.other_char1_layout);
        RelativeLayout otherChar2Banner = (RelativeLayout) view.findViewById(R.id.other_char2_layout);
        LinearLayout equipAndClose = (LinearLayout) view.findViewById(R.id.layout_equipAndClose);
        LinearLayout vaultAndClose = (LinearLayout) view.findViewById(R.id.layout_vaultAndClose);
        final EditText quantityEntry = (EditText) view.findViewById(R.id.entry_quantity);


        if (characterInfo.isEquippedItem()) {
            transferTitle.setVisibility(View.GONE);
            otherChar1Banner.setVisibility(View.GONE);
            otherChar2Banner.setVisibility(View.GONE);
            buttonTransferToVault.setVisibility(View.GONE);
            equipButton.setVisibility(View.INVISIBLE);
        }

        String bucketName = characterInfo.getSelectedItem().getBucketName();

        if (bucketName.equals("Materials") || bucketName.equals("Consumables")
                || bucketName.equals("Ornaments")) {
            equipButton.setVisibility(View.INVISIBLE);
            quantityEntry.setVisibility(View.VISIBLE);
            quantityEntry.setText(characterInfo.getSelectedItem().getQuantity().toString());
            //equipAndClose.setVisibility(View.GONE);
            //buttonTransferToVault.setVisibility(View.GONE);
            //vaultAndClose.setVisibility(View.VISIBLE);
        }

        //Item Name
        Typeface destinyTypeface = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/helveticaNeueRoman.otf");
        itemName.setTypeface(destinyTypeface);
        itemName.setText(characterInfo.getSelectedItem().getItemName());

        switch (characterInfo.getSelectedItem().getTierTypeName()) {
            case "Exotic":
                itemName.setBackgroundColor(getResources().getColor(R.color.item_color_exotic));
                transferTitle.setBackgroundColor(getResources().getColor(R.color.item_color_exotic));
                equipButton.setBackgroundColor(getResources().getColor(R.color.item_color_exotic));
                itemName.setTextColor(getResources().getColor(R.color.black));
                transferTitle.setTextColor(getResources().getColor(R.color.black));
                equipButton.setTextColor(getResources().getColor(R.color.black));
                break;
            case "Legendary":
                itemName.setBackgroundColor(getResources().getColor(R.color.item_color_legendary));
                transferTitle.setBackgroundColor(getResources().getColor(R.color.item_color_legendary));
                equipButton.setBackgroundColor(getResources().getColor(R.color.item_color_legendary));
                itemName.setTextColor(getResources().getColor(R.color.colorWhite));
                transferTitle.setTextColor(getResources().getColor(R.color.colorWhite));
                equipButton.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
            case "Rare":
                itemName.setBackgroundColor(getResources().getColor(R.color.item_color_rare));
                transferTitle.setBackgroundColor(getResources().getColor(R.color.item_color_rare));
                equipButton.setBackgroundColor(getResources().getColor(R.color.item_color_rare));
                itemName.setTextColor(getResources().getColor(R.color.black));
                transferTitle.setTextColor(getResources().getColor(R.color.black));
                equipButton.setTextColor(getResources().getColor(R.color.black));
                break;
            case "Uncommon":
                itemName.setBackgroundColor(getResources().getColor(R.color.item_color_uncommon));
                transferTitle.setBackgroundColor(getResources().getColor(R.color.item_color_uncommon));
                equipButton.setBackgroundColor(getResources().getColor(R.color.item_color_uncommon));
                itemName.setTextColor(getResources().getColor(R.color.black));
                transferTitle.setTextColor(getResources().getColor(R.color.black));
                equipButton.setTextColor(getResources().getColor(R.color.black));
                break;
            default:
                itemName.setBackgroundColor(getResources().getColor(R.color.item_color_common));
                transferTitle.setBackgroundColor(getResources().getColor(R.color.item_color_common));
                equipButton.setBackgroundColor(getResources().getColor(R.color.item_color_common));
                itemName.setTextColor(getResources().getColor(R.color.black));
                transferTitle.setTextColor(getResources().getColor(R.color.black));
                equipButton.setTextColor(getResources().getColor(R.color.black));
                break;
        }

        //Light Level
        if (characterInfo.getSelectedItem().getLightLevel() != -1) {
            lightLevel.setText("Light Level: " + characterInfo.getSelectedItem().getLightLevel().toString());
        } else {
            lightLevel.setVisibility(View.GONE);
        }
        //Item Tier Type
        itemTierType.setText(characterInfo.getSelectedItem().getTierTypeName() + " " +
                characterInfo.getSelectedItem().getItemTypeName());

        //Item description
        itemDescription.setText(characterInfo.getSelectedItem().getDescription());

        //
        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + characterInfo.getSelectedItem().getIconUrl()).into(itemIcon);
        if (characterInfo.getSelectedItem().isGridComplete()) {
            itemIcon.setBackgroundResource(R.drawable.border_complete);
        } else {
            itemIcon.setBackgroundResource(R.drawable.border_not_complete);
        }

        final ArrayList<CharacterInfoObject> otherCharIds = new ArrayList<>();

        String selectedId = characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId();

        for (int i = 0; i < characterInfo.getCharacterList().size(); i++) {
            if (!characterInfo.getCharacterList().get(i).getCharacterId().equals(selectedId)) {
                otherCharIds.add(characterInfo.getCharacterList().get(i));

            }
        }

        switch (otherCharIds.size()) {
            case 0:
                otherChar1Banner.setVisibility(View.GONE);
                otherChar2Banner.setVisibility(View.GONE);
                transferTitle.setVisibility(View.GONE);
                break;
            case 1:
                //Char1
                loadImageIntoBackground(otherChar1Banner, otherChar1Emblem, otherCharIds.get(0).getBackgroundUrl(),
                        otherCharIds.get(0).getEmblemUrl());
                otherChar1Class.setText(otherCharIds.get(0).getClassName());
                otherChar1RaceGender.setText(otherCharIds.get(0).getRaceName() + " " +
                        otherCharIds.get(0).getGenderName());
                otherChar1Level.setText(otherCharIds.get(0).getNormalLevel());
                otherChar1LightLevel.setText(otherCharIds.get(0).getLightLevel());
                otherChar2Banner.setVisibility(View.GONE);
                break;
            case 2:
                //Char1
                loadImageIntoBackground(otherChar1Banner, otherChar1Emblem, otherCharIds.get(0).getBackgroundUrl(),
                        otherCharIds.get(0).getEmblemUrl());
                otherChar1Class.setText(otherCharIds.get(0).getClassName());
                otherChar1RaceGender.setText(otherCharIds.get(0).getRaceName() + " " +
                        otherCharIds.get(0).getGenderName());
                otherChar1Level.setText(otherCharIds.get(0).getNormalLevel());
                otherChar1LightLevel.setText(otherCharIds.get(0).getLightLevel());

                //Char2
                loadImageIntoBackground(otherChar2Banner, otherChar2Emblem, otherCharIds.get(1).getBackgroundUrl(),
                        otherCharIds.get(1).getEmblemUrl());
                otherChar2Class.setText(otherCharIds.get(1).getClassName());
                otherChar2RaceGender.setText(otherCharIds.get(1).getRaceName() + " " +
                        otherCharIds.get(1).getGenderName());
                otherChar2Level.setText(otherCharIds.get(1).getNormalLevel());
                otherChar2LightLevel.setText(otherCharIds.get(1).getLightLevel());
                break;
        }

        equipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEquipListener.onEquipButtonPressed();
                dismiss();
            }
        });

        otherChar1Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quantityEntry.getText().toString().equals("")) {
                    if (Integer.parseInt(quantityEntry.getText().toString()) <= characterInfo.getSelectedItem().getQuantity()) {
                        mTransferListener.onTransferButtonPressed(otherCharIds.get(0).getCharacterId(),
                                quantityEntry.getText().toString());
                        dismiss();
                    } else {
                        quantityEntry.setError("You cannot transfer more than you have.");
                    }
                } else {
                    mTransferListener.onTransferButtonPressed(otherCharIds.get(0).getCharacterId(),
                            quantityEntry.getText().toString());
                    dismiss();
                }
            }
        });

        otherChar2Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quantityEntry.getText().toString().equals("")) {
                    if (Integer.parseInt(quantityEntry.getText().toString()) <= characterInfo.getSelectedItem().getQuantity()) {
                        mTransferListener.onTransferButtonPressed(otherCharIds.get(1).getCharacterId(),
                                quantityEntry.getText().toString());
                        dismiss();
                    } else {
                        quantityEntry.setError("You cannot transfer more than you have.");
                    }
                } else {
                    mTransferListener.onTransferButtonPressed(otherCharIds.get(1).getCharacterId(),
                            quantityEntry.getText().toString());
                    dismiss();
                }
            }
        });

        buttonTransferToVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!quantityEntry.getText().toString().equals("")) {
                    if (Integer.parseInt(quantityEntry.getText().toString()) <= characterInfo.getSelectedItem().getQuantity()) {
                        mTransferListener.onTransferButtonPressed(
                                characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId(),
                                quantityEntry.getText().toString());
                        dismiss();
                    } else {
                        quantityEntry.setError("You cannot transfer more than you have.");
                    }
                } else {
                    mTransferListener.onTransferButtonPressed(
                            characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId(),
                            quantityEntry.getText().toString());
                    dismiss();
                }
            }
        });

//        buttonTransferToVaultSmall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(AppConstants.TAG, "onClick: " + quantityEntry.getText().toString());
//                Log.d(AppConstants.TAG, "onClick: " + characterInfo.getSelectedItem().getQuantity());
//                if (Integer.parseInt(quantityEntry.getText().toString()) <= characterInfo.getSelectedItem().getQuantity()) {
//                    mTransferListener.onTransferButtonPressed(
//                        characterInfo.getCharacterList().get(characterInfo.getSelectedCharacter()).getCharacterId(),
//                        quantityEntry.getText().toString());
//                        dismiss();
//                } else {
//                    quantityEntry.setError("You cannot transfer more than you have.");
//                }
//            }
//        });

        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        buttonDismissOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    public void loadImageIntoBackground(final RelativeLayout otherCharBackground, ImageView otherCharEmblem, String backGroundUrlWithoutBeginning,
                                        String emblemUrlWithoutBeginning) {

        Picasso.with(getContext()).load(BUNGIE_NET_START_URL + emblemUrlWithoutBeginning).into(otherCharEmblem);

        Picasso.with(getActivity()).load(BUNGIE_NET_START_URL + backGroundUrlWithoutBeginning).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                otherCharBackground.setBackground(new BitmapDrawable(getContext().getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(AppConstants.TAG, "onBitmapFailed: ");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(AppConstants.TAG, "onPrepareLoad: ");
            }
        });
    }
}
