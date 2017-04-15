package com.ds.DukeStudy.objects;

import com.google.firebase.auth.FirebaseAuth;

public class Post {

//  Fields

    private String key = "";
    private String title;
    private String message;
    private String author;
    private String time;
    private String uid;

//	Constructors

//    public Post(String message, String author, String time) {
//        this.title = "NoTitle";
//        this.author = author;
//        this.message = message;
//        this.time = time;
//    }

    public Post(String title, String message, String author) {
        this.title = title;
        this.message = message;
        this.author = author;
        this.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Post() {this("NoMessage", "NoAuthor", "NoTime");}

//	Getters

    public String getKey() {return key;}
    public String getTitle() {return title;}
    public String getMessage() {return message;}
    public String getAuthor() {return author;}
    public String getTime() {return time;}
    public String getUid(){return uid;}
//	Setters

    public void setKey(String key) {this.key = key;}
    public void setTitle(String title) {this.title = title;}
    public void setMessage(String message) {this.message = message;}
    public void setAuthor(String author) {this.author = author;}
    public void setTime(String time) {this.time = time;}
    public void setUid(String uid){this.uid = uid;}
//  Database

    public void put() {
        String path = "posts";
        if (key == null || "".equals(key)) {
            key = Database.getNewKey(path);
        }
        Database.put(path, key, this);
    }
}
