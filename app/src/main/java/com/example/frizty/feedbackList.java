package com.example.frizty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.frizty.Model.model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class feedbackList extends AppCompatActivity {

    private  RecyclerView mrecyclerView;
    private List<model>modelList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;


    private FirebaseFirestore db;
    private customAdapter adapter;
    private  ProgressDialog pd;

    private  FloatingActionButton addFeedBtn;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        db=FirebaseFirestore.getInstance();
        addFeedBtn = findViewById(R.id.addFeed);
      mrecyclerView = findViewById(R.id.productsList);


      mrecyclerView.setHasFixedSize(true);
      layoutManager = new LinearLayoutManager(this);
      mrecyclerView.setLayoutManager(layoutManager);


      pd = new ProgressDialog(this);

      showData();

      addFeedBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {


              Intent intent = new Intent(feedbackList.this, feedback.class);
              startActivity(intent);
              finish();
          }
      });

    }

    private void showData() {

            pd.setTitle("Loading Data. . . ");
            pd.show();

            db.collection("Feedback")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            modelList.clear();
                            pd.dismiss();

                            for (DocumentSnapshot  doc :task.getResult()){
                                model  mdl = new model
                                        ( doc.getString("FID"),
                                                doc.getString("Name"),
                                                doc.getString("Email"),
                                                doc.getString("Comment")
                                 );

                                modelList.add(mdl);

                            }

                            adapter =new customAdapter(feedbackList.this,modelList);

                            mrecyclerView.setAdapter(adapter);
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            pd.dismiss();
                            Toast.makeText(feedbackList.this,"Error.." +e.getMessage(),Toast.LENGTH_SHORT).show();




                        }
                    });


    }


    public void deleteData(int index)
   {

       pd.setTitle("Deleting Data. . . ");
       pd.show();

       db.collection("Feedback").document(modelList.get(index).getId())
               .delete()
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {

                       Toast.makeText(feedbackList.this,"Deleted.." ,Toast.LENGTH_SHORT).show();
                       showData();

                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               pd.dismiss();

               Toast.makeText(feedbackList.this,"Error.." +e.getMessage(),Toast.LENGTH_SHORT).show();
           }
       });
   }





}
