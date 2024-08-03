package com.b07project2024.group1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import javax.inject.Inject;

public class LoginFragment extends Fragment {
    private Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this, AuthManager.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button login = requireView().findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton(v);
            }
        });
    }

    public void displayAlert(String alert) {
        Log.d("Login Result: ", alert);
        Toast.makeText(getActivity(), alert, Toast.LENGTH_LONG).show();
    }

    public void loginButton(View v) {
        TextView username = requireView().findViewById(R.id.inputusername);
        TextView password = requireView().findViewById(R.id.inputpassword);
        String inputUser = username.getText().toString();
        String inputPass = password.getText().toString();

        User user = new User(inputUser, inputPass);
        presenter.checkInput(user);
    }

    public void closeFragmentOnLogin() {
        assert getActivity() != null;
        getActivity().getSupportFragmentManager().popBackStack();
    }
}