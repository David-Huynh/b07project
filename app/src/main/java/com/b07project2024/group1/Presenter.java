//package com.b07project2024.group1;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//
//public class Presenter implements ViewMessages{
//    private LoginActivity view;
//    private Model model;
//
//    public Presenter(LoginActivity view, Model model) {
//        this.view = view;
//        this.model = model;
//    }
//
//    public void checkInput(User user) {
//        if (user.getUser().isEmpty() || user.getPass().isEmpty()) {
//            view.displayAlert(incomplete);
//        } else {
//            model.checkDB(user, this);
//        }
//    }
//
//    public void signIn(@NonNull Task<AuthResult> task) {
//        if (task.isSuccessful()) {
//            view.displayAlert(success);
//        } else {
//            view.displayAlert(failure);
//        }
//    }
//}
