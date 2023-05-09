package com.mihai.travelplanning;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mihai.utils.AccountUtils;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView recoverLink = findViewById(R.id.lostPassword);
        recoverLink.setTextColor(Color.parseColor("#5B85B6"));

        recoverLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverLink.setTextColor(Color.parseColor("#638dbe"));
                switchRecover();
            }
        });

        Button signin = findViewById(R.id.login);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signin();
            }
        });

        Button signup = findViewById(R.id.register);
        signup.setOnClickListener(view -> switchSignup());
    }

    private void Signin() {
        EditText address = findViewById(R.id.address);
        EditText password = findViewById(R.id.password);

        String Address = address.getText().toString();
        String Password = password.getText().toString();
        int code = AccountUtils.Authenticate(Address, Password);

        if(code < 0) {
            // register new account
            Intent switchActivityIntent = new Intent(this, RegisterActivity.class);
            startActivity(switchActivityIntent);
        }
        else if(code == 0) {
            // forgotten password
            AlertDialog alertDialog = new AlertDialog.Builder(SigninActivity.this).create();
            alertDialog.setTitle("Forgot Password");
            alertDialog.setMessage("Password does not match, please enter again!");
            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    password.setText("");
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        }
        else {
            // route at quick update activity, to sync server data
            Intent switchActivityIntent = new Intent(this, QuickUpdateActivity.class);
            startActivity(switchActivityIntent);
        }
    }

    private void switchSignup() {
        Intent switchActivityIntent = new Intent(this, RegisterActivity.class);
        startActivity(switchActivityIntent);
    }

    private void switchRecover() {
        Intent switchActivityIntent = new Intent(this, RecoverActivity.class);
        startActivity(switchActivityIntent);
    }
}