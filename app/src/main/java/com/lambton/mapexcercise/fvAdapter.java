package com.lambton.mapexcercise;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public abstract class fvAdapter extends RecyclerView.Adapter<fvAdapter.ViewHolder> {

    Context context;
    public List<FavPlaces> places;

    public fvAdapter(Context context, List<FavPlaces> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  interviews= LayoutInflater.from(context).inflate(R.layout.places_item,parent,false);
        return new ViewHolder(interviews);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.place.setText(places.get(position).getLocation());
        holder.delete.setOnClickListener(v -> deleteAddress(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView place;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            place=(TextView)itemView.findViewById(R.id.place_txt);
            delete=(ImageView) itemView.findViewById(R.id.sub_delete);
            itemView.setOnClickListener(v -> onClick(getAdapterPosition()));
        }
    }
    public abstract void deleteAddress(int i);
    public abstract void onClick(int i);
}