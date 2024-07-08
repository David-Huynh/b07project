package com.b07project2024.group1;

import dagger.hilt.android.HiltAndroidApp;
import android.app.Application;

/**
 * Main Application for Hilt
 */
@HiltAndroidApp
public class B07Application extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseManager.getInstance();
    }
}
