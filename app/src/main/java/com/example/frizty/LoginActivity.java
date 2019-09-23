package com.example.frizty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frizty.Model.Users;
import com.example.frizty.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUserName, inputPassword;
    private ProgressDialog loadingBar;
    private Button loginBtn;
    private TextView adminLink, notAdminLink, forgetPasswordLink;
    private CheckBox checkBoxRememberMe;

//    private SignInButton googleSignInButton;
//    private GoogleApiClient googleApiClient;
//    private static final int REQ_CODE = 1212;

    private String parentDBName = "Users";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginBtn = (Button)findViewById(R.id.main_login_btn);
        inputUserName = (EditText)findViewById(R.id.userName);
        inputPassword = (EditText)findViewById(R.id.login_password);
        loadingBar = new ProgressDialog(this);
        checkBoxRememberMe = (CheckBox)findViewById(R.id.rememberMeCheckbox);
        adminLink = (TextView)findViewById(R.id.adminPanelLink);
        notAdminLink = (TextView)findViewById(R.id.notAdminPanelLink);
        forgetPasswordLink = (TextView)findViewById(R.id.forgetPasswordLink);
        Paper.init(this);


//        googleSignInButton = findViewById(R.id.sign_in_button);





        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RestPasswordActivity.class);
                intent.putExtra("check", "login");
                startActivity(intent);
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login Admin");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parentDBName = "Admin";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login");
                adminLink.setVisibility(View.VISIBLE);
                notAdminLink.setVisibility(View.INVISIBLE);
                parentDBName = "Users";
            }
        });


    }
    private void loginUser(){

        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        String username = inputUserName.getText().toString();
        String password = inputPassword.getText().toString();



        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please write your user name", Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your password", Toast.LENGTH_LONG).show();
        }else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(username, password);
        }
    }

    private void AllowAccessToAccount(final String username, final String password) {

        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(checkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.username, username);
            Paper.book().write(Prevalent.password, password);
        }



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDBName).child(username).exists()){
                    Users userData = dataSnapshot.child(parentDBName).child(username).getValue(Users.class);
                    if(userData.getUsername().equals(username)){
                        if(userData.getPassword().equals(password)){
                            if(parentDBName.equals("Admin")){
                                Toast.makeText(LoginActivity.this, "Welcome admin your are Logged in successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            }else if(parentDBName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = userData;
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with this " + username + " do not exists", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    //Toast.makeText(LoginActivity.this, "You need to cretea a new account", Toast.LENGTH_LONG).show();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
    }
}
