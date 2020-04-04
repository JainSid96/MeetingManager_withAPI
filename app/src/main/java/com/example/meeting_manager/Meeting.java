package com.example.meeting_manager;

import java.util.List;

public class Meeting {
    private String start_time;
    private String end_time;
    private String description;
    private List<String> participants;

    public Meeting(String start_time, String end_time, String description, List<String> participants) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.participants = participants;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
