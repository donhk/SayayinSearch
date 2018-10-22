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
        final String sql = "select path, name from files where REGEXP_LIKE(name,?) order by name limit 500 ";
        final PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, expression);
        final ResultSet r = ps.executeQuery();
        while (r.next()) {
            m.put(r.getString("path"), r.getString("name"));
        }
        return m;
    }

    public long searchWithRegexTotal(String expression) throws SQLException {
        final Map<String, String> m = new HashMap<>();
        final String sql = "select count(name) total from files where REGEXP_LIKE(name,?) ";
        final PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, expression);
        final ResultSet r = ps.executeQuery();
        if (r.next()) {
            return r.getLong("total");
        }
        return -1;
    }

    public Map<String, String> searchWithOutRegex(String target) throws SQLException {
        final Map<String, String> m = new HashMap<>();
        target = target
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
        final String sql = "select path, name from files where name like ? order by name limit 500";
        final PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + target + "%");
        final ResultSet r = ps.executeQuery();
        while (r.next()) {
            m.put(r.getString("path"), r.getString("name"));
        }
        return m;
    }

    public long searchWithOutRegexTotal(String target) throws SQLException {
        final Map<String, String> m = new HashMap<>();
        target = target
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
        final String sql = "select count(name) total from files where name like ? ";
        final PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + target + "%");
        final ResultSet r = ps.executeQuery();
        if (r.next()) {
            return r.getLong("total");
        }
        return -1;
    }

    public Map<String, String> getRows(int rows) throws SQLException {
        final Map<String, String> m = new HashMap<>();
        final PreparedStatement ps;

        if (rows == -1) {
            final String sql1 = "select path, name from files";
            ps = conn.prepareStatement(sql1);
        } else {
            final String sql1 = "select path, name from files limit ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, rows);
        }

        final ResultSet r = ps.executeQuery();
        while (r.next()) {
            m.put(r.getString("path"), r.getString("name"));
        }
        return m;
    }

    public int getTotalRows() throws SQLException {
        final String sql1 = "select count(path) total from files";
        final PreparedStatement ps = conn.prepareStatement(sql1);
        final ResultSet r = ps.executeQuery();
        if (r.next()) {
            return r.getInt("total");
        }
        return -1;
    }

    public void loadSchema() throws SQLException, IOException {
        Statement stmt = conn.createStatement();
        stmt.execute(Utils.resource2txt("sql/schema.sql"));
        stmt.closeOnCompletion();
    }
}
