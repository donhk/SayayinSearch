package ninja.donhk.services.indexer;

import ninja.donhk.services.database.DBManager;

import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;

public class FileIndexer {

    private static FileIndexer instance = null;
    private final List<File> cacheFiles = new ArrayList<>();
    private long totalFiles = 0;
    private final TargetProvider provider;
    private final DBManager dbManager;

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
    }

    public long indexedFiles() {
        return totalFiles;
    }

    public List<File> getCacheFiles() {
        return cacheFiles;
    }

    private void scanFolder8(Path file) throws IOException, SQLException {
        final Stack<Path> stack = new Stack<>();
        stack.push(file);
        while (!stack.empty()) {
            final Path currentFile = stack.pop();
            if (Files.isDirectory(currentFile)) {
                totalFiles++;
                dbManager.insertFile(currentFile.toAbsolutePath().toString(), currentFile.getFileName().toString());
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentFile)) {
                    for (Path path : directoryStream) {
                        stack.push(path);
                    }
                } catch (AccessDeniedException e) {
                    //ignored
                }
            } else {
                dbManager.insertFile(currentFile.toAbsolutePath().toString(), currentFile.getFileName().toString());
                totalFiles++;
            }
        }
    }

}
