package com.example.logindemo.model;

import com.example.logindemo.UserProfile;

import java.util.ArrayList;

public class UserAnswer {
    private String id;
    private String username;
    private ArrayList<Quiz> answerQuizList;

    public UserAnswer() {
    }

    public UserAnswer(String id, String username, ArrayList<Quiz> answerQuizList) {
        this.id = id;
        this.username = username;
        this.answerQuizList = answerQuizList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Quiz> getAnswerQuizList() {
        return answerQuizList;
    }

    public void setAnswerQuizList(ArrayList<Quiz> answerQuizList) {
        this.answerQuizList = answerQuizList;
    }
}
