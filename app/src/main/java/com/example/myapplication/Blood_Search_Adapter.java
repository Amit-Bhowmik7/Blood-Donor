package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Blood_Search_Adapter extends RecyclerView.Adapter<Blood_Search_Adapter.ViewHolder> {
    private List<User> userList;

    public Blood_Search_Adapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blood_search_result_list_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bloodGroupTextView.setText(user.getBloodGroup());
        holder.nameTextView.setText(user.getName());
        holder.upazilaTextView.setText(user.getUpazila());
        holder.districtTextView.setText(user.getDistrict());
        holder.phoneTextView.setText(user.getPhoneNumber());

        // Call button click listener
        holder.callButton.setOnClickListener(v -> {
            String phoneNumber = user.getPhoneNumber();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));

            // Show a chooser to let the user select their preferred app
            Intent chooser = Intent.createChooser(intent, "Select an app to call");
            v.getContext().startActivity(chooser);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bloodGroupTextView,nameTextView,upazilaTextView, districtTextView, phoneTextView;
        ImageButton callButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            upazilaTextView = itemView.findViewById(R.id.upazilaTextView);
            districtTextView = itemView.findViewById(R.id.districtTextView);
            phoneTextView = itemView.findViewById(R.id.phoneNumberTextView);
            callButton = itemView.findViewById(R.id.call_option);
        }
    }
}

