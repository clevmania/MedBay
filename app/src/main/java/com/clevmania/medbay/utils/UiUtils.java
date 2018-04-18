package com.clevmania.medbay.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by grandilo-lawrence on 4/16/18.
 */

public class UiUtils {
    public static void showLongSnackBar(View view, String message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
//            snack.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

    }

    private static void showShortSnackBar(View view, String message){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
    }
}
