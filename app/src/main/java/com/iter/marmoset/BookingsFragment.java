package com.iter.marmoset;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.iter.marmoset.dummy.DummyContent;
import com.iter.marmoset.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public BookingsFragment() {
    }

    public static BookingsFragment newInstance() {
        BookingsFragment fragment = new BookingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookings_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();

            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            db.collection("Appointments").whereEqualTo("client_id", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        ArrayList<Booking> books = new ArrayList<Booking>();
                        for (DocumentSnapshot documentSnapshot : task.getResult()){
                            Log.e("results: ", documentSnapshot.getData().toString());
                            Booking book = documentSnapshot.toObject(Booking.class);
                            books.add(book);
                        }
                        recyclerView.setAdapter(new MyBookingsRecyclerViewAdapter(books));

                    }
                }
            });
        }
        return view;
    }


}
