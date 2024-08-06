package com.b07project2024.group1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewItem extends Fragment {

    private static final String ARG_ITEM = "item";
    private CatalogItem item;

    public ViewItem() {
    }

    /**
     * @param item item.
     * @return A new instance of fragment ViewItem.
     */
    public static ViewItem newInstance(CatalogItem item) {
        ViewItem fragment = new ViewItem();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, (Serializable) item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = (CatalogItem) getArguments().getSerializable(ARG_ITEM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_item, container, false);

        TextView itemNameView = view.findViewById(R.id.textViewName);
        TextView itemLotView = view.findViewById(R.id.textViewLot);
        TextView itemCategoryView = view.findViewById(R.id.textViewCategory);
        TextView itemPeriodView = view.findViewById(R.id.textViewPeriod);
        TextView itemDescriptionView = view.findViewById(R.id.textViewDescription);
        // add images
        // add fragment outline to catalog xml
        // hook up on click to catalog item button


        assert item != null;
        itemNameView.setText(item.getName());
        itemLotView.setText(item.getLot());
        itemCategoryView.setText(item.getCategory());
        itemPeriodView.setText(item.getPeriod());
        itemDescriptionView.setText(item.getDescription());

        return view;
    }
}