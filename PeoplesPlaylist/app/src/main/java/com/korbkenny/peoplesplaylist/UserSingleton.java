package com.korbkenny.peoplesplaylist;

import com.korbkenny.peoplesplaylist.objects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KorbBookProReturns on 12/22/16.
 */

public class UserSingleton {
    private static UserSingleton userInstance;
    private List<User> mUser;

    public static UserSingleton getInstance(){
        if(userInstance == null){
            userInstance = new UserSingleton();
        }
        return userInstance;
    }

    private UserSingleton(){
        mUser = new ArrayList<>();
    }

    public User getUser() {
        return mUser.get(0);
    }

    public void setUser(User user) {
        if(mUser.size() != 0) {
            mUser.remove(0);
        }
        mUser.add(user);
    }
}
