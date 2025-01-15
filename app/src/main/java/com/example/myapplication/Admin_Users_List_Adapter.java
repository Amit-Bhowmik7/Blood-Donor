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

public class Admin_Users_List_Adapter extends RecyclerView.Adapter<Admin_Users_List_Adapter.RequestViewHolder>{
    private List<New_User_Registration_Handler> requestList;

    public Admin_Users_List_Adapter(List<New_User_Registration_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_users_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Users_List_Adapter.RequestViewHolder holder, int position) {
        New_User_Registration_Handler request = requestList.get(position);


        holder.bloodGroupTextView.setText(request.getBloodGroup());
        holder.nameTextView.setText(request.getName());
        holder.districtTextView.setText(request.getDistrict());
        holder.upazilaTextView.setText(request.getUpazila());
        holder.mobileNumberTextView.setText(request.getPhoneNumber());

        // Call button click listener
        holder.callButton.setOnClickListener(v -> {
            String mobileNumber = request.getPhoneNumber();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobileNumber));

            // Show a chooser to let the user select their preferred app
            Intent chooser = Intent.createChooser(intent, "Select an app to call");
            v.getContext().startActivity(chooser);
        });

        // Edit button listener

        if (request.getPhoneNumber() !=null) {
            holder.deleteButton.setOnClickListener(v -> {
                SharedPreferences volunteerPreferences = v.getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                saveToSharedPreferences(volunteerPreferences, request);
                Intent intent = new Intent(v.getContext(), Admin_Users_account_Delete_Dialog.class);
                v.getContext().startActivity(intent);
            });
        } else {
            // Disable the "Edit" button if the mobile numbers don't match
            //holder.editButton.setEnabled(false);

            holder.deleteButton.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "You can only edit your own posts", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void saveToSharedPreferences(SharedPreferences volunteerPreferences, New_User_Registration_Handler request) {
        SharedPreferences.Editor editor = volunteerPreferences.edit();

        editor.putString("name", request.getName());
        editor.putString("bloodGroup", request.getBloodGroup());
        editor.putString("district", request.getDistrict());
        editor.putString("upazila", request.getUpazila());
        editor.putString("mobileNumber", request.getPhoneNumber());
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView bloodGroupTextView, nameTextView, districtTextView, upazilaTextView, mobileNumberTextView;
        ImageButton callButton, deleteButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            districtTextView = itemView.findViewById(R.id.districtTextView);
            upazilaTextView = itemView.findViewById(R.id.upazilaTextView);
            mobileNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            callButton = itemView.findViewById(R.id.callButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
