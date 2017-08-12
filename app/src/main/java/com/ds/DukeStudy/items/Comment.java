package com.ds.DukeStudy.items;

public class Comment {

    // Fields

    private String key = "";
    private String studentKey;
    private String author;
    private String message;

    // Constructors

    public Comment() {}

    public Comment(String studentKey, String author, String text) {
        this.studentKey = studentKey;
        this.author = author;
        this.message = text;
    }

    // Getters

    public String getKey() {return key;}
    public String getAuthor() {return author;}
    public String getMessage() {return message;}
    public String getStudentKey() {return studentKey;}

    // Setters

    public void setKey(String key) {this.key = key;}
    public void setAuthor(String author) {this.author = author;}
    public void setMessage(String text) {this.message = text;}
    public void setStudentKey(String uid){this.studentKey = uid;}

    // Database

    public void put(String prefix) {
        String path = Util.COMMENT_ROOT;
        if (key == null || "".equals(key)) {
            key = prefix + "/" + Database.getNewKey(path);
        }
        Database.put(path + "/" + key, this);
    }

    public void put() {
        if (key == null || "".equals(key)) {
            key = Database.getNewKey("");
        }
        Database.put(key, this);
    }
}
