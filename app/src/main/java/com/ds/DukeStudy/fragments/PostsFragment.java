package com.ds.DukeStudy.fragments;

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

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Post;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This is a generic posts Fragment. This will be expanded to work for classes and groups.
 */

public class PostsFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapter1;
    private ListView postsListView;
    private Button postMessageButton;
    private EditText postMessage;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String myid;
    private Boolean isCourse;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle curBundle=getArguments();
        this.myid=curBundle.getString("myid");
        this.isCourse=curBundle.getBoolean("isCourse");
        View view=inflater.inflate(R.layout.posts_layout,null);
        postMessageButton= (Button) view.findViewById(R.id.submitPost);
        //readBut = (Button) view.findViewById(R.id.readButton);
        postMessage = (EditText) view.findViewById(R.id.postMessage);
        postsListView=(ListView) view.findViewById(R.id.postsListView);
        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference postsRef;
        if (this.isCourse){
            postsRef=databaseRef.child("courses").child(myid).child("postKeys");}else{
            postsRef=databaseRef.child("groups");
        }
        adapter1= new FirebaseListAdapter<String>(getActivity(),String.class,android.R.layout.two_line_list_item,postsRef) {
            @Override
            public void populateView(View v, String model, final int position) {
                DatabaseReference curStudentRef=databaseRef.child("posts").child(model.toString());
                final TextView mytext1=(TextView) v.findViewById(android.R.id.text1);
                final TextView mytext2=(TextView) v.findViewById(android.R.id.text2);
                curStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Post curPost= dataSnapshot.getValue(Post.class);
                        mytext1.setText(curPost.getMessage());
                        mytext2.setText(curPost.getAuthor());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                Post post = new Post (postMessage.getText().toString(), "Author", "Time");
                post.put();
                final String postKey=post.getKey();
                final DatabaseReference classRef;
                if(isCourse){
                    classRef=databaseRef.child("courses").child(myid);}else{
                    classRef=databaseRef.child("groups").child(myid);
                }
                classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (isCourse){
                            final Course curObj= dataSnapshot.getValue(Course.class);
                            curObj.addPostKey(postKey);
                            classRef.setValue(curObj);
                        }else{
                            final Group curObj= dataSnapshot.getValue(Group.class);
                            curObj.addPostKey(postKey);
                            classRef.setValue(curObj);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //Add key to
                //atabase.child("postNote").child("class3").push().setValue(new Post(postMessage.getText().toString()),user.getUid(),"1234");
            }
        });
        return view;

    }
}
