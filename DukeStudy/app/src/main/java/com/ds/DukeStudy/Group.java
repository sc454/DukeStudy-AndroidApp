package com.ds.DukeStudy;

import java.util.ArrayList;
import java.util.Arrays;

public class Group {

//  Fields

    private String courseId;
    private String name;
    private ArrayList<String> studentIds;
    private ArrayList<String> eventIds;
    private ArrayList<String> postIds;

//	Constructors

    public Group(String name) {
        this.name = name;
        studentIds = new ArrayList<String>();
        eventIds = new ArrayList<String>();
        postIds = new ArrayList<String>();
    }

    public Group() {this("NoName");}

//	Getters

    public String getCourseId() {return courseId;}
    public String getName() {return name;}
    public ArrayList<String> getStudentIds() {return studentIds;}
    public ArrayList<String> getEventIds() {return eventIds;}
    public ArrayList<String> getPostIds() {return postIds;}

//	Setters

    public void setCourseId(String id) {courseId = id;}
    public void setName(String name) {this.name = name;}
    public void setStudentIds(ArrayList<String> ids) {studentIds = ids;}
    public void setEventIds(ArrayList<String> ids) {eventIds = ids;}
    public void setPostIds(ArrayList<String> ids) {postIds = ids;}

//  Mutators

    public void addStudent(String id) {
        if (!studentIds.contains(id)) {
            studentIds.add(id);
        }
    }

    public void addEvent(String id) {
        if (!eventIds.contains(id)) {
            eventIds.add(id);
        }
    }

    public void addPost(String id) {
        if (!postIds.contains(id)) {
            postIds.add(id);
        }
    }

    public void removeStudent(String id) {studentIds.remove(id);}
    public void removeEvent(String id) {eventIds.remove(id);}
    public void removePost(String id) {postIds.remove(id);}

//  Database

    public void updateDatabase() {
//        Util.writeToDatabase(id, this, Arrays.asList("groups"));
    }
}
