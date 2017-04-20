package com.ds.DukeStudy.misc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddCourseViewHolder extends RecyclerView.ViewHolder {

    // Fields

    private static final String TAG = "PostViewHolder";
    public TextView titleView, authorView, bodyView, imageView;

    // Methods

    public AddCourseViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        imageView = (ImageView) itemView.findViewById(R.id.post_author_photo);
    }

    public void bindToPost(Post post) {
        titleView.setText(post.getTitle());
        authorView.setText(post.getAuthor());
        bodyView.setText(post.getMessage());
        myfileRef1 = storageRef.child(post.getUid().toString());
        myfileRef1.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(getCircleBitmap(bitmap));
            }
        });


    }
}
