package com.b07project2024.group1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

public class AuthManager {

    private boolean isLoggedIn;
    private final MutableLiveData<Boolean> loginStatus;

    @Inject
    public AuthManager() {
        // Initialize the logged-in status
        this.isLoggedIn = false; // Or load the status from shared preferences or other storage
        this.loginStatus = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoginStatus(){
        loginStatus.setValue(isLoggedIn);
        return loginStatus;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void login() {
        isLoggedIn = true;
        // Save the status to shared preferences or other storage if needed
    }

    public void logout() {
        isLoggedIn = false;
        // Save the status to shared preferences or other storage if needed
    }
}
