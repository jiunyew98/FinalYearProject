package com.example.logindemo.model;

import com.example.logindemo.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizParent {
    private String id;
    private String subjectId;
    private String title;
    private ArrayList<Quiz> quizArrayList;
    private ArrayList<UserProfile> userProfileArrayList;
    private HashMap<String,UserAnswer> answer;

    public QuizParent() {
    }

    public QuizParent(String title, String id,String subjectId, ArrayList<Quiz> quizArrayList) {
        this.id = id;
        this.subjectId =subjectId;
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

    public ArrayList<UserProfile> getUserProfileArrayList() {
        return userProfileArrayList;
    }

    public void setUserProfileArrayList(ArrayList<UserProfile> userProfileArrayList) {
        this.userProfileArrayList = userProfileArrayList;
    }

    public HashMap<String, UserAnswer> getAnswers() {
        return answer;
    }

    public void setAnswers(HashMap<String, UserAnswer> answers) {
        this.answer = answers;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public HashMap<String, UserAnswer> getAnswer() {
        return answer;
    }

    public void setAnswer(HashMap<String, UserAnswer> answer) {
        this.answer = answer;
    }
}
