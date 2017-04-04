package com.ds.DukeStudy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class Event {

//  Fields

    private String id;
    private String date;
    private String time;
    private String location;
    private ArrayList<String> studentIds;

//	Constructors

    public Event(String date, String time, String location) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.studentIds = new ArrayList<String>();
        id = Util.writeToDatabase(this, Arrays.asList("events"));
    }

    public Event(String date) {this(date, "NoTime", "NoLocation");}

    public Event() {this("NoDate", "NoTime", "NoLocation");}

//	Getters

    public String getId() {return id;}
    public String getDate() {return date;}
    public String getTime() {return time;}
    public String getLocation() {return location;}
    public ArrayList<String> getStudentIds() {return studentIds;}

//	Setters

    public void setId(String id) {this.id = id;}
    public void setDate(String date) {this.date = date;}
    public void setTime(String time) {this.time = time;}
    public void setLocation(String location) {this.location = location;}
    public void setStudentIds(ArrayList<String> ids) {studentIds = ids;}

//  Mutators

    public void addStudent(String id) {
        if (!studentIds.contains(id)) {
            studentIds.add(id);
        }
    }

    public void removeStudent(String id) {studentIds.remove(id);}

//  Database

    public void updateDatabase() {
        Util.writeToDatabase(id, this, Arrays.asList("events"));
    }
}
