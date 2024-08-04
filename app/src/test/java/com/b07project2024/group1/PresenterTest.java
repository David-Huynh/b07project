package com.b07project2024.group1;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Test
    public void testIncompleteLogin() {
        when(user.getUser()).thenReturn("");
        when(user.getPass()).thenReturn("");
        Presenter presenter = new Presenter(view, model);
        presenter.checkInput(user);
        verify(view).displayAlert("Please fill in all fields");
    }

    @Test
    public void testWrongLogin() {
        when(user.getUser()).thenReturn("your mom");
        when(user.getPass()).thenReturn("yourmom123");
        Presenter presenter = new Presenter(view, model);
        presenter.checkInput(user);
        verify(view).displayAlert("Login Failed");
    }

    @Test
    public void testCorrectLogin() {
        when(user.getUser()).thenReturn("b07group1@gmail.com");
        when(user.getPass()).thenReturn("helloworld");
        Presenter presenter = new Presenter(view, model);
        presenter.checkInput(user);
        verify(view).displayAlert("Login Successful");
    }

    @Test
    public void testCheckInput() {

    }
}
