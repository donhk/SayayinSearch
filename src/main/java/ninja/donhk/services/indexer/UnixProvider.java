package ninja.donhk.services.indexer;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class UnixProvider implements TargetProvider {

    private final List<File> targets = new ArrayList<>();
    private final List<String> exclusions = new ArrayList<>();
    private final List<String> inclusions = new ArrayList<>();
    private final boolean inverted;

    private UnixProvider(boolean inverted) {
        File[] roots = File.listRoots();
        List<File> rootsFolders = new ArrayList<>(Arrays.asList(roots));
        //C: D: E:
        for (File root : rootsFolders) {
            //C:/a D:/b
            File[] topRootFiles = root.listFiles();
            if (topRootFiles == null) {
                continue;
            }
            if (inverted) {
                exclusions.addAll(Arrays.stream(topRootFiles).map(File::getName).collect(Collectors.toList()));
                targets.addAll(Arrays.asList(topRootFiles));
            } else {
                targets.addAll(Arrays.asList(topRootFiles));
            }

        }
        this.inverted = inverted;
    }

    public static UnixProvider newProvider() {
        return new UnixProvider(false);
    }

    public static UnixProvider newInvertedProvider() {
        return new UnixProvider(true);
    }

    @Override
    public List<File> findTargets() {
        final Set<String> exc;
        if (inverted) {
            exclusions.removeIf(inclusions::contains);
            exc = new HashSet<>(exclusions);
            targets.removeIf(file -> exc.contains(file.getName()));
        } else {
            exc = new HashSet<>(exclusions);
            targets.removeIf(file -> exc.contains(file.getName()));
        }
        return targets;
    }

    @Override
    public void excludeDirs(List<String> list) {
        exclusions.addAll(list);
    }

    @Override
    public List<String> getExclusions() {
        return exclusions;
    }

    @Override
    public void includeDirs(List<String> inc) {
        inclusions.addAll(inc);
    }
}