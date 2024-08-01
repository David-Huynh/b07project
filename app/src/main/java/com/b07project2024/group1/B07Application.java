package com.b07project2024.group1;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Main Application for Hilt
 */
@HiltAndroidApp
public class B07Application extends Application{
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void onCreate() {
        FirebaseApp.initializeApp(this);
        super.onCreate();
        FirebaseManager.getInstance();
    }
}
