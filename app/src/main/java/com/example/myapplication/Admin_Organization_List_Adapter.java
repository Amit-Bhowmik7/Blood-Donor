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

public class Admin_Organization_List_Adapter extends RecyclerView.Adapter<Admin_Organization_List_Adapter.RequestViewHolder> {

    private List<Organization_Add_Handler> requestList;

    public Admin_Organization_List_Adapter(List<Organization_Add_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organizationa_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Organization_List_Adapter.RequestViewHolder holder, int position) {
        Organization_Add_Handler request = requestList.get(position);

        holder.organizationNameTextView.setText(request.getOrganizationName());
        holder.districtTextView.setText(request.getDistrict());
        holder.upazilaTextView.setText(request.getUpazila());
        holder.officeAddressTextView.setText(request.getOfficeAddress());
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
        if (request.getMobileNumber() !=null) {
            holder.editButton.setOnClickListener(v -> {
                SharedPreferences volunteerPreferences = v.getContext().getSharedPreferences("organizationRequest", MODE_PRIVATE);
                saveToSharedPreferences(volunteerPreferences, request);
                Intent intent = new Intent(v.getContext(), Admin_Organization_Post_Edit.class);
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
    private void saveToSharedPreferences(SharedPreferences volunteerPreferences, Organization_Add_Handler request) {
        SharedPreferences.Editor editor = volunteerPreferences.edit();

        editor.putString("o_name",request.getOrganizationName());
        editor.putString("o_district", request.getDistrict());
        editor.putString("o_upazila", request.getUpazila());
        editor.putString("o_officeAddress", request.getOfficeAddress());
        editor.putString("o_mobileNumber", request.getMobileNumber());
        editor.putString("o_accountNumber", request.getAccountNumber());
        editor.apply();
    }




    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView organizationNameTextView, districtTextView, upazilaTextView, officeAddressTextView, mobileNumberTextView;
        ImageButton callButton, editButton;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            organizationNameTextView = itemView.findViewById(R.id.organizationNameShow);
            districtTextView = itemView.findViewById(R.id.districtShow);
            upazilaTextView = itemView.findViewById(R.id.upazilaShow);
            officeAddressTextView = itemView.findViewById(R.id.office_address_show);
            mobileNumberTextView = itemView.findViewById(R.id.mobileNumbershow);
            callButton = itemView.findViewById(R.id.callButton);
            editButton = itemView.findViewById(R.id.editbutton);
        }
    }
}
