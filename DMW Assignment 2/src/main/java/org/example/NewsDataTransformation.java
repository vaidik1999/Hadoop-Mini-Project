package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsDataTransformation {

    public static final String DATABASE_NAME = "Assignment2";
    public static final String COLLECTION_NAME = "NewsData";
    public static final String CLEAN_DATA_REGEX = "[^a-zA-Z0-9\\s]+|http\\S+|[\\x{1F600}-\\x{1F64F}]|\\b\\d+\\s*chars\\b";

    /**
     Performs the news data transformation by reading all the files in the NewsData directory,
     creating a list of documents containing the data from all the files and inserting them into
     the MongoDB database.
     @throws IOException if there is an error while reading the file.
     @throws MongoException if there is an error while connecting to the MongoDB database.
     */
    public void NewsDataTransformation() {
        List<String> fileNames = getAllFileNames();
        List<List<List<String>>> data = new ArrayList<>();

        for (String fileName : fileNames) {
            data.add(readFile("./NewsData/" + fileName));
        }

        ConnectionString connectionString = new ConnectionString("mongodb+srv://vaidik10:vaidik10@cluster0.hbkaez1.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            List<Document> documents = new ArrayList<>();
            for (List<List<String>> fileData : data) {
                for (List<String> tuples : fileData) {
                    documents.add(new Document("title", tuples.get(0)).append("content", tuples.get(1)));
                }
            }
            InsertManyOptions options = new InsertManyOptions().ordered(false);
            collection.insertMany(documents, options);
        }
    }

    /**
     Reads the data from the given file path and returns a list of lists containing title and content data
     @param filePath the file path of the file to be read
     @return a list of lists containing title and content data
     @throws RuntimeException if there is an I/O error while reading the file
     */
    public List<List<String>> readFile(String filePath) {
        List<List<String>> fileData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String title = "";
            String content = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("Title:")) {
                    title = cleanData(line.replace("Title:", ""));
                }
                if (line.contains("Content:")) {
                    content = cleanData(line.replace("Content:", ""));
                    fileData.add(Arrays.asList(title, content));
                    title = "";
                    content = "";
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileData;
    }

    /**
     Returns a list of all file names present in the NewsData directory.
     @return a list of strings containing file names.
     */
    public List<String> getAllFileNames() {
        List<String> files = new ArrayList<>();
        File folder = new File("./NewsData/");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    files.add(file.getName());
                }
            }
        }
        return files;
    }

    /**
     This method receives a string of data and uses regex pattern matching to remove unwanted characters and symbols.
     @param data The string of data to be cleaned.
     @return A string of cleaned data.
     */
    public String cleanData(String data) {
        Pattern pattern = Pattern.compile(CLEAN_DATA_REGEX);
        Matcher matcher = pattern.matcher(data);
        return matcher.replaceAll(" ");
    }
}

