package com.example.tracks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SignUp extends Fragment {

    private EditText etFirstName, etLastName, etPhone, etPassword, etEmail;
    private Button btnSignup;
    private ImageView ivUserPhoto;

    private FirebaseServices fbs;
    private static final int GALLERY_REQUEST_CODE = 123;

    public SignUp() {}

    @Override
    public View onCreateView(android.view.LayoutInflater inflater, android.view.ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();

        etFirstName = getView().findViewById(R.id.etFirstnameSignup);
        etLastName = getView().findViewById(R.id.etLastnameSignup);
        etPhone = getView().findViewById(R.id.etPhoneSignup);
        etEmail = getView().findViewById(R.id.etEmailSignup);
        etPassword = getView().findViewById(R.id.etPasswordSignup);
        btnSignup = getView().findViewById(R.id.btnSignupSignup);
        ivUserPhoto = getView().findViewById(R.id.ivPhotoSignupFragment);

        // اختيار الصورة
        ivUserPhoto.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        });

        btnSignup.setOnClickListener(v -> signupUser());

        TextView tvLogin = getView().findViewById(R.id.tvLoginSignup);
        tvLogin.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new LoginFragment());
            ft.addToBackStack(null);
            ft.commit();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            ivUserPhoto.setImageURI(selectedImageUri);
            fbs.setSelectedImageURL(selectedImageUri);
        }
    }

    private void signupUser() {

        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()
                || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email address");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        // إنشاء المستخدم في Firebase Auth
        fbs.getAuth()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = fbs.getAuth().getCurrentUser();
                        if (firebaseUser == null) return;

                        String uid = firebaseUser.getUid();

                        // رفع الصورة أولًا إذا اختارها
                        Uri imageUri = fbs.getSelectedImageURL();
                        if (imageUri != null) {

                            StorageReference storageRef = FirebaseStorage.getInstance()
                                    .getReference("users_photos/" + uid + ".jpg");

                            storageRef.putFile(imageUri)
                                    .addOnSuccessListener(taskSnapshot ->
                                            storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                                saveUserToFirestore(uid, firstName, lastName, phone, email, uri.toString());
                                            })
                                    )
                                    .addOnFailureListener(e -> Toast.makeText(getActivity(),
                                            "Image upload failed: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show());

                        } else {
                            // إذا ما في صورة، نخزن user بدون رابط الصورة
                            saveUserToFirestore(uid, firstName, lastName, phone, email, "");
                        }

                    } else {
                        Toast.makeText(getActivity(),
                                "Signup failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToFirestore(String uid, String firstName, String lastName, String phone, String email, String photoUrl) {

        // إنشاء user + تهيئة favorites كـ empty list
        User user = new User(firstName, lastName, phone, photoUrl , "", email);
        user.setFavorites(new ArrayList<>());

        fbs.getFire().collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    fbs.setCurrentUser(user);
                    Toast.makeText(getActivity(), "Signup successful!", Toast.LENGTH_SHORT).show();

                    // تحديث Navigation bar حسب Admin
                    setNavigationBarVisible();

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout, new TrackListFragment())
                            .commit();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(),
                        "Error saving user: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void setNavigationBarVisible() {
        BottomNavigationView bottomNav = ((MainActivity)getActivity()).getBottomNavigationView();
        bottomNav.setVisibility(View.VISIBLE);
        Menu menu = bottomNav.getMenu();
        FirebaseUser currentUser = fbs.getAuth().getCurrentUser();

    }
}