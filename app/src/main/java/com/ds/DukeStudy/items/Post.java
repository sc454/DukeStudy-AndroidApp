package com.ds.DukeStudy.items;

public class Post {

//  Fields

    private String key = "";
    private String title;
    private String message;
    private String author;
    private String studentKey;

//	Constructors

    public Post(String title, String message, String author) {
        this.title = title;
        this.message = message;
        this.author = author;
        this.studentKey = Database.getUser().getUid();
    }

    public Post() {this("NoMessage", "NoAuthor", "NoTime");}

//	Getters

    public String getKey() {return key;}
    public String getTitle() {return title;}
    public String getMessage() {return message;}
    public String getAuthor() {return author;}
    public String getStudentKey(){return studentKey;}

//	Setters

    public void setKey(String key) {this.key = key;}
    public void setTitle(String title) {this.title = title;}
    public void setMessage(String message) {this.message = message;}
    public void setAuthor(String author) {this.author = author;}
    public void setStudentKey(String key){this.studentKey = key;}

//  Database

    public void put(String prefix) {
    String path = Util.POST_ROOT;
        if (key == null || "".equals(key)) {
            key = prefix + "/" + Database.getNewKey(path + "/" + prefix);
        }
        Database.put(path + "/" + key, this);
    }

    public void put() {
        String path = Util.POST_ROOT;
        if (key == null || "".equals(key)) {
            key = Database.getNewKey("");
        }
        Database.put(path + "/" + key, this);
    }
}
