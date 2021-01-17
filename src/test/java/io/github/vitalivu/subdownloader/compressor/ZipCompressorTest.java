package io.github.vitalivu.subdownloader.compressor;

import io.github.vitalivu.subdownloader.fs.IndependentFileSystem;
import java.io.File;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ZipCompressorTest {
  @Test
  void decompress(){
    File file = new File("love-and-monsters_vietnamese-2311858.zip");
    ZipCompressor zipCompressor = new ZipCompressor(new IndependentFileSystem());
    Optional<File> subtitle = zipCompressor.decompress(file);
    Assertions.assertTrue(subtitle.isPresent());
  }

}