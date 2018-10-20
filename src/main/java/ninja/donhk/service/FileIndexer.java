package ninja.donhk.service;

import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
        for (int i = 0; i < provider.findTargets().size(); i++) {
            try {
                FileWriter storage = createCacheFile(i);
                scanFolder(cacheFiles.get(i), storage);
                storage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long listFiles() {
        return totalFiles;
    }

    public List<File> getCacheFiles() {
        return cacheFiles;
    }

    private FileWriter createCacheFile(int idx) {
        try {
            File cacheFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "saya" + idx);
            cacheFile.deleteOnExit();
            if (!cacheFile.createNewFile()) {
                throw new IOException("Cannot create cache file");
            }
            cacheFiles.add(cacheFile);
            return new FileWriter(cacheFile);
        } catch (IOException e) {
            throw new InvalidStateException("It was not possible to create the cache file");
        }
    }

    private void scanFolder(File file, FileWriter storage) throws IOException {
        Stack<File> stack = new Stack<>();
        stack.push(file);
        while (!stack.empty()) {
            File file1 = stack.pop();
            if (file1.isDirectory()) {
                totalFiles++;
                storage.write(file1.getAbsolutePath() + System.lineSeparator());
                File[] more = file1.listFiles();
                if (more == null) {
                    continue;
                }
                for (File file2 : more) {
                    stack.push(file2);
                }
            } else {
                storage.write(file1.getAbsolutePath() + System.lineSeparator());
                totalFiles++;
            }
        }
    }
}
