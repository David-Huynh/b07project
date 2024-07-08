package com.b07project2024.group1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class ContextTopMenuFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private CatalogSelectionViewModel selectionViewModel;
    private CatalogViewModel catalogViewModel;

    private Integer lastNavID;
    private boolean isSearched;
    private boolean isSelected;
    private boolean isAuthed;

    private DeleteItem delete;

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

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        selectionViewModel = new ViewModelProvider(requireActivity()).get(CatalogSelectionViewModel.class);
        catalogViewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);

        selectionViewModel.getSelectedItems().observe(getViewLifecycleOwner(), items -> setSelected(appBar, items));
        loginViewModel.getLoginStatus().observe(getViewLifecycleOwner(), status -> setVisibility(login, add, report, delete, status));
        catalogViewModel.getFilterLive().observe(getViewLifecycleOwner(), searched -> setSearched(search, searched));
        return view;
    }



    private void setNavigationIcon(MaterialToolbar appBar){
        if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            appBar.setNavigationIcon(null);
            lastNavID = null;
        }else {
            appBar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
            lastNavID = R.drawable.ic_arrow_back_24dp;
        }
    }


    private void onNavigationClick(MaterialToolbar appBar) {
        if (isSelected) {
            selectionViewModel.clearSelectedItems();
            appBar.setNavigationIcon(null);
            lastNavID = null;
        } else {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }
    private boolean onMenuClick (MenuItem item){
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

        if (item.getItemId() == R.id.search) {
            if (!isSearched) {
                transaction.replace(R.id.fragment_container, new CatalogFragment());
                transaction.addToBackStack("Search");
            }
        } else if (item.getItemId() == R.id.user) {
            if (isAuthed) {
                loginViewModel.logout();
            } else {
                transaction.replace(R.id.fragment_container, new CatalogFragment());
                transaction.addToBackStack("Login");
            }
        } else if (item.getItemId() == R.id.add){
            transaction.replace(R.id.fragment_container, new CatalogFragment());
            transaction.addToBackStack("Add");
        } else if (item.getItemId() == R.id.report) {
            transaction.replace(R.id.fragment_container, new CatalogFragment());
            transaction.addToBackStack("Report");
        } else if (item.getItemId() == R.id.delete) {
            delete = new DeleteItem();
            List<CatalogItem> d_selected = selectionViewModel.getSelectedItems().getValue();
            selectionViewModel.clearSelectedItems();
            if(d_selected == null){
                Toast.makeText(getActivity(), "Please select an item", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if(d_selected != null){
                // Pop-up to confirm delete
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Delete Item");
                if(d_selected.size() == 1){
                    alert.setMessage("Are you sure you want to delete this item? (Lot #" + d_selected.get(0).getLot() + ")");
                }
                else {
                    alert.setMessage("Are you sure you want to delete these items?");
                }
                // Deleting selected items
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int k) {
                        for(int i=0; i<d_selected.size(); i++) {
                            if(d_selected.get(i) != null) {
                                CatalogItem d_item = d_selected.get(i);
                                delete.deleteItem(d_item.getLot());
                            }
                        }
                        Toast.makeText(getContext(), "Item(s) deleted from catalog", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int k) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
            return true;

        } else {
            return false;
        }
        selectionViewModel.clearSelectedItems();
        catalogViewModel.clearSearch();
        transaction.commit();
        return true;
    }

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
    private void setVisibility(MenuItem user, MenuItem add, MenuItem report, MenuItem delete, Boolean isVisible) {
        isAuthed = isVisible;
        if (user != null) {
            if (isVisible) {
                user.setIcon(R.drawable.ic_logout_24dp);
            } else {
                user.setIcon(R.drawable.ic_person_24dp);
            }
        }
        if (add != null) add.setVisible(isVisible);
        if (report != null) report.setVisible(isVisible);
        if (delete != null) delete.setVisible(isVisible);
    }
}
