package com.ds.DukeStudy.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Student;
import com.ds.DukeStudy.objects.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;

public class ViewProfileFragment extends Fragment {

    // Fields

    private static final String TAG = "ViewProfileFragment";
    private static final String KEY_ARG = "key";

    public ViewPager viewPager;
    private String studentKey;
    private DatabaseReference databaseRef;

    // Constructors

    public ViewProfileFragment() {}

    public static ViewProfileFragment newInstance(String key) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARG, key);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://dukestudy-a11a3.appspot.com/");

        // Get arguments
        studentKey = getArguments().getString(KEY_ARG);
        if (studentKey == null) {
            throw new IllegalArgumentException("Must pass " + KEY_ARG);
        }

        // Get view items
        final TextView studentName= (TextView) view.findViewById(R.id.studentName);
        final TextView studentEmail= (TextView) view.findViewById(R.id.studentEmail);
        final TextView studentMajor= (TextView) view.findViewById(R.id.studentMajor);
        final TextView studentYear= (TextView) view.findViewById(R.id.studentYear);
        final ImageView studentImage= (ImageView) view.findViewById(R.id.studentImage);

        // Get values from database
        DatabaseReference curRef = Database.ref.child(Util.STUDENT_ROOT).child(studentKey);
        curRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "OnDataChange: studentListener");
                final Student curStudent= dataSnapshot.getValue(Student.class);
                if (curStudent == null) {
                    Toast.makeText((MainActivity)getActivity(), "Error: Could not fetch student", Toast.LENGTH_SHORT).show();
                } else {
                    studentName.setText(curStudent.getName());
                    studentEmail.setText(curStudent.getEmail());
                    studentMajor.setText(curStudent.getMajor());
                    studentYear.setText(curStudent.getGradYear());
                }
            };
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: studentListener", databaseError.toException());
            };
        });
        //Get image from database if it exists
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            StorageReference myfileRef1 = storageRef.child(studentKey);

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            final File myfile = localFile;
            final StorageTask<FileDownloadTask.TaskSnapshot> taskSnapshotStorageTask = myfileRef1.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot1) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(myfile.getAbsolutePath());
                    studentImage.setImageBitmap(myBitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        Button exitProfileButton=(Button) view.findViewById(R.id.exitProfile);

        exitProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Simulate pressing the back button
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int tag,int view);
    }
}