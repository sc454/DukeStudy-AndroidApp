package com.example.cheli.navigationdrawer;
import android.os.Bundle;

import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;



/**
 * Created by johnb on 3/3/2017.
 */

public class UserPage extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_prof_frag, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        //
        //
    }
}
