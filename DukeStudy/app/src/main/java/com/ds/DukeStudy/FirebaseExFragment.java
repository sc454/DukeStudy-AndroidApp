package com.ds.DukeStudy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FirebaseExFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FirebaseExFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//This example shows the read and write functionality from our firebase database.
public class FirebaseExFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    private String mUserId;
    private EditText usernameEdit;
    private ListView databaseAns;
    private Button submitBut;
    private Button readBut;
    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mynames=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private FirebaseListAdapter<Student> adapter1;
    public FirebaseExFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirebaseExFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirebaseExFragment newInstance(String param1, String param2) {
        FirebaseExFragment fragment = new FirebaseExFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    //Sets listeners for the items added to a particular database child.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_firebase_ex, container, false);
        submitBut = (Button) view.findViewById(R.id.submitButton);
        //readBut = (Button) view.findViewById(R.id.readButton);
        usernameEdit = (EditText) view.findViewById(R.id.userNameInput);
        databaseAns = (ListView) view.findViewById(R.id.databaseListView);
        //Make the array adapter
        //adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mynames);
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference notesRef=databaseRef.child("StudentList");
//        adapter1=new FirebaseListAdapter<Student>(getActivity(),Student.class,android.R.layout.two_line_list_item,notesRef) {
//            @Override
//            protected void populateView(View v, Student model, int position) {
//                TextView mytext1=(TextView) v.findViewById(android.R.id.text1);
//                TextView mytext2=(TextView) v.findViewById(android.R.id.text2);
//                mytext1.setText(model.getEmail());
//                mytext2.setText(model.getGradYear());
//            }
//        };
//        databaseAns.setAdapter(adapter1);



//        databaseRef.child("note").addValueEventListener(new ValueEventListener() {
//            @Override //Gets data initially and whenever things change
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> mychildren = dataSnapshot.getChildren();
//                adapter.clear();
//                mynames.clear();
//                for (DataSnapshot child: mychildren){
//                    String curName = child.getValue(String.class);
//                    mynames.add(curName);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more informatio
        submitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating firebase object
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                //database.child("note").push().setValue(usernameEdit.getText().toString());
                String customField = usernameEdit.getText().toString();
                database.child("StudentList").push().setValue(new Student(customField,"blank@duke.edu","music","2016"));
            }
        });
        /*readBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating firebase object


            }
       });*/

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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int tag,int view);
    }
}
