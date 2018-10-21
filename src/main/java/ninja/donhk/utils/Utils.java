package ninja.donhk.utils;

import com.sun.javafx.PlatformUtil;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.*;

public class Utils {
    private Utils() {
    }

    public static String resource2txt(String resourceName) throws IOException {
        final ClassLoader classLoader = Utils.class.getClassLoader();
        if (classLoader.getResource(resourceName) == null) {
            throw new IOException("Resource not found " + resourceName);
        }
        StringBuilder sb = new StringBuilder();
        try (InputStream in = classLoader.getResourceAsStream(resourceName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public static void openFileWithExplorer(String target) {

    }


    public static void openPath(String target) {
        Platform platform;
        if (PlatformUtil.isWindows()) {
            platform = new Windows();
        } else if (PlatformUtil.isLinux()) {
            platform = new Linux();
        } else if (PlatformUtil.isMac()) {
            platform = new MacOS();
        } else {
            return;
        }
        platform.openFolder(target);
    }

    public static void copyPathToClipboard(String target) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(target);
        clipboard.setContent(content);
    }
}
