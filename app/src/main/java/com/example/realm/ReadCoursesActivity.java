package com.example.realm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ReadCoursesActivity extends AppCompatActivity {

    List<ModelClass> dataModals;


    private Realm realm;
    private RecyclerView coursesRV;
    private AdapterClass courseRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_courses);


        coursesRV = findViewById(R.id.idRVCourses);
        realm = Realm.getDefaultInstance();
        dataModals = new ArrayList<>();

        prepareRecyclerView();
    }

    private void prepareRecyclerView() {
        dataModals = realm.where(ModelClass.class).findAll();
        courseRVAdapter = new AdapterClass(dataModals, this);
        coursesRV.setLayoutManager(new LinearLayoutManager(this));
        coursesRV.setAdapter(courseRVAdapter);
    }
}
