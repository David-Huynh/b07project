package com.b07project2024.group1.addItems;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UploadToFireBase {

    private List<String> uploadedURLs;
    private ArrayList<String> selectedPhotoUris;
    private ArrayList<String> selectedVideoUris;
    private String TAG;
    private FirebaseStorage storage;
    private FirebaseDatabase db;
    private Context context;
    private EditText editTextLotNumber, editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerPeriod;


    public  UploadToFireBase(List<String> uploadedURLS, ArrayList<String> selectedPhotoUris,
                             ArrayList<String> selectedVideoUris, String TAG, FirebaseStorage storage,
                             FirebaseDatabase db, Context context, EditText editTextLotNumber,
                             EditText editTextName, EditText editTextDescription, Spinner spinnerCategory,
                             Spinner spinnerPeriod ){

        this.uploadedURLs = uploadedURLS;
        this.selectedPhotoUris = selectedPhotoUris;
        this.selectedVideoUris = selectedVideoUris;
        this.TAG = TAG;
        this.storage = storage;
        this.db = db;
        this.context = context;
        this.editTextLotNumber = editTextLotNumber;
        this.editTextName = editTextName;
        this.editTextDescription = editTextDescription;
        this.spinnerCategory = spinnerCategory;
        this.spinnerPeriod = spinnerPeriod;
    }

    public void uploadMediaFiles() {
        uploadedURLs.clear();
        Log.d("AddItemFragment", "selectedPhotoUris size3: "+selectedPhotoUris.size());
        int totalFiles = selectedPhotoUris.size() + selectedVideoUris.size();
        Log.d(TAG, "Total files to upload: " + totalFiles);
        AtomicInteger uploadedCount = new AtomicInteger(0);

        if (totalFiles == 0) {
            addItemToDatabase(new ArrayList<>(), new ArrayList<>());
            return;
        }

        for (String uriString : selectedPhotoUris) {
            Log.d("AddItemFragment", "URI String: " + Uri.parse(uriString));
            uploadFile(Uri.parse(uriString), "images/", uploadedCount, totalFiles);
        }
        for (String uriString : selectedVideoUris) {
            uploadFile(Uri.parse(uriString), "videos/", uploadedCount, totalFiles);
        }
    }

    private void uploadFile(Uri fileUri, String folderName, AtomicInteger uploadedCount, int totalFiles) {
        StorageReference fileRef = storage.getReference().child(folderName + UUID.randomUUID().toString());

        try {
            InputStream stream = context.getContentResolver().openInputStream(fileUri);
            if (stream != null) {
                UploadTask uploadTask = fileRef.putStream(stream);

                uploadTask.addOnFailureListener(exception -> {
                    Log.e(TAG, "Upload failed: " + exception.getMessage());
                    Toast.makeText(context, "Upload failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                }).addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadedURLs.add(uri.toString());
                        if (uploadedCount.incrementAndGet() == totalFiles) {
                            List<String> imageUrls = new ArrayList<>(uploadedURLs.subList(0, selectedPhotoUris.size()));
                            List<String> videoUrls = new ArrayList<>(uploadedURLs.subList(selectedPhotoUris.size(), uploadedURLs.size()));
                            addItemToDatabase(imageUrls, videoUrls);
                        }
                    });
                });
            } else {
                Log.e(TAG, "Failed to open input stream for file: " + fileUri);
                Toast.makeText(context, "Failed to open file", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
            Toast.makeText(context, "File not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addItemToDatabase(List<String> imageUrls, List<String> videoUrls) {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String period = spinnerPeriod.getSelectedItem().toString();
        String description = editTextDescription.getText().toString().trim();

        DatabaseReference catalogItemsRef = db.getReference("catalog");

        // Create a HashMap to represent the item
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("category", category);
        item.put("description", description);
        item.put("period", period);
        item.put("imageUrls", imageUrls);
        item.put("videoUrls", videoUrls);

        // Use the lot number as the key for this catalog item
        catalogItemsRef.child(lotNumber).setValue(item)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Item added successfully.");
                    Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show();
                    clearForm();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to add item: " + e.getMessage());
                    Toast.makeText(context, "Failed to add item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

        clearSharedPreferences("PhotoPickerPrefs");
        clearSharedPreferences("VideoPickerPrefs");
    }

    // Clear the SharedPreference file in the internal storage:
    private void clearSharedPreferences(String prefName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void clearForm() {
        editTextLotNumber.setText("");
        editTextName.setText("");
        editTextDescription.setText("");
        spinnerCategory.setSelection(0);
        spinnerPeriod.setSelection(0);

        selectedPhotoUris.clear();
        selectedVideoUris.clear();
        uploadedURLs.clear();
    }
}
