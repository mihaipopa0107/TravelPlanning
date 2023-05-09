package com.mihai.models;

import com.orm.SugarRecord;

public class PostRating extends SugarRecord<PostRating> {
    private String Body;
    private int Rating;
    private Long AccountId;
    private Long LocationId;

    public PostRating() { }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBody(String body) {
        Body = body;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public void setAccountId(Long accountId) {
        AccountId = accountId;
    }

    public void setLocationId(Long locationId) {
        LocationId = locationId;
    }

    public int getRating() {
        return Rating;
    }

    public String getBody() {
        return Body;
    }

    public Long getAccountId() {
        return AccountId;
    }

    public Long getLocationId() {
        return LocationId;
    }

    public Long getId() {
        return id;
    }
}
