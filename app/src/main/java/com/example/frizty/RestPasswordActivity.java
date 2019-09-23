package com.example.frizty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frizty.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RestPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, headingText;
    private EditText findUserName, question1, question2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);

        check = getIntent().getStringExtra("check");
        pageTitle = findViewById(R.id.pageTitle);
        headingText = findViewById(R.id.headingText);
        findUserName = findViewById(R.id.findUserName);
        question1 = findViewById(R.id.question1);
        question2 = findViewById(R.id.question2);
        verifyButton = findViewById(R.id.verifyButton);

    }


    @Override
    protected void onStart() {
        super.onStart();

        findUserName.setVisibility(View.GONE);

        if(check.equals("settings")){
            pageTitle.setText("Set Questions");
            headingText.setText("Answer the following questions");
            verifyButton.setText("Set");

            displayPreviousAnswers();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();
                }
            });
        } else if(check.equals("login")){
            findUserName.setVisibility(View.VISIBLE);

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }
    }

    private void setAnswers(){
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if(question1.equals("") && question2.equals("")){
            Toast.makeText(RestPasswordActivity.this, "Please answer both questions", Toast.LENGTH_SHORT).show();

        } else {
            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getUsername());

            HashMap<String, Object> userDataMap = new HashMap<>();
            userDataMap.put("answer1", answer1);
            userDataMap.put("answer2", answer2);

            ref.child("Security Questions").updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RestPasswordActivity.this, "You have answer the security questions successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RestPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void displayPreviousAnswers(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUsername());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String ans1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void verifyUser() {
        final String userName = findUserName.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        if(!userName.equals("") && !answer1.equals("") && !answer2.equals("")){
            final DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Users")
                    .child(userName);


            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        String username = dataSnapshot.child("Users").toString().toLowerCase();
                        if(!username.equals(userName)){
                            Toast.makeText(RestPasswordActivity.this, "Username dose not exist", Toast.LENGTH_SHORT).show();
                        } else if(dataSnapshot.hasChild("Security Questions")){
                            String ans1 = dataSnapshot.child("Security Questions").child("answer1").toString().toLowerCase();
                            String ans2 = dataSnapshot.child("Security Questions").child("answer2").toString().toLowerCase();

                            if(!ans1.equals(answer1)){
                                Toast.makeText(RestPasswordActivity.this, "Your 2nd answer is wrong", Toast.LENGTH_SHORT).show();
                            } else if(!ans2.equals(answer2)){
                                Toast.makeText(RestPasswordActivity.this, "Your 3rd answer is wrong", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RestPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(RestPasswordActivity.this);
                                newPassword.setHint("Enter New Password here..");
                                builder.setView(newPassword);


                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!newPassword.getText().toString().equals("")){
                                            ref.child("password").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(RestPasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(RestPasswordActivity.this, LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }

                        } else {
                            Toast.makeText(RestPasswordActivity.this, "You have not set security questions", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "Please complete the form", Toast.LENGTH_SHORT).show();
        }
    }
}
