package com.b07project2024.group1;

import static android.app.PendingIntent.getActivity;

import android.widget.Toast;

public class LogOut {
    public static void logOut(AuthManager authManager) {
        authManager.logout();
        authManager.getLoginStatus();
    }
}
