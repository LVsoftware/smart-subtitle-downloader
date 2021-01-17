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
import java.util.Optional;
import org.jsoup.HttpStatusException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DownloadClientTest {

  private static final Logger LOG = LoggerFactory.getLogger(DownloadClientTest.class);

  @Test
  void downloadSubtitles() {
    List<String> movies =
        Arrays.asList("10.Things.I.Hate.About.You.1999.1080p.BrRip.x264.BOKUTOX.YIFY.mp4",
            "A.Beautiful.Mind.2001.1080p.BrRip.x264.YIFY.mp4",
            "Back.to.the.Future.1985.1080p.Brrip.x264.Deceit.YIFY.mp4",
            "Bad.Boys.II.2003.1080.BluRay.X264.YIFY.mp4",
            "Batman.The.Dark.Knight.2008.1080p.BluRay.x264.YIFY.mp4",
            "The.Dark.Knight.Rises.2012.1080p.BluRay.x264.YIFY.mp4",
            "Catch.Me.If.You.Can.2002.1080p.BluRay.x264.YIFY.mp4",
            "Mad.Max.1979.1080p.BRrip.x264.YIFY.mp4",
            "Mad.Max.2.The.Road.Warrior.1980.1080p.BRrip.x264.YIFY.mp4",
            "Mad.Max.Beyond.Thunderdome.1985.1080p.BluRay.x264-[YTS.AG].mp4",
            "Mad.Max.Fury.Road.2015.1080p.BluRay.x264.YIFY.mp4",
            "Ant-Man.And.The.Wasp.2018.1080p.BluRay.x264-[YTS.AM].mp4",
            "Avengers.Age.of.Ultron.2015.1080p.BluRay.x264.YIFY.mp4",
            "Avengers.Endgame.2019.1080p.BluRay.x264-[YTS.LT].mp4",
            "Avengers.Infinity.War.2018.1080p.WEBRip.x264-[YTS.AM].mp4",
            "Black.Panther.2018.1080p.BluRay.x264-[YTS.AM].mp4",
            "Captain.America.The.First.Avenger.1080p.BrRip.x264.YIFY.mp4",
            "Captain.America.The.Winter.Soldier.2014.1080p.BluRay.x264.YIFY.mp4",
            "Doctor.Strange.2016.1080p.BluRay.x264-[YTS.AG].mp4",
            "Guardians.of.the.Galaxy.2014.1080p.BluRay.x264.YIFY.mp4",
            "Guardians.of.the.Galaxy.Vol.2.2017.1080p.BluRay.H264.AAC-RARBG.mp4",
            "Iron.Man.2.2010.1080p.BrRip.x264.YIFY.mp4",
            "Iron.Man.3.2013.1080p.BluRay.x264.YIFY.mp4",
            "Iron.Man.2008.1080p.BrRip.x264.YIFY.mp4",
            "The.Avengers.2012.1080p.BluRay.x264.YIFY.mp4",
            "The.Incredible.Hulk.2008.1080p.BluRay.x264.YIFY.mp4",
            "Thor.The.Dark.World.2013.1080p.BluRay.x264.YIFY.mp4",
            "Thor.2011.1080p.BluRay.H264.AAC-RARBG.mp4",
            "Once.Upon.A.Time.....In.Hollywood.2019.1080p.WEBRip.x264-[YTS.LT].mp4",
            "Pirates.of.the.Caribbean.At.Worlds.End.2007.1080p.BrRip.x264.Deceit.YIFY.mp4",
            "Pirates.of.the.Caribbean.Curse.of.the.Black.Pearl.2003.1080p.BrRip.x264.Deceit.YIFY.mp4",
            "Pirates.of.the.Caribbean.Dead.Man's.Chest.2006.1080p.BrRip.x264.Deceit.YIFY.mp4",
            "Pirates.of.the.Caribbean.On.Stranger.Tides.2011.1080p.BrRip.x264.Deceit.YIFY.mp4",
            "Ready.Player.One.2018.1080p.BluRay.x264-[YTS.AM].mp4",
            "Seven.Years.In.Tibet.1997.1080p.BluRay.x264-[YTS.LT].mp4",
            "Man.of.Steel.2013.1080p.BluRay.x264.YIFY.mp4",
            "Tuyen duong 100 Bi thu chi doan gioi cua Thu do 2014.mp4",
            "The.Dark.Knight.Rises.2012.1080p.BluRay.x264.YIFY.mp4",
            "The.Grand.Budapest.Hotel.2014.1080p.BluRay.x264.YIFY.mp4",
            "The.Imitation.Game.2014.1080p.BluRay.x264.YIFY.mp4",
            "The.Incredibles.2004.1080p.BluRay.x264-[YTS.AG].mp4",
            "The.Matrix.1999.1080p.BrRip.x264.YIFY.mp4",
            "The.Prestige.2006.1080p.BluRay.x264-[YTS.AM].mp4",
            "The.Pursuit.Of.Happyness.2006.1080p.BrRip.x264.YIFY.mp4",
            "The.Wolf.of.Wall.Street.2013.1080p.BluRay.x264.YIFY.mp4",
            "The.Lord.of.the.Rings.The.Fellowship.of.the.Rings.THEATRICAL.EDITION.2001.1080p.BrRip.x264.BOKUTOX.YIFY.mp4",
            "The.Lord.of.the.Rings.The.Return.of.the.King.EXTENDED.2003.1080p.BrRip.x264.YIFY.mp4",
            "The.Lord.of.the.Rings.The.Two.Towers.2002.ExD.1080p.BrRip.x264.YIFY.mp4",
            "Triple.Frontier.2019.1080p.WEBRip.x264-[YTS.AM].mp4",
            "Up.2009.1080p.BluRay.x264.YIFY.mp4",
            "WALL-E.2008.1080p.BrRip.x264.YIFY.mp4",
            "Watchmen.Ultimate.Cut.2009.1080p.BrRip.x264.YIFY.mp4",
            "Deadpool.2.2018.1080p.BluRay.x264-[YTS.AM].mp4",
            "The.Wolverine.2013.1080p.BluRay.x264.YIFY.mp4",
            "X-Men.First.Class.2011.720p.BrRip.264.YIFY.mp4",
            "X-Men.2000.1080p.BrRip.x264.YIFY.mp4",
            "X-Men.2.2003.1080p.BrRip.x264.YIFY.mp4",
            "Your.Name.2016.1080p.BluRay.x264-[YTS.AM].mp4",
            "Doctor.Who.The.Time.Of.The.Doctor.2013.1080p.BluRay.x264-[YTS.AM].mp4",
            "The.Secret.Life.of.Walter.Mitty.2013.1080p.BluRay.x264.YIFY.mp4",
            "Fantastic.Beasts.The.Crimes.Of.Grindelwald.2018.1080p.WEBRip.x264-[YTS.AM].mp4",
            "Fantastic.Beasts.and.Where.to.Find.Them.2016.1080p.BRRip.x264.AAC-ETRG.mp4",
            "2.Fast.2.Furious.2003.720p.BrRip.x264.YIFY+HI.mp4",
            "Fast.and.Furious.2009.1080p.BrRip.x264.YIFY.mp4",
            "Fast.Five.2011.1080p.BrRip.x264.YIFY.mp4",
            "Furious.6.2013.1080p.BluRay.x264.YIFY.mp4",
            "The.Fast.and.the.Furious.2001.1080p.BrRip.x264.YIFY+HI.mp4",
            "The.Fast.and.the.Furious.Tokyo.Drift.2011.1080p.BrRip.x264.YIFY.mp4",
            "Harry.Potter.and.the.Chamber.of.Secrets.2002.1080p.BrRip.x264.YIFY.mp4",
            "Harry.Potter.And.The.Deathly.Hallows.Part.2.2011.720p.BrRip.264.YIFY.mkv-muxed.mp4",
            "Harry.Potter.and.the.Goblet.of.Fire.2005.1080p.BrRip.x264.YIFY.mp4",
            "Harry.Potter.and.the.Half.Blood.Prince.2009.1080p.BrRip.x264.YIFY.mp4",
            "Harry.Potter.and.the.Order.of.the.Phoenix.2007.1080p.BrRip.x264.YIFY.mp4",
            "Harry.Potter.and.the.Sorcerers.Stone.2001.1080p.BrRip.x264.YIFY ( FIRST TRY).mp4",
            "How.To.Train.Your.Dragon.The.Hidden.World.2019.1080p.BluRay.x264-[YTS.AM].mp4",
            "Ice.Age.2002.1080p.BluRay.x264-[YTS.AG].mp4",
            "Ice.Age.Continental.Drift.2012.1080p.BluRay.x264.YIFY.mp4",
            "Ice.Age.Dawn.Of.The.Dinosaurs.2009.1080p.BluRay.x264-[YTS.AG].mp4",
            "Ice.Age.The.Meltdown.2006.1080p.BluRay.x264-[YTS.AG].mp4",
            "Inception.2010.1080p.BrRip.x264.YIFY.mp4",
            "Incredibles.2.2018.1080p.BluRay.x264-[YTS.AM].mp4",
            "Inside.Out.2015.1080p.BluRay.x264.YIFY.mp4",
            "Jumanji.Welcome.To.The.Jungle.2017.1080p.BluRay.x264-[YTS.AM].mp4"
        );


    for (String movie : movies) {
      try {
        File videoFile = newFile(movie);
        SimpleFileNameExtractor fileNameExtractor = new SimpleFileNameExtractor();
        DownloadClient downloadClient =
            new DownloadClient(new SubsceneProvider(),
                fileNameExtractor,
                new AutoCompressor(fileNameExtractor, new IndependentFileSystem()),
                new NameSetter() {
                  @Override
                  public String niceName(String title, Integer year, String languageCode,
                                         String extension) {
                    return String.format("%s.%s.%s", title, languageCode, extension);
                  }
                });

        Optional<File> subtitle = downloadClient.downloadSubtitle(videoFile);
        Assertions.assertTrue(subtitle.isPresent());
      } catch (HttpStatusException e) {
        LOG.warn("failed to download subtitle for {}: {}", movie, e.getMessage());
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        Assertions.fail(e.getMessage());
      } finally {
        File file = new File(movie);
        if (file.exists())
          file.delete();
      }
    }
  }

  @Test
  void downloadSubtitle() {
    try {
      File videoFile = newFile("Love.And.Monsters.2020.1080p.BluRay.x264.AAC5.1-[YTS.MX].mp4");
      SimpleFileNameExtractor fileNameExtractor = new SimpleFileNameExtractor();
      DownloadClient downloadClient =
          new DownloadClient(new SubsceneProvider(),
              fileNameExtractor,
              new AutoCompressor(fileNameExtractor, new IndependentFileSystem()),
              new NameSetter() {
                @Override
                public String niceName(String title, Integer year, String languageCode,
                                       String extension) {
                  return String.format("%s.%s.%s", title, languageCode, extension);
                }
              });

      Optional<File> subtitle = downloadClient.downloadSubtitle(videoFile);
      Assertions.assertTrue(subtitle.isPresent());
    } catch (IOException e) {
      LOG.error(e.getMessage(), e);
      Assertions.fail(e.getMessage());
    }
  }

  private File newFile(String fileName) throws IOException {
    File videoFile = new File(fileName);
    if (!videoFile.exists()) {
      videoFile.createNewFile();
    }
    return videoFile;
  }
}