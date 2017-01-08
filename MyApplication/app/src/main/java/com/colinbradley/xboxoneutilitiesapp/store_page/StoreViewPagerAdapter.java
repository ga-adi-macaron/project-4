package com.colinbradley.xboxoneutilitiesapp.store_page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.colinbradley.xboxoneutilitiesapp.store_page.deals_with_gold.DWGFragment;
import com.colinbradley.xboxoneutilitiesapp.store_page.games_with_gold.GWGFragment;
import com.colinbradley.xboxoneutilitiesapp.store_page.xbox_marketplace.XBMarketplaceFragment;

/**
 * Created by colinbradley on 12/29/16.
 */

public class StoreViewPagerAdapter extends FragmentPagerAdapter {
    public StoreViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new GWGFragment();
            case 1:
                return new DWGFragment();
            case 2:
                return new XBMarketplaceFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Free With Gold";
            case 1:
                return "Sales With Gold";
            case 2:
                return "Latest Items in Marketplace";
            default:
                return "???";
        }
    }
}
