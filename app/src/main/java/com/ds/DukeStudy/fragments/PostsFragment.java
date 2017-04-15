package com.ds.DukeStudy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ds.DukeStudy.NewPostActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Post;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ds.DukeStudy.PostDetailActivity;
import com.ds.DukeStudy.misc.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

public class PostsFragment extends Fragment {

    private static final String TAG = "PostsFragment";
    public static final String DB_PATH_ARG = "dbPath";
    public static final String DB_KEY_ARG = "dbKey";

    private String dbPath;
    private String dbKey;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FloatingActionButton newPostBtn;

    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapter1;
    private ListView postsListView;
    private Button postMessageButton;
    private EditText postMessage;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public PostsFragment() {}

    public static PostsFragment newInstance(String dpPath, String key) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString(DB_PATH_ARG, dpPath);
        args.putString(DB_KEY_ARG, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get arguments
        dbPath = getArguments().getString(DB_PATH_ARG);
        if (dbPath == null) {
            throw new IllegalArgumentException("Must pass " + DB_PATH_ARG);
        }
        dbKey = getArguments().getString(DB_KEY_ARG);
        if (dbKey == null) {
            throw new IllegalArgumentException("Must pass " + DB_KEY_ARG);
        }

        // Set view
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_all_posts, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        // New post button
        newPostBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_post);
        newPostBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startActivity(new Intent(getActivity(), NewPostActivity.class));
              }
          });

        dbRef = Database.ref.child("posts");//.child(dbPath).child(dbKey);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = dbRef.limitToFirst(100);
        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.item_post, PostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post post, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(post);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

//public class PostsFragment extends Fragment {
//
//    private DatabaseReference databaseRef;
//    private FirebaseListAdapter<String> adapter1;
//    private ListView postsListView;
//    private Button postMessageButton;
//    private EditText postMessage;
//    private FirebaseAuth auth;
//    private FirebaseUser user;
//    private String myid;
//    private Boolean isCourse;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Bundle curBundle=getArguments();
//        this.myid=curBundle.getString("myid");
//        this.isCourse=curBundle.getBoolean("isCourse");
//        View view=inflater.inflate(R.layout.posts_layout,null);
//        postMessageButton= (Button) view.findViewById(R.id.submitPost);
//        //readBut = (Button) view.findViewById(R.id.readButton);
//        postMessage = (EditText) view.findViewById(R.id.postMessage);
//        postsListView=(ListView) view.findViewById(R.id.postsListView);
//        final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
//        final DatabaseReference postsRef;
//        if (this.isCourse){
//            postsRef=databaseRef.child("courses").child(myid).child("postKeys");}else{
//            postsRef=databaseRef.child("groups").child(myid).child("postKeys");
//        }
//        adapter1= new FirebaseListAdapter<String>(getActivity(),String.class,android.R.layout.two_line_list_item,postsRef) {
//            @Override
//            public void populateView(View v, String model, final int position) {
//                DatabaseReference curStudentRef=databaseRef.child("posts").child(model.toString());
//                final TextView mytext1=(TextView) v.findViewById(android.R.id.text1);
//                final TextView mytext2=(TextView) v.findViewById(android.R.id.text2);
//                curStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        final Post curPost= dataSnapshot.getValue(Post.class);
//                        mytext1.setText(curPost.getMessage());
//                        mytext2.setText(curPost.getAuthor());
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        };
//        postsListView.setAdapter(adapter1);
//        postMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Creating firebase object
//                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//                MainActivity main = (MainActivity)PostsFragment.this.getActivity();
//                //database.child("note").push().setValue(usernameEdit.getText().toString());
//                Post post = new Post (postMessage.getText().toString(), main.student.getName(), "Time");
//                post.put();
//                final String postKey=post.getKey();
//                final DatabaseReference classRef;
//                if(isCourse){
//                    classRef=databaseRef.child("courses").child(myid);}else{
//                    classRef=databaseRef.child("groups").child(myid);
//                }
//                classRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (isCourse){
//                            final Course curObj= dataSnapshot.getValue(Course.class);
//                            curObj.addPostKey(postKey);
//                            classRef.setValue(curObj);
//                        }else{
//                            final Group curObj= dataSnapshot.getValue(Group.class);
//                            curObj.addPostKey(postKey);
//                            classRef.setValue(curObj);
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {}
//                });
//            }
//        });
//        return view;
//
//    }
//}
