package com.ds.DukeStudy;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupObject {

//  Fields

    private String groupName;
    private ArrayList<String> studentIds;
    private String courseID;
//	Constructors

    public GroupObject(String inputGroupName, String inputCourseID) {
        groupName=inputGroupName;
        courseID=inputCourseID;
        studentIds=new ArrayList<String>();
    }
    public GroupObject(){
        groupName="";
        courseID="";
        studentIds=new ArrayList<String>();
    }


//	Getters

    public String getGroupName(){return this.groupName;};
    public ArrayList<String> getstudentIDs(){return this.studentIds;};
    public String getCourseID(){return this.courseID;};


//	Setters

    public void setGroupName(String name) {this.groupName = name;}
    public void setCourseID(String inputCourseID) {this.courseID = inputCourseID;}
    public void setStudentIds(ArrayList<String> inputIDs){this.studentIds=inputIDs;}

//  Mutators

    public void addStudentID(String id){studentIds.add(id);};
}
