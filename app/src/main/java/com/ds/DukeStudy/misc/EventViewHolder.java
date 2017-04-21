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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Event;
import com.ds.DukeStudy.objects.Post;
import com.ds.DukeStudy.objects.Student;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventViewHolder extends RecyclerView.ViewHolder {

    // Fields

    private static final String TAG = "EventViewHolder";
    public TextView titleView, bodyView;
    public ToggleButton toggleBtn;

    // Methods

    public EventViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.course_title);
        bodyView = (TextView) itemView.findViewById(R.id.course_body);
        toggleBtn = (ToggleButton) itemView.findViewById(R.id.toggle_btn);
    }

    public void bindToEvent(Event event, Boolean checked) {
        Log.i(TAG, "Binding Event");
        titleView.setText(event.getTitle());
        bodyView.setText(event.getDate() + " at " + event.getTime() + " in " + event.getLocation());
        toggleBtn.setChecked(checked);
    }
}
