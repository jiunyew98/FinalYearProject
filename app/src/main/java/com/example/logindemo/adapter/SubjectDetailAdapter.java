package com.example.logindemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.logindemo.R;
import com.example.logindemo.model.Notes;
import com.example.logindemo.model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetailAdapter extends RecyclerView.Adapter<SubjectDetailAdapter.ViewHolder> {

    private List<String> mData;
    private List<Notes> notesList = new ArrayList<>();
    private List<Quiz> quizList = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private int TITLE_TYPE = 0;
    private int DESC_TYPE = 1;


    // data is passed into the constructor
    public SubjectDetailAdapter(Context context, ArrayList<Notes> notesList, ArrayList<Quiz> quizList) {
        this.mInflater = LayoutInflater.from(context);
        this.notesList = notesList;
        this.quizList = quizList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == TITLE_TYPE) {
            view = mInflater.inflate(R.layout.layout_title, parent, false);
        } else {
            view = mInflater.inflate(R.layout.layout_detail, parent, false);
        }

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String text;
        Log.d("###", String.valueOf(getItemViewType(position)));

        if (getItemViewType(position) == TITLE_TYPE) {
            if (position == 0) {
                text = "Notes";
            } else {
                text = "Quiz";
            }
        } else {
            if (position > notesList.size()) {
                text = quizList.get(position - (notesList.size() == 0 ? 1 : notesList.size() + 1)).getTitle();
            } else {
                text = notesList.get(position - 1).getNote();
            }
        }

        holder.myTextView.setText(text);
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("###", "item view type " + position);
        if (position == 0 || position == notesList.size() + 1) {
            return TITLE_TYPE;
        } else {
            return DESC_TYPE;
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if (notesList.size() != 0 || quizList.size() != 0) {
            int total = 0;

            if (notesList.size() != 0) {
                total += notesList.size() + 1;
            }

            if (quizList.size() != 0) {
                total += quizList.size() + 1;
            }

            return total;
        } else {
            return 0;
        }
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.text);
        }

    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
