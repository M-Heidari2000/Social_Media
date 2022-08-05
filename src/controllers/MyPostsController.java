package controllers;


import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import database.PostsDatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MyPostsController extends MainMenuController{
    
    @FXML
    private VBox postsVBox;

    private PostsDatabaseManager dbManager = new PostsDatabaseManager();

    public void initialize(FXMLLoader loader) throws SQLException, IOException{
        dbManager.getAllPosts(CurrentUser.getCurrentUser().getUserID(), loader);
    }

    public String shortenBody(String body){
        int limit = 200;
        return body.length() > limit ? (body.substring(0, limit) + " ...") : body;
    }

    public void addPost(String title, String body, String imageAddress, Timestamp lastEdited){
        VBox newVBox = new VBox();
        HBox newHBox = new HBox();
        newVBox.setStyle("-fx-border-width: 2;");
        newVBox.setStyle("-fx-border-color: #000000;");

        Image image = new Image(getClass().getResourceAsStream(imageAddress));
        ImageView newImageView = new ImageView(image);
        newImageView.setFitHeight(100);
        newImageView.setFitWidth(136);
        newImageView.setPreserveRatio(false);

        VBox newVboxTitle = new VBox();
        Hyperlink newHyperLink = new Hyperlink(title);
        newHyperLink.setStyle("-fx-font: 18 arial;");
        Label newBodyLabel = new Label(this.shortenBody(body));
        newBodyLabel.setStyle("-fx-font: 14 arial");
        newVboxTitle.getChildren().add(newHyperLink);
        newVboxTitle.getChildren().add(newBodyLabel);

        Label newVLabel1 = new Label();
        newVLabel1.setPrefHeight(5);
        Label newVLabel2 = new Label();
        newVLabel2.setPrefHeight(5);
        Label newHLabel1 = new Label();
        newHLabel1.setPrefWidth(15);
        Label newHLabel2 = new Label();
        newHLabel2.setPrefWidth(25);

        postsVBox.getChildren().add(newVBox);
        newVBox.getChildren().add(newVLabel1);
        newVBox.getChildren().add(newHBox);

        newHBox.getChildren().add(newHLabel1);
        newHBox.getChildren().add(newImageView);
        newHBox.getChildren().add(newHLabel2);

        newHBox.getChildren().add(newVboxTitle);

        newVBox.getChildren().add(newVLabel2);
    }

}
