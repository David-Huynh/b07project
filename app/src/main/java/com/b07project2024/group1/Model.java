package com.b07project2024.group1;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Model extends AuthManager{
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void checkDB(User user, Presenter presenter) {
        mAuth.signInWithEmailAndPassword(user.getUser(), user.getPass())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        presenter.signIn(task);
                    }
                });
    }
}
