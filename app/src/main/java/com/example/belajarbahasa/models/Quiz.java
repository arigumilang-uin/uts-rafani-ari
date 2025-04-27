package com.example.belajarbahasa.models;

import java.util.List;

public class Quiz {
    private String question;
    private List<String> options;
    private String correctAnswer;

    public Quiz(String question, List<String> options, String correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
