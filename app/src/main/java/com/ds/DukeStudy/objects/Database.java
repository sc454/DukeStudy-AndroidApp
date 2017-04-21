package com.ds.DukeStudy.objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {

    // Fields

    public static final String url = "https://dukestudy-a11a3.firebaseio.com";
    public static final String store_url = "gs://dukestudy-a11a3.appspot.com/";
    public static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    public static final StorageReference store_ref = FirebaseStorage.getInstance().getReferenceFromUrl(store_url);

    // Getters

    public static FirebaseUser getUser() {return FirebaseAuth.getInstance().getCurrentUser();}

    // Other methods

    public static <T> String getNewKey(String path) {
        return ref.child(path).push().getKey();
    }

    public static <T> void put(String key, T value) {
        ref.child(key).setValue(value);
        System.out.println("Writing to " + key);
    }
}
