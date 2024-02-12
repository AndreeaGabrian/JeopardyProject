package com.example.data_mining_test_index;

import java.util.ArrayList;
import java.util.List;

public class QuestionDTO {
    String textClue;
    String category;
    String correctAnswer;
    List<Integer> answerTop10; // here a list with the doc ids corresponding to top 10 answers (one doc = one wiki page) will be kept

    public QuestionDTO(String text_clue, String category, String correct_answer) {
        this.textClue = text_clue;
        this.category = category;
        this.correctAnswer = correct_answer;
        this.answerTop10 = new ArrayList<>();
    }

}