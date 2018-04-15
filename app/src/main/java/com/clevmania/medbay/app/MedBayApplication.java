package com.clevmania.medbay.app;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by grandilo-lawrence on 4/8/18.
 */

public class MedBayApplication extends Application{
    private static MedBayApplication _INSTANCE;

    public static MedBayApplication getInstance() {
        return _INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        _INSTANCE = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
