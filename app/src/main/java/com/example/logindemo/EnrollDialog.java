package com.example.logindemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.Toast;

import com.example.logindemo.model.SubjectParent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EnrollDialog extends AppCompatDialogFragment {

    public SubjectParent subjectParent;
    public Boolean isProfileGet = false;

    public EnrollDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Subject")
                .setMessage("Click to Enroll")
                .setPositiveButton("Enroll", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendUserData();
                    }
                });

        return builder.create();
    }

    private void sendUserData() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(KeyTag.USERS_KEY).child(KeyTag.STUDENT_KEY).child(FirebaseAuth.getInstance().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!isProfileGet) {
                    isProfileGet = true;
                    UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = firebaseDatabase.getReference(KeyTag.SUBJECT_KEY).child(subjectParent.getLecturerId());

                    if (subjectParent.getStudentArrayList() == null) {
                        subjectParent.setStudentArrayList(new ArrayList<UserProfile>());
                    }
                    userProfile.setUserId(FirebaseAuth.getInstance().getUid());
                    subjectParent.getStudentArrayList().add(userProfile);

                    myRef.child(subjectParent.getId()).setValue(subjectParent);
                    myRef.push();

                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(KeyTag.USERS_KEY).child(KeyTag.STUDENT_KEY).child(FirebaseAuth.getInstance().getUid());
                    if (userProfile.getSubjectParentArrayList() == null) {
                        userProfile.setSubjectParentArrayList(new ArrayList<String>());
                    }
                    userProfile.getSubjectParentArrayList().add(new Gson().toJson(subjectParent));

                    userRef.setValue(userProfile);
                    userRef.push();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(getContext(), "Enroll Successful", Toast.LENGTH_SHORT).show();
    }

}
