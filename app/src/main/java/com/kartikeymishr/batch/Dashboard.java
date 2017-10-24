package com.kartikeymishr.batch;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.common_google_signin_btn_icon_dark);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.getCurrentUser().reload();

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (!firebaseUser.isEmailVerified()) {
            Toast.makeText(this, "Email ID doesn't exist", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
            startActivity(new Intent(Dashboard.this, LoginActivity.class));
            finish();
        }

    }

    // Inflates the menu; this adds items to the action bar if it is present
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handles the action bar clicks here
    public boolean onOptionsItemSelected(MenuItem item) {
        ConstraintLayout menu_layout = (ConstraintLayout) findViewById(R.id.menu_layout);
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            startActivity(new Intent(Dashboard.this, LoginActivity.class));
            finish();
        }
        return true;
    }

}
