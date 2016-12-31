package com.example.jon.eventmeets.event_detail_components;

import com.example.jon.eventmeets.model.VideoGamingEvent;

import java.util.ArrayList;

/**
 * Created by Jon on 12/28/2016.
 */

public class GiantBombRootObject {
    private String error;
    private int limit;
    private int offset;
    private int number_of_page_results;
    private int number_of_total_results;
    private int status_code;
    private ArrayList<VideoGamingEvent> results;
    private String version;

    public GiantBombRootObject() {}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber_of_page_results() {
        return number_of_page_results;
    }

    public void setNumber_of_page_results(int number_of_page_results) {
        this.number_of_page_results = number_of_page_results;
    }

    public int getNumber_of_total_results() {
        return number_of_total_results;
    }

    public void setNumber_of_total_results(int number_of_total_results) {
        this.number_of_total_results = number_of_total_results;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public ArrayList<VideoGamingEvent> getResults() {
        return results;
    }

    public void setResults(ArrayList<VideoGamingEvent> results) {
        this.results = results;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
