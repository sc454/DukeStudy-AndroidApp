package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ds.DukeStudy.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Main Activity that contains this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    FirebaseUser user;
    TextView userNameView, emailView, majorView, yearView;
    Button uploadImageButton;
    ImageView pictureView;
    String encodedImage;
    private static final int SELECT_PICTURE = 100;
    FirebaseAuth auth;
    // creating an instance of Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference,below URL is the Firebase storage URL.
    StorageReference storageRef = storage.getReferenceFromUrl("gs://dukestudy-a11a3.appspot.com/");

    public ProfileFragment() {} // required

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View initalProfileView = inflater.inflate(R.layout.fragment_profile, container, false);
       // View initalProfileView = inflater.inflate(R.layout.profile_layout, container, false);
        Button editProfileButton = (Button) initalProfileView.findViewById(R.id.editProfileButton);
        //  ImageView editImageButton = (ImageView) initalProfileView.findViewById(R.id.profileImageButton);
        userNameView = (TextView) initalProfileView.findViewById(R.id.userNameView);
        emailView = (TextView) initalProfileView.findViewById(R.id.emailView);
        majorView = (TextView)initalProfileView.findViewById(R.id.majorView);
        yearView = (TextView)initalProfileView.findViewById(R.id.classView);
        uploadImageButton = (Button)initalProfileView.findViewById(R.id.editImageButton);
        pictureView = (ImageView)initalProfileView.findViewById(R.id.profileImageView);
        //  profilePictureView = (ImageButton) initalProfileView.findViewById(R.id.profileImageButton);
        // Get a reference to the UID and retrieve profile details to show up on the layout
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
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
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            rootRef.child("students").addValueEventListener(new ValueEventListener() {
                // Update the profile view with new data each time something changes
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user.getUid())){
                        //  Toast.makeText(getActivity(), "UID exists!",
                        //          Toast.LENGTH_SHORT).show();
                        userNameView.setText("Name : "+dataSnapshot.child(user.getUid()).child("name").getValue().toString());
                        emailView.setText("Email : "+dataSnapshot.child(user.getUid()).child("email").getValue().toString());
                        majorView.setText("Major : "+dataSnapshot.child(user.getUid()).child("major").getValue().toString());
                        yearView.setText("Graduation Year : "+dataSnapshot.child(user.getUid()).child("gradYear").getValue().toString());
                  /*      if(!dataSnapshot.child(user.getUid()).child("profileUrl").getValue().equals("NoUrl")){
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            pictureView.setImageBitmap(decodedByte);
                        }
*/                    }
                    else{
                        //    Toast.makeText(getActivity(), "UID doesn't exist!",
                        //          Toast.LENGTH_SHORT).show();
                        userNameView.setText("Name");
                        emailView.setText("Email");
                        majorView.setText("Major");
                        yearView.setText("Year");
                        //pictureView.setImageResource(R.drawable.ic_menu_profile);;
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

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

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creating a reference to the full path of the file. myfileRef now points
                // gs://fir-demo-d7354.appspot.com/myuploadedfile.jpg
                StorageReference myfileRef = storageRef.child(user.getUid());
                pictureView.setDrawingCacheEnabled(true);
                pictureView.buildDrawingCache();
                Bitmap bitmap = pictureView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                //   encodedImage = Base64.encodeToString(data, Base64.NO_WRAP);
                // DatabaseReference database = FirebaseDatabase.getInstance().getReference();
               /* auth=FirebaseAuth.getInstance();
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    database.child("students").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(user.getUid())) {
                                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                database.child("students").child(auth.getCurrentUser().getUid()).child("profileUrl").setValue(encodedImage);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                */
                UploadTask uploadTask1 = myfileRef.putBytes(data);
                final StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask1 = uploadTask1.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext().getApplicationContext(), "TASK FAILED", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot1) {
                        Toast.makeText(getContext().getApplicationContext(), "TASK SUCCEEDED", Toast.LENGTH_SHORT).show();
                        //Uri downloadUrl = taskSnapshot1.getDownloadUrl();
                        //String DOWNLOAD_URL = downloadUrl.getPath();
                        //Log.v("DOWNLOAD URL", DOWNLOAD_URL);
                        //Toast.makeText(getContext().getApplicationContext(), DOWNLOAD_URL, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //profilePictureView.setOnClickListener(this);
        return initalProfileView;
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
        // TODO: Update argument type and name
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
}
