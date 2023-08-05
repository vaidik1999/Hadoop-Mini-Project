package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsDataExtraction {
    private static final List<String> KEYWORDS = Arrays.asList(
            "Canada", "University", "Dalhousie", "Halifax", "Canada Education", "Moncton", "hockey", "Fredericton", "celebration"
    );

    /**
     Retrieves news data for a list of keywords using HTTP requests and sends the data to NewsDataProcessing for further processing.
     @throws IOException if there is an error in retrieving the news data
     */
    public void getNewsData() {
        try {
            List<String> data = new ArrayList<String>();
            for (String keyword : KEYWORDS) {
                String response = sendHttpRequest(keyword);
                data.add(response.toString());
            }

            NewsDataProcessing newsDataProcessing = new NewsDataProcessing();
            newsDataProcessing.NewsDataProcessing(data);;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     Sends an HTTP GET request to the News API with the provided keyword as a query parameter.
     @param keyword the keyword to search for in the News API
     @return the JSON response received from the News API
     @throws IOException if there is an error with the connection or input stream
     */
    public String sendHttpRequest(String keyword) throws IOException {
        URL url = new URL("https://newsapi.org/v2/everything?q=" + keyword + "&from=2023-02-24&sortBy=publishedAt&apiKey=9f48e8acb33d42069b58bc25662946aa");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }
}

