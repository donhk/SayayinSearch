package ninja.donhk.services.indexer;

import ninja.donhk.services.database.DBManager;

import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;

public class FileIndexer {

    private static FileIndexer instance = null;
    private final List<File> cacheFiles = new ArrayList<>();
    private long totalFiles = 0;
    private final TargetProvider provider;
    private final DBManager dbManager;
    private boolean finished = false;

    private FileIndexer(TargetProvider provider, DBManager dbManager) {
        this.provider = provider;
        this.dbManager = dbManager;
    }

    public static FileIndexer newInstance(TargetProvider provider, DBManager dbManager) {
        if (instance == null) {
            return new FileIndexer(provider, dbManager);
        }
        return instance;
    }

    public void loadFiles() {
        List<File> pathsToScan = provider.findTargets();
        for (File aPathsToScan : pathsToScan) {
            try {
                scanFolder8(Paths.get(aPathsToScan.toURI()));
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        finished = true;
    }

    public long indexedFiles() {
        return totalFiles;
    }

    public List<File> getCacheFiles() {
        return cacheFiles;
    }

    private void scanFolder8(Path file) throws IOException, SQLException {
        Stack<Path> stack = new Stack<>();
        stack.push(file);
        while (!stack.empty()) {
            Path file1 = stack.pop();
            if (Files.isDirectory(file1)) {
                totalFiles++;
                dbManager.insertFile(file1.toAbsolutePath().toString(), file1.getFileName().toString());
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(file1)) {
                    for (Path path : directoryStream) {
                        stack.push(path);
                    }
                } catch (AccessDeniedException e) {
                    //ignored
                }
            } else {
                dbManager.insertFile(file1.toAbsolutePath().toString(), file1.getFileName().toString());
                totalFiles++;
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
