package com.nikhil.appointeasy;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddDoctor extends Fragment {

  View view;

    Context contextNullSafe;
    EditText name,experience,amount,location, address, education,number,describe;
    TextView submit;
    Dialog dialog;
    String pushkey;
    DatabaseReference user_reference, reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_add_doctor, container, false);

        name = view.findViewById(R.id.name);
        experience =view. findViewById(R.id.experience);
        amount = view.findViewById(R.id.amount);
        location = view.findViewById(R.id.location);
        education = view.findViewById(R.id.education);
        submit = view.findViewById(R.id.submit_txt);
        number = view.findViewById(R.id.contact);
        describe = view.findViewById(R.id.describe);


        reference = FirebaseDatabase.getInstance().getReference().child("doctors");


        pushkey=reference.push().getKey();

        submit.setOnClickListener(v->{
            dataPush();
        });

        return view;
    }

    private void dataPush() {

        dialog = new Dialog(getActivity());
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        reference.child(pushkey).child("name").setValue(name.getText().toString());
        reference.child(pushkey).child("education").setValue(education.getText().toString());
        reference.child(pushkey).child("experience").setValue(experience.getText().toString());
        reference.child(pushkey).child("amount").setValue(amount.getText().toString());
        reference.child(pushkey).child("appointments").setValue("0");
        reference.child(pushkey).child("about").setValue(describe.getText().toString());
        reference.child(pushkey).child("location").setValue(location.getText().toString());
        reference.child(pushkey).child("contact").setValue(number.getText().toString());
        reference.child(pushkey).child("pushkey").setValue(pushkey);

        dialog.dismiss();
        back();
    }


    private void back() {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().remove(AddDoctor.this).commit();
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