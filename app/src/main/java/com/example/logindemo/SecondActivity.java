package com.example.logindemo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class SecondActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private Button logout;
    private DrawerLayout drawer;
    private ImageView navprofilePic;
    private TextView navprofileName, navprofileEmail;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_feed);
        }


        firebaseAuth = FirebaseAuth.getInstance();

        loadFragment(new FeedFragment());



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        navprofilePic = (ImageView) findViewById(R.id.NavProfilePic);
        navprofileName = (TextView) findViewById(R.id.NavProfileName);
        navprofileEmail = (TextView) findViewById(R.id.NavProfileEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child(KeyTag.USERS_KEY).child(KeyTag.STUDENT_KEY).child(firebaseAuth.getUid());

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Load the image to image View using picasso.
                Picasso.get().load(uri).fit().centerCrop().into(navprofilePic);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Singleton.getInstance().userProfile = dataSnapshot.getValue(UserProfile.class);
                navprofileName.setText( Singleton.getInstance().userProfile.getUserName());
                navprofileEmail.setText( Singleton.getInstance().userProfile.getUserEmail());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(null, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.nav_feed:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FeedFragment()).addToBackStack(null).commit(); //add to back stack is used to allow user to use back button in a more efficient way

                break;

            case R.id.nav_profile:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).addToBackStack(null).commit();

                break;

            case R.id.nav_enroll:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EnrollFragment()).addToBackStack(null).commit();

                break;

            case R.id.nav_subject:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SubjectFragment()).addToBackStack(null).commit();

                break;

            case R.id.nav_chat:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChatFragment()).addToBackStack(null).commit();

                break;

            case R.id.nav_progress:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProgressFragment()).addToBackStack(null).commit();

                break;

            case R.id.nav_LogOut:

                Logout();

                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{

            super.onBackPressed();
        }

    }


    private void Logout(){

        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SecondActivity.this, MainActivity.class));

    }


}
