package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import controllers.CurrentUser;
import controllers.ExplorerController;
import controllers.MyPostsController;
import controllers.PostDetailsController;
import javafx.fxml.FXMLLoader;

public class PostsDatabaseManager {
    
    private Database database;

    public PostsDatabaseManager(){
        this.database = new Database();
    }

    public void getAllPosts(FXMLLoader loader) throws SQLException, IOException{

        String SELECT_POSTS_SQL = "SELECT post_id, username, title, body, image, LENGTH(image) AS img_len, last_edited FROM posts "
                                  + "INNER JOIN accounts ON posts.author = accounts.user_id";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(SELECT_POSTS_SQL);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            FileOutputStream fos = null;
            try {
                int postID = rs.getInt("post_id");
                String imageAddressAbs = "D:\\Projects\\OOP\\Social_Media\\src\\static\\temp\\post_image_temp_" +
                                          Integer.toString(postID) + ".png";
                String imageAddressRlt = "..//static//temp//post_image_temp_" + Integer.toString(postID) + ".png";
                File imgFile = new File(imageAddressAbs);
                fos = new FileOutputStream(imgFile);
                ExplorerController explorerController = loader.getController();
        
                String title = rs.getString("title");
                String body = rs.getString("body");
                String authorName = rs.getString("username");
                Timestamp lastEdited = rs.getTimestamp("last_edited");
    
                int imgLen = rs.getInt("img_len");
                byte[] buf = rs.getBytes("image");
                fos.write(buf, 0, imgLen);
    
                explorerController.addPost(postID, title, authorName, body, imageAddressRlt, lastEdited);
                fos.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getAllUserPosts(int authorID, FXMLLoader loader) throws SQLException, IOException{

        String SELECT_POSTS_SQL = "SELECT post_id, title, body, image, LENGTH(image) AS img_len, last_edited FROM posts WHERE author = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(SELECT_POSTS_SQL);
        ps.setInt(1, authorID);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            FileOutputStream fos = null;
            try {
                int postID = rs.getInt("post_id");
                String imageAddressAbs = "D:\\Projects\\OOP\\Social_Media\\src\\static\\temp\\post_image_temp_" +
                                          Integer.toString(postID) + ".png";
                String imageAddressRlt = "..//static//temp//post_image_temp_" + Integer.toString(postID) + ".png";
                File imgFile = new File(imageAddressAbs);
                fos = new FileOutputStream(imgFile);
                MyPostsController myPostsController = loader.getController();
                
                String title = rs.getString("title");
                String body = rs.getString("body");
                Timestamp lastEdited = rs.getTimestamp("last_edited");
    
                int imgLen = rs.getInt("img_len");
                byte[] buf = rs.getBytes("image");
                fos.write(buf, 0, imgLen);
    
                myPostsController.addPost(postID, title, body, imageAddressRlt, lastEdited);
                fos.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertPost(int authorID, String title, String body, File postImageFile) throws SQLException, IOException{
        String INSERT_POSTS_SQL = "INSERT INTO posts (author, title, body, image, last_edited) VALUES (?, ?, ?, ?, ?)";
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(INSERT_POSTS_SQL);
        FileInputStream fis = new FileInputStream(postImageFile);

        ps.setInt(1, authorID);
        ps.setString(2, title);
        ps.setString(3, body);
        ps.setBinaryStream(4, fis);
        ps.setTimestamp(5, currentTimeStamp);

        ps.executeUpdate();
        fis.close();
        conn.close();
    }

    public void getPostDetails(FXMLLoader loader, int postID) throws SQLException{
        String GET_POST_DETAILS_SQL = "SELECT username, title, body, image, LENGTH(image) AS img_len, last_edited FROM posts "
                                      + "INNER JOIN accounts ON posts.author = accounts.user_id WHERE post_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(GET_POST_DETAILS_SQL);
        ps.setInt(1, postID);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            FileOutputStream fos = null;
            try {
                String imageAddressAbs = "D:\\Projects\\OOP\\Social_Media\\src\\static\\temp\\post_image_temp.png";
                String imageAddressRlt = "..//static//temp//post_image_temp_" + Integer.toString(postID) + ".png";
                File imgFile = new File(imageAddressAbs);
                fos = new FileOutputStream(imgFile);
                PostDetailsController postDetailsController = loader.getController();
                
                String title = rs.getString("title");
                String body = rs.getString("body");
                String authorName = rs.getString("username");
                Timestamp lastEdited = rs.getTimestamp("last_edited");
                int imgLen = rs.getInt("img_len");
                byte[] buf = rs.getBytes("image");
                fos.write(buf, 0, imgLen);
                
                postDetailsController.showPost(authorName, title, body, lastEdited, imageAddressRlt);
                fos.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getPostComments(FXMLLoader loader, int postID) throws SQLException{
        String SELECT_COMMENTS_SQL = "SELECT * FROM comments INNER JOIN accounts ON comments.author_id = accounts.user_id WHERE post_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(SELECT_COMMENTS_SQL);
        ps.setInt(1, postID);
        ResultSet rs = ps.executeQuery();
        PostDetailsController postDetailsController = loader.getController();

        while(rs.next()){
            String authorName = rs.getString("username");
            String body = rs.getString("body");
            Timestamp lastEdited = rs.getTimestamp("last_edited");
            postDetailsController.addExistingComments(authorName, body, lastEdited);
        }

        postDetailsController.addNewComment(postID);
    }

    public void insertComment(int postID, String body) throws SQLException{
        String INSERT_COMMENT_SQL = "INSERT INTO comments (author_id, post_id, body, last_edited) VALUES (?, ?, ?, ?)";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(INSERT_COMMENT_SQL);
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        ps.setInt(1, CurrentUser.getCurrentUser().getUserID());
        ps.setInt(2, postID);
        ps.setString(3, body);
        ps.setTimestamp(4, currentTimeStamp);
        ps.executeUpdate();

        conn.close();
        ps.close();
    }
}
