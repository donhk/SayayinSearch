package ninja.donhk.utils;

import java.io.File;
import java.io.IOException;

public class Linux implements Platform {
    @Override
    public void openFolder(String target) {
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

    @Override
    public void openFile(String target) {
        final File file = new File(target);
        final ProcessBuilder pb = new ProcessBuilder("sh", "-c", "xdg-open '" + file.getAbsolutePath() + "'");
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
