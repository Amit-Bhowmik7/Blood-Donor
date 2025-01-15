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

public class Admin_HelpLIne_List_Adapter extends RecyclerView.Adapter<Admin_HelpLIne_List_Adapter.RequestViewHolder>{

    private List<Help_Line_Add_Handler> requestList;

    public Admin_HelpLIne_List_Adapter(List<Help_Line_Add_Handler> requestList) {
        this.requestList = requestList;
    }
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_help_line_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_HelpLIne_List_Adapter.RequestViewHolder holder, int position) {
        Help_Line_Add_Handler request = requestList.get(position);

        holder.titleTextView.setText(request.getTitle());
        holder.contactTextView.setText(request.getContactNumber());

        holder.callButton.setOnClickListener(v -> {
            String contactNumber = request.getContactNumber();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contactNumber));

            // Show a chooser to let the user select their preferred app
            Intent chooser = Intent.createChooser(intent, "Select an app to call");
            v.getContext().startActivity(chooser);
        });

        if (request.getRequestId() !=null) {
            holder.editButton.setOnClickListener(v -> {
                SharedPreferences factsPreferences = v.getContext().getSharedPreferences("helpLineList", MODE_PRIVATE);
                saveToSharedPreferences(factsPreferences, request);
                Intent intent = new Intent(v.getContext(), Admin_HelpLIne_Post_Edit.class);
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

    private void saveToSharedPreferences(SharedPreferences factsPreferences, Help_Line_Add_Handler request) {
        SharedPreferences.Editor editor = factsPreferences.edit();

        editor.putString("h_title",request.getTitle());
        editor.putString("h_contactNumber", request.getContactNumber());
        editor.putString("h_requestId", request.getRequestId());
        editor.apply();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contactTextView;
        ImageButton callButton, editButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleShow);
            contactTextView = itemView.findViewById(R.id.contactNumberShow);
            callButton = itemView.findViewById(R.id.callButton);
            editButton = itemView.findViewById(R.id.editbutton);
        }
    }
}
