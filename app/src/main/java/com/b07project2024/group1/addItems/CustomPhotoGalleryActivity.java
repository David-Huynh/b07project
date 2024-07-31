package com.b07project2024.group1.addItems;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.b07project2024.group1.R;

import java.util.ArrayList;
import java.util.List;

public class CustomPhotoGalleryActivity extends AppCompatActivity {
    private static final String TAG = "CustomGalleryActivity";
    private static final int MAX_IMAGES = 9;

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private List<String> allImages;
    private ArrayList<String> selectedImageUris;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_photo_gallery);

        Log.d(TAG, "onCreate called");

        recyclerView = findViewById(R.id.recyclerView);
        doneButton = findViewById(R.id.photo_done_Button);

        selectedImageUris = getIntent().getStringArrayListExtra("selectedImages");
        if (selectedImageUris == null) {
            selectedImageUris = new ArrayList<>();
        }

        loadImagesFromGallery();

        adapter = new GalleryAdapter(allImages, selectedImageUris, this::toggleImageSelection);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        doneButton.setOnClickListener(v -> finishSelection());

        updateDoneButtonState();
    }

    private void loadImagesFromGallery() {
        Log.d(TAG, "loadImagesFromGallery called");
        allImages = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder)) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                while (cursor.moveToNext()) {
                    String imagePath = cursor.getString(dataColumn);
                    allImages.add(imagePath);
                }
                Log.d(TAG, "Loaded " + allImages.size() + " images");
            } else {
                Log.e(TAG, "Cursor is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading images: ", e);
        }

        if (allImages.isEmpty()) {
            Log.w(TAG, "No images found");
            Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleImageSelection(String imagePath) {
        if (selectedImageUris.contains(imagePath)) {
            selectedImageUris.remove(imagePath);
        } else if (selectedImageUris.size() < MAX_IMAGES) {
            selectedImageUris.add(imagePath);
        } else {
            Toast.makeText(this, "Maximum " + MAX_IMAGES + " images allowed", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        updateDoneButtonState();
    }

    private void updateDoneButtonState() {
        doneButton.setEnabled(!selectedImageUris.isEmpty());
    }

    private void finishSelection() {
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedImages", selectedImageUris);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}