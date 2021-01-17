package io.github.vitalivu.subdownloader.provider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SubsceneProvider implements SubtitleProvider {
  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(SubsceneProvider.class);
  public static final String HEADER_USER_AGENT =
      "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";
  public static final String HEADER_COOKIE =
      "_ga=GA1.2.1416801158.1607222748; __gads=ID=74152d57847bc04e-22d29cdf2ec500ae:T=1607831185:RT=1607831185:S=ALNI_MZE06x4xCojGUlE-0rbrZ1RNi9LBw; __cfduid=dedd12ea6f7fdf58f170c0bda842f53bb1610289111; cf_clearance=f28db97fc6ba447d52cc32ccb50491aecd0c0f43-1610801978-0-150; _gid=GA1.2.650355659.1610801980; __cf_bm=ba484c735871e9ed1f0896b36f01481515f4d00e-1610808250-1800-AWBzn9wKzCCrq2c3EfamjG++150WlLhp7V9wyqKeBPmNi8eMbDLI3GpIsjuvMMKHU4+8FbYOSZYULXrJ8PVtSUAFzPk6buCvsoPnM/26QOWgAvbWkS+jqAgv0OoUCKPECw==";
  public static final int TIMEOUT_MS = 30_000;

  @Override
  public Optional<String> getDownloadLinkFromTitle(String title, String language)
      throws IOException {
    Document searchPage = Jsoup.connect("https://subscene.com/subtitles/searchbytitle")
        .userAgent(HEADER_USER_AGENT).timeout(TIMEOUT_MS).header("cookie", HEADER_COOKIE)
        .data("query", title)
        .post();

    Elements select = searchPage.select("h2.exact");
    if (select == null || select.isEmpty()) {
      LOG.info("cannot find subtitle by the exact name: {}", title);
      return Optional.empty();
    }
    String linkDetail =
        searchPage.select("#left > div > div > ul:nth-child(2) > li > div.title > a")
            .attr("abs:href");
    LOG.info("link to detail page: {}", linkDetail);
    Document detailPage = Jsoup.connect(linkDetail)
        .userAgent(HEADER_USER_AGENT).timeout(TIMEOUT_MS).header("cookie", HEADER_COOKIE)
        .get();
    Optional<String> linkDownloadPage = extractLinkDownloadPage(language, detailPage);
    if (!linkDownloadPage.isPresent()) {
      return Optional.empty();
    }

    Document downloadPage = Jsoup.connect(linkDownloadPage.get())
        .userAgent(HEADER_USER_AGENT).timeout(TIMEOUT_MS).header("cookie", HEADER_COOKIE)
        .get();
    String linkFile = downloadPage.select("#downloadButton").first().attr("abs:href");
    LOG.info("link to file: {}", linkFile);
    return Optional.of(linkFile);
  }

  private Optional<String> extractLinkDownloadPage(String language, Document detailPage) {
    String linkDownloadPage = null;
    Elements elements =
        detailPage.select("td.a1 span.l.r.positive-icon:containsOwn(" + language + ")");
    if (elements != null && !elements.isEmpty()) {
      linkDownloadPage = elements.parents().first().attr("abs:href");
    }

    if (linkDownloadPage == null) {
      elements =
          detailPage.select("td.a1 span.l.r.normal-icon:containsOwn(" + language + ")");
      if (elements != null && !elements.isEmpty()) {
        linkDownloadPage = elements.parents().first().attr("abs:href");
      }
    }
    if (linkDownloadPage == null) {
      return Optional.empty();
    }
    LOG.info("link to download page: {}", linkDownloadPage);
    return Optional.of(linkDownloadPage);
  }

  @Override
  public Optional<String> getDownloadLinkFromFileName(String fileName, String language) {
    return Optional.empty();
  }


  @Override
  public Optional<File> download(String link) {
    try {
      URLConnection uc = new URL(link).openConnection();
      uc.addRequestProperty("User-Agent", HEADER_USER_AGENT);
      uc.addRequestProperty("cookie", HEADER_COOKIE);
      uc.setConnectTimeout(TIMEOUT_MS);
      uc.connect();
      uc.getInputStream();

      try (InputStream in = new BufferedInputStream(uc.getInputStream());
           ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = in.read(buf))) {
          out.write(buf, 0, n);
        }
        // attachment; filename=toy-story-that-time-forgot_vietnamese-1213748.zip
        String contentDisposition = uc.getHeaderField("Content-Disposition");
        String fileName = !contentDisposition.contains("filename=") ? "tmp" :
            contentDisposition.substring(contentDisposition.lastIndexOf("=") + 1);
        byte[] response = out.toByteArray();
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
          fos.write(response);
        }
        LOG.info("downloaded to {}", fileName);
        return Optional.of(new File(fileName));
      }
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
    }
    return Optional.empty();
  }
}
