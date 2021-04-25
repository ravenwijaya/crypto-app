package com.raven.trcrypto;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class deposit2 extends AppCompatActivity {
    ActionBar mActionBar;
    EditText amount;
    Button depos;
    String userID;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit2);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        userID=intent.getStringExtra("uid");
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        amount=findViewById(R.id.amountss);

        depos=findViewById(R.id.btndepos);

        depos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amounts=amount.getText().toString();
                if (amounts.equals("") || Double.valueOf(amounts)<=0) {
                    amount.setError("Amount is Required");
                    amount.requestFocus();
                }else{
                    updatebalance(amounts);
                }




            }
        });


    }
    public void updatebalance(String amounte){
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    String walletid=userProfile.getWalletid();
                    reference= FirebaseDatabase.getInstance().getReference("Wallets");
                    reference.child(walletid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Wallet wallet=snapshot.getValue(Wallet.class);
                            if(wallet!=null){
                                reference.child(walletid).child("rp").setValue(String.valueOf(Double.valueOf(wallet.getRp())+Double.valueOf(amounte)));
                                Toast.makeText(deposit2.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(deposit2.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(deposit2.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}