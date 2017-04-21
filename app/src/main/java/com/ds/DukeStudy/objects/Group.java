package com.ds.DukeStudy.objects;

import java.util.ArrayList;

public class Group {

//  Fields

    private String key = "";
    private String courseKey;
    private String name;
    private String description;
    private ArrayList<String> studentKeys = new ArrayList<String>();
    private ArrayList<String> eventKeys = new ArrayList<String>();

//	Constructors

    public Group(String name, String description, String courseKey)
    {
        this.name = name;
        this.description = description;
        this.courseKey = courseKey;
    }

    public Group() {this("NoName", "NoDescription", "none");}

//	Getters

    public String getKey() {return key;}
    public String getCourseKey() {return courseKey;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public ArrayList<String> getStudentKeys() {return studentKeys;}
    public ArrayList<String> getEventKeys() {return eventKeys;}

//	Setters

    public void setKey(String key) {this.key = key;}
    public void setCourseKey(String key) {courseKey = key;}
    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}
    public void setStudentKeys(ArrayList<String> keys) {studentKeys = keys;}
    public void setEventKeys(ArrayList<String> keys) {eventKeys = keys;}

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

    public void removeStudentKey(String key) {studentKeys.remove(key);}
    public void removeEventKey(String key) {eventKeys.remove(key);}

//  Database

    public void put(String prefix) {
        String path = Util.GROUP_ROOT;
        if (key == null || "".equals(key)) {
            key = prefix + "/" + Database.getNewKey(path);
        }
        Database.put(path + "/" + key, this);
    }

    public void put() {
        String path = Util.GROUP_ROOT;
        if (key == null || "".equals(key)) {
            key = Database.getNewKey("");
        }
        Database.put(path + "/" + key, this);
    }
}
