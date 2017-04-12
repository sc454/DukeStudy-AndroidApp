package com.ds.DukeStudy.objects;

import java.util.ArrayList;

public class Group {

//  Fields

    private String key = "";
    private String courseKey;
    private String name;
    private ArrayList<String> studentKeys = new ArrayList<String>();
    private ArrayList<String> eventKeys = new ArrayList<String>();
    private ArrayList<String> postKeys = new ArrayList<String>();

//	Constructors

    public Group(String name) {this.name = name;}

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

    public void addStudentKey(String key) {
        if (!studentKeys.contains(key)) {
            studentKeys.add(key);
        }
    }

    public void addEventKey(String key) {
        if (!eventKeys.contains(key)) {
            eventKeys.add(key);
        }
    }

    public void addPostKey(String key) {
        if (!postKeys.contains(key)) {
            postKeys.add(key);
        }
    }

    public void removeStudentKey(String key) {studentKeys.remove(key);}
    public void removeEventKey(String key) {eventKeys.remove(key);}
    public void removePostKey(String key) {postKeys.remove(key);}

//  Database

    public void put() {
        String path = "groups";
        if (key == null || "".equals(key)) {
            key = Database.getNewKey(path);
        }
        Database.put(path, key, this);
    }
}
