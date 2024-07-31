package com.b07project2024.group1.addItems;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.b07project2024.group1.CatalogItem;
import com.b07project2024.group1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AddItemFragment extends Fragment {

    private static final String TAG = "AddItemFragment";

    private EditText editTextLotNumber, editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerPeriod;
    private Button buttonSubmit, buttonUploadImage, buttonUploadVideo;

    private FirebaseDatabase db;
    private FirebaseStorage storage;

    private ArrayList<String> selectedPhotoUris = new ArrayList<>();
    private ArrayList<String> selectedVideoUris = new ArrayList<>();
    private List<String> uploadedURLs = new ArrayList<>();

    @Override
    public void onDestroy(){
        super.onDestroy();
        clearSharedPreferences("PhotoPickerPrefs");
        clearSharedPreferences("VideoPickerPrefs");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Set up fragment result listeners
        getParentFragmentManager().setFragmentResultListener("photoPickerResult", this, (requestKey, result) -> {
            selectedPhotoUris = result.getStringArrayList("selectedImages");
            Log.d(TAG, "Received " + selectedPhotoUris.size() + " images from PhotoPicker");
        });

        getParentFragmentManager().setFragmentResultListener("videoPickerResult", this, (requestKey, result) -> {
            selectedVideoUris = result.getStringArrayList("selectedVideos");
            Log.d(TAG, "Received " + selectedVideoUris.size() + " videos from VideoPicker");
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        initializeViews(view);
        setupFirebase();
        setupSpinners();
        setupButtons();

        return view;
    }

    private void initializeViews(View view) {
        editTextLotNumber = view.findViewById(R.id.editTextLotNumber);
        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerPeriod = view.findViewById(R.id.spinnerPeriod);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        buttonUploadImage = view.findViewById(R.id.buttonUploadImage);
        buttonUploadVideo = view.findViewById(R.id.buttonUploadVideo);
    }

    private void setupFirebase() {
        db = FirebaseDatabase.getInstance("https://scrum-7-default-rtdb.firebaseio.com/");
        storage = FirebaseStorage.getInstance("gs://scrum-7.appspot.com");
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(periodAdapter);
    }

    private void setupButtons() {
        buttonSubmit.setOnClickListener(v -> submitCatalogItem());
        buttonUploadImage.setOnClickListener(v -> loadFragment(new PhotosPickerFragment(), "PHOTOS_PICKER_FRAGMENT"));
        buttonUploadVideo.setOnClickListener(v -> loadFragment(new VideosPickerFragment(), "VIDEOS_PICKER_FRAGMENT"));
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void submitCatalogItem() {
        if (!validateInput()) return;

        Log.d(TAG, "Submitting with " + selectedPhotoUris.size() + " images and " + selectedVideoUris.size() + " videos");

        uploadMediaFiles();
    }

    private boolean validateInput() {
        if (editTextLotNumber.getText().toString().trim().isEmpty() ||
                editTextName.getText().toString().trim().isEmpty() ||
                editTextDescription.getText().toString().trim().isEmpty() ||
                spinnerCategory.getSelectedItemPosition() == 0 ||
                spinnerPeriod.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadMediaFiles() {
        uploadedURLs.clear();
        int totalFiles = selectedPhotoUris.size() + selectedVideoUris.size();
        Log.d(TAG, "Total files to upload: " + totalFiles);
        AtomicInteger uploadedCount = new AtomicInteger(0);

        if (totalFiles == 0) {
            addItemToDatabase(new ArrayList<>(), new ArrayList<>());
            return;
        }

        for (String uriString : selectedPhotoUris) {
            uploadFile(Uri.parse(uriString), "images/", uploadedCount, totalFiles);
        }
        for (String uriString : selectedVideoUris) {
            uploadFile(Uri.parse(uriString), "videos/", uploadedCount, totalFiles);
        }
    }

    private void uploadFile(Uri fileUri, String folderName, AtomicInteger uploadedCount, int totalFiles) {
        StorageReference fileRef = storage.getReference().child(folderName + UUID.randomUUID().toString());
        UploadTask uploadTask = fileRef.putFile(fileUri);

        uploadTask.addOnFailureListener(exception -> {
            Log.e(TAG, "Upload failed: " + exception.getMessage());
            Toast.makeText(getContext(), "Upload failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        }).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                uploadedURLs.add(uri.toString());
                if (uploadedCount.incrementAndGet() == totalFiles) {
                    List<String> imageUrls = uploadedURLs.subList(0, selectedPhotoUris.size());
                    List<String> videoUrls = uploadedURLs.subList(selectedVideoUris.size(), uploadedURLs.size());
                    addItemToDatabase(imageUrls, videoUrls);
                }
            });
        });
    }

    private void addItemToDatabase(List<String> imageUrls, List<String> videoUrls) {
        String lotNumber = editTextLotNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String period = spinnerPeriod.getSelectedItem().toString();
        String description = editTextDescription.getText().toString().trim();

        DatabaseReference itemsRef = db.getReference("catalog_items");

        CatalogItem item = new CatalogItem(lotNumber, name, category, description, period, imageUrls, videoUrls);

        itemsRef.child(lotNumber).setValue(item)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Item added successfully.");
                    Toast.makeText(getContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                    clearForm();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to add item: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to add item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        clearSharedPreferences("PhotoPickerPrefs");
        clearSharedPreferences("VideoPickerPrefs");
    }
    // Clear the SharedPreference file in the internal storage:
    private void clearSharedPreferences(String prefName) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(prefName, Context.MODE_PRIVATE);
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
        selectedPhotoUris.clear();
        uploadedURLs.clear();
    }
}