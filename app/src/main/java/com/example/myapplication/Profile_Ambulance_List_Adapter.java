package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

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

public class Profile_Ambulance_List_Adapter extends RecyclerView.Adapter<Profile_Ambulance_List_Adapter.RequestViewHolder> {

    private List<Ambulance_Add_Handler> requestList;

    public Profile_Ambulance_List_Adapter(List<Ambulance_Add_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ambulance_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Profile_Ambulance_List_Adapter.RequestViewHolder holder, int position) {
        Ambulance_Add_Handler request = requestList.get(position);

        holder.nameTextView.setText(request.getName());
        holder.hospitalNameTextView.setText(request.getHospitalName());
        holder.districtTextView.setText(request.getDistrict());
        holder.upazilaTextView.setText(request.getUpazila());
        holder.presentAddressTextView.setText(request.getPresentAddress());
        holder.mobileNumberTextView.setText(request.getMobileNumber());

        // Call button click listener
        holder.callButton.setOnClickListener(v -> {
            String mobileNumber = request.getMobileNumber();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobileNumber));

            // Show a chooser to let the user select their preferred app
            Intent chooser = Intent.createChooser(intent, "Select an app to call");
            v.getContext().startActivity(chooser);
        });

        // Edit button listener
        // Fetch the stored account number (example from SharedPreferences)
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("UserPrefs", holder.itemView.getContext().MODE_PRIVATE);
        String signInAccountNumber = sharedPreferences.getString("mobileNumber", "N/A");

        if (request.getAccountNumber().equals(signInAccountNumber)) {
            holder.editButton.setOnClickListener(v -> {
                SharedPreferences volunteerPreferences = v.getContext().getSharedPreferences("ambulanceRequest", MODE_PRIVATE);
                saveToSharedPreferences(volunteerPreferences, request);
                Intent intent = new Intent(v.getContext(), Profile_Ambulance_Post_Edit.class);
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

    private void saveToSharedPreferences(SharedPreferences volunteerPreferences, Ambulance_Add_Handler request) {
        SharedPreferences.Editor editor = volunteerPreferences.edit();

        editor.putString("a_name",request.getName());
        editor.putString("a_hospitalName",request.getHospitalName());
        editor.putString("a_district", request.getDistrict());
        editor.putString("a_upazila", request.getUpazila());
        editor.putString("a_presentAddress", request.getPresentAddress());
        editor.putString("a_mobileNumber", request.getMobileNumber());
        editor.putString("a_accountNumber", request.getAccountNumber());
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, hospitalNameTextView, districtTextView, upazilaTextView, presentAddressTextView, mobileNumberTextView;
        ImageButton callButton, editButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameShow);
            hospitalNameTextView = itemView.findViewById(R.id.hospitalNameShow);
            districtTextView = itemView.findViewById(R.id.districtShow);
            upazilaTextView = itemView.findViewById(R.id.upazilaShow);
            presentAddressTextView = itemView.findViewById(R.id.present_address_show);
            mobileNumberTextView = itemView.findViewById(R.id.mobileNumbershow);
            callButton = itemView.findViewById(R.id.callButton);
            editButton = itemView.findViewById(R.id.editbutton);
        }
    }
}
