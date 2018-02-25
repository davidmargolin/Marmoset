package com.iter.marmoset;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iter.marmoset.SalonFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MySalonRecyclerViewAdapter extends RecyclerView.Adapter<MySalonRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    List<Salon> mValues;
    Context context;

    public MySalonRecyclerViewAdapter(List<Salon> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_salon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.nameView.setText(mValues.get(position).getName());
        holder.addressView.setText(mValues.get(position).getAddress().replace("\\n","\n"));
        holder.likesView.setText(Integer.toString(mValues.get(position).getLikes())+" Likes");
        Picasso.with(context).load(mValues.get(position).getImage_url()).resize(600,600).centerCrop().into(holder.promoView);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(mValues.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final TextView addressView;
        public final ImageView promoView;
        public final TextView likesView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            likesView = (TextView) view.findViewById(R.id.likes);
            nameView = (TextView) view.findViewById(R.id.name);
            addressView = (TextView) view.findViewById(R.id.address);
            promoView = (ImageView) view.findViewById(R.id.salonpic);
        }

    }
}
