import ninja.donhk.services.indexer.FileIndexer;
import ninja.donhk.services.indexer.TargetProvider;
import ninja.donhk.services.indexer.UnixProvider;
import org.junit.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.SECONDS;


public class FileIndexerTest {
    @Test
    public void test1() {
        TargetProvider provider = UnixProvider.newProvider();
        provider.excludeDirs(Arrays.asList(
                "mnt",
                "bin",
                "vmlinuz.old",
                "var",
                "swapfile",
                "usr",
                "dev",
                "lib",
                "root",
                "lost+found",
                "snap",
                "sbin",
                "sys",
                "swapfile",
                "svr",
                "boot",
                "lib64",
                "run",
                "vmlinuz",
                "vmlinuz.old",
                "initrd.img.old",
                "initrd.img",
                "proc",
                "srv",
                "etc"
        ));
        FileIndexer indexer = FileIndexer.newInstance(provider);
        LocalTime a = LocalTime.now();
        System.out.println(a);
        indexer.loadFiles();
        LocalTime b = LocalTime.now();
        System.out.println(SECONDS.between(a, b));
        System.out.println("total files " + indexer.indexedFiles());
        System.out.println("cache files " + indexer.getCacheFiles().size());
        System.out.println(LocalTime.now());
    }

    @Test
    public void test2() {
        TargetProvider provider = UnixProvider.newProvider();
        provider.excludeDirs(Arrays.asList(
                "mnt",
                "bin",
                "vmlinuz.old",
                "var",
                "swapfile",
                "usr",
                "dev",
                "lib",
                "root",
                "lost+found",
                "snap",
                "sbin",
                "sys",
                "swapfile",
                "svr",
                "boot",
                "lib64",
                "run",
                "vmlinuz",
                "vmlinuz.old",
                "initrd.img.old",
                "initrd.img",
                "proc",
                "srv",
                "etc"
        ));
        System.out.println(provider.findTargets());
        System.out.println(provider.getExclusions());
    }

    @Test
    public void test3() {
        TargetProvider provider = UnixProvider.newInvertedProvider();
        provider.includeDirs(Arrays.asList("home", "tmp", "mnt", "etc", "opt", "srv", "bin", "var", "dev", "lib", "lib64", "run", "snap", "sbin", "lost+found"));
        FileIndexer indexer = FileIndexer.newInstance(provider);
        LocalTime a = LocalTime.now();
        System.out.println(a);
        indexer.loadFiles();
        LocalTime b = LocalTime.now();
        System.out.println(SECONDS.between(a, b));
        System.out.println("total files " + indexer.indexedFiles());
        System.out.println("cache files " + indexer.getCacheFiles().size());
        System.out.println(LocalTime.now());
    }
}
