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

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DViewHolder> implements Filterable {

    Context context2;
    ArrayList<DonorData> list2;
    ArrayList<DonorData> listFull2;

    public DonorAdapter(Context context, ArrayList<DonorData> list2) {
        this.context2 = context;
        this.listFull2 = list2;
        this.list2=new ArrayList<>(listFull2);
    }


    @NonNull
    @Override
    public DonorAdapter.DViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context2).inflate(R.layout.donor_item,parent,false);
        return new DonorAdapter.DViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull DonorAdapter.DViewHolder holder, int position) {

        DonorData rd = list2.get(position);
        holder.bloodGroup.setText(rd.getBloodGroup());
        holder.city.setText(rd.getCity());
        holder.extraMsg.setText(rd.getExtraMsg());
        holder.name.setText(rd.getName());
        holder.phoneNumber.setText(rd.getPhoneNumber());


        //onclick of itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list2.get(position).isVisible){
                    holder.expandedDonorCard.setVisibility(View.GONE);
                    list2.get(position).isVisible=false;

                }
                else{
                    holder.expandedDonorCard.setVisibility(View.VISIBLE);
                    list2.get(position).isVisible=true;

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
                Toast.makeText(context2, num, Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(num));
                view.getContext().startActivity(callIntent);
            }
        });

        //onclick to track the user
        holder.cardTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                double latitude = Double.parseDouble(lat);
//                double longitude = Double.parseDouble(lon);
//                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 13.0698673,74.9973597);
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                context.startActivity(intent);

                String lat=rd.getLatitude().trim();
                String lon=rd.getLongitude().trim();

                Intent myIntent = new Intent(view.getContext(),TrackMapActivity.class);
                myIntent.putExtra("lat",lat);
                myIntent.putExtra("lon",lon);
                view.getContext().startActivity(myIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list2.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter2;
    }

    private final Filter listFilter2 = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<DonorData> listFiltered2 = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){

                listFiltered2.addAll(listFull2);
            }
            else {

                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DonorData data : listFull2){

                    if(data.bloodGroup.toLowerCase().contains(filterPattern) || data.city.toLowerCase().contains(filterPattern) || data.name.toLowerCase().contains(filterPattern)){
                        listFiltered2.add(data);
                    }
                }
            }

            FilterResults results2=new FilterResults();
            results2.values = listFiltered2;
            results2.count = listFiltered2.size();
            return results2;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            list2.clear();
            list2.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class DViewHolder extends RecyclerView.ViewHolder{

        TextView bloodGroup,city,extraMsg,name,phoneNumber;
        LinearLayout expandedDonorCard;
        FloatingActionButton cardCallBtn,cardTrackBtn;

        public DViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroup=itemView.findViewById(R.id.donorBG);
            city=itemView.findViewById(R.id.donorCity);
            name=itemView.findViewById(R.id.donorName);
            extraMsg=itemView.findViewById(R.id.donorMsg);
            phoneNumber=itemView.findViewById(R.id.donorPhone);
            expandedDonorCard=itemView.findViewById(R.id.expandedDonorCard);
            cardCallBtn=itemView.findViewById(R.id.cardCallBtn2);
            cardTrackBtn=itemView.findViewById(R.id.cardTrackBtn2);

        }
    }
}
