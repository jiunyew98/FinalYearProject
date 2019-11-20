package com.example.logindemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.logindemo.EnrollDialog;
import com.example.logindemo.R;
import com.example.logindemo.UserProfile;
import com.example.logindemo.model.SubjectParent;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by tutlane on 23-08-2017.
 */

//adapter is helping on creating view for list view
public class SubjectAdapter extends BaseAdapter {
    private ArrayList<SubjectParent> listData;
    private LayoutInflater layoutInflater;
    private UserProfile userProfile;
    private Context context;

    public SubjectAdapter(Context aContext, ArrayList<SubjectParent> listData, UserProfile userProfile) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        this.userProfile = userProfile;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View v, ViewGroup vg) {
        final ViewHolder holder;
        if (v == null) {

            v = layoutInflater.inflate(R.layout.item_subject, null);
            holder = new ViewHolder();
            holder.title = (TextView) v.findViewById(R.id.subjectTextView);
            holder.enrollButton = (TextView) v.findViewById(R.id.enrollButton);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.title.setText(listData.get(position).getTitle());

        //disable enroll button when user has already enroll
        //if user profile is null, hide enroll button
        if (userProfile != null && userProfile.getSubjectParentArrayList() != null) {
            holder.enrollButton.setVisibility(View.VISIBLE);
            for (String object : userProfile.getSubjectParentArrayList()) {
                SubjectParent subjectParent = new Gson().fromJson(object, SubjectParent.class);
                if (subjectParent.getId().equals(listData.get(position).getId())) {
                    holder.enrollButton.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            holder.enrollButton.setVisibility(userProfile != null ? View.VISIBLE : View.INVISIBLE);
        }

        holder.enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if enroll button is shown
                if (holder.enrollButton.getVisibility() == View.VISIBLE) {
                    openDialog(listData.get(position));
                }
            }
        });

        return v;
    }

    public void openDialog(SubjectParent subjectParent) {
        //pass subject to enroll dialog
        EnrollDialog enrollDialog = new EnrollDialog();
        enrollDialog.subjectParent = subjectParent;
        enrollDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Dialog Example");

    }

    static class ViewHolder {
        TextView title;
        TextView enrollButton;
    }
}
