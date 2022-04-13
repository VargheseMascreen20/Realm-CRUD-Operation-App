package com.example.realm;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private EditText courseNameEdt, courseDurationEdt, courseDescriptionEdt, courseTracksEdt;
    private Realm realm;
    private String courseName, courseDuration, courseDescription, courseTracks;
    private Button readCourseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription);
        courseDurationEdt = findViewById(R.id.idEdtCourseDuration);
        Button submitCourseBtn = findViewById(R.id.idBtnAddCourse);
        courseTracksEdt = findViewById(R.id.idEdtCourseTracks);
        readCourseBtn = findViewById(R.id.idBtnReadData);
        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseName = courseNameEdt.getText().toString();
                courseDescription = courseDescriptionEdt.getText().toString();
                courseDuration = courseDurationEdt.getText().toString();
                courseTracks = courseTracksEdt.getText().toString();
                if (TextUtils.isEmpty(courseName)) {
                    courseNameEdt.setError("Please enter Course Name");
                } else if (TextUtils.isEmpty(courseDescription)) {
                    courseDescriptionEdt.setError("Please enter Course Description");
                } else if (TextUtils.isEmpty(courseDuration)) {
                    courseDurationEdt.setError("Please enter Course Duration");
                } else if (TextUtils.isEmpty(courseTracks)) {
                    courseTracksEdt.setError("Please enter Course Tracks");
                } else {
                    addDataToDatabase(courseName, courseDescription, courseDuration, courseTracks);
                    Toast.makeText(MainActivity.this, "Course added to database..", Toast.LENGTH_SHORT).show();
                    courseNameEdt.setText("");
                    courseDescriptionEdt.setText("");
                    courseDurationEdt.setText("");
                    courseTracksEdt.setText("");
                }
            }
        });
        readCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ReadCoursesActivity.class);
                startActivity(i);
            }
        });
    }

    private void addDataToDatabase(String courseName, String courseDescription, String courseDuration, String courseTracks) {
        ModelClass model = new ModelClass();
        Number id = realm.where(ModelClass.class).max("id");
        long nextId;
        if (id == null) {
            nextId = 1;
        } else {
            nextId = id.intValue() + 1;
        }
        model.setId(nextId);
        model.setCourseDescription(courseDescription);
        model.setCourseName(courseName);
        model.setCourseDuration(courseDuration);
        model.setCourseTracks(courseTracks);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(model);
            }
        });
    }
}