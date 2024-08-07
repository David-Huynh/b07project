package com.b07project2024.group1;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * CatalogFragment displays a list of CatalogItems in a RecyclerView
 * allowing for infinite scrolling
 */
@AndroidEntryPoint
public class CatalogFragment extends Fragment implements CatalogFragmentCallbackInterface {
    private CatalogViewModel catalogViewModel;
    private CatalogSelectionViewModel selectionViewModel;
    private LoginViewModel loginViewModel;
    private CatalogItemViewModel catalogItemViewModel;

    private LinearLayoutManager layoutManager;

    private RecyclerView recyclerView;
    private CatalogItemAdapter catalogItemAdapter;
    private SelectionTracker<String> tracker;

    boolean isLoading = false;

    /**
     * Adds the Catalog Layout and initializes and populates the ItemAdapter
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog_view, container, false);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        catalogItemViewModel = new ViewModelProvider(requireActivity()).get(CatalogItemViewModel.class);

        catalogItemAdapter = new CatalogItemAdapter(this);
        recyclerView.setAdapter(catalogItemAdapter);

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);


        initCatalogPageObserver();
        initScrollListener();
        initSelectionTracker();
        return view;
    }

    /**
     * Populates item adapter based off of the observed list
     */
    private void initCatalogPageObserver(){
        catalogViewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        catalogViewModel.getInitialCatalogPage().observe(getViewLifecycleOwner(), catalogItems -> {
            catalogItemAdapter.setCatalogList(catalogItems);
            isLoading = false;
        });
    }

    /**
     * Adds a scroll listener to load next page when at the bottom
     */
    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && (layoutManager.findLastVisibleItemPosition() + 1) >= layoutManager.getItemCount()){
                    isLoading = true;
                    catalogViewModel.getNextCatalogPage();
                }
            }
        });
    }

    /**
     * Adds a selection tracker to the recyclerview and item adapter
     */
    private void initSelectionTracker() {
        selectionViewModel = new ViewModelProvider(requireActivity()).get(CatalogSelectionViewModel.class);
        tracker = new SelectionTracker.Builder<>(
                "catalogSelection",
                recyclerView,
                new CatalogItemKeyProvider(catalogItemAdapter),
                new CatalogItemDetailsLookup(recyclerView),
                StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build();

        catalogItemAdapter.setTracker(tracker);

        selectionViewModel.getSelectedItems().observe(getViewLifecycleOwner(), selected ->{
            if (selected == null) {
                tracker.clearSelection();
            }
        });

        tracker.addObserver(new SelectionTracker.SelectionObserver<String>() {
            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                // Set Selection in shared ViewModel
                if (tracker == null)
                    return;
                selectionViewModel.setSelectedItems(catalogItemAdapter.getCatalogList(), tracker.getSelection());
            }
        });
    }

    @Override
    public void onClick(CatalogItem item) {
        catalogItemViewModel.setItem(item);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new ViewItemFragment())
                .addToBackStack(null) // maybe 'item'
                .commit();
    }
}
