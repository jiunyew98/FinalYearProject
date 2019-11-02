package com.example.logindemo.model;

import java.util.ArrayList;

public class QuizParent {
    private String id;
    private String title;
    private ArrayList<Quiz> quizArrayList;

    public QuizParent() {
    }

    public QuizParent(String title, String id, ArrayList<Quiz> quizArrayList) {
        this.id = id;
        this.quizArrayList = quizArrayList;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Quiz> getQuizArrayList() {
        return quizArrayList;
    }

    public void setQuizArrayList(ArrayList<Quiz> quizArrayList) {
        this.quizArrayList = quizArrayList;
    }
}
