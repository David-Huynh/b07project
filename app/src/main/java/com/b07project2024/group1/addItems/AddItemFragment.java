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
import androidx.lifecycle.ViewModelProvider;

import com.b07project2024.group1.CatalogItem;
import com.b07project2024.group1.R;
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

    private PhotoFilesViewModel photoFilesViewModel;
    private VideoFilesViewModel videoFilesViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        photoFilesViewModel = new ViewModelProvider(requireActivity()).get(PhotoFilesViewModel.class);
        videoFilesViewModel = new ViewModelProvider(requireActivity()).get(VideoFilesViewModel.class);

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
        db = FirebaseDatabase.getInstance("https://b07projectgroup1-default-rtdb.firebaseio.com/");
        storage = FirebaseStorage.getInstance("gs://b07projectgroup1.appspot.com");
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
        buttonSubmit.setOnClickListener(v -> submitCatalogItem(new UploadToFireBase(
                uploadedURLs, selectedPhotoUris, selectedVideoUris,
                TAG, storage, db, getContext(), editTextLotNumber, editTextName,
                editTextDescription, spinnerCategory, spinnerPeriod)));

        buttonUploadImage.setOnClickListener(v -> loadFragment(new PhotosPickerFragment(), "PHOTOS_PICKER_FRAGMENT"));
        buttonUploadVideo.setOnClickListener(v -> loadFragment(new VideosPickerFragment(), "VIDEOS_PICKER_FRAGMENT"));
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void submitCatalogItem(UploadToFireBase uploadToFireBase) {
        if (!validateInput()) return;


        // Receive the data from photoViewModel
        photoFilesViewModel.getSelectedItem().observe(getViewLifecycleOwner(), mediaFiles -> {
            if (mediaFiles != null) {
                selectedPhotoUris.clear();
                selectedPhotoUris.addAll(mediaFiles);
                Log.d("AddItemFragment", "selectedPhotoUris size: "+selectedPhotoUris.size());
            }
            });

        // Receive the data from videoViewModel
        videoFilesViewModel.getSelectedItem().observe(getViewLifecycleOwner(), mediaFiles -> {
            if (mediaFiles != null) {
                selectedVideoUris.clear();
                selectedVideoUris.addAll(mediaFiles);
                Log.d("AddItemFragment", "selectedVideoUris size: "+selectedVideoUris.size());
            }
        });



        Log.d(TAG, "Submitting with " + selectedPhotoUris.size() + " images and " + selectedVideoUris.size() + " videos");

        uploadToFireBase.uploadMediaFiles();

    }

    private boolean validateInput() {
        if (editTextLotNumber.getText().toString().trim().isEmpty() ||
                editTextName.getText().toString().trim().isEmpty() ||
                editTextDescription.getText().toString().trim().isEmpty()||
                selectedPhotoUris.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields and upload at least 1 image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}