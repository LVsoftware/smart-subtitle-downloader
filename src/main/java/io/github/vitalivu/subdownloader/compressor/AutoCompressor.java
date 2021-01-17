package io.github.vitalivu.subdownloader.compressor;

import io.github.vitalivu.subdownloader.fs.FileSystem;
import io.github.vitalivu.subdownloader.name.FileNameExtractor;
import java.io.File;
import java.util.Optional;

public class AutoCompressor implements Compressor {
  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(AutoCompressor.class);

  public AutoCompressor(FileNameExtractor fileNameExtractor, FileSystem fileSystem) {
    this.fileNameExtractor = fileNameExtractor;
    this.zipCompressor = new ZipCompressor(fileSystem);
    this.rarCompressor = new RarCompressor();
  }

  private FileNameExtractor fileNameExtractor;
  private ZipCompressor zipCompressor;
  private RarCompressor rarCompressor;

  @Override
  public Optional<File> decompress(File compressedFile) {
    String extension = fileNameExtractor.toExtension(compressedFile);
    switch (extension) {
      case RarCompressor.EXTENSION:
        return rarCompressor.decompress(compressedFile);
      case ZipCompressor.EXTENSION:
        return zipCompressor.decompress(compressedFile);
      default:
        throw new UnsupportedOperationException("Compression type is not supported: " + extension);
    }
  }

  @Override
  public void cleanup(File compressedFile) {
    String extension = fileNameExtractor.toExtension(compressedFile);
    switch (extension) {
      case RarCompressor.EXTENSION:
        rarCompressor.cleanup(compressedFile);
        break;
      case ZipCompressor.EXTENSION:
        zipCompressor.cleanup(compressedFile);
        break;
      default:
        throw new UnsupportedOperationException("Compression type is not supported: " + extension);
    }
  }
}
