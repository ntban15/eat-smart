package com.annguyen.android.eatsmart.login.events;

/**
 * Created by annguyen on 6/4/2017.
 */

public class LoginEvent {

    public static final String SUCCESS = "LOGIN_SUCCESS";
    public static final String FAIL = "LOGIN_FAIL";
    public static final String LOGGED_IN = "LOGGED_IN";
    public static final String NOT_LOGGED_IN = "NOT_LOGGED_IN";

    private String eventCode;

    public LoginEvent(String code) {
        eventCode = code;
    }

    public String getEventCode() {
        return eventCode;
    }
}
