package ninja.donhk.services.indexer;

import ninja.donhk.services.database.DBManager;

import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;
import java.util.concurrent.Callable;

public class IndexWorker implements Callable<Void> {

    private final Path path;
    private final DBManager dbManager;

    public IndexWorker(Path path, DBManager dbManager) {
        this.path = path;
        this.dbManager = dbManager;
    }

    @Override
    public Void call() throws Exception {
        final Stack<Path> stack = new Stack<>();
        stack.push(path);
        while (!stack.empty()) {
            final Path currentFile = stack.pop();
            if (Files.isDirectory(currentFile)) {
                dbManager.insertFile(currentFile.toAbsolutePath().toString(), currentFile.getFileName().toString());
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentFile)) {
                    for (Path path : directoryStream) {
                        stack.push(path);
                    }
                } catch (Exception e) {
                    //ignored
                }
            } else {
                dbManager.insertFile(currentFile.toAbsolutePath().toString(), currentFile.getFileName().toString());
            }
        }
        return null;
    }
}
