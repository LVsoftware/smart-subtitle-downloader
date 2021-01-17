SUBTITLE AUTO DOWNLOADER
===

### What is this?
A tool to download subtitle for your movies.
I have a lot of movies (without Vietnamese subtitle), I spend time on download subtitle for those.
I search for a tool that automate this process, but I can't. So I wrote this one.

### How does it work? 
With the help of `jsoup` library for parsing html, it works like: 
- then you search for a subtitle, eg (on subscene.com),
- then you select a subtitle that match
- then you download subtitle,
- then you unarchive downloaded file, then move it to the video folder 

### Usage
```
➜  java -jar sub-downloader-1.0-SNAPSHOT-jar-with-dependencies.jar -h
usage: -p /data/movies -l Vietnamese -c vie -t mkv mp4
 -c,--code <arg>        Language code to append to name of subtitle,
                        default: vie
 -h,--help              Print help
 -l,--language <arg>    Language of subtitle to download, default:
                        Vietnamese
 -p,--path <arg>        Path to scan for movies, default: current
                        directory
 -t,--file-type <arg>   File type to scans, default: mp4,mkv

```

### Example

```bash
java -jar sub-downloader-1.0-SNAPSHOT-jar-with-dependencies.jar -p /home/linhvu/Downloads -t mp4 -l English -c eng
java -jar sub-downloader-1.0-SNAPSHOT-jar-with-dependencies.jar --path /data/Movies --file-type mp4,mkv --language Vietnamese --code vie
```

### License
MIT License

