package com.ds.DukeStudy.objects;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

// TODO: mimic ondatachange method in ProfileFragment ln 92

public class Student {

//  Fields

    private String key = "";
    private String name;
    private String email;
    private String major;
    private String gradYear;
    private String profileUrl;
    private ArrayList<String> courseKeys = new ArrayList<String>();
    private ArrayList<String> groupKeys = new ArrayList<String>();
    private ArrayList<String> eventKeys = new ArrayList<String>();

//	Constructors

    public Student(String name, String email, String major, String gradYear) {
        this.key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.name = name;
        this.email = email;
        this.major = major;
        this.gradYear = gradYear;
        this.profileUrl = "NoUrl";
    }

    public Student(String email) {this("NoName", email, "NoMajor", "NoGradYear");}

    public Student() {this("NoEmail");}

//	Getters

    public String getKey() {return key;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getMajor() {return major;}
    public String getGradYear() {return gradYear;}
    public String getProfileUrl() {return profileUrl;}
    public ArrayList<String> getCourseKeys() {return courseKeys;}
    public ArrayList<String> getGroupKeys() {return groupKeys;}
    public ArrayList<String> getEventKeys() {return eventKeys;}

//	Setters

    public void setKey(String key) {this.key = key;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setMajor(String major) {this.major = major;}
    public void setGradYear(String year) {gradYear = year;}
    public void setProfileUrl(String url){profileUrl = url;}
    public void setCourseKeys(ArrayList<String> keys) {courseKeys = keys;}
    public void setGroupKeys(ArrayList<String> keys) {groupKeys = keys;}
    public void setEventKeys(ArrayList<String> keys) {eventKeys = keys;}

//  Other mutators

    public void addCourseKey(String key) {
        if (!courseKeys.contains(key)) {
            courseKeys.add(key);
        }
    }

    public void addGroupKey(String key) {
        if (!groupKeys.contains(key)) {
            groupKeys.add(key);
        }
    }

    public void addEventKey(String key) {
        if (!eventKeys.contains(key)) {
            eventKeys.add(key);
        }
    }

    public void removeCourseKey(String key) {courseKeys.remove(key);}
    public void removeGroupKey(String key) {groupKeys.remove(key);}
    public void removeEventKey(String key) {eventKeys.remove(key);}

//  Database

    public void put() {
        String path = "students";
        if (key == null || "".equals(key)) {
            key = Database.getNewKey(path);
        }
        Database.put(path, key, this);
    }
}
