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

        // Navigate to path
        List<String> pathlist = Arrays.asList(path.split("/"));
        DatabaseReference db = ref;
        for (String p : pathlist) {
            db = db.child(p);
        }

        // Generate new key
        db = db.push();
        return db.getKey();
    }

    public static <T> String put(String path, T value) {

        // Navigate to path
        List<String> pathlist = Arrays.asList(path.split("/"));
        DatabaseReference db = ref;
        for (String p : pathlist) {
            db = db.child(p);
        }

        // Write key value pair
        db = db.push();
        db.setValue(value);
        return db.getKey();
    }

    public static <T> void put(String path, String key, T value) {

        // Navigate to path
        List<String> pathlist = Arrays.asList(path.split("/"));
        DatabaseReference db = ref;
        for (String p : pathlist) {
            db = db.child(p);
        }

        // Write key value pair
        db.child(key).setValue(value);
    }
}
