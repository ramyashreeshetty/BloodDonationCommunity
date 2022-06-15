package com.teamrocket.blooddonationcommunity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class PostInfoAdapter extends ArrayAdapter<PostInfoData> {

    public PostInfoAdapter(@NonNull Context context, ArrayList<PostInfoData> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.post_gridview, parent, false);
        }

        PostInfoData postInfoData = getItem(position);
        TextView bgTV = listitemView.findViewById(R.id.postBG);
        TextView cityTV = listitemView.findViewById(R.id.postCity);
        TextView dateTV = listitemView.findViewById(R.id.postDate);

        bgTV.setText(postInfoData.getBloodGroup());
        cityTV.setText(postInfoData.getCity());
        dateTV.setText(postInfoData.getDate());


        //Onclick methods
        ImageView del = listitemView.findViewById(R.id.deletePostBtn);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMessageDel();

            }
        });

        return listitemView;
    }

    public void popupMessageDel(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Do you want remove this post?");
        alertDialogBuilder.setIcon(R.drawable.construction_icon);
        alertDialogBuilder.setTitle("Blood Received!");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                FragmentTransaction ft = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_container,new ReceiverFragment());
                ft.commit();

            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
