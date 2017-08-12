package com.ds.DukeStudy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.DukeStudy.activities.NewPostActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.items.Database;
import com.ds.DukeStudy.items.Post;
import com.ds.DukeStudy.items.Util;
import com.google.firebase.database.DatabaseReference;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ds.DukeStudy.activities.PostDetailActivity;
import com.ds.DukeStudy.adapters.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

public class PostsFragment extends Fragment {

    // Fields

    private static final String TAG = "PostsFragment";
    public static final String DB_KEY_ARG = "dbKey";

    private String dbKey;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private FloatingActionButton newPostBtn;

    //  Constructors

    public PostsFragment() {}

    public static PostsFragment newInstance(String path) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString(DB_KEY_ARG, path);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        // Get arguments
        dbKey = getArguments().getString(DB_KEY_ARG);
        if (dbKey == null) {
            throw new IllegalArgumentException("Must pass " + DB_KEY_ARG);
        }
        dbRef = Database.ref.child(Util.POST_ROOT).child(dbKey);

        // Get view items
        mRecycler = (RecyclerView) view.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        // New post button
        newPostBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_post);
        newPostBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  NewPostActivity.start(getActivity(), dbKey);
              }
          });

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
        Log.i(TAG, "Querying posts from " + dbKey);
        Query postsQuery = dbRef.limitToFirst(100);
        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.item_post, PostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post post, final int position) {
                final String postKey = getRef(position).getKey();
                // Set click listener for the whole post view
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PostDetailActivity.start(getActivity(), dbKey + "/" + postKey);
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
}