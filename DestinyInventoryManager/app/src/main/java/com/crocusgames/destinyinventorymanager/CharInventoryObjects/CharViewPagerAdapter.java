package com.crocusgames.destinyinventorymanager.CharInventoryObjects;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.crocusgames.destinyinventorymanager.ItemCompleteObject;

/**
 * Created by Serkan on 14/12/16.
 */

public class CharViewPagerAdapter extends FragmentPagerAdapter {
    private EmblemEquippedListener mEmblemListener;

    public CharViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public interface EmblemEquippedListener {
        void onEmblemEquipped(ItemCompleteObject equippedEmblem);
    }

    public void setEmblemEquippedListener(EmblemEquippedListener listener) {
        mEmblemListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentCharWeapons();
            case 1:
                return new FragmentCharArmor();
            case 2:
                FragmentCharMisc fragmentCharMisc = new FragmentCharMisc();
                fragmentCharMisc.setEmblemEquippedListener(new FragmentCharMisc.EmblemEquippedListener() {
                    @Override
                    public void onEmblemEquipped(ItemCompleteObject equippedEmblem) {
                        mEmblemListener.onEmblemEquipped(equippedEmblem);
                    }
                });
                return fragmentCharMisc;
            case 3:
                return new FragmentCharInventory();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Weapons";
            case 1:
                return "Armor";
            case 2:
                return "Misc";
            case 3:
                return "Inventory";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
