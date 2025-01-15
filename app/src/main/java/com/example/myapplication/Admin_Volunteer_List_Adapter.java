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

public class Admin_Volunteer_List_Adapter extends RecyclerView.Adapter<Admin_Volunteer_List_Adapter.RequestViewHolder> {

    private List<Volunteer_Add_Handler> requestList;

    public Admin_Volunteer_List_Adapter(List<Volunteer_Add_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Volunteer_List_Adapter.RequestViewHolder holder, int position) {
        Volunteer_Add_Handler request = requestList.get(position);

        holder.volunteerNameTextView.setText(request.getVolinteerName());
        holder.volunteerDistrictTextView.setText(request.getDistrict());
        holder.volunteerUpazilaTextView.setText(request.getUpazila());
        holder.volunteerPresentAddressTextView.setText(request.getPresentAddress());
        holder.volunteerMobileNumberTextView.setText(request.getMobileNumber());

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
        //SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("UserPrefs", holder.itemView.getContext().MODE_PRIVATE);
        //String signInAccountNumber = sharedPreferences.getString("mobileNumber", "N/A");
        if (request.getMobileNumber() !=null) {
            holder.editButton.setOnClickListener(v -> {
                SharedPreferences volunteerPreferences = v.getContext().getSharedPreferences("volunteerRequest", MODE_PRIVATE);
                saveToSharedPreferences(volunteerPreferences, request);
                Intent intent = new Intent(v.getContext(), Admin_Volunter_Post_Edit.class);
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
    private void saveToSharedPreferences(SharedPreferences volunteerPreferences, Volunteer_Add_Handler request) {
        SharedPreferences.Editor editor = volunteerPreferences.edit();

        editor.putString("v_name",request.getVolinteerName());
        editor.putString("v_district", request.getDistrict());
        editor.putString("v_upazila", request.getUpazila());
        editor.putString("v_presentAddress", request.getPresentAddress());
        editor.putString("v_mobileNumber", request.getMobileNumber());
        editor.putString("v_accountNumber", request.getAccountNumber());
        editor.apply();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView volunteerNameTextView, volunteerDistrictTextView, volunteerUpazilaTextView, volunteerPresentAddressTextView, volunteerMobileNumberTextView;
        ImageButton callButton, editButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            volunteerNameTextView = itemView.findViewById(R.id.volunteer_nameTextView);
            volunteerDistrictTextView = itemView.findViewById(R.id.volunteer_districtTextView);
            volunteerUpazilaTextView = itemView.findViewById(R.id.volunteer_upazilaTextView);
            volunteerPresentAddressTextView = itemView.findViewById(R.id.volunteer_present_addressTextView);
            volunteerMobileNumberTextView = itemView.findViewById(R.id.volunteer_mobileNumberTextView);
            callButton = itemView.findViewById(R.id.callButton);
            editButton = itemView.findViewById(R.id.editbutton);
        }
    }
}
