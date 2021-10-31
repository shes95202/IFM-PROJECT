package com.example.newview1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void view2609(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, View1_2.class);
        startActivity(intent);
    }

    public void view_search(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, View2.class);
        startActivity(intent);
    }

    public void view_recommend(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, View3.class);
        startActivity(intent);
    }

    public void view_my(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, View3_2.class);
        startActivity(intent);
    }
}