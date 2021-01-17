package io.github.vitalivu.subdownloader.name;

import java.io.File;

public interface FileNameExtractor {

  default String toExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
  }

  default String toExtension(File file) {
    return toExtension(file.getName());
  }

  Integer toYear(String fileName);

  default Integer toYear(File file) {
    return toYear(file.getName());
  }

  String toTitle(String name);

  default String toTitle(File file) {
    return toTitle(file.getName());
  }
}
