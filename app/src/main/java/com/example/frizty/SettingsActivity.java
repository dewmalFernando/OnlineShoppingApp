package com.example.frizty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frizty.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText  changePassword, changeFirstName, changeLastName, changePhoneNumber, changeEmail;
    private TextView profileChangeTextButton, closeTextButton, saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private String checker = "";
    private Button securityQuestionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageReference = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        profileImageView = findViewById(R.id.settingProfileImage);
        //changeUserName = findViewById(R.id.settingUserName);
        changePassword = findViewById(R.id.settingPassword);
        changeFirstName = findViewById(R.id.settingFirstName);
        changeLastName = findViewById(R.id.settingLastName);
        changePhoneNumber = findViewById(R.id.settingPhone);
        changeEmail = findViewById(R.id.settingEmail);
        profileChangeTextButton = findViewById(R.id.profileImageChange);
        closeTextButton = findViewById(R.id.closeSettings);
        saveTextButton = findViewById(R.id.updateAccount);
        securityQuestionButton = findViewById(R.id.securityQuestionButton);


        userInfoDisplay(profileImageView, changePassword, changeFirstName, changeLastName, changePhoneNumber, changeEmail);

        closeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        securityQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, RestPasswordActivity.class);
                intent.putExtra("check", "settings");
                startActivity(intent);
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });


        profileChangeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            
            profileImageView.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(changeFirstName.getText().toString())) {
            Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(changeLastName.getText().toString())) {
            Toast.makeText(this, "Last Name is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(changeEmail.getText().toString())) {
            Toast.makeText(this, "Email address is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(changePhoneNumber.getText().toString())) {
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(changePassword.getText().toString())) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(changeUserName.getText().toString())) {
//            Toast.makeText(this, "User Name is required", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileRef = storageReference
                    .child(Prevalent.currentOnlineUser.getUsername() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        //userMap.put("username", changeUserName.getText().toString());
                        userMap.put("password", changePassword.getText().toString());
                        userMap.put("firstname", changeFirstName.getText().toString());
                        userMap.put("lastname", changeLastName.getText().toString());
                        userMap.put("phone", changePhoneNumber.getText().toString());
                        userMap.put("email", changeEmail.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile information updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }
            
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        //userMap.put("username", changeUserName.getText().toString());
        userMap.put("password", changePassword.getText().toString());
        userMap.put("firstname", changeFirstName.getText().toString());
        userMap.put("lastname", changeLastName.getText().toString());
        userMap.put("phone", changePhoneNumber.getText().toString());
        userMap.put("email", changeEmail.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getUsername()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile information updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText changePassword, final EditText changeFirstName, final EditText changeLastName, final EditText changePhoneNumebr, final EditText changeEmail) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getUsername());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        //String userName = dataSnapshot.child("username").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();
                        String firstName = dataSnapshot.child("firstname").getValue().toString();
                        String lastName = dataSnapshot.child("lastname").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();
                        //String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        changeFirstName.setText(firstName);
                        changeLastName.setText(lastName);
                        //changeUserName.setText(userName);
                        changePassword.setText(password);
                        changePhoneNumebr.setText(phone);
                        changeEmail.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
