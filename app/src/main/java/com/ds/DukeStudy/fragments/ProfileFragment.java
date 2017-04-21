package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Student;
import com.ds.DukeStudy.objects.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static com.ds.DukeStudy.R.drawable.ic_menu_profile;
import static com.ds.DukeStudy.R.id.editProfileButton;
import static com.ds.DukeStudy.R.id.emailView;
import static com.ds.DukeStudy.R.id.profileImageView;
import static com.ds.DukeStudy.R.id.snap;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    // Fields

    private static final String TAG = "ProfileFragment";
    private OnFragmentInteractionListener mListener;
    FirebaseUser user;
    TextView userNameView, emailView, majorView, yearView;
    ImageView pictureView;
    String encodedImage;
    private static final int SELECT_PICTURE = 100;
    FirebaseAuth auth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://dukestudy-a11a3.appspot.com/");

    // Constructors

    public ProfileFragment() {}

//    public static ProfileFragment newInstance(String param1, String param2) {
//        ProfileFragment fragment = new ProfileFragment();
//        return fragment;
//    }

    // Other methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get view items
        user = Database.getUser();
        Button editProfileButton = (Button) view.findViewById(R.id.editProfileButton);
        userNameView = (TextView) view.findViewById(R.id.userNameView);
        emailView = (TextView) view.findViewById(R.id.emailView);
        majorView = (TextView)view.findViewById(R.id.majorView);
        yearView = (TextView)view.findViewById(R.id.classView);
        pictureView = (ImageView)view.findViewById(R.id.profileImageView);
        //  profilePictureView = (ImageButton) view.findViewById(R.id.profileImageButton);
        // Get a reference to the UID and retrieve profile details to show up on the layout

        if (user != null) {

            StorageReference myfileRef1 = storageRef.child(user.getUid());

            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }
            final File myfile=localFile;
            final StorageTask<FileDownloadTask.TaskSnapshot> taskSnapshotStorageTask = myfileRef1.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot1) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(myfile.getAbsolutePath());
                    pictureView.setImageBitmap(myBitmap);
                    //Set the navBar image as well
                   // ImageView navPic = (ImageView) view.findViewById(R.id.navProfileIcon);
                    //navPic.setImageBitmap(myBitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            Database.ref.child(Util.STUDENT_ROOT).child(user.getUid()).addValueEventListener(new ValueEventListener() {
                // Update the profile view with new data each time something changes
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i(TAG, "OnDataChange: studentListener");
                    Student student = dataSnapshot.getValue(Student.class);
                    if (student != null) {
                        userNameView.setText("Name: " + student.getName());
                        emailView.setText("Email: " + student.getEmail());
                        majorView.setText("Major: " + student.getMajor());
                        yearView.setText("Graduation Year: " + student.getGradYear());
                    } else{
                        Toast.makeText((MainActivity)getActivity(), "Error: Profile could not fetch student", Toast.LENGTH_SHORT).show();
                        userNameView.setText("Name");
                        emailView.setText("Email");
                        majorView.setText("Major");
                        yearView.setText("Year");
                        //pictureView.setImageResource(R.drawable.ic_menu_profile);;
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "OnDataChangeCancelled: studentListener");
                }
            });

        }
        editProfileButton.setOnClickListener(this);

        pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"),SELECT_PICTURE );
            }
        });

        //profilePictureView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        onButtonPressed(mListener, 0, viewID);
    }

    private void onButtonPressed(OnFragmentInteractionListener mListener, int tag, int viewID) {
        if (mListener != null) {
            mListener.onFragmentInteraction(tag,viewID);
        }
    }

    // Communicates with Main Activity to open Edit profile page upon clicking button
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int tag,int viewID);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            if(requestCode==SELECT_PICTURE){
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i("IMAGE PATH TAG", "Image Path : " + path);
                    // Set the image in ImageView
                    pictureView.setImageURI(selectedImageUri);
                    uploadImage();
                }
            }
        }
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void uploadImage() {
        // Creating a reference to the full path of the file. myfileRef now points
        // gs://fir-demo-d7354.appspot.com/myuploadedfile.jpg
        StorageReference myfileRef = storageRef.child(user.getUid());
        pictureView.setDrawingCacheEnabled(true);
        pictureView.buildDrawingCache();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = pictureView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask1 = myfileRef.putBytes(data);
        final StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask1 = uploadTask1.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext().getApplicationContext(), "Profile picture not updated.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot1) {
                Toast.makeText(getContext().getApplicationContext(), "Profile picture updated.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
