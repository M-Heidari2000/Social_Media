package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    

    private final String url = "jdbc:postgresql://localhost/social_media";
    private final String username = "postgres";
    private final String password = "MiladH134679";

    public Connection connect(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(this.url, this.username, this.password);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return conn;
    }
}
