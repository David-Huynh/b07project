package com.b07project2024.group1;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthFacade implements MVPContract.AuthManager {
    private boolean isLoggedIn;
    private final MutableLiveData<Boolean> loginStatus;
    private final FirebaseAuth mAuth;
    private static AuthFacade instance;

    private AuthFacade() {
        this.isLoggedIn = false;
        this.loginStatus = new MutableLiveData<>();
        this.mAuth = FirebaseAuth.getInstance();
    }

    public static AuthFacade getInstance() {
        if (instance == null) {
            instance = new AuthFacade();
        }
        return instance;
    }

    public void checkDB(User user, MVPContract.Presenter presenter) {
        mAuth.signInWithEmailAndPassword(user.getUser(), user.getPass())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        presenter.signIn(task);
                    }
                });
    }

    public LiveData<Boolean> getLoginStatus(){
        loginStatus.setValue(isLoggedIn);
        return loginStatus;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void login() {
        this.isLoggedIn = true;
        // Save the status to shared preferences or other storage if needed
    }

    public void logout() {
        this.isLoggedIn = false;
        // Save the status to shared preferences or other storage if needed
    }
}
