package com.ds.DukeStudy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

//  Utility fields and methods

public class Util {

//  Fields

    public static final String FIREBASE_URL = "https://dukestudy-a11a3.firebaseio.com";

//  Methods

    public static <T> String writeToDatabase(T value, List<String> path) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    //  Navigate to path

        DatabaseReference child;
        for (String p : path) {
            child = db.child(p);
        }

    //  Write key value pair

        child = child.push();
        child.setValue(value);
        return child.getKey();
    }

    public static <T> void writeToDatabase(String key, T value, List<String> path) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    //  Navigate to path

        DatabaseReference child;
        for (String p : path) {
            child = db.child(p);
        }

    //  Write key value pair

        child.child(key).setValue(value);
    }
}
