package com.example.data_mining_test_index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class Utils {
    public static String removeSpecialCharacters(String str) {
        // Define a regular expression pattern to match special characters
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\s]");
        // Replace all occurrences of special characters with an empty string
        return pattern.matcher(str).replaceAll("");
    }

    public static List<QuestionDTO> readQuestions( String filePath) throws IOException {
        List<QuestionDTO> questionsList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String row;
            while ((row = reader.readLine()) != null) {
                String category = row.trim();
                category = removeSpecialCharacters(category);
                //category = TokenizerLematizator.process_text(category);
                row = reader.readLine();
                String clueText = row.trim();
                clueText = removeSpecialCharacters(clueText);
                //clueText = TokenizerLematizator.process_text(clueText);
                row = reader.readLine();
                String correctAnswer = row.trim();
                reader.readLine();
                QuestionDTO question = new QuestionDTO(clueText, category, correctAnswer);
                questionsList.add(question);
            }
        }
        return questionsList;
    }
}
