package com.example.newview1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class View2 extends AppCompatActivity {
    private EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view2);
        edittext =findViewById(R.id.edittext);
    }

    public void view_hot(View view) {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    public void view_recommend(View view) {
        Intent intent = new Intent();
        intent.setClass(this, View3.class);
        startActivity(intent);
    }

    public void view_my(View view) {
        Intent intent = new Intent();
        intent.setClass(this, View3_2.class);
        startActivity(intent);
    }

    public void view1102(View view) {
        Intent intent = new Intent();
        intent.setClass(this, View_1102.class);
        startActivity(intent);
    }

    public void edittext(View view) {
        edittext.setText("");
    }
}