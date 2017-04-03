package com.ds.DukeStudy;

import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Course {

//  Fields

    private String id;
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

    public Course(String title, String department, String code) {
        this.title = title;
        this.department = department;
        this.code = code;
        this.instructor = "";
        this.startTime = "";
        this.endTime = "";
        this.location = "";
        this.studentIds = new ArrayList<String>();
        this.groupIds = new ArrayList<String>();
        this.postIds = new ArrayList<String>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("CourseList").setValue(this);
//        DatabaseReference pushedPostRef
    }

//	Getters

    public String getTitle() {return title;}
    public String getDepartment() {return department;}
    public String getCode() {return code;}
    public String getInstructor() {return instructor;}
    public String getStartTime() {return startTime;}
    public String getEndTime() {return endTime;}
    public String getLocation() {return location;}
    public ArrayList<String> getStudentIds() {return studentIds;}
    public ArrayList<String> getGroupIds() {return groupIds;}
    public ArrayList<String> getPostIds() {return postIds;}

//	Setters

    public void setTitle(String title) {this.title = title;}
    public void setDepartment(String department) {this.department = department;}
    public void setCode(String code) {this.code = code;}
    public void setInstructor(String instructor) {this.instructor = instructor;}
    public void setStartTime(String startTime) {this.startTime = startTime;}
    public void setEndTime(String endTime) {this.endTime = endTime;}
    public void setLocation(String location) {this.location = location;}

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
