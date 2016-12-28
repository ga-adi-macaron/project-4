package com.example.jon.eventmeets.event_detail_components;

/**
 * Created by Jon on 12/28/2016.
 */

public class GiantBombRootObject {
    private String[] results;
    private int number_of_total_results;

    public String[] getResults() {
        if(number_of_total_results < 1) {
            return new String[]{"No results found"};
        }
        return results;
    }

    public void setResults(String[] results) {
        this.results = results;
    }

    public int getNumber_of_total_results() {
        return number_of_total_results;
    }

    public void setNumber_of_total_results(int number_of_total_results) {
        this.number_of_total_results = number_of_total_results;
    }
}
