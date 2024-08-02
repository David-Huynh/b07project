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
public class PresenterTest {

    @Mock
    LoginFragment view;

    @Mock
    AuthManager model;

    @Mock
    User user;

    @Mock
    Task<AuthResult> task;

    @Test
    public void testIncompletePass() {
        when(user.getUser()).thenReturn("hello");
        when(user.getPass()).thenReturn("");
        Presenter presenter = new Presenter(view, model);
        presenter.checkInput(user);
        verify(view).displayAlert("Please fill in all fields");
    }

    @Test
    public void testCompleteLogin() {
        when(user.getUser()).thenReturn("hello");
        when(user.getPass()).thenReturn("world");
        Presenter presenter = new Presenter(view, model);
        presenter.checkInput(user);
        verify(model).checkDB(user, presenter);
    }

    @Test
    public void testCorrectLogin() {
        when(task.isSuccessful()).thenReturn(true);
        Presenter presenter = new Presenter(view, model);
        presenter.signIn(task);
        InOrder inOrder = inOrder(model, view);
        inOrder.verify(model).login();
        inOrder.verify(model).getLoginStatus();
        inOrder.verify(view).displayAlert("Login Successful");
        inOrder.verify(view).closeFragmentOnLogin();
    }

    @Test
    public void testIncorrectLogin() {
        when(task.isSuccessful()).thenReturn(false);
        Presenter presenter = new Presenter(view, model);
        presenter.signIn(task);
        verify(view).displayAlert("Login Failed");
    }
}
