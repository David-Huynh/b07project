package com.b07project2024.group1;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ViewItemFragment extends Fragment {
    private CatalogItemViewModel catalogItemViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catalogItemViewModel = new ViewModelProvider(requireActivity()).get(CatalogItemViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_item, container, false);

        TextView itemNameView = view.findViewById(R.id.textViewName);
        TextView itemLotView = view.findViewById(R.id.textViewLot);
        TextView itemCategoryView = view.findViewById(R.id.textViewCategory);
        TextView itemPeriodView = view.findViewById(R.id.textViewPeriod);
        TextView itemDescriptionView = view.findViewById(R.id.textViewDescription);
        ImageView itemPictureView = view.findViewById(R.id.imageViewItem);

        catalogItemViewModel.getItem().observe(getViewLifecycleOwner(), new Observer<CatalogItem>() {
            @Override
            public void onChanged(@Nullable CatalogItem item) {
                if (item != null) {
                    itemNameView.setText(item.getName());
                    itemLotView.setText(item.getLot());
                    itemCategoryView.setText(item.getCategory());
                    itemPeriodView.setText(item.getPeriod());
                    itemDescriptionView.setText(item.getDescription());

                    if (item.getImageURLs() != null){
                        ViewImageTask(item.getImageURLs().get(0), itemPictureView);
                    }
                }
            }
        });
        return view;
    }
    private void ViewImageTask(String url, ImageView imageView){
        Glide.with(this)
                .load(url)
                .into(imageView);
    }
}