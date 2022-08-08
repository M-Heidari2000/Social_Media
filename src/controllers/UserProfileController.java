package controllers;

import java.sql.Timestamp;

import javax.swing.Action;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserProfileController extends MainMenuController{
    
    @FXML
    ImageView userProfileImageView;
    
    @FXML
    Hyperlink userProfileUsernameLabel;

    @FXML
    Label userProfileBioLabel, userFirstNameLabel, userLastNameLabel, userEmailLabel;
    
    @FXML
    Button followButton;

    public void showProfile(String username, String email, String firstName, String lastName, String bio, String accType, String imgAddress, Timestamp lastLogin){
        Image profileImage = new Image(getClass().getResourceAsStream(imgAddress));
        userProfileImageView.setImage(profileImage);
        userProfileBioLabel.setText(bio);
        userProfileUsernameLabel.setText(username);
        userFirstNameLabel.setText(firstName);
        userLastNameLabel.setText(lastName);
        userEmailLabel.setText(email);
    }

    public void follow(ActionEvent event){
        
    }
}
