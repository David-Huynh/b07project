package com.b07project2024.group1;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {

    @Mock
    LoginFragment view;

    @Mock
    AuthFacade model;

    @Mock
    User user;

    @Mock
    Task<AuthResult> task;

    @Test
    public void testIncompletePass() {
        when(user.getUser()).thenReturn("hello");
        when(user.getPass()).thenReturn("");
        LoginPresenter loginPresenter = new LoginPresenter(view, model);
        loginPresenter.checkInput(user);
        verify(view).displayAlert("Please fill in all fields");
    }

    @Test
    public void testCompleteLogin() {
        when(user.getUser()).thenReturn("hello");
        when(user.getPass()).thenReturn("world");
        LoginPresenter loginPresenter = new LoginPresenter(view, model);
        loginPresenter.checkInput(user);
        verify(model).checkDB(user, loginPresenter);
    }

    @Test
    public void testCorrectLogin() {
        when(task.isSuccessful()).thenReturn(true);
        LoginPresenter loginPresenter = new LoginPresenter(view, model);
        loginPresenter.signIn(task);
        InOrder inOrder = inOrder(model, view);
        inOrder.verify(model).login();
        inOrder.verify(model).getLoginStatus();
        inOrder.verify(view).displayAlert("Login Successful");
        inOrder.verify(view).closeFragmentOnLogin();
    }

    @Test
    public void testIncorrectLogin() {
        when(task.isSuccessful()).thenReturn(false);
        LoginPresenter loginPresenter = new LoginPresenter(view, model);
        loginPresenter.signIn(task);
        verify(view).displayAlert("Login Failed");
    }
}
