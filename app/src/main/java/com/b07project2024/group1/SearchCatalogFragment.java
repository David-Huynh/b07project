package com.b07project2024.group1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SearchCatalogFragment extends Fragment {
    private CatalogViewModel catalogViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_view, container, false);
        catalogViewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);

        EditText lot = view.findViewById(R.id.editLot);
        EditText name = view.findViewById(R.id.editName);
        EditText category = view.findViewById(R.id.editCategory);
        EditText period = view.findViewById(R.id.editPeriod);

        Button search = view.findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lotText = checkNull(String.valueOf(lot.getText()));
                String nameText = checkNull(String.valueOf(name.getText()));
                String categoryText = checkNull(String.valueOf(category.getText()));
                String periodText = checkNull(String.valueOf(period.getText()));

                CatalogItem filter = new CatalogItem();
                filter.setLot(lotText);
                filter.setName(nameText);
                filter.setCategory(categoryText);
                filter.setPeriod(periodText);
                catalogViewModel.setFilter(filter);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private String checkNull(String text) {
        if (text.equals("null")){
            return null;
        }
        if (text.isEmpty()) {
            return null;
        }
        return text;
    }
}
