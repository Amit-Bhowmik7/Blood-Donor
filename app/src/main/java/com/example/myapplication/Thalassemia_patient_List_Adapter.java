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

public class Thalassemia_patient_List_Adapter extends RecyclerView.Adapter<Thalassemia_patient_List_Adapter.RequestViewHolder> {
    private List<Thalassemia_Patient_Handler> requestList;

    public Thalassemia_patient_List_Adapter(List<Thalassemia_Patient_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thalassemia_patient_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Thalassemia_patient_List_Adapter.RequestViewHolder holder, int position) {
        Thalassemia_Patient_Handler request = requestList.get(position);

        holder.bloodGroupTextView.setText(request.getBloodGroup());
        holder.patientNameTextView.setText(request.getPatientName());
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
                SharedPreferences thalassemiaPreferences = v.getContext().getSharedPreferences("thalassemiaRequest", MODE_PRIVATE);
                saveToSharedPreferences(thalassemiaPreferences, request);
                Intent intent = new Intent(v.getContext(), Thalassemia_Patient_Edit.class);
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

    private void saveToSharedPreferences(SharedPreferences volunteerPreferences, Thalassemia_Patient_Handler request) {
        SharedPreferences.Editor editor = volunteerPreferences.edit();

        editor.putString("t_name", request.getPatientName());
        editor.putString("t_bloodGroup", request.getBloodGroup());
        editor.putString("t_district", request.getDistrict());
        editor.putString("t_upazila", request.getUpazila());
        editor.putString("t_presentAddress", request.getPresentAddress());
        editor.putString("t_mobileNumber", request.getMobileNumber());
        editor.putString("t_accountNumber", request.getAccountNumber());
        editor.apply();
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView patientNameTextView, bloodGroupTextView, districtTextView, upazilaTextView, presentAddressTextView, mobileNumberTextView;
        ImageButton callButton, editButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodGroupTextView = itemView.findViewById(R.id.thalessemia_patient_bloodGroupTextView);
            patientNameTextView = itemView.findViewById(R.id.thalassemia_patient_nameTextView);
            districtTextView = itemView.findViewById(R.id.thalassemia_patient_districtTextView);
            upazilaTextView = itemView.findViewById(R.id.thalassemia_patient_upazilaTextView);
            presentAddressTextView = itemView.findViewById(R.id.thalassemia_patient_present_addressTextView);
            mobileNumberTextView = itemView.findViewById(R.id.thalassemia_patient_mobileNumberTextView);
            callButton = itemView.findViewById(R.id.callButton);
            editButton = itemView.findViewById(R.id.editbutton);
        }
    }
}
