package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Admin_Facts_List_Adapter extends RecyclerView.Adapter<Admin_Facts_List_Adapter.RequestViewHolder> {
    private List<Facts_Add_Handler> requestList;

    public Admin_Facts_List_Adapter(List<Facts_Add_Handler> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_facts_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_Facts_List_Adapter.RequestViewHolder holder, int position) {
        Facts_Add_Handler request = requestList.get(position);

        holder.questionTextView.setText(request.getQuestion());
        holder.answerTextView.setText(request.getAnswer());

        if (request.getRequestId() !=null) {
            holder.editButton.setOnClickListener(v -> {
                SharedPreferences factsPreferences = v.getContext().getSharedPreferences("factsList", MODE_PRIVATE);
                saveToSharedPreferences(factsPreferences, request);
                Intent intent = new Intent(v.getContext(), Admin_Facts_Post_Edit.class);
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

    private void saveToSharedPreferences(SharedPreferences factsPreferences, Facts_Add_Handler request) {
        SharedPreferences.Editor editor = factsPreferences.edit();

        editor.putString("f_question",request.getQuestion());
        editor.putString("f_answer", request.getAnswer());
        editor.putString("f_requestId", request.getRequestId());
        editor.apply();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, answerTextView;
        ImageView editButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.facts_questionTextView);
            answerTextView = itemView.findViewById(R.id.facts_answerTextView);
            editButton = itemView.findViewById(R.id.editbutton);
        }
    }
}