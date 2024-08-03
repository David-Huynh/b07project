package com.b07project2024.group1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

/**
 * ChildEventListener to update Catalog data live
 */
public class CatalogChildEventListener implements ChildEventListener {
    MutableLiveData<List<CatalogItem>> catalogLiveData;
    List<CatalogItem> catalogItems;
    CatalogItem search;

    public CatalogChildEventListener (MutableLiveData<List<CatalogItem>> catalogLiveData,
                                      List<CatalogItem> catalogItems){
        this.catalogLiveData = catalogLiveData;
        this.catalogItems = catalogItems;
        this.search = new CatalogItem();
    }

    public CatalogChildEventListener (MutableLiveData<List<CatalogItem>> catalogLiveData,
                                      List<CatalogItem> catalogItems,
                                      CatalogItem search){
        this.catalogLiveData = catalogLiveData;
        this.catalogItems = catalogItems;
        this.search = search;
    }

    /**
     * Calculates the index of the item based off lot
     * @param lot the lot to be searched for
     * @return the index of the lot or -1 if not found
     */
    private int indexOfPreviousChild(@NonNull String lot){
        for (int i = 0; i < catalogItems.size(); i++){
            if (catalogItems.get(i).getLot().equals(lot)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds child to the list if not already in it and to the position requested
     * @param snapshot An immutable snapshot of the data at the new child location
     * @param previousChildName The key name of sibling location ordered before the new child. This
     *     will be null for the first child node of a location.
     */
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        CatalogItem item = snapshot.getValue(CatalogItem.class);
        if (item != null && !catalogItems.contains(item) && search.itemMatchesSearchParams(item)) {
            if (previousChildName == null){
                catalogItems.add(catalogItems.size(), item);
            } else {
                catalogItems.add(indexOfPreviousChild(previousChildName) + 1, item);
            }
            catalogLiveData.setValue(catalogItems);
        }
    }

    /**
     * Updates the child at the specified location
     * @param snapshot An immutable snapshot of the data at the new data at the child location
     * @param previousChildName The key name of sibling location ordered before the child. This will
     *     be null for the first child node of a location.
     */
    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        CatalogItem item = snapshot.getValue(CatalogItem.class);
        if (item != null  && search.itemMatchesSearchParams(item)) {
            if (previousChildName == null) {
                catalogItems.set(0, item);
            } else {
                catalogItems.set(indexOfPreviousChild(previousChildName)+1, item);
            }
            catalogLiveData.setValue(catalogItems);
        }
    }

    /**
     * Removes the specified child in snapshot
     * @param snapshot An immutable snapshot of the data at the child that was removed.
     */
    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        CatalogItem removedItem = snapshot.getValue(CatalogItem.class);
        if (removedItem != null) {
            for (int i = 0; i < catalogItems.size(); i++) {
                if (catalogItems.get(i).getLot().equals(removedItem.getLot())) {
                    catalogItems.remove(i);
                    break;
                }
            }
            catalogLiveData.setValue(catalogItems);
        }
    }
    //Unsupported actions
    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
    @Override
    public void onCancelled(@NonNull DatabaseError error) {}
}
