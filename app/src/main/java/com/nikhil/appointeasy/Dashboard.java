package com.nikhil.appointeasy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Dashboard extends Fragment {


    View view;
    ConstraintLayout addDoctor,editDoctor,userList,total_enquiry,today_enquiry;
    DatabaseReference reference;
    TextView enquiry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        addDoctor = view.findViewById(R.id.addDoctor);
        editDoctor = view.findViewById(R.id.editDoctor);
        userList = view.findViewById(R.id.userList);
        total_enquiry = view.findViewById(R.id.totalEnquiry);
        today_enquiry = view.findViewById(R.id.today_enquiry);
        enquiry = view.findViewById(R.id.enquiry);
        reference = FirebaseDatabase.getInstance().getReference().child("total");

        editDoctor.setOnClickListener(v->{
            Fragment someFragment = new DoctorList();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.change_layout1, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });


        addDoctor.setOnClickListener(v->{
            Fragment someFragment = new AddDoctor();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.change_layout1, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });

        userList.setOnClickListener(v->{
            Fragment someFragment = new UserList();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.change_layout1, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });


        total_enquiry.setOnClickListener(v->{
            Fragment someFragment = new TotalEnquiries();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.change_layout1, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });

        today_enquiry.setOnClickListener(v->{
            Fragment someFragment = new TodayEnquiry();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.change_layout1, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                enquiry.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}