package com.kyle.refreshrecyclerviewdemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TestRecyclerView list = findViewById(R.id.list);
        list.request();
    }
}
