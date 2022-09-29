package com.dahdotech.parks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dahdotech.parks.R;
import com.dahdotech.parks.model.Park;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ParkRecyclerViewAdapter extends RecyclerView.Adapter<ParkRecyclerViewAdapter.ViewHolder> {
    private final List<Park> parkList;

    public ParkRecyclerViewAdapter(List<Park> parkList) {
        this.parkList = parkList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.park_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Park park = parkList.get(position);
        holder.parkName.setText(park.getName());
        holder.parkCategory.setText(park.getDesignation());
        holder.parkState.setText(park.getStates());

        if(park.getImages().size() > 0){
            Picasso.get()
                    .load(park.getImages().get(0).getUrl())
                    .placeholder(android.R.drawable.stat_sys_download)
                    .error(android.R.drawable.stat_notify_error)
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.parkImage);
        }
    }

    @Override
    public int getItemCount() {
        return parkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView parkImage;
        public TextView parkName;
        public TextView parkCategory;
        public TextView parkState;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parkImage = itemView.findViewById(R.id.row_park_image_view);
            parkName = itemView.findViewById(R.id.row_park_name_text_view);
            parkCategory = itemView.findViewById(R.id.row_park_category_text_view);
            parkState = itemView.findViewById(R.id.row_park_state_text_view);
        }
    }
}
