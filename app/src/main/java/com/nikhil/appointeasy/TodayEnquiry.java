package com.nikhil.appointeasy;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikhil.appointeasy.Adapter.EnquiryAdapter;
import com.nikhil.appointeasy.Model.EnquiryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class TodayEnquiry extends Fragment {

    View view;
    Context contextNullSafe;
    DatabaseReference reference;
    RecyclerView recyclerView;

    ArrayList<EnquiryModel> list;
    EnquiryAdapter userAdapter;
    ImageView loadimage;
    ArrayList<EnquiryModel> mylist;
    private ValueEventListener currentListener;
    TextView loadText,date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_today_enquiry, container, false);

        if (contextNullSafe == null) getContextNullSafety();
//Hide the keyboard
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        list = new ArrayList<>();
        mylist = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        date = view.findViewById(R.id.date);

        reference = FirebaseDatabase.getInstance().getReference().child("appointments");
        loadimage = view.findViewById(R.id.loadImage);
        loadText = view.findViewById(R.id.loadText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContextNullSafety());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setItemViewCacheSize(500);
        recyclerView.setLayoutManager(layoutManager);


        date.setOnClickListener(v->{
            datePicker();
            setupEditTextListener();
        });

        getAlumnis();

        return view;
    }

    private void getAlumnis() {
        list.clear();  // Assuming 'list' is your data list that you're populating
        mylist.clear();  // Clearing additional lists if used
        loadimage.setVisibility(View.VISIBLE);  // Show loading indicators
        loadText.setVisibility(View.VISIBLE);

        if (currentListener != null) {
            reference.removeEventListener(currentListener);  // Remove the existing listener
        }

        currentListener = new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String dater = ds.child("date").getValue(String.class);

                    // Determine the correct date string to compare against
                    String compareDate = date.getText().toString().trim().isEmpty() ? currentDate().trim() : date.getText().toString().trim();

                    if (dater != null && dater.equals(compareDate)) {
                        EnquiryModel enquiry = ds.getValue(EnquiryModel.class);
                        if (enquiry != null) {
                            list.add(enquiry);
                            loadimage.setVisibility(View.GONE);
                            loadText.setVisibility(View.GONE);
                        }
                    }
                }

                // Hide loading indicators


                // Notify the adapter of data changes
                Collections.reverse(list);
                userAdapter = new EnquiryAdapter(list, contextNullSafe);
                userAdapter.notifyDataSetChanged();
                if (recyclerView != null) {
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(contextNullSafe, "Error loading data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        reference.addValueEventListener(currentListener);
    }



    private void setupEditTextListener() {

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method can be left empty
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method can be left empty
            }

            @Override
            public void afterTextChanged(Editable s) {
                getAlumnis(); // Call getAlumnis when the text changes

            }
        });
    }


    private String currentDate() {
        String formattedDate = new SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(new Date());
        String[] parts = formattedDate.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        String date = day + "/" + month + "/" + year;
        return date;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentListener != null) {
            reference.removeEventListener(currentListener);
        }
    }


    private void datePicker() {


        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(

                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                },
                year, month, day);
        datePickerDialog.show();


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