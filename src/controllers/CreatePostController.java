package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import database.PostsDatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CreatePostController extends MainMenuController{

    @FXML
    private ImageView postImageView;

    @FXML
    private TextField postTitleText;

    @FXML
    private TextArea postBodyText;

    @FXML
    private Button postChooseImageButton, postSubmitButton;

    private Image postImage;

    private File postImageFile;

    private PostsDatabaseManager dbManager = new PostsDatabaseManager();
    
    Stage stage;

    public void initialize() {
        try {
            postImage = new Image(getClass().getResourceAsStream("..//static//create_post_menu//default_post_img.png"));
            postImageView.setImage(postImage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseImage(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        postImageFile = fileChooser.showOpenDialog(null);
        postImage = new Image(postImageFile.toURI().toString());
        postImageView.setImage(postImage);
        postImageView.setFitHeight(265);
        postImageView.setFitWidth(360);
        postImageView.setPreserveRatio(false);
    }

    public void submitPost(ActionEvent event){
        String title = postTitleText.getText();
        String body = postBodyText.getText();
        int authorID = CurrentUser.getCurrentUser().getUserID();
        try{
            this.dbManager.insertPost(authorID, title, body, this.postImageFile);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}