package com.kartikeymishr.batch;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    TextView batchTextView;
    TextView signUpTextView;
    TextView loginTextView;
    EditText emailText;
    EditText passwordText;
    FloatingActionButton loginButton;
    RelativeLayout backgroundLayout;

    boolean loginModeActive = true;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpTextView) {
            // When SIGN UP is clicked
            loginModeActive = false;
            Log.i("Info", "Switched to Signup Mode");
            signUpTextView.setVisibility(View.INVISIBLE);

            // Verify this animation on devices with different screen sizes
            loginButton.animate().translationXBy(-650);

            loginTextView.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.loginTextView) {
            // When LOGIN is clicked
            loginModeActive = true;
            Log.i("Info", "Switched to Login Mode");
            loginTextView.setVisibility(View.INVISIBLE);

            // Verify this animation on devices with different screen sizes
            loginButton.animate().translationXBy(650);

            signUpTextView.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.backgroundLayout || v.getId() == R.id.batchTextView) {
            // Getting the keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            // Hiding the keyboard on focus lost
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // Checking if Enter key is tapped in the password field. Connected via setOnKeyListener() in onCreate()
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            login(v);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        Toast.makeText(this, "sup bitch", Toast.LENGTH_LONG).show();

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        passwordText.setOnKeyListener(this);

        batchTextView = (TextView) findViewById(R.id.batchTextView);
        batchTextView.setOnClickListener(this);

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(this);

        loginTextView = (TextView) findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);

        loginButton = (FloatingActionButton) findViewById(R.id.loginButton);

        backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);
        backgroundLayout.setOnClickListener(this);

        // Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        // firebaseAuth.getCurrentUser().reload();

        // Check if a session is already in progress. If yes -> Dashboard Activity
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
        }
    }

    public void login(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "An email address and password are required", Toast.LENGTH_SHORT).show();
        } else {
            if (loginModeActive) {
                // Code for logging in user
                Log.i("Info", "Login Mode");
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.i("Info", "Login Failed: " + task.getException());
                                    Toast.makeText(LoginActivity.this, "Login failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i("Info", "Login Successful");
                                    // Add intent to go to Dashboard
                                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user.isEmailVerified()) {
                                        startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            } else {
                // Code for signing up user
                Log.i("Info", "Signup Mode");
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.i("Info", "Signup Failed: " + task.getException());
                                    Toast.makeText(LoginActivity.this, "Signup Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i("Info","Signup Successful");
                                    // Add intent to go to Dashboard
                                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(LoginActivity.this,
                                                                "Verification email sent to " + user.getEmail(),
                                                                Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                                    } else {
                                                        // Log.e(TAG, "sendEmailVerification", task.getException());
                                                        Log.i("Info", "Verification failed");
                                                        Toast.makeText(LoginActivity.this,
                                                                "Failed to send verification email.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        }
            }

    // Just leaving this here for use later
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}
