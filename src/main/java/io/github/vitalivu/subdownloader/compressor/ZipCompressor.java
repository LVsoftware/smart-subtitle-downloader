package io.github.vitalivu.subdownloader.compressor;

import io.github.vitalivu.subdownloader.fs.FileSystem;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipCompressor implements Compressor {
  private static final Logger LOG = LoggerFactory.getLogger(ZipCompressor.class);
  private static final int BUFFER_SIZE = 32768;

  public static final String EXTENSION = "zip";
  public static final String TMP_FOLDER = ".tmpzip";
  private FileSystem fileSystem;

  public ZipCompressor(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  public void cleanup(File compressedFile) {
    fileSystem.delete(new File(TMP_FOLDER, compressedFile.getName()));
    fileSystem.delete(compressedFile);
  }

  @Override
  public Optional<File> decompress(File compressedFile) {
    if (!isZip(compressedFile)) {
      return Optional.empty();
    }

    File destDir = fileSystem.createExtractingFolder(TMP_FOLDER, compressedFile.getName());
    LOG.info("unzipping file {} to {}", destDir.getPath(), compressedFile.getPath());
    byte[] bytesIn = new byte[BUFFER_SIZE];

    ZipInputStream zipIn = null;
    try {
      zipIn = new ZipInputStream(new FileInputStream(compressedFile));
      ZipEntry entry;

      while ((entry = zipIn.getNextEntry()) != null) {
        File f = new File(destDir, entry.getName());
        if (!entry.isDirectory()) {
          f.createNewFile();
          try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))) {
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
              bos.write(bytesIn, 0, read);
            }
          }

        } else {
          f.mkdir();
        }
        zipIn.closeEntry();
      }
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    } finally {
      if (zipIn != null) {
        try {
          zipIn.close();
        } catch (IOException ok) {
        }
      }
    }
    List<File> subtitles = new ArrayList<>();
    fileSystem.searchFileHasExtension(destDir.listFiles(), subtitles, "srt", "ass");
    if (!subtitles.isEmpty()) {
      LOG.info("subtitle is available: {}", subtitles);
      return Optional.ofNullable(subtitles.get(0));
    }
    return Optional.empty();
  }

  private boolean isZip(File file) {
    int fileSignature = 0;
    try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
      fileSignature = raf.readInt();
    } catch (IOException ok) {
    }
    return fileSignature == 0x504B0304 || fileSignature == 0x504B0506 ||
        fileSignature == 0x504B0708;
  }
}
