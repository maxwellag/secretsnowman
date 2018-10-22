package com.secretsnowman.secretsnowman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.secretsnowman.secretsnowman.DB.DatabaseHelper;

import com.secretsnowman.secretsnowman.Entity.User;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper helper;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button b = findViewById(R.id.button_register);
        final EditText t = findViewById(R.id.editText_register);
        helper = new DatabaseHelper(getApplicationContext());
        currentUser = helper.getUser(1);
        final Intent startMainIntent = new Intent(this, MainActivity.class);
        if(currentUser != null){ // If the user already registered and there is a stored instance of them, open the main app
            startActivity(startMainIntent);
            finish(); // Prevents the user from hitting back to get back to the register screen
        }
        b.setOnClickListener(new View.OnClickListener() { // If the user has not registered, stay in the register activity
            @Override
            public void onClick(View v) {
               if(currentUser == null){
                   User userToBeAdded = new User(0, t.getText().toString(), 0);
                   helper.insertUser(userToBeAdded);
                   // TODO: Create the server request to register a user and then update the local user with the serverId
                   startActivity(startMainIntent);
                   finish(); // Prevents the user from hitting back to get back to the register screen
               }
            }
        });
    }
}
