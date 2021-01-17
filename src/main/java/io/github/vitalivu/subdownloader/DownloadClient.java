package io.github.vitalivu.subdownloader;

import io.github.vitalivu.subdownloader.compressor.Compressor;
import io.github.vitalivu.subdownloader.name.NameSetter;
import io.github.vitalivu.subdownloader.provider.SubtitleProvider;
import io.github.vitalivu.subdownloader.name.FileNameExtractor;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class DownloadClient {
  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(DownloadClient.class);

  private SubtitleProvider subtitleProvider;
  private FileNameExtractor fileNameExtractor;
  private Compressor compressor;
  private NameSetter nameSetter;

  public DownloadClient(SubtitleProvider subtitleProvider, FileNameExtractor fileNameExtractor,
                        Compressor compressor, NameSetter nameSetter) {
    this.subtitleProvider = subtitleProvider;
    this.fileNameExtractor = fileNameExtractor;
    this.compressor = compressor;
    this.nameSetter = nameSetter;
  }

  public Optional<File> downloadSubtitle(File videoFile) throws IOException {
    LOG.info("Downloading subtitle for {}", videoFile.getName());
    Optional<File> subtitleFile = downloadSubtitle(videoFile, "Vietnamese", "vie");
    if (!subtitleFile.isPresent()) {
      subtitleFile = downloadSubtitle(videoFile, "English", "eng");
    }
    if (subtitleFile.isPresent()) {
      LOG.info("Downloaded subtitle, saved to {}", subtitleFile.get());
    } else {
      LOG.info("Not found subtitle for {}", videoFile.getName());
    }
    return subtitleFile;
  }

  public Optional<File> downloadSubtitle(File videoFile, String language, String languageCode)
      throws IOException {
    String title = fileNameExtractor.toTitle(videoFile);
    Integer year = fileNameExtractor.toYear(videoFile);

    LOG.info("searching subtitle for {} (released {}?)", title, year);

    Optional<File> compressedSubtitle = subtitleProvider.downloadSubtitle(title, language);
    if (!compressedSubtitle.isPresent()) {
      return Optional.empty();
    }

    Optional<File> subtitleFile = compressor.decompress(compressedSubtitle.get());
    if (!subtitleFile.isPresent()) {
      return Optional.empty();
    }

    String extension = fileNameExtractor.toExtension(subtitleFile.get());

    File renamedFile = nameSetter.rename(videoFile,
        subtitleFile.get(), title, year, languageCode, extension);
    compressor.cleanup(compressedSubtitle.get());
    return Optional.of(renamedFile);

  }
}
