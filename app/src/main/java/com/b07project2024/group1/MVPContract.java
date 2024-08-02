package com.b07project2024.group1;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface MVPContract {
    public interface AuthManager {
        public void checkDB(User user, Presenter presenter);
        public LiveData<Boolean> getLoginStatus();
        public void login();
        public void logout();
    }

    public interface Presenter {
        public void checkInput(User user);
        public void signIn(Task<AuthResult> task);
    }

    public interface LoginFragment {
        public void displayAlert(String message);
        public void closeFragmentOnLogin();
    }
}
