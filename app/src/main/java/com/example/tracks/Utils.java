package com.example.tracks;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Utils {

    private static Utils instance;
    private FirebaseServices fbs;

    public Utils() {
        fbs = FirebaseServices.getInstance();
    }

    public static Utils getInstance() {
        if (instance == null)
            instance = new Utils();

        return instance;
    }

    public void showMessageDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void uploadImage(Context context, Uri selectedImageUri) {

        if (selectedImageUri == null) {
            Toast.makeText(context, "Please choose an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = "images/" + UUID.randomUUID().toString() + ".jpg";

        StorageReference imageRef =
                fbs.getStorage().getReference().child(fileName);

        UploadTask uploadTask = imageRef.putFile(selectedImageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {

            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                fbs.setSelectedImageURL(uri);

                Toast.makeText(context,
                        "Image uploaded successfully",
                        Toast.LENGTH_SHORT).show();

                Log.d("UPLOAD", "Image URL: " + uri.toString());

            }).addOnFailureListener(e ->
                    Log.e("UPLOAD_URL_ERROR", e.getMessage())
            );

        }).addOnFailureListener(e -> {
            Toast.makeText(context,
                    "Failed to upload image",
                    Toast.LENGTH_SHORT).show();

            Log.e("UPLOAD_ERROR", e.getMessage());
        });
    }
}