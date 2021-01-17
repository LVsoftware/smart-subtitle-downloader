package io.github.vitalivu.subdownloader.name;

import java.io.File;

public abstract class NameSetter {

  public abstract String niceName(String title, Integer year, String language, String extension);

  public File rename(File videoFile, File subtitleFile,
                     String title, Integer year, String languageCode, String extension) {
    File dest = new File(videoFile.getParentFile(), niceName(title, year, languageCode, extension));
    if (dest.exists()) {
      dest.delete();
    }
    subtitleFile.renameTo(dest);
    return dest;
  }
}
