package com.e.mytravel4;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter {

    private AdapterListener listener;
    private Context context;
    private List<Address> locations;

    public SearchAdapter(Context context, List<Address> locations, AdapterListener listener) {
        this.locations = locations;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myHolder = ((MyViewHolder)holder);
        final TextView text = myHolder.text;
        View parent = myHolder.parent;
        Address location = locations.get(position);
        String show = location.getCountryName() + ", " + location.getLocality() + ", "
                + location.getThoroughfare() + ", " + location.getSubThoroughfare();
        text.setText(show);

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(locations.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public View parent;

        public MyViewHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.row_parent);
            text = view.findViewById(R.id.row_text);
        }
    }

    public interface AdapterListener{
        void onItemClicked(Address location);
    }
}
