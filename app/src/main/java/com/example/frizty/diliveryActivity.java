package com.example.frizty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class diliveryActivity extends AppCompatActivity {

    private TextView buttonClose, ListBtn;
    private Button submitBtn;
    private EditText streetText, cityText, stateText, codeText;
    private ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private TextView closeTextButton, listFeedbackButton;
    private String userID;
    private FirebaseFirestore firebaseFirestore;
    private String rDID, rStreetName, rCityName, rState, rZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dilivery);

        firebaseFirestore = FirebaseFirestore.getInstance();

        submitBtn = findViewById(R.id.subBtn);
        streetText = findViewById(R.id.streettxt);
        cityText = findViewById(R.id.citytext);
        stateText = findViewById(R.id.statetext);
        codeText = findViewById(R.id.codetext);
        buttonClose = findViewById(R.id.close);
        ListBtn = findViewById(R.id.list);


        pd = new ProgressDialog(this);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
  //update data

            submitBtn.setText("Update");

            rDID = bundle.getString("rDID");

            //get Data
            rStreetName = bundle.getString("rStreetName");
            rCityName = bundle.getString("rCityName");
            rState = bundle.getString("rState");
            rZipCode = bundle.getString("rZipCode");


            streetText.setText(rStreetName);
            cityText.setText(rCityName);
            stateText.setText(rState);
            codeText.setText(rZipCode);

        } else {

            //new data

            submitBtn.setText("Save");
        }

        ListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(diliveryActivity.this, ListDilivery.class);
                startActivity(intent);
                finish();
            }
        });

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle1 = getIntent().getExtras();

                if (bundle != null) {
                    //update,input



                    String StreetName = streetText.getText().toString().trim();
                    String CityName = cityText.getText().toString().trim();
                    String State = stateText.getText().toString().trim();
                    String Code = codeText.getText().toString().trim();

                    updateData(StreetName, CityName, State, Code);
                } else {

                    //input Data
                    Validation();

                }

                AddData();



            }
        });

    }

    private void updateData(String streetName, String cityName, String state, String code) {


        pd.setTitle("Updating Data . . ");
        pd.show();



        firebaseFirestore.collection("Delivery_Details").document(code)
                .update("Street Name", streetName, "City Name", cityName, "State", state, "Zip Code", code)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        pd.dismiss();
                        Toast.makeText(diliveryActivity.this, "Updated . . .", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(diliveryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void Validation() {

        String street = streetText.getText().toString();
        String city = cityText.getText().toString();
        String state = stateText.getText().toString();
        String code = codeText.getText().toString();


        if (TextUtils.isEmpty(street) && TextUtils.isEmpty(city) && TextUtils.isEmpty(state) && TextUtils.isEmpty(code)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(street)) {
            Toast.makeText(this, "Street Address  is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "City Name is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(state)) {
            Toast.makeText(this, " State is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, " Your Zip Code is required", Toast.LENGTH_SHORT).show();
        } else {
            sendData(street, city, state, code);
        }


    }

    private void sendData(String street, String city, String state, String code) {

//        String street = streetText.getText().toString();
//        String city = cityText.getText().toString();
//        String state = stateText.getText().toString();
//        String code = codeText.getText().toString();
//
//
//        if  (TextUtils.isEmpty(street) && TextUtils.isEmpty(city) && TextUtils.isEmpty(state) && TextUtils.isEmpty(code)) {
//            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(street)) {
//            Toast.makeText(this, "Street Address  is required", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(city)) {
//            Toast.makeText(this, "City Name is required", Toast.LENGTH_SHORT).show();
//        }else if (TextUtils.isEmpty(state)) {
//            Toast.makeText(this, " State is required", Toast.LENGTH_SHORT).show();
//        }else if (TextUtils.isEmpty(code)) {
//            Toast.makeText(this, " Your Zip Code is required", Toast.LENGTH_SHORT).show();
//        }else {

        pd.setTitle("Adding Data . . .");
        pd.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Delivery_Details");




        Map<String, Object> DelivaryMap = new HashMap<>();


        DelivaryMap.put("Street Name", street);
        DelivaryMap.put("City Name", city);
        DelivaryMap.put("State", state);
        DelivaryMap.put("Zip Code", code);

        firebaseFirestore.collection("Delivery_Details").document(code).set(DelivaryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {

                    pd.dismiss();

                    Toast.makeText(diliveryActivity.this, " Details Added Successfully ...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(diliveryActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();


                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(diliveryActivity.this, " Firestore Retrieve : " + error, Toast.LENGTH_LONG).show();
                }


            }
        });


    }



    private void AddData() {

        String street = streetText.getText().toString();
        String city = cityText.getText().toString();
        String state = stateText.getText().toString();
        String code = codeText.getText().toString();


        if (TextUtils.isEmpty(street) && TextUtils.isEmpty(city) && TextUtils.isEmpty(state) && TextUtils.isEmpty(code)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(street)) {
            Toast.makeText(this, "Street Address  is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "City Name is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(state)) {
            Toast.makeText(this, " State is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, " Your Zip Code is required", Toast.LENGTH_SHORT).show();
        } else {

            pd.setTitle("Adding Data . . .");
            pd.show();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Delivery_Details");




            Map<String, Object> DelivaryMap = new HashMap<>();


            DelivaryMap.put("Street Name", street);
            DelivaryMap.put("City Name", city);
            DelivaryMap.put("State", state);
            DelivaryMap.put("Zip Code", code);

            firebaseFirestore.collection("Delivery_Details").document(code).set(DelivaryMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if (task.isSuccessful()) {

                        pd.dismiss();

                        Toast.makeText(diliveryActivity.this, " Details Added Successfully ...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(diliveryActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();


                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(diliveryActivity.this, " Firestore Retrieve : " + error, Toast.LENGTH_LONG).show();
                    }


                }
            });


        }
    }





}
