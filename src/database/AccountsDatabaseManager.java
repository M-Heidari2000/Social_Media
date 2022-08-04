package database;

import java.sql.Timestamp;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountsDatabaseManager {
    
    private Database database;

    public AccountsDatabaseManager(){
        this.database = new Database();
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
