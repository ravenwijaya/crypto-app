package com.raven.trcrypto;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignupActivity extends AppCompatActivity{

    private EditText mEmail , mPass,mName;
    private TextView mTextView;
    private Button signUpBtn;
    private FirebaseAuth mAuth;

    private String walletID;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
  //  private DatabaseReference mFirebaseDatabase;
   // private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmail = findViewById(R.id.email);
        mPass = findViewById(R.id.password);
        mName = findViewById(R.id.fullname);
        mTextView = findViewById(R.id.textview);
        signUpBtn = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
       // mFirebaseDatabase = mFirebaseInstance.getReference("users");

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this , SigninActivity.class));
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();


            }
        });

    }
    private void createUser(){

        String email = mEmail.getText().toString().trim();
        String pass = mPass.getText().toString().trim();
        String name= mName.getText().toString().trim();
        String walletid="";



        if(email.isEmpty()){
            mEmail.setError("Email is Required");
            mEmail.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            mPass.setError("Pass is Required");
            mPass.requestFocus();
            return;
        }
        if(name.isEmpty()){
            mName.setError("Name is Required");
            mName.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            User user=new User(name,email,pass,walletid);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //createWallet( FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                                    if(task.isSuccessful()){
                                        createWallet();
                                        Toast.makeText(SignupActivity.this,"Success",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignupActivity.this , SigninActivity.class));

                                    }else{
                                        Toast.makeText(SignupActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(SignupActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void createWallet(){
       // user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Wallets");

       // Log.d("fuck2", user.getUid());
        walletID = reference.push().getKey();
        Wallet wallet = new Wallet("0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00" );
        reference.child(walletID).setValue(wallet);
        fillwalletid(walletID);


      //  createUser(walletID);


    }
    private void fillwalletid(String walletID){
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
       // Log.d("fuck1", user.getUid());
        reference.child(userID).child("walletid").setValue(walletID);
    }
//////

}