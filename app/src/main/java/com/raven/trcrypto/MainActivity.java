package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.raven.trcrypto.Adapter.CoinAdapter;
import com.raven.trcrypto.Interface.ILoadMore;
import com.raven.trcrypto.Model.CoinModel;
import com.squareup.picasso.OkHttpDownloader;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadFirst10coin(0);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                items.clear();
                loadFirst10coin(0);
                setupAdapter();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.coin_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            Toast.makeText(getApplicationContext(),"You Click Account",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupAdapter(){
        adapter=new CoinAdapter(recyclerView,MainActivity.this,items);
        recyclerView.setAdapter(adapter);
        adapter.setiLoadMore(new ILoadMore(){
            @Override
            public void onLoadMore(){
                if(items.size()<=1000){
                    loadNext10Coin(items.size());
                }else
                {
                    Toast.makeText(MainActivity.this,"MAX",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void loadNext10Coin(int index){
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
                Log.d("aaab", "onResponse: "+body);
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

                        adapter.updateData(newItems);

                    }
                });

            }
        });
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

    }

}