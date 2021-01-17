package io.github.vitalivu.subdownloader.compressor;

import java.io.File;
import java.util.Optional;

public class RarCompressor implements Compressor {
  public static final String EXTENSION = "rar";
  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(RarCompressor.class);

  @Override
  public void cleanup(File compressedFile) {

  }

  @Override
  public Optional<File> decompress(File file) {
    return Optional.empty();
  }
}
