package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WithdrawActivity extends AppCompatActivity {
    ActionBar mActionBar;
    EditText amount;
    Button wd;
    String userID;
    private FirebaseUser user;
    private DatabaseReference reference;
    private List<Button> x = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        Toolbar toolbar=findViewById(R.id.toolbarmap);
        setSupportActionBar(toolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        //------------------------
        Intent intent=getIntent();
        userID=intent.getStringExtra("uid");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        amount=findViewById(R.id.amount);
        wd=findViewById(R.id.withdraw);
        Toast.makeText(this, "Tereksekusi", Toast.LENGTH_SHORT).show();
        Log.e("mencoba","ini saya");
        wd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amounts=amount.getText().toString();
                if (amounts.isEmpty()){
                    amounts="0.00";
                }
                Log.e("tereksekusi", "click on withdraww");
                Toast.makeText(WithdrawActivity.this,"mencoba",Toast.LENGTH_SHORT).show();
                updatebalance(amounts);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updatebalance(String amounte){
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        Log.d("eee", "updatebalance: "+userID);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    String walletid=userProfile.getWalletid();
                    //  user= FirebaseAuth.getInstance().getCurrentUser();
                    reference= FirebaseDatabase.getInstance().getReference("Wallets");
                    reference.child(walletid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Wallet wallet=snapshot.getValue(Wallet.class);
                            if(wallet!=null){
                                if(Double.valueOf(wallet.getRp()) - Double.valueOf(amounte) > 0){
                                    reference.child(walletid).child("rp").setValue(String.valueOf(Double.valueOf(wallet.getRp())-Double.valueOf(amounte)));
                                    Toast.makeText(WithdrawActivity.this,"Withdraw success",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(WithdrawActivity.this,"Insufficient amount to withdraw",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(WithdrawActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WithdrawActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}