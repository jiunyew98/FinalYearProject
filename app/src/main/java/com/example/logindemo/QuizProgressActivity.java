package com.example.logindemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.logindemo.model.Quiz;
import com.example.logindemo.model.QuizParent;
import com.example.logindemo.model.SubjectParent;
import com.example.logindemo.model.UserAnswer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizProgressActivity extends Activity {

    public static String SUBJECT_KEY = "SUBJECT_KEY";
    public static String LECTURER_KEY = "LECTURER_KEY";

    private ArrayList<SubjectParent> subjectParentArrayList = new ArrayList<>();
    private HashMap<String, UserAnswer> subjectMarksArrayList = new HashMap<>();
    private HashMap<String, String> titleMarksArrayList = new HashMap<>();
    private BarChart barChart;
    private static final String TAG = "ProgressFragment";
    private String subjectId = null;
    private String lecturerId = null;

    public static Intent newInstance(Context context, String lecturerId, String subjectId) {
        Intent intent = new Intent(context, QuizProgressActivity.class);
        intent.putExtra(SUBJECT_KEY, subjectId);
        intent.putExtra(LECTURER_KEY, lecturerId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_progress);
        subjectId = getIntent().getStringExtra(SUBJECT_KEY);
        lecturerId = getIntent().getStringExtra(LECTURER_KEY);
        barChart = findViewById(R.id.barchart);

        getData();
    }


    private void getData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(KeyTag.SUBJECT_KEY).child(lecturerId).child(subjectId).child(KeyTag.QUIZ_KEY);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                SubjectParent university = dataSnapshot.getValue(SubjectParent.class);
//
//                if(university.getQuiz() ==null){
//                    Toast.makeText(QuizProgressActivity.this, "No quiz for currently", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                for (String key : university.getQuiz().keySet()) {
//                    if (university.getQuiz().get(key).getAnswers() != null && university.getQuiz().get(key).getAnswers().containsKey(FirebaseAuth.getInstance().getUid())) {
//                        UserAnswer userAnswer = university.getQuiz().get(key).getAnswers().get(FirebaseAuth.getInstance().getUid());
//                        subjectMarksArrayList.put(key, userAnswer);
//                        titleMarksArrayList.put(key, university.getQuiz().get(key).getTitle());
//                    } else {
//                        if (!subjectMarksArrayList.containsKey(university.getId())) {
//                            UserAnswer existMark = new UserAnswer();
//                            existMark.setTotalCorrect(0);
//                            existMark.setAnswerQuizList(new ArrayList<Quiz>());
//                            subjectMarksArrayList.put(key, existMark);
//                            titleMarksArrayList.put(key, university.getQuiz().get(key).getTitle());
//                        }
//                    }
//                }

                for(DataSnapshot quizSnapshot : dataSnapshot.getChildren()){
                    QuizParent quizParent = quizSnapshot.getValue(QuizParent.class);

                    for(DataSnapshot answerSnapShot : quizSnapshot.child("answer").getChildren()){
                        UserAnswer quiz = answerSnapShot.getValue(UserAnswer.class);
                        if (quiz.getId().equals(FirebaseAuth.getInstance().getUid())) {
                            subjectMarksArrayList.put(quizParent.getId(), quiz);
                            titleMarksArrayList.put(quizParent.getId(), quizParent.getTitle());
                        } else {
                            if (!subjectMarksArrayList.containsKey(quizParent.getId())) {
                                UserAnswer existMark = new UserAnswer();
                                existMark.setTotalCorrect(0);
                                existMark.setAnswerQuizList(new ArrayList<Quiz>());
                                subjectMarksArrayList.put(quizParent.getId(), existMark);
                                titleMarksArrayList.put(quizParent.getId(), quizParent.getTitle());
                            }
                        }

                    }
                }

                setupBarChart();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(QuizProgressActivity.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        if (subjectMarksArrayList == null)
            return;

        int i = 0;
        for (String key : subjectMarksArrayList.keySet()) {
            entries.add(new BarEntry(((Float.valueOf(subjectMarksArrayList.get(key).getTotalCorrect()) / Float.valueOf(subjectMarksArrayList.get(key).getAnswerQuizList().size())) * 100), i));
            labels.add(titleMarksArrayList.get(key));
            i++;
        }

        BarDataSet bardataset = new BarDataSet(entries, "Cells");
        barChart.setAutoScaleMinMaxEnabled(true);

        BarData data = new BarData(labels, bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinValue(0f); // start at zero
        yAxis.setAxisMaxValue(100f); // the axis maximum is 100
        yAxis.setTextColor(Color.BLACK);
        yAxis.setGranularity(1f); // interval 1
        yAxis.setLabelCount(6, true);

        YAxis yAxis2 = barChart.getAxisRight();
        yAxis2.setEnabled(false);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription("Set Bar Chart Description Here");  // set the description

        barChart.animateY(5000);
    }


}
