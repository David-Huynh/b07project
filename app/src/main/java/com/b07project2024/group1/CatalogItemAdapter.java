package com.b07project2024.group1;

import android.graphics.drawable.PictureDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * CatalogItemAdapter class for RecyclerView in CatalogFragment
 */
public class CatalogItemAdapter extends RecyclerView.Adapter<CatalogItemAdapter.ViewHolder> {
    public final List<CatalogItem> catalogList;
    SelectionTracker<String> tracker;
    public CatalogItemAdapter() {
        setHasStableIds(true);
        catalogList = new ArrayList<>();
    }

    /**
     * Sets the list of catalog, and updates based off the difference
     * @param catalogList the new list of items
     */
    public void setCatalogList(List<CatalogItem> catalogList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CatalogDiffCallback(this.catalogList, catalogList));
        this.catalogList.clear();
        this.catalogList.addAll(catalogList);
        diffResult.dispatchUpdatesTo(this);
    }
    public List<CatalogItem> getCatalogList(){
        return catalogList;
    }

    public void setTracker(SelectionTracker<String> tracker){
        this.tracker = tracker;
    }

    /**
     * View holder class for items
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName, textViewLot, textViewCategory, textViewPeriod, textViewDescription;
        private final ImageView imageViewPreview;

        public ViewHolder(View view) {
            super(view);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewLot = itemView.findViewById(R.id.textViewLot);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewPeriod = itemView.findViewById(R.id.textViewPeriod);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewPreview = itemView.findViewById(R.id.imageViewPreview);
        }

        /**
         * Binds CatalogItem to the View
         *
         * @param item       CatalogItem to be bound
         * @param isSelected Whether the item is selected
         */
        public void bind(CatalogItem item, boolean isSelected) {
            textViewName.setText(item.getName());
            textViewLot.setText(item.getLot());
            textViewCategory.setText(item.getCategory());
            textViewPeriod.setText(item.getPeriod());
            textViewDescription.setText(item.getDescription());
            ViewGroup.LayoutParams params = imageViewPreview.getLayoutParams();
            if (isSelected){
                params.width = 120;
                imageViewPreview.setLayoutParams(params);
                imageViewPreview.setImageResource(R.drawable.ic_check_circle_24dp);
            } else {
                params.width = 300;
                params.height = 300;
                imageViewPreview.setLayoutParams(params);
                if(item.getImageURLs() != null) {
                    Glide.with(imageViewPreview).load(item.getImageURLs().get(0)).centerCrop().into(imageViewPreview);
                }
            }
        }

        /**
         * Retrieves the key and position of the selected item
         * @return Position and Key
         */
        public ItemDetailsLookup.ItemDetails<String> getItemDetails() {
            return new ItemDetailsLookup.ItemDetails<String>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Override
                public String getSelectionKey() {
                    return getAdapterPosition() != RecyclerView.NO_POSITION ? catalogList.get(getAdapterPosition()).getLot() : null;
                }
            };
        }
    }

    /**
     * Sets the layout
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return returns the ViewHolder layout
     */
    @NonNull
    @Override
    public CatalogItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item_layout, parent, false);
        return new CatalogItemAdapter.ViewHolder(view);
    }

    /**
     * Binds the item to holder
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CatalogItemAdapter.ViewHolder holder, int position) {
        CatalogItem item = catalogList.get(position);
        boolean isSelected = tracker != null && tracker.isSelected(item.getLot());
        holder.bind(item, isSelected);
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }
    @Override
    public long getItemId(int position){
        return position;
    }


}
