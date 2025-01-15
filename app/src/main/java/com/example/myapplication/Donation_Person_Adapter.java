package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Donation_Person_Adapter extends RecyclerView.Adapter<Donation_Person_Adapter.RequestViewHolder> {
    private List<Donation_Person_Entry_Form_Handler> requestList;

    public Donation_Person_Adapter(List<Donation_Person_Entry_Form_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donation_person_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Donation_Person_Adapter.RequestViewHolder holder, int position) {
        Donation_Person_Entry_Form_Handler request = requestList.get(position);

        holder.contributorNameTextView.setText(request.getName());
        holder.contributordistrictTextView.setText(request.getDistrict());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView contributorNameTextView, contributordistrictTextView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            contributorNameTextView = itemView.findViewById(R.id.contributorName);
            contributordistrictTextView = itemView.findViewById(R.id.contributorDistrict);
        }
    }
}
