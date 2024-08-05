package com.b07project2024.group1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import javax.annotation.Nullable;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ViewSpecificItem extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState){
//       super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.view_specfiic_item, container, false);



        Item item = getIntent().getParcelableExtra("item");

        TextView itemTitleView = findViewById(R.id.textViewName);
        TextView itemAuthorView = findViewById(R.id.textViewLot);
        TextView itemGenreView = findViewById(R.id.textViewCategory);
        TextView itemDescriptionView = findViewById(R.id.textViewDescription);

        assert item != null;
        itemTitleView.setText(item.getTitle());
        itemAuthorView.setText(item.getAuthor());
        itemGenreView.setText(item.getGenre());
        itemDescriptionView.setText(item.getDescription());

    }
}

