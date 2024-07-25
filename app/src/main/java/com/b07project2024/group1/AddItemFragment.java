package com.b07project2024.group1;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddItemFragment extends Fragment {

    private static final String TAG = "UploadFragment";

    private static final int REQUEST_IMAGE_PICK = 101;
    private static final int REQUEST_VIDEO_PICK = 102;
    private static final int MAX_IMAGES = 9;
    private static final int MAX_VIDEOS = 2;

    private GridLayout gridLayoutSelectedFiles;
    private List<Uri> selectedFiles = new ArrayList<>();
    private List<String> uploadedURLs = new ArrayList<>();

    private EditText editTextLotNumber;
    private EditText editTextName;
    private EditText editTextDescription;
    private Spinner spinnerCategory;
    private Spinner spinnerPeriod;
    private Button buttonSubmit;
    private TextView textViewURL;

    private FirebaseDatabase db;
    private FirebaseStorage storage;

    private String currentItemID; // Store the ID of the current item being added

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);

        editTextLotNumber = view.findViewById(R.id.editTextLotNumber);
        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerPeriod = view.findViewById(R.id.spinnerPeriod);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);
        Button buttonUploadImage = view.findViewById(R.id.buttonUploadImage);
        Button buttonUploadVideo = view.findViewById(R.id.buttonUploadVideo);
        gridLayoutSelectedFiles = view.findViewById(R.id.gridLayoutSelectedFiles);

        db = FirebaseDatabase.getInstance("https://scrum-7-default-rtdb.firebaseio.com/");
        storage = FirebaseStorage.getInstance(); // Initialize Firebase Storage

        // Set up the spinner with categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set up the spinner with periods
        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.periods_array, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriod.setAdapter(periodAdapter);

        // Set up the button to submit the item
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        buttonUploadImage.setOnClickListener(v -> openImagePicker());
        buttonUploadVideo.setOnClickListener(v -> openVideoPicker());

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void openVideoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_VIDEO_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMAGE_PICK) {
                handleFileSelection(data, "image");
            } else if (requestCode == REQUEST_VIDEO_PICK) {
                handleFileSelection(data, "video");
            }
        }
    }

    private void handleFileSelection(Intent data, String fileType) {
        if (data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            if (("image".equals(fileType) && count + selectedFiles.size() > MAX_IMAGES) ||
                    ("video".equals(fileType) && count + selectedFiles.size() > MAX_VIDEOS)) {
                Toast.makeText(getContext(), "Exceeded max file limit", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < count; i++) {
                Uri fileUri = data.getClipData().getItemAt(i).getUri();
                selectedFiles.add(fileUri);
                addThumbnailToGridLayout(fileUri, fileType);
            }
        } else if (data.getData() != null) {
            Uri fileUri = data.getData();
            if (("image".equals(fileType) && selectedFiles.size() + 1 > MAX_IMAGES) ||
                    ("video".equals(fileType) && selectedFiles.size() + 1 > MAX_VIDEOS)) {
                Toast.makeText(getContext(), "Exceeded max file limit", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedFiles.add(fileUri);
            addThumbnailToGridLayout(fileUri, fileType);
        }
    }

    private void addThumbnailToGridLayout(Uri fileUri, String fileType) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        if ("image".equals(fileType)) {
            imageView.setImageURI(fileUri);
        } else if ("video".equals(fileType)) {
            imageView.setImageBitmap(getThumbnailFromVideoUri(fileUri));
        }

        gridLayoutSelectedFiles.addView(imageView);
    }

    private Bitmap getThumbnailFromVideoUri(Uri videoUri) {
        return ThumbnailUtils.createVideoThumbnail(videoUri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
    }

    private void addItem() {
        String LotNumber = editTextLotNumber.getText().toString().trim();
        String Name = editTextName.getText().toString().trim();
        String Category = spinnerCategory.getSelectedItem().toString().toLowerCase();
        String Period = spinnerPeriod.getSelectedItem().toString().toLowerCase();
        String description = editTextDescription.getText().toString().trim();

        if (LotNumber.isEmpty() || Name.isEmpty() || Category.isEmpty() || Period.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadFiles(() -> {
            DatabaseReference categoriesRef = db.getReference("categories/" + Category);
            DatabaseReference periodsRef = db.getReference("periods/" + Period);

            currentItemID = categoriesRef.push().getKey(); // Generate a unique ID for the item

            if (currentItemID != null) {
                CatalogItem item = new CatalogItem(LotNumber, Name, Category, description, Period, uploadedURLs);

                // Store item under "categories"
                categoriesRef.child(currentItemID).setValue(item).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Item added under categories.");
                    } else {
                        Log.e(TAG, "Failed to add item under categories: " + task.getException().getMessage());
                    }
                });

                // Store item under "periods"
                periodsRef.child(currentItemID).setValue(item).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Item added under periods.");
                    } else {
                        Log.e(TAG, "Failed to add item under periods: " + task.getException().getMessage());
                    }
                });

                Toast.makeText(getContext(), "Item added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to generate item ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadFiles(Runnable onComplete) {
        uploadedURLs.clear();
        for (Uri fileUri : selectedFiles) {
            uploadFile(fileUri, onComplete);
        }
    }

    private void uploadFile(Uri fileUri, Runnable onComplete) {
        StorageReference storageRef = storage.getReference();

        String filePath = "uploads/" + UUID.randomUUID().toString();
        StorageReference fileRef = storageRef.child(filePath);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    uploadedURLs.add(url);

                    // Check if all files are uploaded
                    if (uploadedURLs.size() == selectedFiles.size()) {
                        onComplete.run();
                    }
                }))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Upload failed: " + e.getMessage());
                    Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
