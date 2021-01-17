package io.github.vitalivu.subdownloader.provider;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public interface SubtitleProvider {
  Optional<String> getDownloadLinkFromTitle(String title, String language) throws IOException;

  Optional<String> getDownloadLinkFromFileName(String fileName, String language);

  Optional<File> download(String link);

  default Optional<File> downloadSubtitle(String title, String language) throws IOException {
    Optional<String> link = getDownloadLinkFromTitle(title, language);
    if (link.isPresent()) {
      return download(link.get());
    }
    return Optional.empty();
  }
}
