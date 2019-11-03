package com.example.logindemo.model;

public class Quiz {
    public String question;
    public Boolean answer;
    public Boolean studentAnswer;

    public Quiz() {
    }

    public Quiz(String question, Boolean answer) {
        this.question = question;
        this.answer = answer;
    }

    public Quiz(String question, Boolean answer, Boolean studentAnswer) {
        this.question = question;
        this.answer = answer;
        this.studentAnswer = studentAnswer;
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

    public Boolean getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(Boolean studentAnswer) {
        this.studentAnswer = studentAnswer;
    }
}
