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
import controllers.MyPostsController;
import javafx.fxml.FXMLLoader;

public class PostsDatabaseManager {
    
    private Database database;

    public PostsDatabaseManager(){
        this.database = new Database();
    }

    public void getAllPosts(int authorID, FXMLLoader loader) throws SQLException, IOException{

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
    
                myPostsController.addPost(title, body, imageAddressRlt, lastEdited);
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

}
