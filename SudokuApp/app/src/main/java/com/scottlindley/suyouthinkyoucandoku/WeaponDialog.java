package com.scottlindley.suyouthinkyoucandoku;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;

import java.util.List;

/**
 * Created by Scott Lindley on 12/28/2016.
 */

public class WeaponDialog extends AlertDialog {
    public String mSelectedWeapon = "";

    public WeaponDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {}
}
