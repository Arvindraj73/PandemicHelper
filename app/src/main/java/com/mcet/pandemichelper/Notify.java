package com.mcet.pandemichelper;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class Notify {
    View v;
    String msg;

    public Notify(View v, String msg) {
        this.v = v;
        this.msg = msg;
    }

    public void showSnack(String time) {
        if (time.equals("short")) {
            Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAnchorView(R.id.anv);
            snackbar.show();
        } else if (time.equals("long")) {
            Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setAnchorView(R.id.anv);
            snackbar.show();
        }
    }

}
