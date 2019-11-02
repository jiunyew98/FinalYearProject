package com.example.logindemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.gson.Gson;

import java.util.ArrayList;

public class SubjectFragment extends Fragment {

    private ArrayList<SubjectParent> subjectParentArrayList = new ArrayList<>();
    private SubjectAdapter subjectAdapter;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_subject, container, false);

        listView = (ListView) v.findViewById(R.id.SubjectListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(SubjectDetailActivity.newInstance(getContext(), subjectParentArrayList.get(position)));
            }
        });

        getSubjectData();
        return v;
    }



    private void getSubjectData() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(KeyTag.USERS_KEY).child(KeyTag.STUDENT_KEY).child(FirebaseAuth.getInstance().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                subjectParentArrayList.clear();
                if (userProfile.getSubjectParentArrayList() != null)
                    for (String postSnapshot : userProfile.getSubjectParentArrayList()) {
                        SubjectParent subjectParent = new Gson().fromJson(postSnapshot, SubjectParent.class);
                        subjectParentArrayList.add(subjectParent);
                    }

                subjectAdapter = new SubjectAdapter(getContext(), subjectParentArrayList,null);
                listView.setAdapter(subjectAdapter);
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
