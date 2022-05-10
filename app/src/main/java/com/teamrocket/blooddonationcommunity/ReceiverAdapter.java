package com.teamrocket.blooddonationcommunity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.RViewHolder> implements Filterable {

    Context context;
    ArrayList<ReceiverData> list;
    ArrayList<ReceiverData> listFull;

    public ReceiverAdapter(Context context, ArrayList<ReceiverData> list) {
        this.context = context;
        this.listFull = list;
        this.list=new ArrayList<>(listFull);
    }

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.receiver_item,parent,false);
        return new RViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {

        ReceiverData rd = list.get(position);
        holder.bloodGroup.setText(rd.getBloodGroup());
        holder.city.setText(rd.getCity());
        holder.date.setText(rd.getDate());
        holder.extraMsg.setText(rd.getExtraMsg());
        holder.name.setText(rd.getName());
        holder.phoneNumber.setText(rd.getLatitude());
        holder.time.setText(rd.getLongitude());

        //onclick of itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list.get(position).isVisible){
                    holder.expandedReceiverCard.setVisibility(View.GONE);
                    list.get(position).isVisible=false;

                }
                else{
                    holder.expandedReceiverCard.setVisibility(View.VISIBLE);
                    list.get(position).isVisible=true;

                }

            }
        });

        //onclick to make a phone call
        holder.cardCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String num=rd.getPhoneNumber().trim();
                num=num.replace("+91","");
                num="tel:"+num;
                Toast.makeText(context, num, Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(num));
                view.getContext().startActivity(callIntent);
            }
        });

        //onclick to track the user
        holder.cardTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String lat=rd.getLatitude().trim();
                String lon=rd.getLongitude().trim();

//                double latitude = Double.parseDouble(lat);
//                double longitude = Double.parseDouble(lon);
//                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 13.0698673,74.9973597);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                context.startActivity(intent);


                Intent myIntent = new Intent(view.getContext(),TrackMapActivity.class);
                myIntent.putExtra("lat",lat);
                myIntent.putExtra("lon",lon);
                view.getContext().startActivity(myIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private final Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<ReceiverData> listFiltered = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){

                listFiltered.addAll(listFull);
            }
            else {

                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ReceiverData data : listFull){

                    if(data.bloodGroup.toLowerCase().contains(filterPattern) || data.city.toLowerCase().contains(filterPattern) || data.name.toLowerCase().contains(filterPattern)){
                        listFiltered.add(data);
                    }
                }
            }

            FilterResults results=new FilterResults();
            results.values = listFiltered;
            results.count = listFiltered.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            list.clear();
            list.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };



    public static  class RViewHolder extends RecyclerView.ViewHolder{

        TextView bloodGroup,city,date,extraMsg,name,phoneNumber,time;
        LinearLayout expandedReceiverCard;
        FloatingActionButton cardCallBtn,cardTrackBtn;

        public RViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroup=itemView.findViewById(R.id.receiverBG);
            city=itemView.findViewById(R.id.receiverCity);
            name=itemView.findViewById(R.id.receiverName);
            date=itemView.findViewById(R.id.receiverDate);
            extraMsg=itemView.findViewById(R.id.receiverMsg);
            phoneNumber=itemView.findViewById(R.id.receiverPhone);
            time=itemView.findViewById(R.id.receiverTime);
            expandedReceiverCard=itemView.findViewById(R.id.expandedReceiverCard);
            cardCallBtn=itemView.findViewById(R.id.cardCallBtn);
            cardTrackBtn=itemView.findViewById(R.id.cardTrackBtn);

        }
    }

}
