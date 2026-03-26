package com.example.tracks.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tracks.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordFragment extends Fragment {

    private EditText etEmailForgot;
    private Button btnResetForgot , btngohome;
    private FirebaseAuth auth;

    public ForgotPasswordFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmailForgot = view.findViewById(R.id.etEmailForfgotpassword);
        btnResetForgot = view.findViewById(R.id.btnResetForgotPassword);
        btngohome = view.findViewById(R.id.btngohome);

        auth = FirebaseAuth.getInstance();

        btnResetForgot.setOnClickListener(v -> {
            String email = etEmailForgot.getText().toString().trim();
            if(email.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Reset link sent to your email!", Toast.LENGTH_LONG).show();
                    etEmailForgot.setText("");
                } else {
                    Toast.makeText(getActivity(), "Failed to send reset email! Check your email address.", Toast.LENGTH_SHORT).show();
                }
            });
        });
        btngohome.setOnClickListener(v -> {gotoLoginFragment();});

    }
    private void gotoLoginFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new LoginFragment());
        ft.commit();
    }

}
