package com.example.frizty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frizty.Model.delivery;
import com.example.frizty.ViewHolder.ViewHolderDilivery;

import java.util.List;

public class deliveryCusAdapter extends RecyclerView.Adapter<ViewHolderDilivery> {


    private ListDilivery listDilivery;
    private List<delivery> userList;
    private Context context;

       public deliveryCusAdapter(ListDilivery listDilivery, List<delivery> userList) {
           this.listDilivery = listDilivery;
           this.userList = userList;

       }

    @NonNull
    @Override
    public ViewHolderDilivery onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_user,parent,false);

        ViewHolderDilivery viewHolder= new ViewHolderDilivery((itemView));

        viewHolder.setOnClickListener(new ViewHolderDilivery.ClickListener() {


            @Override
            public void onItemClick(View view, int position) {


                String streetname = userList.get(position).getStreetName();
                String cityname = userList.get(position).getCity();
                String statename = userList.get(position).getState();
                String codename = userList.get(position).getZipcode();


                Toast.makeText(listDilivery,streetname+"\n"+cityname+"\n"+statename+"\n"+codename,Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(listDilivery);
                String[] options = {"Update","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0 )
                        {
                            //update
//                            String id   = userList.get(position).getId();
                            String street = userList.get(position).getStreetName();
                            String city = userList.get(position).getCity();
                            String state = userList.get(position).getState();
                            String code = userList.get(position).getZipcode();


                            Intent intent = new Intent(listDilivery,diliveryActivity.class);

//                            intent.putExtra("rDID",id);
                            intent.putExtra("rStreetName",street);
                            intent.putExtra("rCityName",city);
                            intent.putExtra("rState",state);
                            intent.putExtra("rZipCode",code);


                            listDilivery.startActivity(intent);


                        }
                        if (which == 1)
                        {
                            //delete
                            listDilivery.deleteData(position);

                        }


                    }
                }).create().show();

            }
        });

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolderDilivery viewHolder, int i) {

        viewHolder.streetname.setText(userList.get(i).getStreetName());
        viewHolder.cityname.setText(userList.get(i).getCity());
        viewHolder.statename.setText(userList.get(i).getState());
        viewHolder.codename.setText(userList.get(i).getZipcode());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
