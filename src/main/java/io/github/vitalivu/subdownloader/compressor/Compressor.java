package io.github.vitalivu.subdownloader.compressor;

import java.io.File;
import java.util.Optional;

public interface Compressor {
  void cleanup(File compressedFile);

  Optional<File> decompress(File compressedFile);
}
