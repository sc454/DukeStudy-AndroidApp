package com.ds.DukeStudy;

/*
 * Created by johnb on 3/27/2017.
 */
import com.google.firebase.database.ServerValue;

import java.util.Map;

public class PostObject {
    String myAuthor;
    String myMessage;
    public PostObject(){
        myAuthor=null;
        myMessage=null;
    }
    public PostObject(String thisName,String thisMessage){
        myAuthor=thisName;
        myMessage=thisMessage;

    }
    public String getmyAuthor( ){
        return myAuthor;
    }
    public String getmyMessage( ){
        return myMessage;
    }

}
