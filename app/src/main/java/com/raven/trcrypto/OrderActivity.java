package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
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
import com.raven.trcrypto.Model.CoinModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<CoinModel>items=new ArrayList<>();
    CoinAdapter adapter;
    private ActionBar mActionBar;
    public ImageView coin_icon;
    public EditText amount;
    private Button submitBtn;
    public TextView coin_name,coin_price,one_hour_change,twenty_hours_change,seven_days_change;
    OkHttpClient client;
    Request request;
    String symbol;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        submitBtn=findViewById(R.id.btn_submit);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        symbol=intent.getStringExtra("Symbol");
        coin_icon=findViewById(R.id.iconbig);
        amount=findViewById(R.id.amount);
        coin_name=findViewById(R.id.coinnamee);
        coin_price=findViewById(R.id.currentprice);
        one_hour_change=findViewById(R.id.oneHour);
        twenty_hours_change=findViewById(R.id.twentyfourHour);
        seven_days_change=findViewById(R.id.sevenDay);

        loadData(symbol);
        Spinner spinner=findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.ordertype, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = spinner.getSelectedItem().toString();
                String amounts=amount.getText().toString();
                String pricenow=coin_price.getText().toString();


                order(text,amounts,pricenow);
            }
        });

    }

    private void order(String typeorder,String amounts,String pricenow) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
        //  String walletid=reference.child(userID).child("walletid").toString();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    String walletid=userProfile.getWalletid();
                    //user= FirebaseAuth.getInstance().getCurrentUser();
                    reference= FirebaseDatabase.getInstance().getReference("Wallets");
                    reference.child(walletid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Wallet wallet = snapshot.getValue(Wallet.class);
                            if (wallet != null) {
                                if (typeorder.equals("Buy")) {

                                    if (Double.valueOf(wallet.getRp()) >= 100 && Double.valueOf(wallet.getRp()) >= Double.valueOf(amounts)) {
                                        if (symbol.equals("bitcoin")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getBtc()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("btc").setValue(total);
                                        } else if (symbol.equals("dogecoin")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getDoge()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("doge").setValue(total);
                                        } else if (symbol.equals("cardano")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getAda()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("ada").setValue(total);
                                        } else if (symbol.equals("uniswap")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getUni()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("uni").setValue(total);
                                        } else if (symbol.equals("chainlink")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getLink()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("link").setValue(total);
                                        } else if (symbol.equals("aave")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getAave()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("aave").setValue(total);
                                        } else if (symbol.equals("thorchain")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getRune()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("rune").setValue(total);
                                        } else if (symbol.equals("sushi")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getSushi()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("sushi").setValue(total);
                                        } else if (symbol.equals("polkadot")) {
                                            String total = String.valueOf(Double.valueOf(wallet.getDot()) + (Double.valueOf(amounts) / Double.valueOf(pricenow)));
                                            String rpnow = String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounts));
                                            reference.child(walletid).child("rp").setValue(rpnow);
                                            reference.child(walletid).child("dot").setValue(total);
                                        }
                                    } else {
                                        Toast.makeText(OrderActivity.this, "The minimum buying amount is worth of $100", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if (typeorder.equals("Sell")) {
                                    if (symbol.equals("bitcoin")) {
                                        if (Double.valueOf(amounts)<=Double.valueOf(wallet.getBtc())  ) {
                                            String total = String.valueOf(Double.valueOf(wallet.getBtc()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("btc").setValue(total);
                                        }
                                    } else if (symbol.equals("dogecoin")) {
                                        if (Double.valueOf(amounts)<=Double.valueOf(wallet.getDoge())) {
                                            String total = String.valueOf(Double.valueOf(wallet.getDoge()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("doge").setValue(total);
                                        }
                                    } else if (symbol.equals("cardano")) {
                                        if (Double.valueOf(amounts)<=Double.valueOf(wallet.getAda())  ) {
                                            String total = String.valueOf(Double.valueOf(wallet.getAda()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("ada").setValue(total);
                                        }
                                    } else if (symbol.equals("uniswap")) {
                                        if ( Double.valueOf(amounts)<=Double.valueOf(wallet.getUni()) ) {
                                            String total = String.valueOf(Double.valueOf(wallet.getUni()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("uni").setValue(total);
                                        }
                                    } else if (symbol.equals("chainlink")) {
                                        if (Double.valueOf(amounts)<=Double.valueOf(wallet.getLink())) {
                                            String total = String.valueOf(Double.valueOf(wallet.getLink()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("link").setValue(total);
                                        }
                                    } else if (symbol.equals("aave")) {
                                        if ( Double.valueOf(amounts)<=Double.valueOf(wallet.getAave()) ) {
                                            String total = String.valueOf(Double.valueOf(wallet.getAave()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("aave").setValue(total);
                                        }
                                    } else if (symbol.equals("thorchain")) {
                                        if (Double.valueOf(amounts)<=Double.valueOf(wallet.getRune()) ) {
                                            String total = String.valueOf(Double.valueOf(wallet.getRune()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("rune").setValue(total);
                                        }
                                    } else if (symbol.equals("sushi")) {
                                        if ( Double.valueOf(amounts)<=Double.valueOf(wallet.getSushi())) {
                                            String total = String.valueOf(Double.valueOf(wallet.getSushi()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("sushi").setValue(total);
                                        }
                                    } else if (symbol.equals("polkadot")) {
                                        if (Double.valueOf(amounts)<=Double.valueOf(wallet.getDot())) {
                                            String total = String.valueOf(Double.valueOf(wallet.getDot()) - Double.valueOf(amounts));
                                            String rp=String.valueOf(Double.valueOf(wallet.getRp())+(Double.valueOf(amounts)*Double.valueOf(pricenow)));
                                            reference.child(walletid).child("rp").setValue(rp);
                                            reference.child(walletid).child("dot").setValue(total);
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(OrderActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }}@Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(OrderActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderActivity.this, "ERROR  !!", Toast.LENGTH_SHORT).show();
            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text=parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void loadData(String symbol){
        client=new OkHttpClient();

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids="+symbol+"&order=market_cap_desc&sparkline=false&price_change_percentage=1h%2C24h%2C7d";
        request=new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(OrderActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body=response.body().string();

                Gson gson=new Gson();
                final List<CoinModel> newItems=gson.fromJson(body,new TypeToken<List<CoinModel>>(){}.getType());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        coin_name.setText(newItems.get(0).getName());
                        coin_price.setText(newItems.get(0).getCurrent_price());

                        Picasso.with(OrderActivity.this).load(newItems.get(0).getImage()).into(coin_icon);
                        coin_name.setText(newItems.get(0).getName());
                        coin_price.setText(newItems.get(0).getCurrent_price());
                        one_hour_change.setText(getDecimalFormat(newItems.get(0).getPrice_change_percentage_1h_in_currency())+"%");
                        twenty_hours_change.setText(getDecimalFormat(newItems.get(0).getPrice_change_percentage_24h_in_currency())+"%");
                        seven_days_change.setText(getDecimalFormat(newItems.get(0).getPrice_change_percentage_7d_in_currency())+"%");
                        one_hour_change.setTextColor(newItems.get(0).getPrice_change_percentage_1h_in_currency().contains("-") ?
                                Color.parseColor("#FF0000") : Color.parseColor("#32CD32"));
                        twenty_hours_change.setTextColor(newItems.get(0).getPrice_change_percentage_24h_in_currency().contains("-") ?
                                Color.parseColor("#FF0000") : Color.parseColor("#32CD32"));
                        seven_days_change.setTextColor(newItems.get(0).getPrice_change_percentage_7d_in_currency().contains("-") ?
                                Color.parseColor("#FF0000") : Color.parseColor("#32CD32"));
                    }
                });

            }
        });
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