package com.example.logindemo.model;

import com.example.logindemo.UserProfile;

import java.util.ArrayList;

public class SubjectParent {
    private String id;
    private String lecturerId;
    private String title;
    private ArrayList<Notes> notesArrayList;
    private ArrayList<UserProfile> studentArrayList;

    public SubjectParent() {
    }

    public SubjectParent(String id, String title, ArrayList<Notes> notesArrayList) {
        this.id = id;
        this.title = title;
        this.notesArrayList = notesArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Notes> getNotesArrayList() {
        return notesArrayList;
    }

    public void setNotesArrayList(ArrayList<Notes> notesArrayList) {
        this.notesArrayList = notesArrayList;
    }

    public ArrayList<UserProfile> getStudentArrayList() {
        return studentArrayList;
    }

    public void setStudentArrayList(ArrayList<UserProfile> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }
}
