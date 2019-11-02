package com.example.logindemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.logindemo.adapter.SubjectDetailAdapter;
import com.example.logindemo.model.QuizParent;
import com.example.logindemo.model.SubjectParent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

        getSubjectDetail(subjectParent);

    }

    void getSubjectDetail(final SubjectParent subjectParent){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(KeyTag.SUBJECT_KEY).child(subjectParent.getLecturerId()).child(subjectParent.getId());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               SubjectParent data = dataSnapshot.getValue(SubjectParent.class);
                ArrayList<QuizParent> quizParents = new ArrayList<>();
                for(String key : data.getQuiz().keySet()){
                    quizParents.add(data.getQuiz().get(key));
                }

                data.setLecturerId(subjectParent.getLecturerId());
                subjectDetailAdapter = new SubjectDetailAdapter(SubjectDetailActivity.this,data, data.getNotesArrayList(),quizParents);
                subjectDetailAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(subjectDetailAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(SubjectDetailActivity.this));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(null, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void initView(){
        recyclerView = findViewById(R.id.rv);
    }
}
