package ninja.donhk.services.indexer;

import ninja.donhk.services.database.DBManager;

import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class FileIndexer {

    private static FileIndexer instance = null;
    private final List<File> cacheFiles = new ArrayList<>();
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
        final List<File> pathsToScan = provider.findTargets();
        final ExecutorService executor = Executors.newFixedThreadPool(8);
        final CompletionService<Void> service = new ExecutorCompletionService<>(executor);

        for (File aPathsToScan : pathsToScan) {
            Path path = Paths.get(aPathsToScan.toURI());
            service.submit(new IndexWorker(path, dbManager));
        }

        for (int i = 0; i < pathsToScan.size(); i++) {
            try {
                service.take().get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }

    public long indexedFiles() {
        try {
            return dbManager.getTotalRows();
        } catch (SQLException e) {
            return -1;
        }
    }

    public List<File> getCacheFiles() {
        return cacheFiles;
    }

}
