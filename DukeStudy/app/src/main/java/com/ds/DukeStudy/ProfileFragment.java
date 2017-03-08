package com.ds.DukeStudy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String param2;
    private static String param1;

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;
    DrawerLayout drawer;

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
    // TODO: Rename and change types and number of parameters
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
        TextView userNameView = (TextView) initalProfileView.findViewById(R.id.userNameView);
        userNameView.setText("UserName : Justin Bieber");
        TextView emailView = (TextView) initalProfileView.findViewById(R.id.emailView);
        if (getArguments() != null) {
            Bundle args = this.getArguments();
            param1 = args.getString("UserName");
            param2 = args.getString("Email");
            if(param1!=null){
                userNameView.setText("UserName " + ": " + param1);
            }
            if(param2!=null) {
                emailView.setText("Email " + ": " + param2);
            }
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int tag,int viewID);
    }
}
