package com.ds.DukeStudy.objects;

import java.util.ArrayList;

public class Student {

//  Fields

    private String key = "";
    private String name;
    private String email;
    private String major;
    private String gradYear;
    private ArrayList<String> courseKeys = new ArrayList<String>();
    private ArrayList<String> groupKeys = new ArrayList<String>();
    private ArrayList<String> eventKeys = new ArrayList<String>();

//	Constructors

    public Student(String name, String email, String major, String gradYear) {
        this.name = name;
        this.email = email;
        this.major = major;
        this.gradYear = gradYear;
    }

//    public Student(String email) {this("NoName", email, "NoMajor", "NoGradYear");}
    public Student(String email) {this("Edit your information below...", email, "", "");}

    public Student() {this("NoEmail");}

//	Getters

    public String getKey() {return key;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getMajor() {return major;}
    public String getGradYear() {return gradYear;}
    public ArrayList<String> getCourseKeys() {return courseKeys;}
    public ArrayList<String> getGroupKeys() {return groupKeys;}
    public ArrayList<String> getEventKeys() {return eventKeys;}

//	Setters

    public void setKey(String key) {this.key = key;}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
    public void setMajor(String major) {this.major = major;}
    public void setGradYear(String year) {gradYear = year;}
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
        String path = Util.STUDENT_ROOT;
        if (key == null || "".equals(key)) {
            key = Database.getUser().getUid();
        }
        Database.put(path + "/" + key, this);
    }

    public void toggleAndPut(Course course) {
        // Get keys
        String cKey = course.getKey();

        // Check
        if (courseKeys.contains(cKey)) {
            courseKeys.remove(cKey);
            course.removeStudentKey(key);
        } else {
            courseKeys.add(cKey);
            course.addStudentKey(key);
        }

        // Update database
        put();
        course.put();
    }

    public void toggleAndPut(Group group) {
        // Get keys
        String gKey = group.getKey();

        // Check
        if (groupKeys.contains(gKey)) {
            groupKeys.remove(gKey);
            group.removeStudentKey(key);
            System.out.println("Removing student " + key + " from " + group.getKey());
        } else {
            groupKeys.add(gKey);
            group.addStudentKey(key);
            System.out.println("Adding student " + key + " from " + group.getKey());
        }

        // Update database
        put();
        group.put();
    }

    public void toggleAndPut(Event event) {
        // Get keys
        String cKey = event.getKey();

        // Check
        if (eventKeys.contains(cKey)) {
            eventKeys.remove(cKey);
            event.removeStudentKey(key);
        } else {
            eventKeys.add(cKey);
            event.addStudentKey(key);
        }

        // Update database
        put();
        event.put();
    }
}
