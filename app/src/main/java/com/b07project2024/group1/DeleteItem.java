package com.b07project2024.group1;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Public class DeleteItem is used to implement the deletion of selected items in the catalogue
 */
public class DeleteItem {
    private FirebaseDatabase db;

    public DeleteItem(){
        db = FirebaseDatabase.getInstance();
    }

    /**
     * Deletes item from catalog by searching through firebase realtime database by lot
     * @param lot The lot number of the item to be deleted
     */
    public void deleteItem(String lot) {
        DatabaseReference dataRef = db.getReference("catalog/");

        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    CatalogItem item = child.getValue(CatalogItem.class);
                    if (item != null && item.getLot().equals(lot)) {
                        // Delete item and related images/videos from catalog
                        deleteFiles(item.getImageURLs());
                        deleteFiles(item.getVideoURLs());
                        dataRef.child(item.getLot()).removeValue();
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", "database error");
            }
        });
    }

    /**
     * Deletes all uploaded images/videos of the selected item from the firebase storage
     * @param pics List of strings containing links of files to iterate through and delete
     */
    private void deleteFiles(List<String> pics){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if(pics == null || pics.isEmpty()){
            return;
        }
        for(int i=0; i<pics.size(); i++){
            if(pics.get(i) != null){
                StorageReference storageRef = storage.getReferenceFromUrl(pics.get(i));
                storageRef.delete();
            }
        }
    }
}
