package com.nikhil.appointeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikhil.appointeasy.Adapter.DoctorAdapter;
import com.nikhil.appointeasy.Model.DoctorModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Home extends AppCompatActivity {


    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    RecyclerView recyclerView;
    EditText search;
    ArrayList<DoctorModel> list;
    ArrayList<DoctorModel> mylist;
        ImageView loadimage;
    TextView loadText,yes,no;

    SwipeRefreshLayout mSwipeRefreshLayout;
    DoctorAdapter userAdapter;
    TextView name;
    ImageView dashboard,logout,myappointment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("doctors");
        mylist=new ArrayList<>();
        list=new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        loadimage =findViewById(R.id.loadImage);
        loadText = findViewById(R.id.loadText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Home.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setLayoutManager(layoutManager);
        dashboard = findViewById(R.id.dashboard);
        logout = findViewById(R.id.logout);
        search= findViewById(R.id.input);
        mSwipeRefreshLayout = findViewById(R.id.change_layout);
        myappointment = findViewById(R.id.myappointment);




        getPost();

        mSwipeRefreshLayout.setOnRefreshListener(this::getPost);

        name.setText("Hi! " + user.getDisplayName() );

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s+"");
            }
        });

        dashboard.setOnClickListener(v->{
            Fragment fragment = new Dashboard();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.change_layout1, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        });

        myappointment.setOnClickListener(v->{
            Fragment fragment = new MyAppointments();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.change_layout1, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
        });


        logout.setOnClickListener(v->{
            LogOut();
        });

    }

    private void getPost() {

        mSwipeRefreshLayout.setRefreshing(true);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mylist.clear();
                    list.clear();
                    loadimage.setVisibility(View.VISIBLE);
                    loadText.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(DoctorModel.class));
                        loadimage.setVisibility(View.GONE);
                        loadText.setVisibility(View.GONE);
                    }
                    Collections.reverse(list);
                    mSwipeRefreshLayout.setRefreshing(false);

                    userAdapter = new DoctorAdapter(list, Home.this);
                    userAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(userAdapter);
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    loadimage.setVisibility(View.VISIBLE);
                    loadText.setVisibility(View.VISIBLE);
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LogOut(){
        Dialog dialog;
        dialog = new Dialog(Home.this);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        yes = dialog.findViewById(R.id.yes);
        no = dialog.findViewById(R.id.no);

        yes.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(Home.this, login.class));
            finish();
        });

        no.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search (String s) {
        mylist.clear();
        for (DoctorModel object : list) {
            try {
                if (object.getName().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                } else if (object.getAmount().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DoctorAdapter userAdapter = new DoctorAdapter(mylist,Home.this);
        userAdapter.notifyDataSetChanged();
        if (recyclerView != null)
            recyclerView.setAdapter(userAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getAdmin();
    }

    public void getAdmin(){

        DatabaseReference admin_ref = FirebaseDatabase.getInstance().getReference().child("admins");
        admin_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    if (Objects.requireNonNull(ds.getKey()).equals(user.getUid())) {
                        dashboard.setVisibility(View.VISIBLE);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}