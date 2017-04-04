package com.ds.DukeStudy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This is a generic posts Fragment. This will be expanded to work for classes and groups.
 */

public class PostsFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<PostObject> adapter1;
    private ListView postsListView;
    private Button postMessageButton;
    private EditText postMessage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.posts_layout,null);
         postMessageButton= (Button) view.findViewById(R.id.submitPost);
        //readBut = (Button) view.findViewById(R.id.readButton);
        postMessage = (EditText) view.findViewById(R.id.postMessage);
        postsListView=(ListView) view.findViewById(R.id.postsListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postsRef=databaseRef.child("postNote").child("class3");
        adapter1= new FirebaseListAdapter<PostObject>(getActivity(),PostObject.class,android.R.layout.two_line_list_item,postsRef) {
            @Override
            public void populateView(View v, PostObject model, final int position) {
                System.out.println("Populated View");
                TextView mytext1=(TextView) v.findViewById(android.R.id.text1);
                TextView mytext2=(TextView) v.findViewById(android.R.id.text2);
                mytext1.setText(model.getmyMessage());
                mytext2.setText(model.getmyAuthor());
                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = getRef(position).getKey();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
            }
        };
        postsListView.setAdapter(adapter1);
        postMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating firebase object
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                //database.child("note").push().setValue(usernameEdit.getText().toString());
                database.child("postNote").child("class3").push().setValue(new PostObject("John",postMessage.getText().toString()));

            }
        });
        return view;

    }
}
