package ninja.donhk.service;

import java.io.File;
import java.util.List;

public interface TargetProvider {

    List<File> findTargets();

    void excludeDirs(List<String> exclusions);

    List<String> getExclusions();

    void includeDirs(List<String> exclusions);
}
