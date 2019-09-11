package com.example.frizty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText inputFirstName, inputLastName, inputEmail, inputPhoneNumber, inputPassword, inputUserName;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = (Button)findViewById(R.id.registerButton);
        inputFirstName = (EditText)findViewById(R.id.firstName);
        inputLastName = (EditText)findViewById(R.id.lastName);
        inputPhoneNumber = (EditText)findViewById(R.id.phoneNumebr);
        inputPassword = (EditText)findViewById(R.id.password);
        inputEmail = (EditText)findViewById(R.id.eMail);
        inputUserName = (EditText)findViewById(R.id.userName);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

    private void createAccount() {
        String firstName = inputFirstName.getText().toString();
        String lastName = inputLastName.getText().toString();
        String email = inputEmail.getText().toString();
        String phoneNo = inputPhoneNumber.getText().toString();
        String password = inputPassword.getText().toString();
        String userName = inputUserName.getText().toString();
        loadingBar = new ProgressDialog(this);

        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please write your first first name", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Please write your first last name", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(phoneNo)){
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please write your email", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your password", Toast.LENGTH_LONG).show();
        } else if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "Please write your password", Toast.LENGTH_LONG).show();
        }else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateEmail(firstName, lastName, phoneNo, email, password, userName);
        }
    }

    private void ValidateEmail(final String fName, final String lName, final String phoneNo, final String email, final String password, final String userName){
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(userName).exists())){
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("username", userName);
                    userDataMap.put("email", email);
                    userDataMap.put("phone", phoneNo);
                    userDataMap.put("password", password);
                    userDataMap.put("firstname", fName);
                    userDataMap.put("lastname", lName);


                    databaseReference.child("Users").child(userName).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Your account has created successfully", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network connection error. Please try again later.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "This " + userName + " already exists.", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using different email address", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
