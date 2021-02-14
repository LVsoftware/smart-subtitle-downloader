package io.github.vitalivu.subdownloader.provider;

import java.io.File;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubsceneProviderTest {

  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(SubsceneProviderTest.class);

  @Test
  void getDownloadLinkFromTitle_exactResult() {
    try {

      SubsceneProvider subsceneProvider = new SubsceneProvider();
      Optional<String> link =
          subsceneProvider.getDownloadLinkFromTitle("love and monsters", "Vietnamese");
      Assertions.assertTrue(link.isPresent());
    } catch (Throwable e) {
      LOG.error(e.getMessage(), e);
      Assertions.fail(e.getMessage());
    }
  }

  @Test
  void getDownloadLinkFromTitle_closeResult() {
    try {

      SubsceneProvider subsceneProvider = new SubsceneProvider();
      Optional<String> link =
          subsceneProvider
              .getDownloadLinkFromTitle("The Hobbit An Unexpected Journey", "Vietnamese");
      Assertions.assertTrue(link.isPresent());
    } catch (Throwable e) {
      LOG.error(e.getMessage(), e);
      Assertions.fail(e.getMessage());
    }
  }

  @Test
  void download() {
    Optional<File> file = null;
    try {
      file = new SubsceneProvider().download(
          "https://subscene.com/subtitles/vietnamese-text/phctL61QlCFwJWZjtP0n1lBL26LxLsAu6ILdtmfHBE9TkDGst2fDPc8t60MlYzBISL_16stm3rwu_rHDp2CM1iK5K2uVC0caX2AIBXX9jzvWcjangKIuTv50G22ou0S80");
      Assertions.assertTrue(file.isPresent());
    } finally {
      //      if (file != null && file.isPresent()) {
      //        file.get().delete();
      //      }
    }
  }
}