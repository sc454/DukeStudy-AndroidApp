package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Text;
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

//  Use tab_layout.xml to show three tabs in Groups ???

public class ViewProfileFragment extends Fragment {

//  Fields

    public static ViewPager viewPager;
    private String studentUID;
    private DatabaseReference databaseRef;

//  Methods

    public ViewProfileFragment() {} // required

    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // creating an instance of Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //creating a storage reference,below URL is the Firebase storage URL.
        StorageReference storageRef = storage.getReferenceFromUrl("gs://dukestudy-a11a3.appspot.com/");
        Bundle mybundle=getArguments();
        this.studentUID=mybundle.getString("myid");
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getContext(), this.studentUID, duration);
        toast.show();

        //Set all of the values for the current student.
        final View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        final TextView studentName= (TextView) view.findViewById(R.id.studentName);
        final TextView studentEmail= (TextView) view.findViewById(R.id.studentEmail);
        final TextView studentMajor= (TextView) view.findViewById(R.id.studentMajor);
        final TextView studentYear= (TextView) view.findViewById(R.id.studentYear);
        final ImageView studentImage= (ImageView) view.findViewById(R.id.studentImage);

        databaseRef= FirebaseDatabase.getInstance().getReference();
        //Get values from database
        DatabaseReference curRef=databaseRef.child("students").child(this.studentUID);
        curRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Student curStudent= dataSnapshot.getValue(Student.class);
                studentName.setText(curStudent.getName());
                studentEmail.setText(curStudent.getEmail());
                studentMajor.setText(curStudent.getMajor());
                studentYear.setText(curStudent.getGradYear());
            };
            @Override
            public void onCancelled(DatabaseError databaseError) {};
        });
        //Get image from database if it exists
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            StorageReference myfileRef1 = storageRef.child(this.studentUID);

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




    /* Interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * See http://developer.android.com/training/basics/fragments/communicating.html
     */
    public interface OnFragmentInteractionListener {
        //  TODO: Update argument type and name
        void onFragmentInteraction(int tag,int view);
    }
}