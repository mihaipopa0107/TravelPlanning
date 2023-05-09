package com.mihai.travelplanning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);

        Button goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToSignIn();
            }
        });
    }

    private void GoToSignIn() {
        Intent switchActivityIntent = new Intent(this, SigninActivity.class);
        startActivity(switchActivityIntent);
    }
}