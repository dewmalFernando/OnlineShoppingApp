package com.example.frizty;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frizty.Model.Users;
import com.example.frizty.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.frizty.Model.Users;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.os.Bundle;

public class feedback extends AppCompatActivity {

    private Button submitbtn;
    private EditText nameText, emailText, commentText;
    FirebaseDatabase firebaseDatabase;
    private String parentDBName = "Users";
    private ProgressDialog loadingBar;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private TextView closeTextButton;
    private String userID;
    private Users User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);



        firebaseFirestore = FirebaseFirestore.getInstance();

        nameText = findViewById(R.id.nametxt);
        emailText = findViewById(R.id.emailtxt);
        commentText = findViewById(R.id.commenttxt);
        closeTextButton = findViewById(R.id.closeSettings);
        submitbtn = findViewById(R.id.sumitbtn);


//        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
//        if(mFirebaseUser != null) {
//            userID = mFirebaseUser.getUid(); //Do what you need to do with the id
//        }
//        firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                if (task.isSuccessful()){
//
//                    if (task.getResult().exists()){
//
//                        nameText = findViewById(R.id.nametxt);
//                        String  username =  task.getResult().getString("username");
//                        nameText.setText(username);
//                    }
//
//                }
//                else {
//                    Toast.makeText(feedback.this, "FireStore Retrieve Error . . .", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateData();


            }
        });

        closeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

//    private void storeData() {
////
////        String username = nameText.getText().toString();
////        String email = emailText.getText().toString();
////        String comment = commentText.getText().toString();
////        String id = UUID.randomUUID().toString();
////
////        if (TextUtils.isEmpty(username)) {
////            Toast.makeText(feedback.this, "UserName is required", Toast.LENGTH_SHORT).show();
////        } else if (TextUtils.isEmpty(email)) {
////            Toast.makeText(feedback.this, "Email Address  is required", Toast.LENGTH_SHORT).show();
////        } else if (TextUtils.isEmpty(comment)) {
////            Toast.makeText(feedback.this, "Your Opinion is required", Toast.LENGTH_SHORT).show();
////        } else {
////
////
////            Map<String, String> FeedbackMap = new HashMap<>();
////
////            FeedbackMap.put("FID", id);
////            FeedbackMap.put("Name", username);
////            FeedbackMap.put("Email", email);
////            FeedbackMap.put("Comment", comment);
////
////            firebaseFirestore.collection("Feedback").add(FeedbackMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
////                @Override
////                public void onComplete(@NonNull Task<DocumentReference> task) {
////
////                    if (task.isSuccessful()) {
////
////                        Toast.makeText(feedback.this, " Feedback Submitted ...", Toast.LENGTH_LONG).show();
////                    } else {
////                        String error = task.getException().getMessage();
////                        Toast.makeText(feedback.this, " Firestore Retrieve : " + error, Toast.LENGTH_LONG).show();
////                        Intent intent = new Intent(feedback.this, HomeActivity.class);
////                        startActivity(intent);
////                        finish();
////
////                    }
////
////                }
////            });
////
////
////        }
////
////
////    }


    private void validateData() {

        String username = nameText.getText().toString();
        String email = emailText.getText().toString();
        String comment = commentText.getText().toString();


        if  (TextUtils.isEmpty(username) && TextUtils.isEmpty(email) && TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email Address  is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "Your Opinion is required", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, " UseName is required", Toast.LENGTH_SHORT).show();
        }else {
            uploadData(username,email,comment);
        }
    }


    private void uploadData(String username, String email, String comment) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Feedback");

        String id = UUID.randomUUID().toString();


        Map<String, Object> FeedbackMap = new HashMap<>();

        FeedbackMap.put("FID", id);
        FeedbackMap.put("Name", username);
        FeedbackMap.put("Email", email);
        FeedbackMap.put("Comment", comment);


        firebaseFirestore.collection("Feedback").document(id).set(FeedbackMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {

                    Toast.makeText(feedback.this, " Feedback Submitted ...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(feedback.this, HomeActivity.class);
                    startActivity(intent);
                    finish();


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(feedback.this, " Firestore Retrieve : " + error, Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}
