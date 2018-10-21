package ninja.donhk.services.database;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseServer {

    private final Logger logger = LoggerFactory.getLogger(DatabaseServer.class);
    private final String user;
    private final String password;
    private final String database;
    private Connection conn = null;
    private Server webServer = null;
    private Server tcpServer = null;

    public DatabaseServer(String user, String password, String database) {
        this.user = user;
        this.password = password;
        this.database = database;

    }

    public void startServer() throws Exception {
        new Thread(() -> {
            try {
                webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082");
                webServer.start();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                tcpServer = Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "9094");
                tcpServer.start();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        //wait until the servers are created
        while (tcpServer == null || webServer == null) {
            Thread.sleep(40);
        }
    }

    public Connection getConnection() throws SQLException {
        String url = "jdbc:h2:" + tcpServer.getURL() + database + ";LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0;DB_CLOSE_ON_EXIT=FALSE";
        logger.info("url: " + url);
        conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public void stopServer() {
        if (tcpServer != null) {
            logger.info("stopping tcpServer");
            logger.info("tcpServer: " + tcpServer.getStatus());
            tcpServer.stop();
            logger.info("tcpServer: " + tcpServer.getStatus());
        }

        if (webServer != null) {
            logger.info("stopping webServer");
            logger.info("webServer: " + webServer.getStatus());
            webServer.stop();
            logger.info("webServer: " + webServer.getStatus());
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                //ignored
            }
        }

    }
}
