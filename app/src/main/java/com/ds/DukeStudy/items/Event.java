package com.ds.DukeStudy.items;

import java.util.ArrayList;

public class Event {

    // Fields

    private String key = "";
    private String groupKey;
    private String title;
    private String date;
    private String time;
    private String location;
    private ArrayList<String> studentKeys = new ArrayList<String>();

    // Constructors

    public Event(String title, String date, String time, String location, String groupKey) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.groupKey = groupKey;
    }

    public Event(String date) {this("NoTitle", date, "NoTime", "NoLocation", "None");}

    public Event() {this("NoTitle", "NoDate", "NoTime", "NoLocation", "None");}

    // Getters

    public String getKey() {return key;}
    public String getTitle() {return title;}
    public String getDate() {return date;}
    public String getTime() {return time;}
    public String getLocation() {return location;}
    public String getGroupKey() {return groupKey;}
    public ArrayList<String> getStudentKeys() {return studentKeys;}

    // Setters

    public void setKey(String key) {this.key = key;}
    public void setTitle(String title) {this.title = title;}
    public void setDate(String date) {this.date = date;}
    public void setTime(String time) {this.time = time;}
    public void setLocation(String location) {this.location = location;}
    public void setGroupKey(String key) {groupKey = key;}
    public void setStudentKeys(ArrayList<String> keys) {studentKeys = keys;}

    // Other mutators

    public void addStudentKey(String key) {
        if (!studentKeys.contains(key)) {
            studentKeys.add(key);
        }
    }

    public void removeStudentKey(String key) {studentKeys.remove(key);}

    // Database

    public void put(String prefix) {
        String path = Util.EVENT_ROOT;
        if (key == null || "".equals(key)) {
            key = prefix + "/" + Database.getNewKey(path + "/" + prefix);
        }
        Database.put(path + "/" + key, this);
    }

    public void put() {
        String path = Util.EVENT_ROOT;
        if (key == null || "".equals(key)) {
            key = Database.getNewKey("");
        }
        Database.put(path + "/" + key, this);
    }
}
