package com.b07project2024.group1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

/**
 * CatalogItemKeyProvider gets the key and position given the ItemAdapter
 */
public class CatalogItemKeyProvider extends ItemKeyProvider<String> {
    private final CatalogItemAdapter adapter;

    /**
     * Creates a new provider with the given scope.
     */
    protected CatalogItemKeyProvider(CatalogItemAdapter adapter) {
        super(SCOPE_CACHED);
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public String getKey(int position) {
        if (adapter.getCatalogList().get(position) != null){
            return adapter.getCatalogList().get(position).getLot();
        }
        return null;
    }

    @Override
    public int getPosition(@NonNull String key) {
        for(int i = 0; i < adapter.getCatalogList().size(); i++){
            if (adapter.getCatalogList().get(i).getLot().equals(key)) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }
}
