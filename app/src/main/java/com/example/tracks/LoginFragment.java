package com.example.tracks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;
    private TextView tvSignupLink, tvForgotpassword;
    private Button btnLogin;
    private FirebaseServices fbs;

    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUsername = view.findViewById(R.id.etUsernameLogin);
        etPassword = view.findViewById(R.id.etPasswordLogin);
        btnLogin = view.findViewById(R.id.btnLoginLogin);
        tvSignupLink = view.findViewById(R.id.tvSignupLogin);
        tvForgotpassword = view.findViewById(R.id.tvForgotPasswordLogin);

        fbs = FirebaseServices.getInstance();

        tvSignupLink.setOnClickListener(v -> gotoSignupFragment());
        tvForgotpassword.setOnClickListener(v -> gotoForgotPasswordFragment());

        btnLogin.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireActivity(),
                        "Some fields are empty!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            fbs.getAuth().signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(requireActivity(), task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(requireActivity(),
                                    "You have successfully logged in!",
                                    Toast.LENGTH_SHORT).show();

                            MainActivity activity = (MainActivity) requireActivity();
                            activity.getBottomNavigationView().setVisibility(View.VISIBLE);
                            activity.pushFragment(new TrackListFragment());

                        } else {

                            Toast.makeText(requireActivity(),
                                    "Failed to login! Check user or password!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void gotoSignupFragment() {
        FragmentTransaction ft = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.frameLayout, new SignUp());
        ft.commit();
    }

    private void gotoForgotPasswordFragment() {
        FragmentTransaction ft = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.frameLayout, new ForgotPasswordFragment());
        ft.commit();
    }
}