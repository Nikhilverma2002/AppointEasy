package com.nikhil.appointeasy.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nikhil.appointeasy.DoctorProfile;
import com.nikhil.appointeasy.Model.DoctorModel;
import com.nikhil.appointeasy.R;

import java.util.ArrayList;

public class ListAdapter  extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context context;
    ArrayList<DoctorModel> list ;
    DatabaseReference reference;
    TextView yes,no;
    Dialog dialog;


    public ListAdapter(ArrayList<DoctorModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_doctors,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        reference = FirebaseDatabase.getInstance().getReference().child("doctors");

        if (position < list.size()) {


            holder.name.setText(list.get(position).getName());
            holder.experience.setText(list.get(position).getExperience());
            holder.education.setText(list.get(position).getEducation());

            holder.layout.setOnClickListener(v -> {
                DoctorProfile profile = new DoctorProfile();

                Bundle args = new Bundle();
                args.putString("sending_user_from_home","addstack");
                // args.putString("uid_sending_post", list.get(position).getUid());
                args.putString("name",list.get(position).getName());
                args.putString("experience",list.get(position).getExperience());
                args.putString("number",list.get(position).getContact());
                args.putString("amount",list.get(position).getAmount());
                args.putString("about",list.get(position).getAbout());
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


            holder.delete.setVisibility(View.VISIBLE);

            holder.delete.setOnClickListener(v -> {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_delete);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                yes = dialog.findViewById(R.id.yes);
                no = dialog.findViewById(R.id.no);

                yes.setOnClickListener(v1 -> {
                    dialog.dismiss();
                    reference.child(list.get(position).getPushkey()).removeValue();
                });


                no.setOnClickListener(v2 -> {
                    dialog.dismiss();
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView education;
        TextView experience;
        LinearLayout layout;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            education = itemView.findViewById(R.id.experience);
            experience = itemView.findViewById(R.id.education);
            layout = itemView.findViewById(R.id.layout);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
