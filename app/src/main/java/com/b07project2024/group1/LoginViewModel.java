package com.b07project2024.group1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * TODO: THIS IS A MOCK IMPLEMENTATION NEEDED
 */
@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final LoginRepository repository;
    private final MutableLiveData<Boolean> loginStatus;

    @Inject
    public LoginViewModel(LoginRepository repository) {
        this.repository = repository;
        this.loginStatus = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLoginStatus(){
        loginStatus.setValue(repository.getLoginStatus());
        return loginStatus;
    }

    public void login(){
        loginStatus.setValue(true);
    }
    public void logout(){
        loginStatus.setValue(false);
    }
}
