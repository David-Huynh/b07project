package com.b07project2024.group1;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CatalogItemViewModel extends ViewModel {
    private final MutableLiveData<CatalogItem> item;

    public CatalogItemViewModel(){
        item = new MutableLiveData<>();
    }
    public void setItem(CatalogItem item) {
        this.item.setValue(item);
    }

    public LiveData<CatalogItem> getItem() {
        return item;
    }
}
