package com.mihai.core;

public class CommentViewModel {
    private Long id;
    private String Body;
    private int Rating;
    private Long AccountId;
    private String Username;
    private String Avatar;

    public CommentViewModel() { }

    public CommentViewModel(String Body, int Rating, Long AccountId) {
        this.Body = Body;
        this.AccountId = AccountId;
        this.Rating = Rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
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

    public Long getAccountId() {
        return AccountId;
    }

    public String getBody() {
        return Body;
    }

    public int getRating() {
        return Rating;
    }
}
