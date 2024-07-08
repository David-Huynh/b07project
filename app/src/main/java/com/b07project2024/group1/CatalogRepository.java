package com.b07project2024.group1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Abstraction for Server/Repository of Catalog so that the concrete implementations can be swapped out easily
 * Following Open/Closed and Interface Segregation
 */
public interface CatalogRepository {
    void getCatalogPage(MutableLiveData<List<CatalogItem>> items);
    void getNextCatalogPage(String lastItemID, MutableLiveData<List<CatalogItem>> items);
    void getCatalogPageByItem(CatalogItem item, MutableLiveData<List<CatalogItem>> items);
}
