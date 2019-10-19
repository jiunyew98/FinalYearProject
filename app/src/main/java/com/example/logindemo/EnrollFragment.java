package com.example.logindemo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EnrollFragment extends Fragment {


    ListView listView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_enroll,container,false);


        String[] listview = {"Subject 1","Subject 2","Subject 3"};

        ListView listView = (ListView) v.findViewById(R.id.listview);

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(

                getActivity(),
                android.R.layout.simple_list_item_1,
                listview
        );

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                openDialog();


            }
        });



        return v;




    }

    public void openDialog(){

        EnrollDialog enrollDialog = new EnrollDialog();
        enrollDialog.show(getFragmentManager(), "Dialog Example" );


    }
}
