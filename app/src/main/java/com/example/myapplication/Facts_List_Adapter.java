package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Facts_List_Adapter extends RecyclerView.Adapter<Facts_List_Adapter.RequestViewHolder> {
    private List<Facts_Add_Handler> requestList;

    public Facts_List_Adapter(List<Facts_Add_Handler> requestList) {
        this.requestList = requestList;
    }
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.facts_list_sample, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Facts_List_Adapter.RequestViewHolder holder, int position) {
        Facts_Add_Handler request = requestList.get(position);

        holder.questionTextView.setText(request.getQuestion());
        holder.answerTextView.setText(request.getAnswer());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, answerTextView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.facts_questionTextView);
            answerTextView = itemView.findViewById(R.id.facts_answerTextView);
        }
    }
}
