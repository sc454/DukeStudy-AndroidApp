package com.ds.DukeStudy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Group {

//  Fields

    private String key = "";
    private String courseKey;
    private String name;
    private ArrayList<String> studentKeys = new ArrayList<String>();
    private ArrayList<String> eventKeys = new ArrayList<String>();
    private ArrayList<String> postKeys = new ArrayList<String>();

//	Constructors

    public Group(String name) {
        this.name = name;
    }

    public Group() {this("NoName");}

//	Getters

    public String getKey() {return key;}
    public String getCourseKey() {return courseKey;}
    public String getName() {return name;}
    public ArrayList<String> getStudentKeys() {return studentKeys;}
    public ArrayList<String> getEventKeys() {return eventKeys;}
    public ArrayList<String> getPostKeys() {return postKeys;}

//	Setters

    public void setKey(String key) {this.key = key;}
    public void setCourseKey(String key) {courseKey = key;}
    public void setName(String name) {this.name = name;}
    public void setStudentKeys(ArrayList<String> keys) {studentKeys = keys;}
    public void setEventKeys(ArrayList<String> keys) {eventKeys = keys;}
    public void setPostKeys(ArrayList<String> keys) {postKeys = keys;}

//  Other mutators

    public void addStudent(String key) {
        if (!studentKeys.contains(key)) {
            studentKeys.add(key);
        }
    }

    public void addEvent(String key) {
        if (!eventKeys.contains(key)) {
            eventKeys.add(key);
        }
    }

    public void addPost(String key) {
        if (!postKeys.contains(key)) {
            postKeys.add(key);
        }
    }

    public void removeStudent(String key) {studentKeys.remove(key);}
    public void removeEvent(String key) {eventKeys.remove(key);}
    public void removePost(String key) {postKeys.remove(key);}

//  Database

    public void put() {
        List<String> path = Arrays.asList("groups");
        if (key == null || "".equals(key)) {
            key = Database.getNewKey(path);
        }
        Database.put(key, this, path);
    }
}
