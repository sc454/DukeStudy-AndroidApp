package com.ds.DukeStudy;

import java.util.Arrays;
import java.util.List;

public class Post {

//  Fields

    private String key = "";
    private String message;
    private String author;
    private String time;

//	Constructors

    public Post(String message, String author, String time) {
        this.author = author;
        this.message = message;
        this.time = time;
    }

    public Post() {this("NoMessage", "NoAuthor", "NoTime");}

//	Getters

    public String getKey() {return key;}
    public String getMessage() {return message;}
    public String getAuthor() {return author;}
    public String getTime() {return time;}

//	Setters

    public void setKey(String key) {this.key = key;}
    public void setMessage(String message) {this.message = message;}
    public void setAuthor(String author) {this.author = author;}
    public void setTime(String time) {this.time = time;}

//  Database

    public void put() {
        String path = "posts";
        if (key == null || "".equals(key)) {
            key = Database.getNewKey(path);
        }
        Database.put(path, key, this);
    }
}
