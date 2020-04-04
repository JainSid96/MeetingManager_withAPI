package com.example.meeting_manager;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    private EditText name, pass;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name);
        pass = findViewById(R.id.pass);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate(name.getText().toString(), pass.getText().toString());
                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
            }
        });

    }

    /*private void validate(String name, String pass) {

        if((name.equals("Vinsol"))&&(pass.equals("1234"))){
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
        }

    }

     */
}
