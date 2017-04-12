package com.ds.DukeStudy.fragments;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Event;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

/**
 * Created by cheli on 3/5/2017.
 */
//This is an events fragment that retrieves events listed and displays them in a list for a given
    //course
public class EventsFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<Event> adapter1;
    private ListView eventsListView;
    private String sourceID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.events_layout, container, false);
        eventsListView = (ListView) view.findViewById(R.id.eventsListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postsRef = databaseRef.child("events");
        adapter1 = new FirebaseListAdapter<Event>(getActivity(), Event.class, android.R.layout.two_line_list_item, postsRef) {
            @Override
            protected void populateView(View v, Event model, int position) {
                TextView mytext = (TextView) v.findViewById(android.R.id.text1);
                TextView mytext1 = (TextView) v.findViewById(android.R.id.text2);
                mytext.setText("Date:" + model.getDate() + "   Time:" + model.getTime());
                mytext1.setText("Location:" + model.getLocation());
            }
        };
        eventsListView.setAdapter(adapter1);

        //Code from http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        final Calendar myCalendar = Calendar.getInstance();
        final EditText dateTimeText = (EditText) view.findViewById(R.id.eventDateTimeText);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dateTimeText, myCalendar);
            }

        };
        dateTimeText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(view.getContext(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });
        final EditText locationText=(EditText) view.findViewById(R.id.locationEntry);
        final EditText timeText=(EditText) view.findViewById(R.id.timeEntry);
        //Set the onClick to add a new event
        Button addEventButton=(Button) view.findViewById(R.id.addeventbutton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating firebase object
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                //database.child("note").push().setValue(usernameEdit.getText().toString());
                Event event = new Event(dateTimeText.getText().toString(), timeText.getText().toString(),locationText.getText().toString());
                event.put();
                //atabase.child("postNote").child("class3").push().setValue(new Post(postMessage.getText().toString()),user.getUid(),"1234");
            }
        });
        return view;
    }

    private void updateLabel(EditText myeditText, Calendar myCal) {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        myeditText.setText(sdf.format(myCal.getTime()));
    }


}
