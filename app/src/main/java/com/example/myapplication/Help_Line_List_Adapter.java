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

public class Help_Line_List_Adapter extends RecyclerView.Adapter<Help_Line_List_Adapter.RequestViewHolder>{
    private List<Help_Line_Add_Handler> requestList;

    public Help_Line_List_Adapter(List<Help_Line_Add_Handler> requestList) {
        this.requestList = requestList;
    }
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_line_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Help_Line_List_Adapter.RequestViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contactTextView;
        ImageButton callButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleShow);
            contactTextView = itemView.findViewById(R.id.contactNumberShow);
            callButton = itemView.findViewById(R.id.callButton);
        }
    }
}
