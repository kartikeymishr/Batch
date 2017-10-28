package com.kartikeymishr.batch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    Button loginButton;
    RelativeLayout backgroundLayout;

    boolean loginModeActive = true;

    private FirebaseAuth firebaseAuth;

    Window window;
    View decor;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.signUpTextView) {
            // When SIGN UP is clicked
            loginModeActive = false;
            Log.i("Info", "Switched to Signup Mode");
            signUpTextView.setVisibility(View.INVISIBLE);

            // Verify this animation on devices with different screen sizes
            // loginButton.animate().translationXBy(-650);

            loginTextView.setVisibility(View.VISIBLE);
            loginButton.setText("Sign Up");
        } else if (v.getId() == R.id.loginTextView) {
            // When LOGIN is clicked
            loginModeActive = true;
            Log.i("Info", "Switched to Login Mode");
            loginTextView.setVisibility(View.INVISIBLE);

            // Verify this animation on devices with different screen sizes
            // loginButton.animate().translationXBy(650);

            signUpTextView.setVisibility(View.VISIBLE);
            loginButton.setText("Login");
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

        // Changing status bar colour to WHITE
        /*window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.WHITE);

        decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);*/

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        passwordText.setOnKeyListener(this);

        batchTextView = (TextView) findViewById(R.id.batchTextView);
        batchTextView.setOnClickListener(this);

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(this);

        loginTextView = (TextView) findViewById(R.id.loginTextView);
        loginTextView.setOnClickListener(this);

        loginButton = (Button) findViewById(R.id.loginButton);

        backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);
        backgroundLayout.setOnClickListener(this);

        // Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        // firebaseAuth.getCurrentUser().reload();

        // Check if a session is already in progress. If yes -> Dashboard Activity
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
            finish();
        }
    }

    public void login(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "An email address and password are required", Toast.LENGTH_SHORT).show();
        } else {
            if (loginModeActive) {
                // Code for logging in user
                Log.i("Info", "Login Mode");
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.i("Info", "Login Failed: " + task.getException());
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Login failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i("Info", "Login Successful");
                                    // Add intent to go to Dashboard
                                    progressDialog.dismiss();

                                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if (user.isEmailVerified()) {
                                        startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            } else {
                // Code for signing up user
                Log.i("Info", "Signup Mode");
                progressDialog.setMessage("Signing up...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.i("Info", "Signup Failed: " + task.getException());
                                    Toast.makeText(LoginActivity.this, "Signup Failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i("Info","Signup Successful");
                                    // Send verification email
                                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(LoginActivity.this,
                                                                "Verification email sent to " + user.getEmail(),
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Log.e(TAG, "sendEmailVerification", task.getException());
                                                        Log.i("Info", "Verification failed");
                                                        progressDialog.dismiss();
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
