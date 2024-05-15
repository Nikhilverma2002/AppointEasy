package com.nikhil.appointeasy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class AppointForm extends Fragment {

    View view;
    FirebaseAuth auth;
    FirebaseUser user;
    Context contextNullSafe;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    EditText name,email,address;
    TextView date,time, submit;
    Dialog dialog;
    String totalEnquiry,doctorEnquiry;
    int totalEnquiryInt,doctorEnquiryInt;
    String pushkey, doctor_pushkey;
    DatabaseReference user_reference, reference;
    DatabaseReference total,doctorReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_appoint_form, container, false);
        auth=FirebaseAuth.getInstance();
        name = view.findViewById(R.id.name);
        email =view. findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        submit = view.findViewById(R.id.submit_txt);

        user = auth.getCurrentUser();
        user_reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        reference = FirebaseDatabase.getInstance().getReference().child("appointments");
         total =  FirebaseDatabase.getInstance().getReference().child("total");
        doctorReference =  FirebaseDatabase.getInstance().getReference().child("doctors");

        if (contextNullSafe == null) getContextNullSafety();
        try {
            assert getArguments() != null;
            doctor_pushkey = getArguments().getString("doctor_pushkey");

        } catch (Exception e) {
            e.printStackTrace();
        }

        pushkey=reference.push().getKey();

        if(user!= null){
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            address.setText("BILASPUR");
        }

        date.setOnClickListener(v->{
            datePicker();
        });
        time.setOnClickListener(v->{
            timePicker();
        });
        getTotalEnquiry();
        getDoctorAppointment();

        submit.setOnClickListener(v->{
            dataPush();
        });

        return view;
    }


    private void timePicker() {
        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // Convert hourOfDay to AM/PM format
                        String amPm;
                        if (hourOfDay < 12) {
                            amPm = "AM";
                        } else {
                            amPm = "PM";
                            // Convert 24-hour format to 12-hour format
                            hourOfDay -= 12;
                        }

                        // Adjust hourOfDay to 12-hour format
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }

                        // Update the text view with the selected time including AM/PM
                        time.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hourOfDay, minute, amPm));
                    }
                }, hour, minute, false);

        timePickerDialog.show();
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
    private void dataPush() {

        dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        reference.child(pushkey).child("name").setValue(name.getText().toString());
        reference.child(pushkey).child("email").setValue(email.getText().toString());
        reference.child(pushkey).child("date").setValue(date.getText().toString());
        reference.child(pushkey).child("time").setValue(time.getText().toString());
        reference.child(pushkey).child("address").setValue(address.getText().toString());
        reference.child(pushkey).child("uid").setValue(user.getUid());
        reference.child(pushkey).child("pushkey").setValue(pushkey);
        reference.child(pushkey).child("doctor").setValue(doctor_pushkey);
        reference.child(pushkey).child("enquiry").setValue("no");
        totalEnquiryInt = totalEnquiryInt +1;
        total.setValue(String.valueOf(totalEnquiryInt));

        doctorEnquiryInt  = doctorEnquiryInt+1;
        doctorReference.child(doctor_pushkey).child("appointments").setValue(doctorEnquiryInt + "");
        dialog.dismiss();
        back();
    }


    private void getDoctorAppointment(){
        doctorReference.child(doctor_pushkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                doctorEnquiry =  snapshot.child("appointments").getValue(String.class);
                if (doctorEnquiry != null) {
                    try {
                        doctorEnquiryInt = Integer.parseInt(doctorEnquiry);
                    } catch (NumberFormatException e) {
                        // Handle the case where totalEnquiry is not a valid integer string
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getTotalEnquiry(){

        total.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                totalEnquiry =  snapshot.getValue(String.class);
                if (totalEnquiry != null) {
                    try {
                        totalEnquiryInt = Integer.parseInt(totalEnquiry);
                    } catch (NumberFormatException e) {
                        // Handle the case where totalEnquiry is not a valid integer string
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void back() {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().remove(AppointForm.this).commit();
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