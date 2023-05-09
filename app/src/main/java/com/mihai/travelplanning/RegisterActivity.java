package com.mihai.travelplanning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.mihai.models.Account;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RegisterActivity extends AppCompatActivity {
    int SELECT_PHOTO = 1;
    String ProfilePhoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.requestPermissions(new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
        this.requestPermissions(new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_PHOTO);
        });

        Button register = findViewById(R.id.button);
        register.setOnClickListener(view -> CreateAccount());

        Button cancel = findViewById(R.id.button2);
        cancel.setOnClickListener(view -> SignInPage());
    }

    private void CreateAccount() {
        EditText key = findViewById(R.id.editPassword);
        EditText ckey = findViewById(R.id.editConfirmPassword);

        if(key.getText().toString().compareTo(ckey.getText().toString()) == 0 && !ProfilePhoto.isEmpty()) {
            EditText username = findViewById(R.id.username);
            EditText address = findViewById(R.id.editAddress);
            CheckBox box = findViewById(R.id.checkBox);

            Account account = new Account();
            account.setUsername(username.getText().toString());

            account.setAddress(address.getText().toString());
            account.setPassword(key.getText().toString());

            account.setAdmin(box.isChecked());
            account.setAvatar(ProfilePhoto);

            account.save();
            GoToQuickUpdatePage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                String path = getRealPathFromURI(uri);
                byte[] array = Files.readAllBytes(Paths.get(path));

                int index = path.lastIndexOf("/");
                ProfilePhoto = getDataDir().getPath() + "/Photos/";

                File folder = new File(ProfilePhoto);
                if(!folder.exists()) folder.mkdir();

                if(index != -1) ProfilePhoto += path.substring(index + 1);
                Files.write(Paths.get(ProfilePhoto), array);
            }
            catch (IOException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {
                MediaStore.Audio.Media.DATA
        };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void SignInPage() {
        Intent switchActivityIntent = new Intent(this, SigninActivity.class);
        startActivity(switchActivityIntent);
    }

    private void GoToQuickUpdatePage() {
        Intent quickUpdateIntent = new Intent(this, QuickUpdateActivity.class);
        startActivity(quickUpdateIntent);
    }
}