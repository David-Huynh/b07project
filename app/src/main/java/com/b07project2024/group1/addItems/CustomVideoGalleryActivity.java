package com.b07project2024.group1.addItems;

import android.app.Activity;
import android.content.ContentUris;
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
    private ArrayList<String> selectedVideoUris;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_video_gallery);

        Log.d(TAG, "onCreate called");

        recyclerView = findViewById(R.id.recyclerView);
        doneButton = findViewById(R.id.video_done_Button);

        selectedVideoUris = getIntent().getStringArrayListExtra("selectedVideos");
        if (selectedVideoUris == null) {
            selectedVideoUris = new ArrayList<>();
        }

        loadVideosFromGallery();

        adapter = new VideoGalleryAdapter(allVideos, selectedVideoUris, this::toggleVideoSelection);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        doneButton.setOnClickListener(v -> finishSelection());

        updateDoneButtonState();
    }

    private void loadVideosFromGallery() {
        Log.d(TAG, "loadVideosFromGallery called");
        allVideos = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.Media._ID};
        String sortOrder = MediaStore.Video.Media.DATE_TAKEN + " DESC";

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder)) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                    allVideos.add(contentUri.toString());
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
        if (selectedVideoUris.contains(videoPath)) {
            selectedVideoUris.remove(videoPath);
        } else if (selectedVideoUris.size() < MAX_VIDEOS) {
            selectedVideoUris.add(videoPath);
        } else {
            Toast.makeText(this, "Maximum " + MAX_VIDEOS + " videos allowed", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        updateDoneButtonState();
    }

    private void updateDoneButtonState() {
        doneButton.setEnabled(!selectedVideoUris.isEmpty());
    }

    private void finishSelection() {
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedVideos", selectedVideoUris);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}