package controllers;

public class CurrentUser {
    
    private int userID;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String imgAdress;
    private String bio;
    private String accountType;

    private static final CurrentUser user = new CurrentUser();

    private CurrentUser(){}

    public static CurrentUser getCurrentUser(){
        return user;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setImgAddress(String imgAddress){
        this.imgAdress = imgAddress;
    }

    public String getImgAddress(){
        return this.imgAdress;
    }

    public void setBio(String bio){
        this.bio = bio;
    }

    public String getBio(){
        return this.bio;
    }

    public void setAccountType(String accountType){
        this.accountType = accountType;
    }

    public String setAccountType(){
        return this.accountType;
    }

    public void setUserID(int userID){
        this.userID = userID;
    }

    public int getUserID(){
        return this.userID;
    }
}
