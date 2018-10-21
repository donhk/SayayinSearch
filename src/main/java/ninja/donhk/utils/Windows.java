package ninja.donhk.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Windows implements Platform {
    @Override
    public void openFolder(String target) {
        Desktop desktop = Desktop.getDesktop();
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
}
