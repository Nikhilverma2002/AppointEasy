package com.nikhil.appointeasy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class DoctorProfile extends Fragment {

   View view;

   Context contextNullSafe;
   ImageView back;
   LinearLayout layout1, layout2;
    FirebaseAuth auth;
    FirebaseUser user;
    Button btnMakeVisit;
   TextView name,experience,location,amount,appointments,about,contact;
    String addtostack, dat, nam, abou,amoun,experienc,contac ,locatio,appointment,number,pushke;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
        name = view.findViewById(R.id.name);
        experience = view.findViewById(R.id.experience);
        appointments = view.findViewById(R.id.appointments);
        amount = view.findViewById(R.id.amount);
        about = view.findViewById(R.id.about);
        back = view.findViewById(R.id.next);
        contact = view.findViewById(R.id.contact);
        btnMakeVisit = view.findViewById(R.id.btnMakeVisit);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();


        if (contextNullSafe == null) getContextNullSafety();

        try {
            assert getArguments() != null;
            addtostack=getArguments().getString("sending_user_from_home");
            //uid = getArguments().getString("uid_sending_post");
            nam = getArguments().getString("name");
            dat = getArguments().getString("date");
            abou = getArguments().getString("about");
            pushke = getArguments().getString("pushkey");
            amoun = getArguments().getString("amount");
            number = getArguments().getString("number");
            experienc = getArguments().getString("experience");
            locatio = getArguments().getString("location");
            appointment = getArguments().getString("appointment");
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnMakeVisit.setOnClickListener(v->{
            AppointForm fragment = new AppointForm();
            Bundle args = new Bundle();
            args.putString("doctor_pushkey", pushke);
            fragment.setArguments(args);
            ((FragmentActivity)getContextNullSafety()).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.post_layout, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        back.setOnClickListener(v->{
            back();
        });

        layout2.setOnClickListener(v->{
            String message = "To " + nam + "\n\nhello Sir, I am " + user.getDisplayName() + ", I want to book an appointment of";

            if(number!=null) {
                startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse(
                                        String.format("https://api.whatsapp.com/send?phone=%s&text=%s", "+91" + number, message)
                                )
                        )
                );
            }
        });




        layout1.setOnClickListener(v-> {

                    if (locatio != null) {
                        String strUri = "http://maps.google.com/maps?q=loc:" + locatio + " (" + nam + "'s Clinic" + ")";
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                        startActivity(intent);

                    }
                }
        );

        return view;
    }

    private void back() {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().remove(DoctorProfile.this).commit();
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


    @Override
    public void onStart() {
        super.onStart();

        if(!nam.equals(""))
            name.setText(nam);
        if(!experienc.equals(""))
            experience.setText(experienc);
        if(!amoun.equals(""))
            amount.setText(amoun);
        if(!appointment.equals(""))
            appointments.setText(appointment);
        if(!abou.equals(""))
            about.setText(abou);

        if(!number.equals(""))
            contact.setText(number);

    }
}