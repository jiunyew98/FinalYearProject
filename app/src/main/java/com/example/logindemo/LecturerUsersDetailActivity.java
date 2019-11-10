package com.example.logindemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class LecturerUsersDetailActivity extends AppCompatActivity {

    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> keyList = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_users_detail);

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        pd = new ProgressDialog(LecturerUsersDetailActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://finalyear-56381.firebaseio.com/users/lecturer.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(LecturerUsersDetailActivity.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Singleton.getInstance().userProfile.chatWith = al.get(position);
                startActivity(new Intent(LecturerUsersDetailActivity.this, LecturerChatDetailActivity.class));
            }
        });
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                UserProfile userProfile = new Gson().fromJson(obj.get(key).toString(), UserProfile.class);

                if(!userProfile.getUserName().equals(Singleton.getInstance().userProfile.userName)) {
                    al.add(userProfile.userName);
                    keyList.add(key);
                    totalUsers++;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers < 1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }

}