package com.iter.marmoset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SalonFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    String query = "*";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SalonFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SalonFragment newInstance(String query) {
        SalonFragment fragment = new SalonFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = getArguments().getString("query");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_salon_list, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        if (query.equals("*")){
            db.collection("Salons").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        List<Salon> salons = new ArrayList<Salon>();
                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            Log.e("results: ", documentSnapshot.getData().toString());
                            Salon salon = documentSnapshot.toObject(Salon.class);
                            salons.add(salon);
                        }
                        recyclerView.setAdapter(new MySalonRecyclerViewAdapter(salons, mListener, getActivity()));

                    }
                }
            });
        }else {
            db.collection("Salons").whereEqualTo("zipcode", query).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Salon> salons = new ArrayList<Salon>();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Log.e("results: ", documentSnapshot.getData().toString());
                            Salon salon = documentSnapshot.toObject(Salon.class);
                            salons.add(salon);
                            recyclerView.setAdapter(new MySalonRecyclerViewAdapter(salons, mListener, getActivity()));
                        }
                    }
                }
            });
        }
        // Set the adapter

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Salon salon);
    }
}
