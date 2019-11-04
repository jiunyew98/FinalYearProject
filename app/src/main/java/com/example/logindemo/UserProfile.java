package com.example.logindemo;

import com.example.logindemo.model.SubjectParent;

import java.util.ArrayList;

public class UserProfile {

    public String userAge;
    public String userEmail;
    public String userName = "";
    public String chatWith = "";
    public Boolean isLecturer;
    public ArrayList<String> subjectParentArrayList;

    public UserProfile(){

        //A default constructor

    }

    public UserProfile(String userAge, String userEmail, String userName) {

        this.userEmail = userEmail;
        this.userName = userName;
        this.userAge = userAge;
        this.isLecturer = false;


    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChatWith() {
        return chatWith;
    }

    public void setChatWith(String chatWith) {
        this.chatWith = chatWith;
    }

    public Boolean getLecturer() {
        return isLecturer;
    }

    public void setLecturer(Boolean lecturer) {
        isLecturer = lecturer;
    }

    public ArrayList<String> getSubjectParentArrayList() {
        return subjectParentArrayList;
    }

    public void setSubjectParentArrayList(ArrayList<String> subjectParentArrayList) {
        this.subjectParentArrayList = subjectParentArrayList;
    }
}
