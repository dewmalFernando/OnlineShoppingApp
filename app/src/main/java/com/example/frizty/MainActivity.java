package com.example.frizty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.frizty.Model.Users;
import com.example.frizty.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowBtn, loginBtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowBtn = (Button) findViewById(R.id.main_join_btn);
        loginBtn = (Button) findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

//        String username = Paper.book().read(Prevalent.username);
//        String password = Paper.book().read(Prevalent.password);
//
//
//        if (username != "" && password != "") {
//            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
//                allowAccess(username, password);
//
//                loadingBar.setTitle("Already Logged in");
//                loadingBar.setMessage("Please wait, while we are checking the credentials");
//                loadingBar.setCanceledOnTouchOutside(false);
//                loadingBar.show();
//            }
//        }
    }


    private void allowAccess(final String username, final String password) {

        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(username).exists()){
                    Users userData = dataSnapshot.child("Users").child(username).getValue(Users.class);
                    if(userData.getUsername().equals(username)){
                        if(userData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = userData;
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Account with this " + username + " do not exists", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    //Toast.makeText(LoginActivity.this, "You need to cretea a new account", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
