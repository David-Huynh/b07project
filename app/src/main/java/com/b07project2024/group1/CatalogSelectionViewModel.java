package com.b07project2024.group1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.selection.Selection;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of selected items for report generation and deletion
 */
public class CatalogSelectionViewModel extends ViewModel {
    private final MutableLiveData<List<CatalogItem>> selectedItems;

    public CatalogSelectionViewModel() {
        selectedItems = new MutableLiveData<>();
    }

    /**
     * Sets the items that are selected on the view to the view model's live data
     * @param items in the Catalog
     * @param selected items in the view
     */
    public void setSelectedItems(List<CatalogItem> items, Selection<String> selected){
        List<CatalogItem> selectedItemsTemp = new ArrayList<>();
        if (selected != null && items != null) {
            for (String key : selected) {
                for (CatalogItem item : items) {
                    if (item.getLot().equals(key)) {
                        selectedItemsTemp.add(item);
                        break;
                    }
                }
            }
        }
        selectedItems.setValue(selectedItemsTemp);
    }

    public void clearSelectedItems(){
        selectedItems.setValue(null);
    }

    /**
     * Retrieves the livedata to be observed by the view
     * @return live data to be observed
     */
    public LiveData<List<CatalogItem>> getSelectedItems() {
        return selectedItems;
    }
}
