package com.b07project2024.group1.addItems;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class PhotoFilesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> selectedItem = new MutableLiveData<>();

    public void selectItem(ArrayList<String> item) {
        selectedItem.setValue(item);
    }

    public LiveData<ArrayList<String>> getSelectedItem() {
        return selectedItem;
    }
}
