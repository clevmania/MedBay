package com.clevmania.medbay.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by grandilo-lawrence on 4/8/18.
 */

public class FirebaseUtils {
    public static DatabaseReference getRootReference(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getMedicationsReference(){
        return getRootReference().child("medications");
    }
}