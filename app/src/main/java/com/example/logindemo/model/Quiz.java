package com.example.logindemo.model;

public class Quiz {
    public String question;
    public Boolean answer;

    public Quiz() {
    }

    public Quiz(String question, Boolean answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }
}
