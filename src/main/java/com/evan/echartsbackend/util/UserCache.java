package com.evan.echartsbackend.util;

public class UserCache {

    public static String username;

    public static void setUsername(String aUsername) {
        username = aUsername;
    }

    public static String getUsername() {
        return username;
    }
}
