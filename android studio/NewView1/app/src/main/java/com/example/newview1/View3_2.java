package com.example.newview1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class View3_2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view32);
    }

    public void view_hot(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    public void view_search(View view) {
        Intent intent = new Intent();
        intent.setClass(this, View2.class);
        startActivity(intent);
    }

    public void view_recommend(View view) {
        Intent intent = new Intent();
        intent.setClass(this, View3.class);
        startActivity(intent);
    }
}