package com.example.quizbuzz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mPhoneNumber;
    Button mRegisterButton;
    TextView mCreateText, result;
    FirebaseAuth fAuth;
    ProgressBar progressBar;


//    if(fAuth.getCurrentUser() != null){
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mFullName = findViewById(R.id.FullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mPhoneNumber = findViewById(R.id.PhoneNumber);
        mRegisterButton = findViewById(R.id.RegisterButton);
        mCreateText = findViewById(R.id.CreateText);
        result = findViewById(R.id.textView);
        String emPass = result.getText().toString().trim();
        fAuth = FirebaseAuth.getInstance(); //current instance of the database
        progressBar = findViewById(R.id.progressBar);
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mRegisterButton.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required");
                return;
            }
            if (password.length() < 6) {
                mPassword.setError("Password must be >= 6 Characters");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            mRegisterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    computeMD5Hash(password.toString());

                }

                private void computeMD5Hash(String toString) {
                }
            });

            //REGISTER THE USER IN FIREBASE

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));

                } else {
                    Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }
            });

        });

        mCreateText.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }

    public void computeMD5Hash(String mPassword)
    {
        try {
            //Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(mPassword.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }
            result.setText(MD5Hash);

        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();

        }
    }
}


