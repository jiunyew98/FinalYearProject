package com.example.logindemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.logindemo.adapter.SubjectAdapter;
import com.example.logindemo.adapter.SubjectDetailAdapter;
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
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProgressFragment extends Fragment {

    private ArrayList<SubjectParent> subjectParentArrayList = new ArrayList<>();
    private HashMap<String,String> quizHashMap= new HashMap<>();
    private HashMap<String,UserAnswer> subjectMarksArrayList= new HashMap<>();
    private HashMap<String,SubjectParent> subjectWithMarksArrayList = new HashMap<>();
    private ArrayList<String> quizWithMarksArrayList = new ArrayList<>();
    private BarChart barChart;
    private LineChart lineChart;
    private static final String TAG = "ProgressFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_progress,container,false);

        barChart = v.findViewById(R.id.barchart);

        getData();

        return v;
    }


    private void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(KeyTag.SUBJECT_KEY);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subjectParentArrayList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot childSnapshot: postSnapshot.getChildren()) {

                        SubjectParent university = childSnapshot.getValue(SubjectParent.class);
                        university.setLecturerId(postSnapshot.getKey());
                        subjectParentArrayList.add(university);
                        for(DataSnapshot quizSnapshot : postSnapshot.child(university.getId()).child("quiz").getChildren()){
                            QuizParent quizParent = quizSnapshot.getValue(QuizParent.class);

                            for(DataSnapshot answerSnapShot : quizSnapshot.child("answer").getChildren()){
                                UserAnswer quiz = answerSnapShot.getValue(UserAnswer.class);
                                if (quiz.getId().equals(FirebaseAuth.getInstance().getUid())) {
                                    if (!subjectMarksArrayList.containsKey(university.getId())) {
                                        UserAnswer existMark = new UserAnswer();
                                        existMark.setTotalCorrect(quiz.getTotalCorrect());
                                        existMark.setAnswerQuizList(quiz.getAnswerQuizList());
                                        subjectMarksArrayList.put(university.getId(), existMark);
                                        subjectWithMarksArrayList.put(university.getId(), university);
                                    } else {
                                        UserAnswer existMark = subjectMarksArrayList.get(university.getId());
                                        int marks = existMark.getTotalCorrect() + quiz.getTotalCorrect();
                                        ArrayList<Quiz> total = existMark.getAnswerQuizList();
                                        total.addAll(quiz.getAnswerQuizList());
                                        existMark.setTotalCorrect(marks);
                                        existMark.setAnswerQuizList(total);
                                        subjectMarksArrayList.put(university.getId(), existMark);
                                    }
                                    quizHashMap.put(university.getId(), quizParent.getId());
                                } else {

                                    if (!subjectMarksArrayList.containsKey(university.getId())) {
                                        UserAnswer existMark = new UserAnswer();
                                        existMark.setTotalCorrect(0);
                                        existMark.setAnswerQuizList(new ArrayList<Quiz>());
                                        subjectMarksArrayList.put(university.getId(), existMark);
                                        subjectWithMarksArrayList.put(university.getId(), university);
                                        quizHashMap.put(university.getId(), quizParent.getId());
                                    }
                                }

                            }
                        }
                    }
                }

                setupBarChart();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupBarChart(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        if(subjectMarksArrayList == null)
            return;

        int i = 0;
        for (String key :  subjectMarksArrayList.keySet()){
            entries.add(new BarEntry(((Float.valueOf(subjectMarksArrayList.get(key).getTotalCorrect()) / Float.valueOf(subjectMarksArrayList.get(key).getAnswerQuizList().size())) * 100), i));
            labels.add(subjectWithMarksArrayList.get(key).getTitle());
            quizWithMarksArrayList.add(key);
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

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                getContext().startActivity(QuizProgressActivity.newInstance(getContext(),
                        subjectWithMarksArrayList.get(quizWithMarksArrayList.get(e.getXIndex())).getLecturerId(),quizWithMarksArrayList.get(e.getXIndex())));
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }


}
