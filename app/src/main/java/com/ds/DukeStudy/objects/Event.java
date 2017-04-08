package com.ds.DukeStudy.objects;

import com.ds.DukeStudy.objects.Database;

import java.util.ArrayList;

public class Event {

    // Fields

    private String key = "";
    private String date;
    private String time;
    private String location;
    private ArrayList<String> studentKeys = new ArrayList<String>();

    // Constructors

    public Event(String date, String time, String location) {
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public Event(String date) {this(date, "NoTime", "NoLocation");}

    public Event() {this("NoDate", "NoTime", "NoLocation");}

    // Getters

    public String getKey() {return key;}
    public String getDate() {return date;}
    public String getTime() {return time;}
    public String getLocation() {return location;}
    public ArrayList<String> getStudentKeys() {return studentKeys;}

    // Setters

    public void setKey(String key) {this.key = key;}
    public void setDate(String date) {this.date = date;}
    public void setTime(String time) {this.time = time;}
    public void setLocation(String location) {this.location = location;}
    public void setStudentKeys(ArrayList<String> keys) {studentKeys = keys;}

    // Other mutators

    public void addStudent(String key) {
        if (!studentKeys.contains(key)) {
            studentKeys.add(key);
        }
    }

    public void removeStudent(String key) {studentKeys.remove(key);}

    // Database

    public void put() {
        String path = "events";
        if (key == null || "".equals(key)) {
            key = Database.getNewKey(path);
        }
        Database.put(path, key, this);
    }
}
