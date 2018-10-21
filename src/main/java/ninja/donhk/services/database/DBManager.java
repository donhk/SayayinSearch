package ninja.donhk.services.database;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import ninja.donhk.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBManager {

    private final Logger logger = LoggerFactory.getLogger(DBManager.class);
    private final Connection conn;
    private static DBManager instance = null;

    private DBManager(Connection conn) {
        this.conn = conn;
    }

    public static DBManager newInstance(Connection connection) {
        if (instance == null) {
            instance = new DBManager(connection);
        }
        return instance;
    }

    public static DBManager getInstance() {
        return instance;
    }

    public void insertFile(String path, String name) throws SQLException {
        final PreparedStatement ps = conn.prepareStatement("insert into files(path,name) values (?,?)");
        ps.setString(1, path);
        ps.setString(2, name);
        ps.executeUpdate();
        ps.close();
    }

    public void truncateFilesTable() throws SQLException {
        final PreparedStatement ps = conn.prepareStatement("truncate files;");
        ps.executeUpdate();
        ps.close();
    }

    public Map<String, String> searchWithRegex(String expression) throws SQLException {
        final Map<String, String> m = new HashMap<>();
        final String sql = "select path, name from files where REGEXP_LIKE(name,?)";
        final PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, expression);
        final ResultSet r = ps.executeQuery();
        while (r.next()) {
            m.put(r.getString("path"), r.getString("name"));
        }
        return m;
    }

    public Map<String, String> getAllFiles() throws SQLException {
        final Map<String, String> m = new HashMap<>();
        final String sql = "select path, name from files";
        final PreparedStatement ps = conn.prepareStatement(sql);
        final ResultSet r = ps.executeQuery();
        while (r.next()) {
            m.put(r.getString("path"), r.getString("name"));
        }
        return m;
    }


    public void loadSchema() throws SQLException, IOException {
        Statement stmt = conn.createStatement();
        stmt.execute(Utils.resource2txt("sql/schema.sql"));
        stmt.closeOnCompletion();
    }
}
