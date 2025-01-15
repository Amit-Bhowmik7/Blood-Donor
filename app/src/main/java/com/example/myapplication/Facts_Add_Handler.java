package com.example.myapplication;

import java.util.UUID;

public class Facts_Add_Handler {
    String question, answer, requestId;

    public Facts_Add_Handler() {

    }

    public Facts_Add_Handler(String question, String answer, String requestId) {
        this.question = question;
        this.answer = answer;
        this.requestId = requestId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
