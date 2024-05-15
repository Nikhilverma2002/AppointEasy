package com.nikhil.appointeasy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikhil.appointeasy.Adapter.ListAdapter;
import com.nikhil.appointeasy.Adapter.UserAdapter;
import com.nikhil.appointeasy.Model.DoctorModel;
import com.nikhil.appointeasy.Model.UserModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class UserList extends Fragment {


    View view;
    Context contextNullSafe;
    DatabaseReference reference;
    RecyclerView recyclerView;
    EditText search;
    ArrayList<UserModel> list;
    UserAdapter userAdapter;
    ImageView loadimage;
    ArrayList<UserModel> mylist;
    TextView loadText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_list, container, false);


        if (contextNullSafe == null) getContextNullSafety();
//Hide the keyboard
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        list=new ArrayList<>();
        mylist=new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);

        reference = FirebaseDatabase.getInstance().getReference().child("users");
        loadimage = view.findViewById(R.id.loadImage);
        loadText = view.findViewById(R.id.loadText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContextNullSafety());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setLayoutManager(layoutManager);

        search=view.findViewById(R.id.input);


        getAlumnis();

        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s+"");
            }
        });

        return view;
    }
    private void getAlumnis() {
        mylist.clear();
        list.clear();
        loadimage.setVisibility(View.VISIBLE);
        loadText.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    list.add(snapshot.child(Objects.requireNonNull(ds.getKey())).getValue(UserModel.class));
                    loadimage.setVisibility(View.GONE);
                    loadText.setVisibility(View.GONE);
                }
                Collections.reverse(list);

                userAdapter = new UserAdapter(list,contextNullSafe);
                userAdapter.notifyDataSetChanged();
                if (recyclerView != null)
                    recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search (String s) {
        mylist.clear();
        for (UserModel object : list) {
            try {
                if (object.getName().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                } else if (object.getEmail().toLowerCase().contains(s.toLowerCase().trim())) {
                    mylist.add(object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        UserAdapter userAdapter = new UserAdapter(mylist,getContextNullSafety());
        userAdapter.notifyDataSetChanged();
        if (recyclerView != null)
            recyclerView.setAdapter(userAdapter);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }
    public Context getContextNullSafety() {
        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (contextNullSafe != null) return contextNullSafe;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }

}