package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Blood_Request_Adapter extends RecyclerView.Adapter<Blood_Request_Adapter.RequestViewHolder> {
    private List<Blood_Request_Handler> requestList;

    public Blood_Request_Adapter(List<Blood_Request_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_request_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Blood_Request_Handler request = requestList.get(position);

        // Populate UI components
        holder.bloodGroupTextView.setText(request.getBloodGroup());
        holder.nameTextView.setText(request.getFullName());
        holder.districtTextView.setText(request.getDistrict());
        holder.diseasesTextView.setText(request.getDisease());
        holder.bloodQuantityTextView.setText(request.getBloodQuantity());
        holder.dateTextView.setText(request.getDate());
        holder.timeTextView.setText(request.getTime());
        holder.mobileNumberTextView.setText(request.getMobileNumber());
        holder.hospitalNameTextView.setText(request.getHospitalName());
        holder.hospitalLocationTextView.setText(request.getHospitalLocation());

        // Call button listener
        holder.callButton.setOnClickListener(v -> initiateCall(v.getContext(), request.getMobileNumber()));

        // Edit button listener
        // Fetch the stored account number (example from SharedPreferences)
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("UserPrefs", holder.itemView.getContext().MODE_PRIVATE);
        String signInAccountNumber = sharedPreferences.getString("mobileNumber", "N/A");

        if (request.getAccountNumber().equals(signInAccountNumber)) {
            holder.editButton.setOnClickListener(v -> {
                SharedPreferences bloodPreferences = v.getContext().getSharedPreferences("bloodRequest", MODE_PRIVATE);
                saveToSharedPreferences(bloodPreferences, request);
                Intent intent = new Intent(v.getContext(), Blood_Request_Post_Edit.class);
                v.getContext().startActivity(intent);
            });
        } else {
            // Disable the "Edit" button if the mobile numbers don't match
            //holder.editButton.setEnabled(false);

            holder.editButton.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "You can only edit your own posts", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    // Reusable method to initiate a call
    private void initiateCall(Context context, String mobileNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mobileNumber));
        Intent chooser = Intent.createChooser(intent, "Select an app to call");
        context.startActivity(chooser);
    }

    // Save request data to SharedPreferences
    private void saveToSharedPreferences(SharedPreferences preferences, Blood_Request_Handler request) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("p_name", request.getFullName());
        editor.putString("p_district", request.getDistrict());
        editor.putString("p_bloodGroup", request.getBloodGroup());
        editor.putString("p_bloodQuantity", request.getBloodQuantity());
        editor.putString("p_date", request.getDate());
        editor.putString("p_time", request.getTime());
        editor.putString("p_disease", request.getDisease());
        editor.putString("p_hospital_Name", request.getHospitalName());
        editor.putString("p_hospital_Location", request.getHospitalLocation());
        editor.putString("p_mobileNumber", request.getMobileNumber());
        editor.putString("p_requestId", request.getRequestId());
        editor.apply();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView bloodGroupTextView, nameTextView, districtTextView, diseasesTextView, bloodQuantityTextView, dateTextView, timeTextView, hospitalNameTextView, hospitalLocationTextView, mobileNumberTextView;
        ImageButton callButton, editButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupView);
            nameTextView = itemView.findViewById(R.id.nameView);
            districtTextView = itemView.findViewById(R.id.districtView);
            diseasesTextView = itemView.findViewById(R.id.diseasesView);
            bloodQuantityTextView = itemView.findViewById(R.id.blood_quantityView);
            dateTextView = itemView.findViewById(R.id.dateView);
            timeTextView = itemView.findViewById(R.id.timeView);
            hospitalNameTextView = itemView.findViewById(R.id.hospitalNameView);
            hospitalLocationTextView = itemView.findViewById(R.id.hospital_LocationView);
            mobileNumberTextView = itemView.findViewById(R.id.mobileNumberView);
            callButton = itemView.findViewById(R.id.call_option);
            editButton = itemView.findViewById(R.id.editbutton);
        }
    }
}
