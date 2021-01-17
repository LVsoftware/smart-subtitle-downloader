package io.github.vitalivu.subdownloader.name;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimpleFileNameExtractorTest {

  private static final String FILE_NAME =
      "Love.And.Monsters.2020.1080p.BluRay.x264.AAC5.1-[YTS.MX].mp4";

  @Test
  void toExtension() {
    SimpleFileNameExtractor extractor = new SimpleFileNameExtractor();
    Assertions.assertEquals("mp4", extractor.toExtension(FILE_NAME), "toExtension not matches");
  }

  @Test
  void toYear() {
    SimpleFileNameExtractor extractor = new SimpleFileNameExtractor();
    Assertions.assertEquals(2020, extractor.toYear(FILE_NAME), "toYear not matches");
  }

  @Test
  void toTitle() {
    SimpleFileNameExtractor extractor = new SimpleFileNameExtractor();
    Assertions.assertEquals("Love And Monsters", extractor.toTitle(FILE_NAME),
        "toTitle not matches");
  }
}