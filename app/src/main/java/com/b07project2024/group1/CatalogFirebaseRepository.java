package com.b07project2024.group1;


import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * CatalogFirebase is a concrete implementation of CatalogRepository that uses Firebase
 * Uses Singleton Pattern for Firebase Realtime Database Connection
 */
public class CatalogFirebaseRepository implements CatalogRepository{
    private final FirebaseManager fM;
    private final Executor executor;
    private List<CatalogItem> catalogItems;
    private Query catalogQuery;
    private final List<CatalogChildEventListener> listeners;
    public CatalogFirebaseRepository(FirebaseManager fM, Executor executor) {
        this.fM = fM;
        this.executor = executor;
        catalogItems = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    private void clearListeners(){
        for (CatalogChildEventListener listener: listeners){
            catalogQuery.removeEventListener(listener);
        }
    }

    /**
     * Attaches the first childEventListener to load the initial page
     * @param liveItems is a MutableLiveData that is attached to a ChildEventListener to observe updates
     */
    @Override
    public void getCatalogPage(MutableLiveData<List<CatalogItem>> liveItems){
        clearListeners();
        catalogItems = new ArrayList<>();
        liveItems.setValue(catalogItems);
        executor.execute(() -> {
            catalogQuery = fM.getReference().child("catalog").orderByKey().limitToFirst(9);
            CatalogChildEventListener listener = new CatalogChildEventListener(liveItems, catalogItems);
            listeners.add(listener);
            catalogQuery.addChildEventListener(listener);
        });
    }

    /**
     * Attaches the next childEventListener based on the lastItemID loaded previously
     * @param lastItemID the id of the last item loaded previously
     * @param liveItems the mutable live data that is attached to a ChildEventListener
     */
    @Override
    public void getNextCatalogPage(String lastItemID, MutableLiveData<List<CatalogItem>> liveItems){
        executor.execute(() -> {
            catalogQuery = fM.getReference().child("catalog").orderByKey().limitToFirst(9);
            if (lastItemID != null) {
                catalogQuery = catalogQuery.startAt(lastItemID);
            }
            CatalogChildEventListener listener = new CatalogChildEventListener(liveItems, catalogItems);
            listeners.add(listener);
            catalogQuery.addChildEventListener(listener);
        });

    }

    /**
     * Attaches the childEventListener based off the item filter
     * @param filter the CatalogItem filter; can be incomplete Lot takes priority
     * @param liveItems the mutable live data that is attached to a ChildEventListener
     */
    @Override
    public void getCatalogPageByItem(CatalogItem filter, MutableLiveData<List<CatalogItem>> liveItems) {
        clearListeners();
        catalogItems = new ArrayList<>();
        liveItems.setValue(catalogItems);
        executor.execute(() -> {
            catalogQuery = fM.getReference().child("catalog");
            CatalogChildEventListener listener = new CatalogChildEventListener(liveItems, catalogItems, filter);
            listeners.add(listener);
            catalogQuery.addChildEventListener(listener);
        });
    }
}
