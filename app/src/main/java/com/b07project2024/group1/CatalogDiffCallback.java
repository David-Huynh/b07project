package com.b07project2024.group1;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/**
 * CatalogDiffCallback finds the changes between the the last Catalog List and the new List
 * to optimize UI redraws
 */
public class CatalogDiffCallback extends DiffUtil.Callback {
    private final List<CatalogItem> oldList;
    private final List<CatalogItem> newList;

    public CatalogDiffCallback(List<CatalogItem> oldList, List<CatalogItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    /**
     * Checks if an item at the old list and an item in the new list is referring to the same item
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return true if the items are the same lot
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getLot().equals(newList.get(newItemPosition).getLot());
    }

    /**
     * Checks that if an item in the old list and an item in the new list is unchanged
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list which replaces the
     *                        oldItem
     * @return true if the items are exactly the same
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if(newItemPosition == 0 & oldItemPosition != 1){
            return false;
        }
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
