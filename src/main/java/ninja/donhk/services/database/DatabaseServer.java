package ninja.donhk.services.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseServer {

    private final String user;
    private final String password;
    private final String database;

    public DatabaseServer(String user, String password, String database) {
        this.user = user;
        this.password = password;
        this.database = database;

    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:h2:" + database + ";LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0;DB_CLOSE_ON_EXIT=FALSE";
        return DriverManager.getConnection(url, user, password);
    }
}
