package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.raven.trcrypto.Adapter.RecycleBankAdapter;
import com.raven.trcrypto.Model.bank_account;

import java.util.ArrayList;
import java.util.List;

public class WithdrawActivity extends AppCompatActivity implements AddBankDialog.AddBankDialogListener {
    ActionBar mActionBar;
    EditText amount;
    TextView balance, addbank;
    Button wd, getblnc;
    String userID;
    private FirebaseUser user;
    private DatabaseReference reference;

    /*
        adapter of recycle view bank
    */
    RecyclerView bank;
    RecycleBankAdapter rba;

    ////
    @Override
    public void applytext(String acc_bank, String nama_bank, int pos) {
        Toast.makeText(WithdrawActivity.this, acc_bank + ", " + nama_bank + ", " + Integer.toString(pos), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        Toolbar toolbar = findViewById(R.id.toolbarmap);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        //------------------------
        Intent intent = getIntent();
        userID = intent.getStringExtra("uid");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getBalance();
        balance = findViewById(R.id.balance);
        amount = findViewById(R.id.amount);
        wd = findViewById(R.id.withdraw);
        getblnc = findViewById(R.id.getall);
        //Toast.makeText(this, "Tereksekusi", Toast.LENGTH_SHORT).show();
        addbank = findViewById(R.id.addbank);

        //RECYCLE BANK ADAPTER
            List<bank_account> akunbank = new ArrayList<>();
            //akunbank.add(new bank_account("Ong William Raven Wijaya", "672018070","1",false));
            //akunbank.add(new bank_account("Samuel Nugraha", "672018053","0",false));
            //akunbank.add(new bank_account("Felix Vicky Lugas Dewangga", "1234567890123456789012345678901234567890","2",false));
            bank = findViewById(R.id.daftarbank);
            bank.setLayoutManager(new LinearLayoutManager(this));
            rba = new RecycleBankAdapter(this, akunbank);
            bank.setAdapter(rba);
            akunbank.add(new bank_account("Ong William Raven Wijaya", "672018070","1",false));
            akunbank.add(new bank_account("Samuel Nugraha", "672018053","0",false));
            akunbank.add(new bank_account("Felix Vicky Lugas Dewangga", "1234567890123456789012345678901234567890","2",false));
            rba.setbankaccount(akunbank);
        ///////////////////////////
        wd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amounts = amount.getText().toString();
                try {
                    if (rba.getSelected() != null) {
                        //Toast.makeText(WithdrawActivity.this, rba.getSelected().getNama(), Toast.LENGTH_SHORT).show();
                        if (amount.equals("")) {
                            Toast.makeText(WithdrawActivity.this, "you have not filled in the amount to be withdrawn", Toast.LENGTH_SHORT).show();
                        } else {
                            if (amounts.isEmpty()) {
                                amounts = "0.00";
                            }
                            Toast.makeText(WithdrawActivity.this, "Request to Withdraw", Toast.LENGTH_SHORT).show();
                            updatebalance(amounts);
                        }
                    } else {
                        Toast.makeText(WithdrawActivity.this, "You're not selected your bank account to be transfered", Toast.LENGTH_SHORT).show();
                    }
                } catch (IndexOutOfBoundsException e) {
                    Toast.makeText(WithdrawActivity.this, "Empty bank to withdraw, fill it", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getblnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getallbalance();
            }
        });
        addbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addbank_dialog();
                Toast.makeText(WithdrawActivity.this, "add bank", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addbank_dialog() {
        AddBankDialog abd = new AddBankDialog();
        abd.show(getSupportFragmentManager(), "Dialog Add Bank");
    }

    ;

    public void getallbalance() {
        String f = (String) balance.getText();

        f = f.replace("$", "");
        Toast.makeText(WithdrawActivity.this, f, Toast.LENGTH_SHORT).show();
        String belakang_koma = f.split("[.]")[1];
        String hasil = "";
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
                        if (us != null) {
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
    }

    ;

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

    public void updatebalance(String amounte) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String walletid = userProfile.getWalletid();
                    //  user= FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Wallets");
                    reference.child(walletid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Wallet wallet = snapshot.getValue(Wallet.class);
                            if (wallet != null) {
                                Double walletrp = Double.parseDouble(getDecimalFormat(wallet.getRp()));
                                Log.e("jumlah wallet anda", Double.toString(walletrp));
                                if (Double.valueOf(amounte) >= 1.00) {
                                    if (walletrp - Double.valueOf(amounte) == 0) {
                                        reference.child(walletid).child("rp").setValue("0.00");
                                        Toast.makeText(WithdrawActivity.this, "Withdraw success", Toast.LENGTH_SHORT).show();
                                    } else if (walletrp - Double.valueOf(amounte) >= 0.00) {
                                        reference.child(walletid).child("rp").setValue(String.valueOf(Double.valueOf(wallet.getRp()) - Double.valueOf(amounte)));
                                        Toast.makeText(WithdrawActivity.this, "Withdraw success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(WithdrawActivity.this, "Insufficient amount to withdraw", Toast.LENGTH_SHORT).show();
                                    }
                                    getBalance();
                                } else {
                                    Toast.makeText(WithdrawActivity.this, "Minimum amount to withdraw $1.00", Toast.LENGTH_SHORT).show();
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