package com.secretsnowman.secretsnowman;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.secretsnowman.secretsnowman.DBHelpers.DatabaseHelper;

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
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(currentUser == null){
                   User userToBeAdded = new User(0, t.getText().toString(), 0);
                   Log.d("User's id:", String.valueOf(helper.insertUser(userToBeAdded)));
               }
               else
                   Toast.makeText(getApplicationContext(), "Hello, " + currentUser.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
