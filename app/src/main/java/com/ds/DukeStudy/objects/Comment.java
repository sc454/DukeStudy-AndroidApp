package com.ds.DukeStudy.objects;

//import com.google.firebase.database.IgnoreExtraProperties;

//@IgnoreExtraProperties
public class Comment {

    // Fields

    private String key;
    private String studentKey;
    private String author;
    private String text;

    // Constructors

    public Comment() {}

    public Comment(String studentKey, String author, String text) {
        this.studentKey = studentKey;
        this.author = author;
        this.text = text;
    }

    // Getters

    public String getKey() {return key;}
    public String getAuthor() {return author;}
    public String getText() {return text;}
    public String getStudentKey() {return studentKey;}

    // Setters

    public void setKey(String key) {this.key = key;}
    public void setAuthor(String author) {this.author = author;}
    public void setText(String text) {this.text = text;}
    public void setStudentKey(String uid){this.studentKey = uid;}

    // Database

    public void put(String path) {
//  String path = "posts";
        if (key == null || "".equals(key)) {
            key = Database.getNewKey(path);
        }
        Database.put(path, key, this);
    }
}
