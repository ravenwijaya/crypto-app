package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WithdrawActivity extends AppCompatActivity {
    ActionBar mActionBar;
    EditText amount;
    TextView balance;
    Button wd,getblnc;
    String userID;
    private FirebaseUser user;
    private DatabaseReference reference;



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
        getBalance();
        balance = findViewById(R.id.balance);
        amount=findViewById(R.id.amount);
        wd=findViewById(R.id.withdraw);
        getblnc = findViewById(R.id.getall);
        Toast.makeText(this, "Tereksekusi", Toast.LENGTH_SHORT).show();
        wd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amounts=amount.getText().toString();
                if(amount.equals("")){
                    Toast.makeText(WithdrawActivity.this,"you have not filled in the amount to be withdrawn",Toast.LENGTH_SHORT).show();
                }else{
                    if (amounts.isEmpty()){
                        amounts="0.00";
                    }
                    Toast.makeText(WithdrawActivity.this,"Request to Withdraw",Toast.LENGTH_SHORT).show();
                    updatebalance(amounts);
                }
            }
        });
        getblnc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getallbalance();
            }
        });
    }

    public void getallbalance(){
        String f = (String) balance.getText();

        f = f.replace("$","");
        Toast.makeText(WithdrawActivity.this, f, Toast.LENGTH_SHORT).show();
        String belakang_koma = f.split("[.]")[1];
        String hasil="";
        if (belakang_koma.length() == 1) {
            hasil = f.split("[.]")[0] +
                    "." + belakang_koma.substring(0, 1) +
                    String.format("%0" + 1 + "d", 0);
        } else {
             hasil = f.split("[.]")[0]
                    + "." + belakang_koma.substring(0, 2);
        }
        amount.setText(hasil);
    }

    public void getBalance() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User us = snapshot.getValue(User.class);
                        if(us!=null){
                            String walletid = us.getWalletid();
                            reference = FirebaseDatabase.getInstance().getReference("Wallets");
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference("Wallets")
                                    .child(walletid)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Wallet w = snapshot.getValue(Wallet.class);
                                            if (w != null) {
                                                balance.setText("$" + getDecimalFormat(w.getRp()));
                                            } else {
                                                balance.setText("Can't load null value");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(WithdrawActivity.this, "Error !!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(WithdrawActivity.this, "Error !!", Toast.LENGTH_SHORT).show();
                    }
                });
    };

    private static String getDecimalFormat(String value) {
        String getValue = String.valueOf(value).split("[.]")[1];
        if (getValue.length() == 1) {
            return String.valueOf(value).split("[.]")[0] +
                    "." + getValue.substring(0, 1) +
                    String.format("%0" + 1 + "d", 0);
        } else {
            return String.valueOf(value).split("[.]")[0]
                    + "." + getValue.substring(0, 2);
        }
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
                    //  user= FirebaseAuth.getInstance().getCurrentUser();
                    reference= FirebaseDatabase.getInstance().getReference("Wallets");
                    reference.child(walletid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Wallet wallet=snapshot.getValue(Wallet.class);
                            if(wallet!=null){
                                Double walletrp = Double.parseDouble(getDecimalFormat(wallet.getRp()));
                                if(walletrp - Double.valueOf(amounte) >= 0){
                                    if(walletrp - Double.valueOf(amounte) == 0){
                                        reference.child(walletid).child("rp").setValue("0.00");
                                    }else{
                                        reference.child(walletid).child("rp").setValue(String.valueOf(Double.valueOf(wallet.getRp())-Double.valueOf(amounte)));
                                    }
                                    Toast.makeText(WithdrawActivity.this,"Withdraw success",Toast.LENGTH_SHORT).show();
                                    getBalance();
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