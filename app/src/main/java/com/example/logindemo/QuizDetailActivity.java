package com.example.logindemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logindemo.adapter.SubjectDetailAdapter;
import com.example.logindemo.model.Quiz;
import com.example.logindemo.model.QuizParent;
import com.example.logindemo.model.SubjectParent;
import com.example.logindemo.model.UserAnswer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;


public class QuizDetailActivity extends AppCompatActivity {
    public static String QUIZ_KEY = "QUIZ_KEY";
    public static String SUBJECT_KEY = "SUBJECT_KEY";

    private LinearLayout parentLayout;
    private TextView titleET,submitQuizButton;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();
    private ArrayList<View> childViewList = new ArrayList<View>();
    private QuizParent quizParent = new QuizParent();
    private SubjectParent subjectParent = new SubjectParent();

    //open from subject detail adapter
    public static Intent newInstance(Context context,SubjectParent subjectParent, QuizParent quizParent){
        Intent intent = new Intent(context,QuizDetailActivity.class);
        intent.putExtra(SUBJECT_KEY, new Gson().toJson(subjectParent));
        intent.putExtra(QUIZ_KEY, new Gson().toJson(quizParent));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupDetail();

        if(getIntent() != null && getIntent().getStringExtra(QUIZ_KEY) != null){
            //retrieve data that pass into here
            quizParent = new Gson().fromJson(getIntent().getStringExtra(QUIZ_KEY), QuizParent.class);
            subjectParent = new Gson().fromJson(getIntent().getStringExtra(SUBJECT_KEY), SubjectParent.class);

            titleET.setText(quizParent.getTitle());

            //add quiz layout
            for(int a= 0; a<quizParent.getQuizArrayList().size(); a++){
                addLayout(quizParent.getQuizArrayList().get(a));
            }

            getQuizDetail();
        }
    }

    private void addLayout(Quiz quiz){
        final View createQuizChild =  this.getLayoutInflater().inflate(R.layout.layout_quiz_items, null);

        if(quiz != null){
            ((TextView)createQuizChild.findViewById(R.id.questionET)).setText(quiz.question);
            ((RadioGroup)createQuizChild.findViewById(R.id.radioAnswer)).clearCheck();
        }

        childViewList.add(createQuizChild);
        parentLayout.addView(createQuizChild);
    }

    void getQuizDetail(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(KeyTag.SUBJECT_KEY).child(subjectParent.getLecturerId()).child(subjectParent.getId())
                .child(KeyTag.QUIZ_KEY).child(quizParent.getId()).child(KeyTag.ANSWER_KEY).child(FirebaseAuth.getInstance().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAnswer data = dataSnapshot.getValue(UserAnswer.class);

                //to check if user has already answer the quiz
                if(data != null){
                    submitQuizButton.setVisibility(View.GONE);

                    for (int b = 0; b < data.getAnswerQuizList().size(); b++) {
                        RadioGroup radioGroup = childViewList.get(b).findViewById(R.id.radioAnswer);
                        childViewList.get(b).findViewById(R.id.radioTrue).setEnabled(false);
                        childViewList.get(b).findViewById(R.id.radioFalse).setEnabled(false);
                        radioGroup.check(data.getAnswerQuizList().get(b).answer ? R.id.radioTrue : R.id.radioFalse);
                        radioGroup.setEnabled(false);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(null, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendUserData() {
        int counter = 0;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(KeyTag.SUBJECT_KEY).child(subjectParent.getLecturerId()).child(subjectParent.getId()).child(KeyTag.QUIZ_KEY).child(quizParent.getId()).child(KeyTag.ANSWER_KEY);
        //get user answer
        for (int a= 0 ; a< childViewList.size() ; a++){
            View childView = childViewList.get(a);
            String question = ((TextView)childView.findViewById(R.id.questionET)).getText().toString();
            Boolean answer = ((RadioGroup) childView.findViewById(R.id.radioAnswer)).getCheckedRadioButtonId() == R.id.radioTrue;

            Boolean studentAnswer = answer == quizParent.getQuizArrayList().get(a).answer;

            //to check how many answer correct
            if(studentAnswer){
                counter += 1;
            }

            //add into quizList
            quizList.add(new Quiz(question,answer,studentAnswer));
        }
        //send to database
        UserAnswer quizParent = new UserAnswer(FirebaseAuth.getInstance().getUid(),Singleton.getInstance().userProfile.userName,quizList,counter);
        myRef.child(quizParent.getId()).setValue(quizParent);
        myRef.push();

        //display how many correct answer user answered
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            new AlertDialog.Builder(QuizDetailActivity.this)
                    .setTitle("")
                    .setMessage("You've got " + counter + " out of " + this.quizParent.getQuizArrayList().size() + " question correct !" )

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                            Toast.makeText(QuizDetailActivity.this, "Create Successful", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }else{
            //to close the activity
            finish();
            Toast.makeText(QuizDetailActivity.this, "Create Successful", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupDetail(){
        parentLayout = findViewById(R.id.parentLayout);
        titleET= findViewById(R.id.titleET);
        submitQuizButton = findViewById(R.id.submitQuizButton);

        submitQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserData();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
