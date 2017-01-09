package com.scottlindley.suyouthinkyoucandoku;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Created by Scott Lindley on 12/28/2016.
 */

public class WeaponDialog extends AlertDialog.Builder {
    public String mSelectedWeapon = "";
    public String mWeaponSlot1 = "none";
    public String mWeaponSlot2 = "none";

    public WeaponDialog(@NonNull Context context) {
        super(context);
    }
}
