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

public class UpdateCourseActivity extends AppCompatActivity {
    private EditText courseNameEdt, courseDurationEdt, courseDescriptionEdt, courseTracksEdt;
    private String courseName, courseDuration, courseDescription, courseTracks;
    private long id;
    private Button updateCourseBtn, deleteCourseBtn;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        realm = Realm.getDefaultInstance();
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription);
        courseDurationEdt = findViewById(R.id.idEdtCourseDuration);
        courseTracksEdt = findViewById(R.id.idEdtCourseTracks);
        updateCourseBtn = findViewById(R.id.idBtnUpdateCourse);
        deleteCourseBtn = findViewById(R.id.idBtnDeleteCourse);
        courseName = getIntent().getStringExtra("courseName");
        courseDuration = getIntent().getStringExtra("courseDuration");
        courseDescription = getIntent().getStringExtra("courseDescription");
        courseTracks = getIntent().getStringExtra("courseTracks");
        id = getIntent().getLongExtra("id", 0);
        courseNameEdt.setText(courseName);
        courseDurationEdt.setText(courseDuration);
        courseDescriptionEdt.setText(courseDescription);
        courseTracksEdt.setText(courseTracks);
        updateCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseName = courseNameEdt.getText().toString();
                String courseDescription = courseDescriptionEdt.getText().toString();
                String courseDuration = courseDurationEdt.getText().toString();
                String courseTracks = courseTracksEdt.getText().toString();
                if (TextUtils.isEmpty(courseName)) {
                    courseNameEdt.setError("Please enter Course Name");
                } else if (TextUtils.isEmpty(courseDescription)) {
                    courseDescriptionEdt.setError("Please enter Course Description");
                } else if (TextUtils.isEmpty(courseDuration)) {
                    courseDurationEdt.setError("Please enter Course Duration");
                } else if (TextUtils.isEmpty(courseTracks)) {
                    courseTracksEdt.setError("Please enter Course Tracks");
                } else {
                    final ModelClass modal = realm.where(ModelClass.class).equalTo("id", id).findFirst();
                    updateCourse(modal, courseName, courseDescription, courseDuration, courseTracks);
                }
                Toast.makeText(UpdateCourseActivity.this, "Course Updated.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(UpdateCourseActivity.this, ReadCoursesActivity.class);
                startActivity(i);
                finish();
            }
        });
        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCourse(id);
                Toast.makeText(UpdateCourseActivity.this, "Course Deleted.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(UpdateCourseActivity.this, ReadCoursesActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void updateCourse(ModelClass modal, String courseName, String courseDescription, String courseDuration, String courseTracks) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {


                modal.setCourseDescription(courseDescription);
                modal.setCourseName(courseName);
                modal.setCourseDuration(courseDuration);
                modal.setCourseTracks(courseTracks);

                realm.copyToRealmOrUpdate(modal);
            }
        });
    }

    private void deleteCourse(long id) {

        ModelClass modal = realm.where(ModelClass.class).equalTo("id", id).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                modal.deleteFromRealm();
            }
        });
    }
}