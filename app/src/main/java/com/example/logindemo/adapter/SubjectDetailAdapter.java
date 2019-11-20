package com.example.logindemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.logindemo.QuizDetailActivity;
import com.example.logindemo.R;
import com.example.logindemo.model.Notes;
import com.example.logindemo.model.Quiz;
import com.example.logindemo.model.QuizParent;
import com.example.logindemo.model.SubjectParent;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetailAdapter extends RecyclerView.Adapter<SubjectDetailAdapter.ViewHolder> {

    private List<String> mData;
    private List<Notes> notesList = new ArrayList<>();
    private List<QuizParent> quizList = new ArrayList<>();
    private SubjectParent subjectParent = new SubjectParent();
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private int TITLE_TYPE = 0;
    private int DESC_TYPE = 1;


    // data is passed into the constructor
    public SubjectDetailAdapter(Context context, SubjectParent subjectParent, ArrayList<Notes> notesList, ArrayList<QuizParent> quizList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.notesList = notesList;
        this.quizList = quizList;
        this.subjectParent = subjectParent;
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

        //setup view

        holder.itemView.setOnClickListener(null);
        //set title
        if (getItemViewType(position) == TITLE_TYPE) {
            if (notesList != null && position == 0) {
                text = "Notes";
            } else {
                text = "Quiz";
            }
        } else {
            //set data
            if (notesList == null || position > notesList.size()) {
                final QuizParent quizParent = notesList != null ? quizList.get(position - (notesList.size() == 0 ? 1 : notesList.size() + 2)) :
                        quizList.get(position - 1);
                text = quizParent.getTitle();

                //open quiz detail
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(QuizDetailActivity.newInstance(context, subjectParent, quizParent));
                    }
                });
            } else {
                text =  notesList.get(position - 1).getNote();
            }
        }

        holder.myTextView.setText(text);
    }

    @Override
    public int getItemViewType(int position) {
        //to pass different view
        Log.d("###", "item view type " + position);
        if (position == 0 || (notesList != null && position == notesList.size() + 1)) {
            return TITLE_TYPE;
        } else {
            return DESC_TYPE;
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        //set item count
        if ((notesList != null && notesList.size() != 0) || (quizList != null && quizList.size() != 0)) {
            int total = 0;

            //plus 1 for title
            if (notesList != null && notesList.size() != 0) {
                total += notesList.size() + 1;
            }

            if (quizList != null && quizList.size() != 0) {
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
