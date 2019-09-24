package com.example.frizty;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.Model.delivery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListDilivery extends AppCompatActivity {

    private RecyclerView rrecyclerView;
    private List<delivery> userList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

    private TextView streetName, CityName, StateName,Code;



    private FirebaseFirestore db;
    private deliveryCusAdapter adapter;
    private ProgressDialog pd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dilivery);

        db=FirebaseFirestore.getInstance();
        rrecyclerView = findViewById(R.id.deliveryList);
        streetName = findViewById(R.id.streetname);
        CityName = findViewById(R.id.cityname);
        StateName = findViewById(R.id.statename);
        Code = findViewById(R.id.codename);




        rrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rrecyclerView.setLayoutManager(layoutManager);


        pd = new ProgressDialog(this);

        showData();


    }

    private void showData() {

        pd.setTitle("Loading Data. . . ");
        pd.show();

        db.collection("Delivery_Details")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        userList.clear();
                        pd.dismiss();

                        for (DocumentSnapshot doc :task.getResult()){
                            delivery use = new delivery(
                                            doc.getString("Street Name"),
                                            doc.getString("City Name"),
                                            doc.getString("State"),
                                             doc.getString("Zip Code")
                                    );

                            userList.add(use);

                        }

                        adapter =new deliveryCusAdapter(ListDilivery.this,userList);

                        rrecyclerView.setAdapter(adapter);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pd.dismiss();
                        Toast.makeText(ListDilivery.this,"Error.." +e.getMessage(),Toast.LENGTH_SHORT).show();




                    }
                });
    }


    public void deleteData(int index) {

        pd.setTitle("Deleting Data. . . ");
        pd.show();

        db.collection("Delivery_Details").document(userList.get(index).getZipcode())
                    .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(ListDilivery.this,"Deleted ... ",Toast.LENGTH_SHORT).show();
                        showData();   //update data

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();

                Toast.makeText(ListDilivery.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });



    }
}

