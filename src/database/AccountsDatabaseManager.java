package database;

import java.sql.Timestamp;
import controllers.CurrentUser;
import controllers.UserProfileController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import form_exception.InvalidDataException;
import javafx.fxml.FXMLLoader;

public class AccountsDatabaseManager {
    
    private Database database;
    private CurrentUser currentUser = CurrentUser.getCurrentUser();

    public AccountsDatabaseManager(){
        this.database = new Database();
    }

    public void populateCurrentUser(int user_id) throws SQLException{
        String GET_IMG_SQL = "SELECT username, email, first_name, last_name, email, profile_img," + 
                             "LENGTH(profile_img) AS img_len, bio, account_type FROM accounts WHERE user_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(GET_IMG_SQL);
        preparedStatement.setInt(1, user_id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

        while(resultSet.next()){
            this.currentUser.setUserID(user_id);
            this.currentUser.setUsername(resultSet.getString("username"));
            this.currentUser.setEmail(resultSet.getString("email"));
            this.currentUser.setFirstName(resultSet.getString("first_name"));
            this.currentUser.setLastName(resultSet.getString("last_name"));
            this.currentUser.setEmail(resultSet.getString("bio"));
            this.currentUser.setAccountType(resultSet.getString("account_type"));
            this.currentUser.setLastLogin(currentTimeStamp);
            this.currentUser.setAuthenticated(true);
            
            File imgFile = new File("D:\\Projects\\OOP\\Social_Media\\src\\static\\temp\\profile_image_temp.png");
            try {
                FileOutputStream fos = new FileOutputStream(imgFile);
                int imgLen = resultSet.getInt("img_len");
                byte[] buf = resultSet.getBytes("profile_img");
                fos.write(buf, 0, imgLen);
                this.currentUser.setImgAddress("..//static//temp//profile_image_temp.png");
                // java.util.concurrent.TimeUnit.SECONDS.sleep(1);
                fos.close();
            }
            catch (Exception e) {
                this.currentUser.setImgAddress("..//static//profile_img_default.png");
            }
        }

    }

    public void changePassword(int userID, String oldPassword, String newPassword, String confirmNewPassword) throws SQLException, InvalidDataException{
        if (this.authenticate(userID, oldPassword) && newPassword.equals(confirmNewPassword)){
            String UPDATE_PASSWORD_SQL = "UPDATE accounts SET password = ? WHERE user_id = ?";
            Connection conn = this.database.connect();
            PreparedStatement ps = conn.prepareStatement(UPDATE_PASSWORD_SQL);
            ps.setString(1, newPassword);
            ps.setInt(2, userID);
            ps.executeUpdate();
            conn.close();
            ps.close();
        }
        else{
            throw new InvalidDataException("Invalid password");
        }
    }

    public boolean authenticate(int userID, String password) throws SQLException{
        String AUTHENTICATE_SQL = "SELECT COUNT(*) AS exists FROM accounts WHERE user_id = ? AND password = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(AUTHENTICATE_SQL);
        ps.setInt(1, userID);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int exists = rs.getInt("exists");
        return exists > 0 ? true : false;
    }

    public void authenticate(String username, String password) throws SQLException, InvalidDataException{
        int userID = -1;
        String AUTHENTICATE_SQL = "SELECT user_id FROM accounts WHERE username = ? AND password = ?";
        Connection conn = this.database.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(AUTHENTICATE_SQL);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        boolean exists = false;
        while(resultSet.next()){
            exists = true;
            userID = resultSet.getInt("user_id");
        }
        if (!exists){
            throw new InvalidDataException("Invalid username or password");
        }
        populateCurrentUser(userID);
        conn.close();
        resultSet.close();
    }

    public void updateLastLogin() throws SQLException{
        String UPDATE_ACCOUNTS_SQL = "UPDATE accounts SET last_login = ? WHERE user_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(UPDATE_ACCOUNTS_SQL);
        ps.setTimestamp(1, this.currentUser.getLastLogin());
        ps.setInt(2, this.currentUser.getUserID());
        ps.executeUpdate();
        conn.close();
        ps.close();
    }

    public void updateProfileImage(int userID, File profileImageFile) throws SQLException, IOException, InterruptedException{
        String UPDATE_PROFILE_IMAGE_SQL = "UPDATE accounts SET profile_img = ? WHERE user_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(UPDATE_PROFILE_IMAGE_SQL);
        FileInputStream fis = new FileInputStream(profileImageFile);
        ps.setBinaryStream(1, fis);
        ps.setInt(2, userID);
        ps.executeLargeUpdate();
        populateCurrentUser(userID);
        fis.close();
        conn.close();
        ps.close();
    }

    public void updateBio(int userID, String newBio) throws SQLException{
        String UPDATE_BIO_SQL = "UPDATE accounts SET bio = ? WHERE user_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(UPDATE_BIO_SQL);
        ps.setString(1, newBio);
        ps.setInt(2, userID);
        ps.executeUpdate();
        conn.close();
        ps.close();
    }

    public void insert(String username, String email, String password, String firstName, String lastName, String accountType) throws IOException, SQLException{
        String INSERT_ACCOUNTS_SQL = "INSERT INTO accounts (username, email, password, first_name, last_name, account_type, created_on) " + 
                                        "VALUES (?, ?, ?, ?, ?, CAST(? AS acc_type), ?);";
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        Connection conn = this.database.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ACCOUNTS_SQL);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, password);
        preparedStatement.setString(4, firstName);
        preparedStatement.setString(5, lastName);
        preparedStatement.setString(6, accountType);
        preparedStatement.setTimestamp(7, currentTimeStamp);
        preparedStatement.executeUpdate();
        conn.close();
    }

    public void getProfile(FXMLLoader loader, String username) throws SQLException, InterruptedException{
        String GET_USER_PROFILE_SQL = "SELECT user_id, username, email, first_name, last_name, profile_img, LENGTH(profile_img) AS img_len" + 
                                      ", bio, last_login, account_type FROM accounts WHERE username = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(GET_USER_PROFILE_SQL);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int userID = rs.getInt("user_id");
            String email = rs.getString("email");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String bio = rs.getString("bio");
            Timestamp lastLogin = rs.getTimestamp("last_login");
            String accType = rs.getString("account_type");
            String imgAddress = "";

            File imgFile = new File("D:\\Projects\\OOP\\Social_Media\\src\\static\\temp\\user_profile_image_temp.png");
            try {
                FileOutputStream fos = new FileOutputStream(imgFile);
                int imgLen = rs.getInt("img_len");
                byte[] buf = rs.getBytes("profile_img");
                fos.write(buf, 0, imgLen);
                // java.util.concurrent.TimeUnit.SECONDS.sleep(1);
                fos.close();
                imgAddress = "..//static//temp//user_profile_image_temp.png";
            }
            catch (Exception e) {
                e.printStackTrace();
                imgAddress = "..//static//profile_img_default.png";
            }
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
            UserProfileController userProfileController = loader.getController();
            userProfileController.showProfile(userID, username, email, firstName, lastName, bio, accType, imgAddress, lastLogin);
        }

        conn.close();
        ps.close();
    }

    public void delete(int userID) throws SQLException{
        String DELETE_ACCOUNTS_SQL = "DELETE FROM accounts WHERE user_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(DELETE_ACCOUNTS_SQL);
        ps.setInt(1,userID);
        ps.executeUpdate();
        conn.close();
    }

    public boolean isFollowed(int followerID, int followeeID) throws SQLException{
        String IS_LIKED_SQL = "SELECT COUNT(*) FROM follows WHERE follower_id = ? AND followee_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(IS_LIKED_SQL);
        ps.setInt(1, followerID);
        ps.setInt(2, followeeID);
        ResultSet rs = ps.executeQuery();
        rs.next();
        if (rs.getInt(1) > 0){
            return true;
        }
        return false;
    }

    public void follow(int followerID, int followeeID) throws SQLException{
        String USER_FOLLOW_SQL = "INSERT INTO follows (follower_id, followee_id, follow_date) VALUES (?, ?, ?)";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(USER_FOLLOW_SQL);
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        ps.setInt(1, followerID);
        ps.setInt(2, followeeID);
        ps.setTimestamp(3, currentTimestamp);
        ps.executeUpdate();

        conn.close();
        ps.close();
    }

    public void unfollow(int followerID, int followeeID) throws SQLException{
        String USER_UNFOLLOW_SQL = "DELETE FROM follows WHERE follower_id = ? AND followee_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(USER_UNFOLLOW_SQL);
        ps.setInt(1, followerID);
        ps.setInt(2, followeeID);
        ps.executeUpdate();

        conn.close();
        ps.close();
    }
}
