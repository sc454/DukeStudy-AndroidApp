package com.ds.DukeStudy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Course {

//  Fields

    private String title;
    private String department;
    private String code;
    private String instructor;
    private String startTime;
    private String endTime;
    private String location;
    private ArrayList<String> studentIds;
    private ArrayList<String> groupIds;
    private ArrayList<String> postIds;

//	Constructors

    public Student(String name, String email, String major, String gradYear) {
        this.name = name;
        this.email = email;
        this.major = major;
        this.gradYear = gradYear;
        this.courseIds = new ArrayList<String>();
        this.groupIds = new ArrayList<String>();
        this.eventIds = new ArrayList<String>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.child("StudentList").child(uid).setValue(this);
    }

    public Student(String email) {
        this.name = "";
        this.email = email;
        this.major = "";
        this.gradYear = "";
        this.profileURL = "";
        this.courseIds = new ArrayList<String>();
        this.groupIds = new ArrayList<String>();
        this.eventIds = new ArrayList<String>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.child("StudentList").child(user.getUid()).setValue(this);
    }

//	Getters

    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getMajor() {return major;}
    public String getGradYear() {return gradYear;}
    public ArrayList<String> getCourseIds() {return courseIds;}
    public ArrayList<String> getGroupIds() {return groupIds;}
    public ArrayList<String> getEventIds() {return eventIds;}
    public String getProfileURL(){return profileURL;}

//	Setters

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setMajor(String major) {this.major = major;}
    public void setGradYear(String gradYear) {this.gradYear = gradYear;}
    public void setProfileURL(String url){this.profileURL = url;}

//  Mutators

    public void addStudent(String id) {
        if (!studentIds.contains(id)) {
            studentIds.add(id);
//            FirebaseDatabase.getInstance().getReference().child("CourseList").;
        }
    }

    public void addGroup(String id) {
        if (!groupIds.contains(id)) {
            groupIds.add(id);
        }
    }

    public void addPost(String id) {
        if (!postIds.contains(id)) {
            postIds.add(id);
        }
    }

    public void removeStudent(String id) {studentIds.remove(id);}
    public void removeGroup(String id) {groupIds.remove(id);}
    public void removePost(String id) {postIds.remove(id);}
}
