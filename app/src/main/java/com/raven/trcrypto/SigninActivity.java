package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {
    /// felix
    private Button gomaps,godepo;
    ///
    private EditText mEmail , mPass;
    private TextView mTextView;
    private Button signInBtn;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME="mypref";
   // private static final String KEY_NAME="name";
    private static final String KEY_EMAIL="email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mEmail = findViewById(R.id.emailsignin);
        mPass = findViewById(R.id.passwordsignin);
        signInBtn = findViewById(R.id.btn_signin);
        mTextView = findViewById(R.id.textviewsignin);
        gomaps = findViewById(R.id.gomaps);

        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String email=sharedPreferences.getString(KEY_EMAIL,null);
        if(email!=null){
            startActivity(new Intent(SigninActivity.this , MainActivity.class));
        }




        mAuth = FirebaseAuth.getInstance();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this , SignupActivity.class));
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString(KEY_EMAIL,mEmail.getText().toString());
                editor.apply();
                loginUser();
            }
        });
        //felix
        gomaps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, MapsActivity.class));
            }
        });


        //

    }
    private void loginUser(){
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();
        if(email.isEmpty()){
            mEmail.setError("Email is Required");
            mEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please enter a valid email!");
            mEmail.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            mPass.setError("Pass is Required");
            mPass.requestFocus();
            return;
        }
                mAuth.signInWithEmailAndPassword(email , pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                Toast.makeText(SigninActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(SigninActivity.this , MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SigninActivity.this, "Login Failed !!", Toast.LENGTH_SHORT).show();
                    }
                });



}}