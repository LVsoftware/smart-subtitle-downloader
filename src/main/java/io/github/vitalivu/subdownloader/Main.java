package io.github.vitalivu.subdownloader;

import io.github.vitalivu.subdownloader.compressor.AutoCompressor;
import io.github.vitalivu.subdownloader.fs.IndependentFileSystem;
import io.github.vitalivu.subdownloader.name.NameSetter;
import io.github.vitalivu.subdownloader.name.SimpleFileNameExtractor;
import io.github.vitalivu.subdownloader.provider.SubsceneProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    Options options = new Options();
    options.addOption("p", "path", true, "Path to scan for movies, default: current directory");
    options.addOption(Option.builder("t").hasArgs().longOpt("file-type")
        .desc("File type to scans, default: mp4,mkv").build());
    options
        .addOption("l", "language", true, "Language of subtitle to download, default: Vietnamese");
    options
        .addOption("c", "code", true, "Language code to append to name of subtitle, default: vie");
    options
        .addOption("ry", "release-year", true, "The release year");
    options.addOption("h", "help", false, "Print help");

    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine commandLine = parser.parse(options, args);
      if (commandLine.hasOption("h")) {
        printHelp(options);
        return;
      }

      String pathToScan = commandLine.getOptionValue("p", System.getProperty("user.dir"));
      String[] fileTypes = commandLine.getOptionValues("t");
      List<String> movieFileTypes;
      if (fileTypes == null) {
        movieFileTypes = Arrays.asList("mkv", "mp4");
      } else {
        movieFileTypes = Arrays.asList(fileTypes).stream()
            .map(t -> t.toLowerCase())
            .collect(Collectors.toList());
      }
      String language = commandLine.getOptionValue("l", "Vietnamese");
      String languageCode = commandLine.getOptionValue("c", "vie");
      if (commandLine.hasOption("ry")) {
        LOG.info("set release year to {}", commandLine.getOptionValue("ry"));
        System.setProperty("r.release_year", commandLine.getOptionValue("ry"));
      }

      File path = new File(pathToScan);

      LOG.info("--- start scan {} ---", pathToScan);
      LOG.info("to find subtile in {} ({}) for movies file {}", language, languageCode,
          movieFileTypes);

      SimpleFileNameExtractor fileNameExtractor = new SimpleFileNameExtractor();
      NameSetter nameSetter = new NameSetter() {
        @Override
        public String niceName(String title, Integer year, String languageCode,
                               String extension) {
          return String.format("%s.%s.%s", title, languageCode, extension);
        }
      };
      DownloadClient downloadClient =
          new DownloadClient(new SubsceneProvider(),
              fileNameExtractor,
              new AutoCompressor(fileNameExtractor, new IndependentFileSystem()),
              nameSetter);

      scanPath(path, movieFileTypes, downloadClient);
      LOG.info("--- finish scan ---");
    } catch (ParseException | IOException e) {
      LOG.error(e.getMessage(), e);
      printHelp(options);
    }
  }

  private static void printHelp(Options options) {
    HelpFormatter helpFormatter = new HelpFormatter();
    helpFormatter.printHelp("-p /data/movies -l Vietnamese -c vie -t mkv mp4", options);
  }

  private static void scanPath(File path, List<String> movieFileTypes, DownloadClient downloader)
      throws IOException {
    if (!path.isFile()) {
      if (path.isDirectory()) {
        File[] childPaths = path.listFiles();
        for (File childPath : childPaths) {
          scanPath(childPath, movieFileTypes, downloader);
        }
      }
    } else {
      int i = path.getName().lastIndexOf(".");
      if (i < 0) {
        return;
      }
      String fileType = path.getName().substring(i + 1).toLowerCase();
      for (String type : movieFileTypes) {
        if (type.equals(fileType)) {
          LOG.info("### {}", path.getPath());
          downloader.downloadSubtitle(path);
        }
      }
    }
  }

}
