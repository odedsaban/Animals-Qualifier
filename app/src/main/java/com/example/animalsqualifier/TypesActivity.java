package com.example.animalsqualifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TypesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);
    }

    public void airAnimals(View view) {
        Intent intent=new Intent(getApplicationContext(),ListingActivity.class);
        intent.putExtra("type","AirAnimals");
        startActivity(intent);
    }

    public void landAnimals(View view) {
        Intent intent=new Intent(getApplicationContext(),ListingActivity.class);
        intent.putExtra("type","LandAnimals");
        startActivity(intent);
    }

    public void waterAnimals(View view) {
        Intent intent=new Intent(getApplicationContext(),ListingActivity.class);
        intent.putExtra("type","WaterAnimals");
        startActivity(intent);
    }

    public void insects(View view) {
        Intent intent=new Intent(getApplicationContext(),ListingActivity.class);
        intent.putExtra("type","InsectsAnimals");
        startActivity(intent);
    }
}