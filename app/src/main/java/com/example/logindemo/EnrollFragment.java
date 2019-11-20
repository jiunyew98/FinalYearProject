package com.example.logindemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.logindemo.adapter.SubjectAdapter;
import com.example.logindemo.model.QuizParent;
import com.example.logindemo.model.SubjectParent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EnrollFragment extends Fragment {

    private ArrayList<SubjectParent> subjectParentArrayList = new ArrayList<>();
    private SubjectAdapter subjectAdapter;
    ListView listView;
    private UserProfile userProfile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_enroll,container,false);

        listView = (ListView) v.findViewById(R.id.EnrollListView);

        getUserData();
        return v;
    }

    //get user data to refresh user subject parent list
    private void getUserData(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(KeyTag.USERS_KEY).child(KeyTag.STUDENT_KEY).child(FirebaseAuth.getInstance().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
                getData();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(KeyTag.SUBJECT_KEY);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subjectParentArrayList.clear();
                //get lecturer list
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    //get subject inside lecturer list
                    for (DataSnapshot childSnapshot: postSnapshot.getChildren()) {

                        SubjectParent university = childSnapshot.getValue(SubjectParent.class);
                        university.setLecturerId(postSnapshot.getKey());
                        //add into array list for list view adapter
                        subjectParentArrayList.add(university);
                    }
                }

                subjectAdapter = new SubjectAdapter(getContext(), subjectParentArrayList,userProfile);
                listView.setAdapter(subjectAdapter);
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
