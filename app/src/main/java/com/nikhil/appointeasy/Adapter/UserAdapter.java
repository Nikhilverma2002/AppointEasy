package com.nikhil.appointeasy.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikhil.appointeasy.Model.UserModel;
import com.nikhil.appointeasy.R;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    Context context;
    ArrayList<UserModel> list ;



    public UserAdapter(ArrayList<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        if (position < list.size()) {

            String name = list.get(position).getName();
            if (name != null && !name.isEmpty()) {
                setRandomCircularBackground(holder.first);
                String firstLetter = String.valueOf(name.charAt(0));
                holder.first.setText(firstLetter.toUpperCase());
                holder.name.setText(name);
                holder.email.setText(list.get(position).getEmail());
            }

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email,first;
        CircleImageView profile_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            first = itemView.findViewById(R.id.first);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    public int getRandomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public void setRandomCircularBackground(TextView textView) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(getRandomColor());  // Set the random color
        textView.setBackground(drawable);
    }
}
