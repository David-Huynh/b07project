package com.b07project2024.group1;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface MVPContract {
    interface AuthManager {
        void checkDB(User user, Presenter presenter);
        LiveData<Boolean> getLoginStatus();
        void login();
        void logout();
    }

    interface Presenter {
        void checkInput(User user);
        void signIn(Task<AuthResult> task);
    }

    interface LoginFragment {
        void displayAlert(String message);
        void closeFragmentOnLogin();
    }
}
