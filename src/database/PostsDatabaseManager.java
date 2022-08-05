package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostsDatabaseManager {
    
    private Database database;

    public PostsDatabaseManager(){
        this.database = new Database();
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
