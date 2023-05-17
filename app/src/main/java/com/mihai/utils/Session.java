package com.mihai.utils;

import com.mihai.models.Account;

public class Session {
    static boolean isAuthorized;
    static Long accountId;
    static String username;
    static String avatar;
    static boolean isAdmin;
    static Long TravelLocationId;

    public static void Authorize(Account account) {
        isAuthorized = true;
        TravelLocationId = -1L;
        accountId = account.getId();
        username = account.getUsername();
        avatar = account.getAvatar();
        isAdmin = account.getAdmin();
    }

    public static void Logout() {
        if(IsAuthenticated()) {
            isAuthorized = false;
            TravelLocationId = -1L;
            accountId = 0L;
            username = "";
            avatar = "";
            isAdmin = false;
        }
    }

    public static Long getTravelLocationId() {
        return TravelLocationId;
    }
    public static void setTravelLocationId(Long locationId) {
        TravelLocationId = locationId;
    }
    public static boolean IsAuthenticated() {
        return isAuthorized;
    }
    public static String getUsername() {
        return username;
    }
    public static String getAvatar() {
        return avatar;
    }

    public static Long GetAccountId() {
        return accountId;
    }

    public static boolean IsAdmin() {
        return isAdmin;
    }
}
