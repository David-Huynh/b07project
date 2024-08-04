package com.b07project2024.group1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class ContextTopMenuFragment extends Fragment {
    private AuthManager authManager;
    private CatalogSelectionViewModel selectionViewModel;
    private CatalogViewModel catalogViewModel;

    private boolean isSearched;
    private boolean isSelected;
    private boolean isAuthed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_context_top_menu, container, false);
        MaterialToolbar appBar = view.findViewById(R.id.topAppBar);

        Menu menu = appBar.getMenu();
        MenuItem login = menu.findItem(R.id.user);
        MenuItem search = menu.findItem(R.id.search);
        MenuItem add = menu.findItem(R.id.add);
        MenuItem report = menu.findItem(R.id.report);
        MenuItem delete = menu.findItem(R.id.delete);

        appBar.setOnMenuItemClickListener(this::onMenuClick);
        appBar.setNavigationOnClickListener(nav -> onNavigationClick(appBar));

        setNavigationIcon(appBar);
        requireActivity().getSupportFragmentManager().addOnBackStackChangedListener(() -> setNavigationIcon(appBar));

        authManager = AuthManager.getInstance();
        selectionViewModel = new ViewModelProvider(requireActivity()).get(CatalogSelectionViewModel.class);
        catalogViewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);

        selectionViewModel.getSelectedItems().observe(getViewLifecycleOwner(), items -> setSelected(appBar, items));
        authManager.getLoginStatus().observe(getViewLifecycleOwner(), status -> setVisibility(login, add, report, delete, status));
        catalogViewModel.getFilterLive().observe(getViewLifecycleOwner(), searched -> setSearched(search, searched));
        return view;
    }

    /**
     * Sets the Navigation icon to a back arrow or nothing based off stack count
     * @param appBar navigation icon
     */
    private void setNavigationIcon(MaterialToolbar appBar) {
        int backStackCount = requireActivity().getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount <= 1) {
            appBar.setNavigationIcon(null);
        }else {
            appBar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        }
    }

    /**
     * Clears selection if something is selected or goes back in the stack if exists
     * @param appBar the navigation icon
     */
    private void onNavigationClick(MaterialToolbar appBar) {
        if (isSelected) {
            selectionViewModel.clearSelectedItems();
            appBar.setNavigationIcon(null);
        } else {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Navigates users to the correct fragment based of the view clicked
     * @param item the item clicked by the user
     * @return true if successfully found the item
     */
    private boolean onMenuClick (MenuItem item){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        int itemId = item.getItemId();
        //TODO: REPLACE R.id.fragment_container with the actual corresponding fragment
        if (itemId == R.id.search) {
            if (!isSearched) {
                transaction.replace(R.id.fragment_container, new SearchCatalogFragment());
                transaction.addToBackStack("Search");
            }
        } else if (itemId == R.id.user) {
            if (isAuthed) {
                authManager.logout();
                authManager.getLoginStatus();
            } else {
                transaction.replace(R.id.fragment_container, new LoginFragment());
                transaction.addToBackStack("Login");
            }
        } else if (itemId == R.id.add){
            transaction.replace(R.id.fragment_container, new CatalogFragment());
            transaction.addToBackStack("Add");
        } else if (itemId == R.id.report) {
            transaction.replace(R.id.fragment_container, new CatalogFragment());
            transaction.addToBackStack("Report");
        } else if (itemId == R.id.delete) {
            transaction.replace(R.id.fragment_container, new CatalogFragment());
            transaction.addToBackStack("Delete");
        } else {
            return false;
        }
        selectionViewModel.clearSelectedItems();
        catalogViewModel.clearSearch();
        transaction.commit();
        return true;
    }

    /**
     * Changes the navigation icon of selected based off whether an item has been selected or not
     * @param appBar selected icon
     * @param items items selected
     */
    private void setSelected(MaterialToolbar appBar, List<CatalogItem> items) {
        if (appBar != null && requireActivity().getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            if (items != null && !items.isEmpty()) {
                isSelected = true;
                appBar.setNavigationIcon(R.drawable.ic_close_24dp);
            } else {
                isSelected = false;
                appBar.setNavigationIcon(null);
            }
        }
    }

    /**
     * Changes the navigation icon of search based off whether an item has been searched
     * @param search icon
     * @param searched true if searched false if not
     */
    private void setSearched(MenuItem search, CatalogItem searched) {
        if (search != null) {
            if (searched != null) {
                isSearched = true;
                search.setIcon(R.drawable.ic_close_24dp);
            } else {
                isSearched = false;
                search.setIcon(R.drawable.ic_search_24dp);
            }
        }
    }

    /**
     * Sets the visibility of the navigation icons based off whether isVisible is true
     * @param user user's login/logout icon
     * @param add add catalog item icon
     * @param report generate report icon
     * @param delete delete item icon
     * @param isAuthed the state of whether they are authed
     */
    private void setVisibility(MenuItem user, MenuItem add, MenuItem report, MenuItem delete, Boolean isAuthed) {
        this.isAuthed = isAuthed;
        if (user != null) {
            if (isAuthed) {
                user.setIcon(R.drawable.ic_logout_24dp);
            } else {
                user.setIcon(R.drawable.ic_person_24dp);
            }
        }
        if (add != null) add.setVisible(isAuthed);
        if (report != null) report.setVisible(isAuthed);
        if (delete != null) delete.setVisible(isAuthed);
    }
}