package io.github.vitalivu.subdownloader.fs;

import java.io.File;
import java.util.List;

public interface FileSystem {

  default File createExtractingFolder(String folderPath, String file) {
    File dir = new File(String.format("%s%s%s", folderPath, File.separator, file));
    dir.mkdirs();
    return dir;
  }

  default void searchFileHasExtension(File[] sources, List<File> results, String... extensions) {
    for (File f : sources) {
      if (f.isFile()) {
        for (String extension : extensions) {
          if (f.getName().toLowerCase().endsWith(extension)) {
            results.add(f);
          }
        }
      } else if (f.isDirectory()) {
        searchFileHasExtension(f.listFiles(), results, extensions);
      }
    }
  }

  default void delete(File path) {
    if (path.isFile()) {
      path.delete();
    } else if (path.isDirectory()) {
      for (File f : path.listFiles()) {
        delete(f);
      }
      path.delete();
    }
  }
}
