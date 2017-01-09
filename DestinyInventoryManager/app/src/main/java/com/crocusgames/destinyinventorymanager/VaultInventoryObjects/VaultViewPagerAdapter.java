package com.crocusgames.destinyinventorymanager.VaultInventoryObjects;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Serkan on 23/12/16.
 */

public class VaultViewPagerAdapter extends FragmentPagerAdapter {

    public VaultViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentVaultWeapons();
            case 1:
                return new FragmentVaultArmor();
            case 2:
                return new FragmentVaultMisc();
            case 3:
                return new FragmentVaultInventory();
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
