package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import database.AccountsDatabaseManager;
import form_exception.InvalidDataException;

public class SettingsController extends MainMenuController{
    
    @FXML
    TextArea profileBioText;

    @FXML
    TextField currentPasswordText, newPasswordText, confirmNewPasswordText;

    @FXML
    Label usernameLabel;

    @FXML
    ImageView profileImageView;

    private AccountsDatabaseManager dbManager = new AccountsDatabaseManager();
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initializeElements(FXMLLoader loader){
    }

    public void updateBio(ActionEvent event){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Update Bio");
        alert.setHeaderText("Bio updated successfully");
        String newBio = profileBioText.getText();
        try{
            dbManager.updateBio(CurrentUser.getCurrentUser().getUserID(), newBio);
            profileBioText.clear();
            alert.showAndWait();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void changePassword(ActionEvent event){

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Change Password");

        try{
            String oldPassword = currentPasswordText.getText();
            String newPassword = newPasswordText.getText();
            String confirmNewPassword = confirmNewPasswordText.getText();
            this.dbManager.changePassword(CurrentUser.getCurrentUser().getUserID(), oldPassword, newPassword, confirmNewPassword);
            alert.setHeaderText("Password changed successfully");
            alert.showAndWait();
        }
        catch(SQLException | InvalidDataException e){
            alert.setHeaderText("Wrong password. Please try again");
            alert.showAndWait();
        }
    }

    public void chooseProfileImage(ActionEvent event) throws FileNotFoundException{
        FileChooser fileChooser = new FileChooser();
        File profileImageFile = fileChooser.showOpenDialog(null);
        Image profileImage = new Image(profileImageFile.toURI().toString());
        profileImageView.setImage(profileImage);
        try{
            dbManager.updateProfileImage(CurrentUser.getCurrentUser().getUserID(), profileImageFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteAccount(ActionEvent event) throws SQLException, IOException{
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("You're about to delete your account!");
        alert.setContentText("Are you sure you want to delete your account?");

        if (alert.showAndWait().get() == ButtonType.OK){
            dbManager.delete(CurrentUser.getCurrentUser().getUserID());
            this.gotoLoginPage(event);
        }
    }

    public void gotoLoginPage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("..//scenes//login_page.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
