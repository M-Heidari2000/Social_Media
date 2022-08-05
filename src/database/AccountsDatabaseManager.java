package database;

import java.sql.Timestamp;
import controllers.CurrentUser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import form_exception.InvalidDataException;

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
            

            File imgFile = new File("..//static//temp//profile_image_temp.jpg");
            try (FileOutputStream fos = new FileOutputStream(imgFile)){
                int imgLen = resultSet.getInt("img_len");
                byte[] buf = resultSet.getBytes("profile_img");
                fos.write(buf, 0, imgLen);
                this.currentUser.setImgAddress("..//static//temp//profile_image_temp.jpg");
            }
            catch (Exception e) {
                this.currentUser.setImgAddress("..//static//profile_img_default.png");
            }
        }

    }

    public void authenticate(String username, String password) throws SQLException, InvalidDataException{
        int user_id = -1;
        String AUTHENTICATE_SQL = "SELECT user_id FROM accounts WHERE username = ? AND password = ?";
        Connection conn = this.database.connect();
        PreparedStatement preparedStatement = conn.prepareStatement(AUTHENTICATE_SQL);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        boolean exists = false;
        while(resultSet.next()){
            exists = true;
            user_id = resultSet.getInt("user_id");
        }
        if (!exists){
            throw new InvalidDataException("Invalid username or password");
        }
        populateCurrentUser(user_id);
        conn.close();
        resultSet.close();
    }

    public void update() throws SQLException{
        String UPDATE_ACCOUNTS_SQL = "UPDATE accounts SET last_login = ? WHERE user_id = ?";
        Connection conn = this.database.connect();
        PreparedStatement ps = conn.prepareStatement(UPDATE_ACCOUNTS_SQL);
        ps.setTimestamp(1, this.currentUser.getLastLogin());
        ps.setInt(2, this.currentUser.getUserID());
        ps.executeUpdate();
        conn.close();
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
}
