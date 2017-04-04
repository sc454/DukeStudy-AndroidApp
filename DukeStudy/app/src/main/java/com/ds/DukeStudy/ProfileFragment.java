package com.ds.DukeStudy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.ds.DukeStudy.R.id.emailView;
import static com.ds.DukeStudy.R.id.snap;

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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String param1;
    private static String param2;
    private OnFragmentInteractionListener mListener;
    FirebaseUser user;
    TextView userNameView;
    TextView emailView;
    TextView majorView;
    TextView yearView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */

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
        Button editProfileButton = (Button) initalProfileView.findViewById(R.id.editProfileButton);
        ImageView editImageButton = (ImageView) initalProfileView.findViewById(R.id.profileImageButton);
        userNameView = (TextView) initalProfileView.findViewById(R.id.userNameView);
        emailView = (TextView) initalProfileView.findViewById(R.id.emailView);
        majorView = (TextView)initalProfileView.findViewById(R.id.majorView);
        yearView = (TextView)initalProfileView.findViewById(R.id.classView);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            rootRef.child("students").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user.getUid())){
                      //  Toast.makeText(getActivity(), "UID exists!",
                      //          Toast.LENGTH_SHORT).show();
                        userNameView.setText("Name : "+dataSnapshot.child(user.getUid()).child("name").getValue().toString());
                        emailView.setText("Email : "+dataSnapshot.child(user.getUid()).child("email").getValue().toString());
                        majorView.setText("Major : "+dataSnapshot.child(user.getUid()).child("major").getValue().toString());
                        yearView.setText("Graduation Year : "+dataSnapshot.child(user.getUid()).child("gradYear").getValue().toString());
                    }
                    else{
                    //    Toast.makeText(getActivity(), "UID doesn't exist!",
                      //          Toast.LENGTH_SHORT).show();
                        userNameView.setText("Name");
                        emailView.setText("Email");
                        majorView.setText("Major");
                        yearView.setText("Year");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        editProfileButton.setOnClickListener(this);
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

            onButtonPressed(mListener,0,viewID);

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
}
