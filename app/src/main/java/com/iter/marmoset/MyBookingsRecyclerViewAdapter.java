package com.iter.marmoset;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyBookingsRecyclerViewAdapter extends RecyclerView.Adapter<MyBookingsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Booking> mValues;
    Boolean business;
    Context context;
    public MyBookingsRecyclerViewAdapter(ArrayList<Booking> bookings, Boolean business, Context context) {
        mValues = bookings;
        this.business = business;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bookings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.timeView.setText(mValues.get(position).getDate() + " - "+mValues.get(position).getSession());
        holder.nameView.setText(mValues.get(position).getSalon_name());
        holder.serviceView.setText(mValues.get(position).getService()+" with "+mValues.get(position).getStylist());
        if (business){
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BookingViewActivity.class);
                    intent.putExtra("id", mValues.get(position).getClient_id());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final TextView timeView;
        public final TextView serviceView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            timeView = (TextView) view.findViewById(R.id.time);
            nameView = (TextView) view.findViewById(R.id.name);
            serviceView = (TextView) view.findViewById(R.id.service);
        }
    }
}
