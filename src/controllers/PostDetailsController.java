package controllers;

import java.sql.Timestamp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PostDetailsController extends MainMenuController{

    @FXML
    Hyperlink postTitleHyperlink;

    @FXML
    ImageView postImageView;

    @FXML
    Label postBodyText;
    
    @Override
    public void initializeElements(FXMLLoader loader){
    }

    public void showPost(String authorName, String title, String body, Timestamp lastEdited, String imageAddress){
        postTitleHyperlink.setText(title);
        Image postImage = new Image(getClass().getResourceAsStream(imageAddress));
        postImageView.setImage(postImage);
        postBodyText.setText(body);
    }
}
