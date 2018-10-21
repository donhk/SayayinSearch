import ninja.donhk.pojos.DBCredentials;
import ninja.donhk.services.database.DBManager;
import ninja.donhk.services.database.DatabaseServer;
import ninja.donhk.services.indexer.FileIndexer;
import ninja.donhk.services.indexer.TargetProvider;
import ninja.donhk.services.indexer.UnixProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.SECONDS;


public class FileIndexerTest {

    private static DBManager dbManager;
    private static DatabaseServer server;

    @BeforeClass
    public static void setup() {
        server = new DatabaseServer(
                DBCredentials.USERNAME.val(),
                DBCredentials.PASSWD.val(),
                DBCredentials.DATABASE.val()
        );
        try {
            server.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Connection connection = server.getConnection();
            dbManager = DBManager.newInstance(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        TargetProvider provider = UnixProvider.newProvider();
        provider.excludeDirs(Arrays.asList(
                "mnt",
                "bin",
                "vmlinuz.old",
                "var",
                "swapfile",
                "usr",
                "dev",
                "lib",
                "root",
                "lost+found",
                "snap",
                "sbin",
                "sys",
                "swapfile",
                "svr",
                "boot",
                "lib64",
                "run",
                "vmlinuz",
                "vmlinuz.old",
                "initrd.img.old",
                "initrd.img",
                "proc",
                "srv",
                "etc"
        ));
        FileIndexer indexer = FileIndexer.newInstance(provider, dbManager);
        LocalTime a = LocalTime.now();
        System.out.println(a);
        indexer.loadFiles();
        LocalTime b = LocalTime.now();
        System.out.println(SECONDS.between(a, b));
        System.out.println("total files " + indexer.indexedFiles());
        System.out.println("cache files " + indexer.getCacheFiles().size());
        System.out.println(LocalTime.now());
    }

    @Test
    public void test2() {
        TargetProvider provider = UnixProvider.newProvider();
        provider.excludeDirs(Arrays.asList(
                "mnt",
                "bin",
                "vmlinuz.old",
                "var",
                "swapfile",
                "usr",
                "dev",
                "lib",
                "root",
                "lost+found",
                "snap",
                "sbin",
                "sys",
                "swapfile",
                "svr",
                "boot",
                "lib64",
                "run",
                "vmlinuz",
                "vmlinuz.old",
                "initrd.img.old",
                "initrd.img",
                "proc",
                "srv",
                "etc"
        ));
        System.out.println(provider.findTargets());
        System.out.println(provider.getExclusions());
    }

    @Test
    public void test3() {
        TargetProvider provider = UnixProvider.newInvertedProvider();
        provider.includeDirs(Arrays.asList("home", "tmp", "mnt", "etc", "opt", "srv", "bin", "var", "dev", "lib", "lib64", "run", "snap", "sbin", "lost+found"));
        FileIndexer indexer = FileIndexer.newInstance(provider, dbManager);
        LocalTime a = LocalTime.now();
        System.out.println(a);
        indexer.loadFiles();
        LocalTime b = LocalTime.now();
        System.out.println(SECONDS.between(a, b));
        System.out.println("total files " + indexer.indexedFiles());
        System.out.println("cache files " + indexer.getCacheFiles().size());
        System.out.println(LocalTime.now());
    }

    @AfterClass
    public static void end() {
        server.stopServer();
    }
}
