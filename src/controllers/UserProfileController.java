package controllers;

import java.sql.SQLException;
import java.sql.Timestamp;

import database.AccountsDatabaseManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class UserProfileController extends MainMenuController{
    
    @FXML
    ImageView userProfileImageView;
    
    @FXML
    Hyperlink userProfileUsernameLabel;

    @FXML
    Label userProfileBioLabel, userFirstNameLabel, userLastNameLabel, userEmailLabel;
    
    @FXML
    Button followButton;

    @FXML
    VBox followVBox;

    private AccountsDatabaseManager dbManager = new AccountsDatabaseManager();

    public void showProfile(int followeeID, String username, String email, String firstName, String lastName, String bio, String accType, String imgAddress, Timestamp lastLogin) throws SQLException{
        Image profileImage = new Image(getClass().getResourceAsStream(imgAddress));
        userProfileImageView.setImage(profileImage);
        userProfileBioLabel.setText(bio);
        userProfileUsernameLabel.setText(username);
        userFirstNameLabel.setText(firstName);
        userLastNameLabel.setText(lastName);
        userEmailLabel.setText(email);
        Button followButton = new Button();
        followVBox.setPadding(new Insets(10, 0, 0, 0));
        followButton.setId(Integer.toString(followeeID));

        boolean isFollowing = dbManager.isFollowed(CurrentUser.getCurrentUser().getUserID(), followeeID);

        if (isFollowing){
            followButton.setText("unfollow");
        }
        else{
            followButton.setText("follow");
        }

        followVBox.getChildren().add(followButton);

        followButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int followeeID = Integer.parseInt(((Control)event.getSource()).getId());
                try {
                    boolean isFollowing = dbManager.isFollowed(CurrentUser.getCurrentUser().getUserID(), followeeID);
                    if (isFollowing){
                        dbManager.unfollow(CurrentUser.getCurrentUser().getUserID(), followeeID);
                        followButton.setText("follow");
                    }
                    else{
                        dbManager.follow(CurrentUser.getCurrentUser().getUserID(), followeeID);
                        followButton.setText("unfollow");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Database error");
                    alert.setHeaderText("You cannot follow yourself!");
                    alert.showAndWait();
                }
            }
        });
    }
}
