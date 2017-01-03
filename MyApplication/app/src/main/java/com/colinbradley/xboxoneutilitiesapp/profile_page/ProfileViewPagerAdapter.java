package com.colinbradley.xboxoneutilitiesapp.profile_page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.colinbradley.xboxoneutilitiesapp.profile_page.friends_list.ProfileFriendsFragment;
import com.colinbradley.xboxoneutilitiesapp.profile_page.gameclips.ProfileGameClipsFragment;
import com.colinbradley.xboxoneutilitiesapp.profile_page.screenshots.ProfileScreenshotsFragment;

/**
 * Created by colinbradley on 12/19/16.
 */

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {
    public ProfileViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ProfileFriendsFragment();
            case 1:
                return new ProfileGameClipsFragment();
            case 2:
                return new ProfileScreenshotsFragment();
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
                return "Friends";
            case 1:
                return "Game Clips";
            case 2:
                return "Screenshots";
            default:
                return null;
        }
    }
}
