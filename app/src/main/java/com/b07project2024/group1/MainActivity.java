package com.b07project2024.group1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import dagger.hilt.android.AndroidEntryPoint;
import java.io.Serializable;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new CatalogFragment(), R.id.fragment_container);
    }

    private void loadFragment(Fragment fragment, int id) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void viewItem(View view){
        String id = "id";
        String title = "title";
        String author = "author";
        String genre = "genre";
        String description = "description";
        Item item = new Item(id, title, author, genre, description);
        Intent i = new Intent(this, ViewSpecificItem.class);
        String name = "item";
        i.putExtra(name, item);
        startActivity(i);
    }

}