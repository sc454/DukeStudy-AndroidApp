package com.ds.DukeStudy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Course {

    // Fields

    private String key = "";
    private String title;
    private String department;
    private String code;
    private String instructor;
    private String startTime;
    private String endTime;
    private String location;
    private ArrayList<String> studentKeys = new ArrayList<String>();
    private ArrayList<String> groupKeys = new ArrayList<String>();
    private ArrayList<String> postKeys = new ArrayList<String>();

    // Constructors

    public Course(String title, String department, String code, String instructor) {
        this.title = title;
        this.department = department;
        this.code = code;
        this.instructor = instructor;
        this.startTime = "";
        this.endTime = "";
        this.location = "";
    }

    public Course() {this("NoTitle","NoDepartment","NoCode","NoInstructor");}

    // Getters

    public String getKey() {return key;}
    public String getTitle() {return title;}
    public String getDepartment() {return department;}
    public String getCode() {return code;}
    public String getInstructor() {return instructor;}
    public String getStartTime() {return startTime;}
    public String getEndTime() {return endTime;}
    public String getLocation() {return location;}
    public ArrayList<String> getStudentKeys() {return studentKeys;}
    public ArrayList<String> getGroupKeys() {return groupKeys;}
    public ArrayList<String> getPostKeys() {return postKeys;}

    // Setters

    public void setKey(String key) {this.key = key;}
    public void setTitle(String title) {this.title = title;}
    public void setDepartment(String department) {this.department = department;}
    public void setCode(String code) {this.code = code;}
    public void setInstructor(String instructor) {this.instructor = instructor;}
    public void setStartTime(String startTime) {this.startTime = startTime;}
    public void setEndTime(String endTime) {this.endTime = endTime;}
    public void setLocation(String location) {this.location = location;}
    public void setStudentKeys(ArrayList<String> keys) {studentKeys = keys;}
    public void setGroupKeys(ArrayList<String> keys) {groupKeys = keys;}
    public void setPostKeys(ArrayList<String> keys) {postKeys = keys;}

    // Other mutators

    public void addStudentKey(String key) {
        if (!studentKeys.contains(key)) {
            studentKeys.add(key);
        }
    }

    public void addGroupKey(String key) {
        if (!groupKeys.contains(key)) {
            groupKeys.add(key);
        }
    }

    public void addPostKey(String key) {
        if (!postKeys.contains(key)) {
            postKeys.add(key);
        }
    }

    public void removeStudentKey(String key) {studentKeys.remove(key);}
    public void removeGroupKey(String key) {groupKeys.remove(key);}
    public void removePostKey(String key) {postKeys.remove(key);}

    // Database

    public void put() {
        String path = "courses";
        if (key == null || "".equals(key)) {
            key = Database.getNewKey(path);
        }
        Database.put(path, key, this);
    }
}
