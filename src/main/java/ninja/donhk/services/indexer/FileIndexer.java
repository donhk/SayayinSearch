package ninja.donhk.services.indexer;

import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileIndexer {

    private static FileIndexer instance = null;
    private final List<File> cacheFiles = new ArrayList<>();
    private long totalFiles = 0;
    private final TargetProvider provider;

    private FileIndexer(TargetProvider provider) {
        this.provider = provider;
    }

    public static FileIndexer newInstance(TargetProvider provider) {
        if (instance == null) {
            return new FileIndexer(provider);
        }
        return instance;
    }

    public void loadFiles() {
        List<File> pathsToScan = provider.findTargets();
        for (int i = 0; i < pathsToScan.size(); i++) {
            try {
                FileWriter storage = createCacheFile(i);
                scanFolder8(Paths.get(pathsToScan.get(i).toURI()), storage);
                storage.close();
            } catch (IOException e) {
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

    private FileWriter createCacheFile(int idx) {
        try {
            String path = System.getProperty("java.io.tmpdir") + File.separator + "saya" + idx;
            File cacheFile = new File(path);
            cacheFiles.add(cacheFile);
            return new FileWriter(cacheFile);
        } catch (IOException e) {
            throw new InvalidStateException("It was not possible to create the cache file");
        }
    }

    private void scanFolder8(Path file, FileWriter storage) throws IOException {
        Stack<Path> stack = new Stack<>();
        stack.push(file);
        while (!stack.empty()) {
            Path file1 = stack.pop();
            if (Files.isDirectory(file1)) {
                totalFiles++;
                storage.write(file1.toAbsolutePath().toString() + System.lineSeparator());
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(file1)) {
                    for (Path path : directoryStream) {
                        stack.push(path);
                    }
                } catch (AccessDeniedException e) {
                    //ignored
                }
            } else {
                storage.write(file1.toAbsolutePath().toString() + System.lineSeparator());
                totalFiles++;
            }
        }
    }

}
