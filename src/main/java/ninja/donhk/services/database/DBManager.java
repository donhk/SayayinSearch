package ninja.donhk.services.database;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import ninja.donhk.utils.Utils;


public class DBManager {

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
        final String sql = "select path, name from files where REGEXP_LIKE(name,?) order by hints,name limit 500 ";
        final PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, expression);
        final ResultSet r = ps.executeQuery();
        while (r.next()) {
            m.put(r.getString("path"), r.getString("name"));
        }
        return m;
    }

    public long searchWithRegexTotal(String expression) throws SQLException {
        final String sql = "select count(1) total from files where REGEXP_LIKE(name,?)";
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
        final String sql = "select path, name from files where lower(name) like ? order by hints,name limit 500";
        final PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + target.toLowerCase() + "%");
        final ResultSet r = ps.executeQuery();
        while (r.next()) {
            m.put(r.getString("path"), r.getString("name"));
        }
        return m;
    }

    public long searchWithOutRegexTotal(String target) throws SQLException {
        target = target
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
        final String sql = "select count(1) total from files where lower(name) like ? ";
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
            final String sql1 = "select path, name from files order by hints,name";
            ps = conn.prepareStatement(sql1);
        } else {
            final String sql1 = "select path, name from files order by hints,name limit ?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, rows);
        }

        final ResultSet r = ps.executeQuery();
        while (r.next()) {
            m.put(r.getString("path"), r.getString("name"));
        }

        ps.close();
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

        final boolean tableExists;
        final String queryCheck = "SELECT count(*) count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'FILES';";
        final PreparedStatement ps = conn.prepareStatement(queryCheck);
        final ResultSet r = ps.executeQuery();
        if (r.next()) {
            tableExists = r.getInt("count") == 1;
        } else {
            tableExists = false;
        }

        if (tableExists) {
            //System.out.println("The table exists, nothing to do");
        } else {
            Statement stmt = conn.createStatement();
            stmt.execute(Utils.resource2txt("sql/schema.sql"));
            stmt.closeOnCompletion();
        }

    }

    public void updateHints(String target) {
        long cVal = 1;
        try (PreparedStatement total = conn.prepareStatement("select hints from files where path=?")) {
            total.setString(1, target);
            ResultSet rs = total.executeQuery();
            if (rs.next()) {
                cVal = rs.getLong("hints");
            }
        } catch (SQLException p) {
            p.printStackTrace();
        }
        cVal++;
        try (PreparedStatement psUpd = conn.prepareStatement("update files set hints=? where path=?")) {
            psUpd.setLong(1, cVal);
            psUpd.setString(2, target);
            psUpd.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
