package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    ActionBar mActionBar;
    String userID;
    private FirebaseUser user;
    private DatabaseReference reference;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME="mypref";
    TextView email,name;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        logout=findViewById(R.id.logout);
        email=findViewById(R.id.emails);
        name=findViewById(R.id.name);
        loaduser();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Toast.makeText(ProfileActivity.this,"Log out successfull",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(ProfileActivity.this , SigninActivity.class));

            }
        });

    }
    private void loaduser(){
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    email.setText(userProfile.getEmail());
                    name.setText(userProfile.getFullname());

                   }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}