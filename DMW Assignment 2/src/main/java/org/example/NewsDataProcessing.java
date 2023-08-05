package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsDataProcessing {
     int fileCount = 1;

/**
 Processes news data by extracting titles and contents from a list of strings, and writing them to files with a maximum of 5 entries each.
 Also initiates news data transformation.
 @param data List of strings containing news data to be processed
 */
    public void NewsDataProcessing(List<String> data) {
        int dataCount = 0;
        for (String dataElement : data) {
            Pattern pattern1 = Pattern.compile("\"title\":\"(.*?)\"");
            Pattern pattern2 = Pattern.compile("\"content\":\"(.*?)\"");
            Matcher matcher1 = pattern1.matcher(dataElement);
            Matcher matcher2 = pattern2.matcher(dataElement);

            while (matcher1.find() && matcher2.find()) {
                if (dataCount >= 5) {
                    createFile();
                    dataCount = 0;
                }
                addDataToFile(getPathOfLastFile(), matcher1.group(1), matcher2.group(1));
                dataCount++;
            }
        }

        NewsDataTransformation dataTransformation = new NewsDataTransformation();
        dataTransformation.NewsDataTransformation();
    }

    /**
     Returns the file path of the last created file.
     @return the file path of the last created file
     */
    public String getPathOfLastFile() {
        return "./NewsData/" + (fileCount - 1);
    }

    /**
     Appends the given title and content to the end of the file located at the given file path.
     @param filePath the path of the file to which data needs to be added
     @param title the title of the news article to be added
     @param content the content of the news article to be added
     @throws RuntimeException if there is an I/O error while writing to the file
     */
    public void addDataToFile(String filePath, String title, String content) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            fw.write("Title:" + title + '\n');
            fw.write("Content:" + content + '\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     Creates a new file in the "NewsData" directory with a file name generated based on fileCount.
     Increments the fileCount for the next file creation.
     @throws RuntimeException if there is an IOException when creating the file.
     */
    public void createFile() {
        String filePath = "./NewsData/" + fileCount;
        File file = new File(filePath);
        try {
            file.createNewFile();
            fileCount++;
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

