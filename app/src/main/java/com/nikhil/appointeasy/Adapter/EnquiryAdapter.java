package com.nikhil.appointeasy.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikhil.appointeasy.Model.DoctorModel;
import com.nikhil.appointeasy.Model.EnquiryModel;
import com.nikhil.appointeasy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnquiryAdapter  extends RecyclerView.Adapter<EnquiryAdapter.ViewHolder>{

    List<EnquiryModel> list;
    View view;
    Context context;
    DatabaseReference reference;
    DatabaseReference enquiryRef;


    public EnquiryAdapter(ArrayList<EnquiryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public EnquiryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_enquiry,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnquiryAdapter.ViewHolder holder, int position) {

        reference = FirebaseDatabase.getInstance().getReference().child("doctors");
        enquiryRef = FirebaseDatabase.getInstance().getReference().child("appointments");

        if (position < list.size()) {

            if (list.get(position).getName() != null) {
                holder.name.setText(list.get(position).getName());
            }

            if (list.get(position).getDate() != null) {
                holder.tvAppointmentDate.setText(list.get(position).getDate());
            }

            if (list.get(position).getTime() != null) {
                holder.tvAppointmentTime.setText(list.get(position).getTime());
            }

            if (list.get(position).getDoctor() != null) {
                reference.child(list.get(position).getDoctor()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.doctorName.setText("For Dr. " + snapshot.child("name").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            if(list.get(position).getEnquiry().equals("yes")){

                setRandomCircularBackground(holder.cardConstraintLayout);
            }

            holder.enquiry.setOnClickListener(v->{

                enquiryRef.child(list.get(position).getPushkey()).child("enquiry").setValue("yes");
                list.clear();
                Toast.makeText(context, "Patient Checked", Toast.LENGTH_SHORT).show();

            });

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAppointmentDate,tvAppointmentTime,name,doctorName;
        ConstraintLayout cardConstraintLayout;
        ImageView enquiry;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            name = itemView.findViewById(R.id.name);
            doctorName = itemView.findViewById(R.id.doctorName);
            enquiry = itemView.findViewById(R.id.enquiry);
            cardConstraintLayout = itemView.findViewById(R.id.cardConstraintLayout);
        }
    }

    public void setRandomCircularBackground(ConstraintLayout layout) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor("#b2ffb2"));  // Set the specified green color
        drawable.setCornerRadius(10 * context.getResources().getDisplayMetrics().density);  // Set the corner radius
        layout.setBackground(drawable);

    }

}
