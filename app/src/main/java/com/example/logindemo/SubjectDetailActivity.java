package com.example.logindemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.logindemo.adapter.SubjectDetailAdapter;
import com.example.logindemo.model.QuizParent;
import com.example.logindemo.model.SubjectParent;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SubjectDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectDetailAdapter subjectDetailAdapter;

    private static String SUBJECT_PARENT = "SUBJECT_PARENT";


    public static Intent newInstance(Context context, SubjectParent subjectParent){
        Intent intent = new Intent(context,SubjectDetailActivity.class);
        intent.putExtra(SUBJECT_PARENT, new Gson().toJson(subjectParent));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);

        initView();

        SubjectParent subjectParent = new Gson().fromJson(getIntent().getExtras().getString(SUBJECT_PARENT), SubjectParent.class);

        ArrayList<QuizParent> quizParents = new ArrayList<>();
        for(String key : subjectParent.getQuiz().keySet()){
            quizParents.add(subjectParent.getQuiz().get(key));
        }


        subjectDetailAdapter = new SubjectDetailAdapter(this, subjectParent.getNotesArrayList(),quizParents);
        subjectDetailAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(subjectDetailAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView(){
        recyclerView = findViewById(R.id.rv);
    }
}
