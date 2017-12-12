package com.kartikeymishr.batch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth auth;

    ListView functionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auth = FirebaseAuth.getInstance();
        auth.getCurrentUser().reload();

        final FirebaseUser user = auth.getCurrentUser();

        if (!user.isEmailVerified()) {
            Toast.makeText(this, "Email ID doesn't exist", Toast.LENGTH_SHORT).show();
            auth.signOut();
            startActivity(new Intent(Dashboard.this, LoginActivity.class));
            finish();
        }

        functionsList = (ListView) findViewById(R.id.functionsList);

        ArrayList<String> functions = new ArrayList<>();
        functions.add("Calendar");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, functions);
        functionsList.setAdapter(adapter);

        functionsList.setClickable(true);
        functionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(Dashboard.this, CalendarActivity.class));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(Dashboard.this, LoginActivity.class));
            finish();
        }
        return true;
    }
}
