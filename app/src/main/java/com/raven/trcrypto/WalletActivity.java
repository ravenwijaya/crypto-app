package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WalletActivity extends AppCompatActivity {
    ActionBar mActionBar;
    String userID;
    private FirebaseUser user;
    private DatabaseReference reference;
    TextView usd,aave,ada,btc,doge,dot,link,uni,sushi,rune;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        usd=findViewById(R.id.usd);
        aave=findViewById(R.id.aave);
        ada=findViewById(R.id.ada);
        btc=findViewById(R.id.btc);
        doge=findViewById(R.id.doge);
        dot=findViewById(R.id.dot);
        link=findViewById(R.id.link);
        uni=findViewById(R.id.uni);
        sushi=findViewById(R.id.sushi);
        rune=findViewById(R.id.rune);
        userID=user.getUid();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                loadBalance();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadBalance();
            }
        });



    }
    private void loadBalance(){
        swipeRefreshLayout.setRefreshing(true);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    String walletid=userProfile.getWalletid();
                    user= FirebaseAuth.getInstance().getCurrentUser();
                    reference= FirebaseDatabase.getInstance().getReference("Wallets");
                    reference.child(walletid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Wallet wallet=snapshot.getValue(Wallet.class);
                            if(wallet!=null){
                                String rp=wallet.getRp();
                                usd.setText("$ "+getDecimalFormat(wallet.getRp()));
                                aave.setText(getDecimalFormat(wallet.getAave()));
                                ada.setText(getDecimalFormat(wallet.getAda()));
                                btc.setText(getDecimalFormat(wallet.getBtc()));
                                doge.setText(getDecimalFormat(wallet.getDoge()));
                                dot.setText(getDecimalFormat(wallet.getDot()));
                                link.setText(getDecimalFormat(wallet.getLink()));
                                uni.setText(getDecimalFormat(wallet.getUni()));
                                sushi.setText(getDecimalFormat(wallet.getSushi()));
                                rune.setText(getDecimalFormat(wallet.getRune()));
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(WalletActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WalletActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();


            }
        });
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

    }
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
}