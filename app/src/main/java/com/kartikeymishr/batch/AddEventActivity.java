package com.kartikeymishr.batch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {

    Button saveButton;
    Button cancelButton;

    EditText className;
    EditText classroom;
    EditText faculty;
    EditText classNumber;

    Spinner daySpinner;

    private DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        className = (EditText) findViewById(R.id.className);
        classroom = (EditText) findViewById(R.id.classroom);
        faculty = (EditText) findViewById(R.id.faculty);
        classNumber = (EditText) findViewById(R.id.classNumber);

        daySpinner = (Spinner) findViewById(R.id.spinner);

        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassDetails details = new ClassDetails(classNumber.getText().toString(),
                                                        className.getText().toString(),
                                                        classroom.getText().toString(),
                                                        faculty.getText().toString());

                String weekDay = daySpinner.getSelectedItem().toString();

                dbref = FirebaseDatabase.getInstance().getReference("timetable");
                dbref.child(weekDay).push().setValue(details);
                startActivity(new Intent(AddEventActivity.this, CalendarActivity.class));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddEventActivity.this, CalendarActivity.class));
                finish();
            }
        });
    }
}
