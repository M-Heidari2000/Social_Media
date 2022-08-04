package controllers;

public class CurrentUser {
    
    private int userID;
    private static final CurrentUser user = new CurrentUser();

    private CurrentUser(){}

    public static CurrentUser getCurrentUser(){
        return user;
    }

    public void setUserID(int userID){
        this.userID = userID;
    }

    public int getUserID(){
        return this.userID;
    }
}
