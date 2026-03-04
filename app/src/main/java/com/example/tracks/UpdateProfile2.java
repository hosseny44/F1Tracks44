package com.example.tracks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UpdateProfile2 extends Fragment {

    private EditText etFirstName, etLastName, etPhone, etUsername, etEmail;
    private ImageView ivUser;
    private Button btnUpdate;
    private FirebaseServices fbs;
    private Utils utils;
    private static final int GALLERY_REQUEST_CODE = 134;

    public UpdateProfile2() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_profile2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();
        utils = Utils.getInstance();

        etFirstName = getView().findViewById(R.id.etFirstnameUserDetailsEdit);
        etLastName = getView().findViewById(R.id.etLastnameUserDetailsEdit);
        etPhone = getView().findViewById(R.id.etPhoneUserDetailsEdit);
        etUsername = getView().findViewById(R.id.etUsername);
        etEmail = getView().findViewById(R.id.etEmail);
        ivUser = getView().findViewById(R.id.ivUserUserDetailsEdit);
        btnUpdate = getView().findViewById(R.id.btnUpdateUserDetails);

        fillUserData();

        btnUpdate.setOnClickListener(v -> updateUserData());

        ivUser.setOnClickListener(v -> openGallery());
    }

    private void fillUserData() {
        User current = fbs.getCurrentUser();
        if (current != null) {
            etFirstName.setText(current.getFirstName());
            etLastName.setText(current.getLastName());
            etPhone.setText(current.getPhone());
            etEmail.setText(current.getAddress());
            etUsername.setText(current.getUsername());

            if (current.getPhoto() != null && !current.getPhoto().isEmpty()) {
                Glide.with(this).load(current.getPhoto()).into(ivUser);
                fbs.setSelectedImageURL(Uri.parse(current.getPhoto()));
            }
        }
    }

    private void updateUserData() {
        String firstname = etFirstName.getText().toString().trim();
        String lastname = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String selectedImage = fbs.getSelectedImageURL() != null ? fbs.getSelectedImageURL().toString() : "";

        if (firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        User current = fbs.getCurrentUser();
        if (current != null) {
            boolean isChanged = !current.getFirstName().equals(firstname) ||
                    !current.getLastName().equals(lastname) ||
                    !current.getUsername().equals(username) ||
                    !current.getPhone().equals(phone) ||
                    !current.getAddress().equals(email) ||
                    !current.getPhoto().equals(selectedImage);

            if (!isChanged) {
                utils.showMessageDialog(getActivity(), "No changes!");
                return;
            }

            User updatedUser = new User(firstname, lastname, phone, selectedImage, username ,email);

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            fbs.getFire().collection("users").document(uid)
                    .set(updatedUser)
                    .addOnSuccessListener(aVoid -> {
                        fbs.setCurrentUser(updatedUser);
                        utils.showMessageDialog(getActivity(), "Data updated successfully!");
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout, new ProfileFragment())
                                .commit();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(),
                            "Error updating user: " + e.getMessage(),
                            Toast.LENGTH_LONG).show());
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                btnUpdate.setEnabled(false);
                Glide.with(this).load(imageUri).into(ivUser);
                uploadImage(imageUri);
            }
        }
    }

    private void uploadImage(Uri selectedImageUri) {
        if (selectedImageUri == null) return;

        StorageReference imageRef = fbs.getStorage().getReference()
                .child("images/" + UUID.randomUUID() + ".jpg");

        imageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> fbs.setSelectedImageURL(uri))
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to get image URL", Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show())
                .addOnCompleteListener(task -> btnUpdate.setEnabled(true));
    }
    private void setNavigationBarVisible() {
        ((MainActivity)getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

}