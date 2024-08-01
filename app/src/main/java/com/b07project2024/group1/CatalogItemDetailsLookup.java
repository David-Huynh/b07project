package com.b07project2024.group1;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

/**
 * CatalogItemDetailsLookup helps to find the position and key of the selected child
 */
public class CatalogItemDetailsLookup extends ItemDetailsLookup<String> {
    private final RecyclerView recyclerView;

    public CatalogItemDetailsLookup(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }
    @Nullable
    @Override
    public ItemDetails<String> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null)
            return ((CatalogItemAdapter.ViewHolder)recyclerView.getChildViewHolder(view)).getItemDetails();
        return null;
    }
}
