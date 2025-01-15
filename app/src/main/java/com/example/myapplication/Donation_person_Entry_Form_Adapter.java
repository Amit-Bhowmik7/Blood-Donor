package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Donation_person_Entry_Form_Adapter extends RecyclerView.Adapter<Donation_person_Entry_Form_Adapter.RequestViewHolder> {
    private List<Donation_Person_Entry_Form_Handler> requestList;

    public Donation_person_Entry_Form_Adapter(List<Donation_Person_Entry_Form_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_person_entry_form_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Donation_Person_Entry_Form_Handler contributorRequest = requestList.get(position);

        holder.contributorNameTextView.setText(contributorRequest.getName());
        holder.contributordistrictTextView.setText(contributorRequest.getDistrict());
        holder.contributormobileNumberTextView.setText(contributorRequest.getMobileNumber());
        holder.contributorAmountTextView.setText(contributorRequest.getAmount());
        holder.contributorTnxIdTextView.setText(contributorRequest.getTnx_id());

        holder.publishedButton.setOnClickListener(v -> {
            // Save data to Firebase
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ContributorsList");
            String key = databaseReference.push().getKey();

            if (key != null) {
                databaseReference.child(key).setValue(contributorRequest)
                        .addOnSuccessListener(aVoid -> {
                            // Change button text to "Done"
                            holder.publishedButton.setText("Done");
                            holder.publishedButton.setEnabled(false);
                            Toast.makeText(v.getContext(), "Published Successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(v.getContext(), "Failed to Publish: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView contributorNameTextView, contributordistrictTextView, contributormobileNumberTextView, contributorAmountTextView, contributorTnxIdTextView, publishedButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            contributorNameTextView = itemView.findViewById(R.id.contributorNameShow);
            contributordistrictTextView = itemView.findViewById(R.id.contributorDistrictShow);
            contributormobileNumberTextView = itemView.findViewById(R.id.contributorMobileNumbershow);
            contributorAmountTextView = itemView.findViewById(R.id.contributorAmountshow);
            contributorTnxIdTextView = itemView.findViewById(R.id.contributorTnxIdshow);
            publishedButton = itemView.findViewById(R.id.publishedButton);
        }
    }
}
