package com.example.frizty;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.Model.Feedback;
import com.example.frizty.Model.model;
import com.example.frizty.Prevalent.Prevalent;
import com.example.frizty.ViewHolder.feedbackViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class feedbackList extends AppCompatActivity {

    private  RecyclerView mrecyclerView;
    private List<model>modelList = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

    private EditText feedName, feedEmail, feedComment;
    private Button feedSubmitButton;


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
      feedComment = findViewById(R.id.commenttxt);
      feedEmail = findViewById(R.id.emailtxt);
      feedName = findViewById(R.id.nametxt);




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

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        FirebaseRecyclerOptions<Feedback> options = new FirebaseRecyclerOptions.Builder<Feedback>()
                .setQuery(databaseReference.child("User Feedback")
                        .child(Prevalent.currentOnlineUser.getUsername()).child("Products"), Feedback.class)//Something is wrong in this line
                .build();

        FirebaseRecyclerAdapter<Feedback, feedbackViewHolder> adapter = new FirebaseRecyclerAdapter<Feedback, feedbackViewHolder>(options){

            @NonNull
            @Override
            public feedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                feedbackViewHolder holder = new feedbackViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull feedbackViewHolder holder, int i, @NonNull final Feedback model) {
                holder.rcomment.setText(model.getFeedComment());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Remove"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(feedbackList.this);
                        builder.setTitle("Delete Feedback");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    databaseReference.child("Users")
                                            .child(Prevalent.currentOnlineUser.getUsername())
                                            .child("User Feedback")
                                            .child(model.getFeedComment())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(feedbackList.this, "Removed successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(feedbackList.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
        };
        mrecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public void deleteData(int index)
   {
       onStart();

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
