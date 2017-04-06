package com.ds.DukeStudy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Database {

//  Fields

    public static final String url = "https://dukestudy-a11a3.firebaseio.com";

//  Methods

    public static <T> String getNewKey(List<String> path) {

    //  Navigate to path

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        for (String p : path) {
            db = db.child(p);
        }

    //  Generate new key

        db = db.push();
        return db.getKey();
    }

    public static <T> String put(T value, List<String> path) {

    //  Navigate to path

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        for (String p : path) {
            db = db.child(p);
        }

    //  Write key value pair

        db = db.push();
        db.setValue(value);
        return db.getKey();
    }

    public static <T> void put(String key, T value, List<String> path) {

    //  Navigate to path

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        for (String p : path) {
            db = db.child(p);
        }

    //  Write key value pair

        db.child(key).setValue(value);
    }
}
