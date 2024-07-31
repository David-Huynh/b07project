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

public class CustomVideoGalleryActivity extends AppCompatActivity {
    private static final String TAG = "CustomVideoGalleryActivity";
    private static final int MAX_VIDEOS = 9;

    private RecyclerView recyclerView;
    private VideoGalleryAdapter adapter;
    private List<String> allVideos;
    private ArrayList<String> selectedPhotoUris;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_video_gallery);

        Log.d(TAG, "onCreate called");

        recyclerView = findViewById(R.id.recyclerView);
        doneButton = findViewById(R.id.video_done_Button);

        selectedPhotoUris = getIntent().getStringArrayListExtra("selectedVideos");
        if (selectedPhotoUris == null) {
            selectedPhotoUris = new ArrayList<>();
        }

        loadVideosFromGallery();

        adapter = new VideoGalleryAdapter(allVideos, selectedPhotoUris, this::toggleVideoSelection);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        doneButton.setOnClickListener(v -> finishSelection());

        updateDoneButtonState();
    }

    private void loadVideosFromGallery() {
        Log.d(TAG, "loadVideosFromGallery called");
        allVideos = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA};
        String sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC";

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder)) {
            if (cursor != null) {
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                while (cursor.moveToNext()) {
                    String videoPath = cursor.getString(dataColumn);
                    allVideos.add(videoPath);
                }
                Log.d(TAG, "Loaded " + allVideos.size() + " videos");
            } else {
                Log.e(TAG, "Cursor is null");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading videos: ", e);
        }

        if (allVideos.isEmpty()) {
            Log.w(TAG, "No videos found");
            Toast.makeText(this, "No videos found", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleVideoSelection(String videoPath) {
        if (selectedPhotoUris.contains(videoPath)) {
            selectedPhotoUris.remove(videoPath);
        } else if (selectedPhotoUris.size() < MAX_VIDEOS) {
            selectedPhotoUris.add(videoPath);
        } else {
            Toast.makeText(this, "Maximum " + MAX_VIDEOS + " videos allowed", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        updateDoneButtonState();
    }

    private void updateDoneButtonState() {
        doneButton.setEnabled(!selectedPhotoUris.isEmpty());
    }

    private void finishSelection() {
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedVideos", selectedPhotoUris);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}