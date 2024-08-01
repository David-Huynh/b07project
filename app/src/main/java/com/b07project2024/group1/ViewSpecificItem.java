package com.b07project2024.group1;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewSpecificItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_specfiic_item);


        Item item = getIntent().getParcelableExtra("item");

        TextView itemTitleView = findViewById(R.id.textViewTitle);
        TextView itemAuthorView = findViewById(R.id.textViewAuthor);
        TextView itemGenreView = findViewById(R.id.textViewGenre);
        TextView itemDescriptionView = findViewById(R.id.textViewDescription);

        assert item != null;
        itemTitleView.setText(item.getTitle());
        itemAuthorView.setText(item.getAuthor());
        itemGenreView.setText(item.getGenre());
        itemDescriptionView.setText(item.getDescription());

    }
}

