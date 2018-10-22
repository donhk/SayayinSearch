package ninja.donhk.utils;

import com.sun.javafx.PlatformUtil;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import ninja.donhk.services.database.DBManager;

import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class Utils {
    private static final DecimalFormat formatter = new DecimalFormat("###, ###, ###");

    private Utils() {
    }

    public static String resource2txt(String resourceName) throws IOException {
        final ClassLoader classLoader = Utils.class.getClassLoader();
        if (classLoader.getResource(resourceName) == null) {
            throw new IOException("Resource not found " + resourceName);
        }
        final StringBuilder sb = new StringBuilder();
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
        if (PlatformUtil.isLinux()) {
            openFileLinux(target);
        } else {
            openFile(target);
        }
    }

    public static void openPath(String target) {
        if (PlatformUtil.isLinux()) {
            exploreFileLinux(target);
        } else {
            exploreFile(target);
        }
    }

    public static void copyPathToClipboard(String target) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(target);
        clipboard.setContent(content);
    }

    public static String prepareExpression(String string) {
        string = string.replace(".", "\\.");
        string = string.replace("*", ".*");
        return string;
    }

    public static String getAppFolder() {
        final File home = new File(System.getProperty("user.home") + File.separator + ".saya_search");
        if (!home.exists()) {
            if (!home.mkdirs()) {
                throw new IllegalStateException("Cannot create app folder");
            }
        }
        return home.getAbsolutePath() + File.separator;
    }

    public static void openFile(String target) {
        final Desktop desktop = Desktop.getDesktop();
        try {
            File file = new File(target);
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exploreFile(String target) {
        final Desktop desktop = Desktop.getDesktop();
        try {
            File file = new File(target);
            if (file.isDirectory()) {
                desktop.open(file);
            } else {
                if (file.getParentFile() != null) {
                    desktop.open(file.getParentFile());
                } else {
                    desktop.open(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openFileLinux(String target) {
        final File file = new File(target);
        final ProcessBuilder pb = new ProcessBuilder("sh", "-c", "xdg-open '" + file.getAbsolutePath() + "'");
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exploreFileLinux(String target) {
        final File file = new File(target);
        final ProcessBuilder pb;
        if (file.isDirectory()) {
            pb = new ProcessBuilder("sh", "-c", "xdg-open '" + file.getAbsolutePath() + "'");
        } else {
            if (file.getParentFile() != null) {
                pb = new ProcessBuilder("sh", "-c", "xdg-open '" + file.getParentFile().getAbsolutePath() + "'");
            } else {
                pb = new ProcessBuilder("sh", "-c", "xdg-open '" + file.getAbsolutePath() + "'");
            }
        }
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String totalFilesText() {
        DBManager dbManager = DBManager.getInstance();
        try {
            return formatter.format(dbManager.getTotalRows()) + " files";
        } catch (SQLException e) {
            return "-1";
        }
    }

    public static String totalFilesText(long total) {
        return formatter.format(total) + " matches";
    }
}
