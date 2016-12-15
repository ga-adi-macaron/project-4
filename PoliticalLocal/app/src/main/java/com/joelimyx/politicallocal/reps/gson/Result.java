
package com.joelimyx.politicallocal.reps.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("party")
    @Expose
    private String party;
    @SerializedName("times_topics_url")
    @Expose
    private String timesTopicsUrl;
    @SerializedName("twitter_id")
    @Expose
    private String twitterId;
    @SerializedName("seniority")
    @Expose
    private String seniority;
    @SerializedName("next_election")
    @Expose
    private String nextElection;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getTimesTopicsUrl() {
        return timesTopicsUrl;
    }

    public void setTimesTopicsUrl(String timesTopicsUrl) {
        this.timesTopicsUrl = timesTopicsUrl;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getSeniority() {
        return seniority;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public String getNextElection() {
        return nextElection;
    }

    public void setNextElection(String nextElection) {
        this.nextElection = nextElection;
    }

}
