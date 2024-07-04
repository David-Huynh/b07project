package com.b07project2024.group1;


public class LogOut {
    public static void logOut(AuthFacade authFacade) {
        authFacade.logout();
        authFacade.getLoginStatus();
    }
}
