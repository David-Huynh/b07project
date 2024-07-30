package com.b07project2024.group1.addItems;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.b07project2024.group1.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PhotosPickerFragment extends Fragment {
    private static final String TAG = "PhotosPickerFragment";
    private static final int REQUEST_GALLERY = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int MAX_IMAGES = 9;
    private static final String PREF_NAME = "PhotoPickerPrefs";
    private static final String KEY_SELECTED_IMAGES = "SelectedImages";

    private GridLayout gridLayout;
    private Button selectButton;
    private Button uploadButton;
    private ArrayList<String> selectedImageUris = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos_picker, container, false);

        gridLayout = view.findViewById(R.id.gridLayout);
        selectButton = view.findViewById(R.id.selectButton);
        uploadButton = view.findViewById(R.id.uploadButton);

        selectButton.setOnClickListener(v -> checkPermissionAndOpenGallery());
        uploadButton.setOnClickListener(v -> uploadImages());

        loadSelectedImages();
        updateGridLayout();
        return view;
    }

    private void checkPermissionAndOpenGallery() {
        Log.d(TAG, "checkPermissionAndOpenGallery called");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
            } else {
                openCustomGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openCustomGallery();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCustomGallery();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCustomGallery() {
        try {
            Intent intent = new Intent(requireActivity(), CustomGalleryActivity.class);
            intent.putStringArrayListExtra("selectedImages", selectedImageUris);
            startActivityForResult(intent, REQUEST_GALLERY);
        } catch (Exception e) {
            Log.e(TAG, "Error opening gallery: ", e);
            Toast.makeText(requireContext(), "Error opening gallery: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUris = data.getStringArrayListExtra("selectedImages");
            saveSelectedImages();
            updateGridLayout();
        }
    }

    private void updateGridLayout() {
        gridLayout.removeAllViews();
        for (String uriString : selectedImageUris) {
            ImageView imageView = new ImageView(requireContext());
            imageView.setImageURI(Uri.parse(uriString));
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = getResources().getDimensionPixelSize(R.dimen.grid_image_size);
            params.height = getResources().getDimensionPixelSize(R.dimen.grid_image_size);
            params.setMargins(8, 8, 8, 8);
            gridLayout.addView(imageView, params);
        }
    }

    private void uploadImages() {
        // Implement your upload logic here
        Toast.makeText(requireContext(), "Uploading " + selectedImageUris.size() + " images", Toast.LENGTH_SHORT).show();
        // After upload is complete, return to previous fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void saveSelectedImages() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedImageUris);
        editor.putString(KEY_SELECTED_IMAGES, json);
        editor.apply();
    }

    private void loadSelectedImages() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_SELECTED_IMAGES, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> loadedUris = gson.fromJson(json, type);

        if (loadedUris != null) {
            selectedImageUris = loadedUris;
        } else {
            selectedImageUris = new ArrayList<>();
        }
    }
}