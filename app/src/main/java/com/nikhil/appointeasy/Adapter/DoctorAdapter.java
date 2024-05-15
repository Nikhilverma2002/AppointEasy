package com.nikhil.appointeasy.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nikhil.appointeasy.AppointForm;
import com.nikhil.appointeasy.DoctorProfile;
import com.nikhil.appointeasy.Model.DoctorModel;
import com.nikhil.appointeasy.R;
import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>{


    List<DoctorModel> list;
    View view;
    Context context;
    FirebaseAuth auth;
    FirebaseUser user;

    public DoctorAdapter(ArrayList<DoctorModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_doctor,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder holder, int position) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (position < list.size()) {

            if (list.get(position).getName() != null) {
                holder.name.setText("Dr. " +list.get(position).getName());
            }

            if (list.get(position).getExperience() != null) {
                holder.experience.setText(list.get(position).getExperience() + "+ years experience");
            }

//            if (list.get(position).getDate() != null) {
//                holder.date.setText(list.get(position).getLocation());
//            }

        }

        holder.card.setOnClickListener(v->{
            DoctorProfile profile = new DoctorProfile();

            Bundle args = new Bundle();
            args.putString("sending_user_from_home","addstack");
           // args.putString("uid_sending_post", list.get(position).getUid());
            args.putString("name",list.get(position).getName());
            args.putString("experience",list.get(position).getExperience());
            args.putString("number",list.get(position).getContact());
            args.putString("amount",list.get(position).getAmount());
            args.putString("about",list.get(position).getAbout());
            args.putString("pushkey",list.get(position).getPushkey());
            args.putString("date",list.get(position).getDate());
            args.putString("appointment",list.get(position).getAppointments());
            args.putString("location",list.get(position).getLocation());
            profile.setArguments(args);
            ((FragmentActivity)context).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.post_layout, profile)
                    .addToBackStack(null)
                    .commit();
        });


        holder.btnMakeVisit.setOnClickListener(v->{

            AppointForm fragment = new AppointForm();
            Bundle args = new Bundle();
            args.putString("doctor_pushkey", list.get(position).getPushkey());
            fragment.setArguments(args);
            ((FragmentActivity)context).getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.post_layout, fragment)
                    .addToBackStack(null)
                    .commit();


        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,experience,date,tvAppointmentDate;
        ConstraintLayout card;
        Button btnMakeVisit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardConstraintLayout);
            name = itemView.findViewById(R.id.tvDoctorName);
            experience = itemView.findViewById(R.id.tvDoctorExperience);
            date = itemView.findViewById(R.id.tvAppointmentDate);
            btnMakeVisit = itemView.findViewById(R.id.btnMakeVisit);
        }
    }



}
