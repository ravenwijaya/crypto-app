package com.raven.trcrypto;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raven.trcrypto.Adapter.CoinAdapter;
import com.raven.trcrypto.Interface.ILoadMore;
import com.raven.trcrypto.Model.CoinModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    List<CoinModel>items=new ArrayList<>();
    CoinAdapter adapter;
    RecyclerView recyclerView;
    OkHttpClient client;
    Request request;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    private FirebaseUser user;
    private DatabaseReference reference;
    private CardView wall,depo,wd;
    private String userID;
    private RecyclerView.RecyclerListener listener;
    private TextView digit;
    private static final String SHARED_PREF_NAME="mypref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        digit = findViewById(R.id.digit);
        wall=findViewById(R.id.wallete);
        depo=findViewById(R.id.deposit);
        wd=findViewById(R.id.withdraw);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadFirst10coin(0);
                loadBalance();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                loadFirst10coin(0);
                loadBalance();
                setupAdapter();
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.coin_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,WalletActivity.class);
                startActivity(myIntent);

            }
        });
        wd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, WithdrawActivity.class);
                myIntent.putExtra("uid",userID);
                startActivity(myIntent);
            }
        });
        depo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,deposit1.class);
                myIntent.putExtra("uid",userID);
                startActivity(myIntent);

            }
        });

        setupAdapter();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.account){
            startActivity(new Intent(MainActivity.this , ProfileActivity.class));
            Toast.makeText(getApplicationContext(),"You Click Account",Toast.LENGTH_SHORT).show();
        }
//        else if(id==R.id.logout){
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.clear();
//            editor.commit();
//            Toast.makeText(MainActivity.this,"Log out successfull",Toast.LENGTH_SHORT).show();
//            finish();
//            startActivity(new Intent(MainActivity.this , SigninActivity.class));
//        }
        return super.onOptionsItemSelected(item);
    }

    public void setupAdapter(){
        adapter=new CoinAdapter(recyclerView,MainActivity.this,items);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CoinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String symbol=   items.get(position).getName().toLowerCase();
                Intent intent=new Intent(MainActivity.this,OrderActivity.class);
                intent.putExtra("Symbol",symbol);
                startActivity(intent);
            }
        });
    }

    private void loadFirst10coin(int index){
        client=new OkHttpClient();
        request=new Request.Builder().url("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=uniswap%2Caave%2Cpolkadot%2Cdogecoin%2Ccardano%2Cbitcoin%2Cchainlink%2Csushi%2Cthorchain&order=market_cap_desc&sparkline=false&price_change_percentage=1h%2C24h%2C7d").build();
        swipeRefreshLayout.setRefreshing(true);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body=response.body().string();
                Gson gson=new Gson();
                final List<CoinModel> newItems=gson.fromJson(body,new TypeToken<List<CoinModel>>(){}.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items.addAll(newItems);
                        adapter.setLoaded();
                        adapter.updateData(items);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        });
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

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
                                digit.setText(getDecimalFormat(rp));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if(swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
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