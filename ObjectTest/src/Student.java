// package com.ds.DukeStudy;

import java.util.ArrayList;

public class Student {

//  Fields

    private String name;
    private String email;
    private String major;
    private String gradYear;
    private ArrayList<String> courseIds;
    private ArrayList<String> groupIds;
    private ArrayList<String> eventIds;

//	Constructors

    public Student(String email)
    {
        this.name = "";
        this.email = email;
        this.major = "";
        this.gradYear = "";
        this.courseIds = new ArrayList<String>();
        this.groupIds = new ArrayList<String>();
        this.eventIds = new ArrayList<String>();
    }

//	Getters

    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getMajor() {return major;}
    public String getGradYear() {return gradYear;}
    public ArrayList<String> getCourseIds() {return courseIds;}
    public ArrayList<String> getGroupIds() {return groupIds;}
    public ArrayList<String> getEventIds() {return eventIds;}

//	Setters

    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setMajor(String major) {this.major = major;}
    public void setGradYear(String gradYear) {this.gradYear = gradYear;}

//  Mutators

    public void addCourse(String id) {
        if (!courseIds.contains(id)) {
        	courseIds.add(id);
        }
    }
    
    public void addGroup(String id) {
        if (!groupIds.contains(id)) {
        	groupIds.add(id);
        }
    }
    
    public void addEvent(String id) {
        if (!eventIds.contains(id)) {
        	eventIds.add(id);
        }
    }

    public void removeCourse(String id) {courseIds.remove(id);}
    public void removeGroup(String id) {groupIds.remove(id);}
    public void removeEvent(String id) {eventIds.remove(id);}

}