package com.example.animalsqualifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
      //  getSupportActionBar().hide();
    }

    public void addNew(View view) {
        startActivity(new Intent(getApplicationContext(),AddNewAnimalActivity.class));
    }

    public void goToTypes(View view) {
        startActivity(new Intent(getApplicationContext(),TypesActivity.class));
    }
}