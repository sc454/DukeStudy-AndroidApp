package com.ds.DukeStudy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Event {

//  Fields

    private String id;
    private String date;
    private String time;
    private String location;
    private ArrayList<String> studentIds;

//	Constructors

    public Event(String date, String time, String location) {
        this.id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.date = date;
        this.time = time;
        this.location = location;
        this.studentIds = new ArrayList<String>();
        updateDatabase();
    }

    public Event(String date) {
        this(date, "NoTime", "NoLocation");
    }

    public Event() {
        this("NoDate", "NoTime", "NoLocation");
    }

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
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//        DataSnapshot dbData = FirebaseDatabase.getInstance().getReference();
//        if (dbData.child("students").hasChild(id)) {
//        if (db.child("students").child(id) != null) {
//            System.out.println("Student exists!");
//        }
        db.child("events").child(id).setValue(this);
    }
}
