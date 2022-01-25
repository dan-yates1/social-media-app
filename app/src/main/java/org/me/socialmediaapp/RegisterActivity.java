package org.me.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmail, mName, mPassword;
    private Button mRegister;
    private TextView mLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initInterface();

        mAuth = FirebaseAuth.getInstance();
    }

    public void initInterface() {
        mEmail = findViewById(R.id.emailEt);
        mName = findViewById(R.id.nameEt);
        mPassword = findViewById(R.id.passwordEt);
        mRegister = findViewById(R.id.registerBtn);
        mRegister.setOnClickListener(this);
        mLogin = findViewById(R.id.loginTv);
        mLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn:
                checkCredentials();
                break;
            case R.id.loginTv:
                startActivity(new Intent(this, LoginActivity.class));
                break;

        }
    }

    public void checkCredentials() {
        String email = mEmail.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();

        if (email.isEmpty()) {
            mEmail.setError("Email address is required!");
            mEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please provide a valid email address!");
            mEmail.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            mName.setError("Full name is required!");
            mName.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            mPassword.setError("Password is required!");
            mPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Password must be at least 6 characters!");
            mPassword.requestFocus();
            return;
        }
        registerUser(email, password);
    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User account created successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}