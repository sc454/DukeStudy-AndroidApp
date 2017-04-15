package com.ds.DukeStudy.misc;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public TextView bodyView;

    public PostViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(Post post) {
        titleView.setText(post.getTitle());
        authorView.setText(post.getAuthor());
        bodyView.setText(post.getMessage());
    }
}
