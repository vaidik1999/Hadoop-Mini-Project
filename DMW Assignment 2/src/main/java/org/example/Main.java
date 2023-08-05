package org.example;

public class Main {

    /**
     The main method for running the NewsDataTransformation program.
     This method creates an instance of NewsDataTransformation and calls the NewsDataTransformation method
     to transform the news data into the required format and store it in a MongoDB database.
     If an exception occurs during the transformation, the error message and stack trace will be printed to the console.
     @param args command line arguments (not used in this program)
     */
    public static void main(String[] args) {
        try {
            NewsDataTransformation te = new NewsDataTransformation();
            te.NewsDataTransformation();
        } catch (Exception e) {
            System.err.println("An error occurred during data transformation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


