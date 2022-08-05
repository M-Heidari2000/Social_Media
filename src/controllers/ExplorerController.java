package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import database.AccountsDatabaseManager;
import controllers.CurrentUser;

public class ExplorerController implements Initializable{
    
    @FXML
    ImageView profileImageView;

    @FXML
    Label usernameLabel;

    private Image profileImg;

    private AccountsDatabaseManager dbManager = new AccountsDatabaseManager();
    private CurrentUser currentUser = CurrentUser.getCurrentUser();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            profileImg = new Image(getClass().getResourceAsStream(currentUser.getImgAddress()));
            profileImageView.setImage(profileImg);
            usernameLabel.setText("Welcome " + currentUser.getFirstName() + "!");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}