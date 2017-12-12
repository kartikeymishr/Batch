package com.kartikeymishr.batch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    TextView day;
    String weekDay;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        day = (TextView) findViewById(R.id.day);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.UK);
        Calendar c = Calendar.getInstance();
        weekDay = dayFormat.format(c.getTime());
        day.setText(weekDay);
    }

    public void addAgenda(View view) {
        startActivity(new Intent(CalendarActivity.this, AddEventActivity.class));
    }

}
