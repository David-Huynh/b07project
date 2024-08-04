package com.b07project2024.group1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
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
        String lot = "10";
        String name = "title";
        String category = "author";
        String period = "genre";
        String description = "description";
        String pictureURL = "description";
        CatalogItem item = new CatalogItem(lot, name, category, period, description, pictureURL);
        Intent i = new Intent(this, ViewSpecificItem.class);
        String PE_name = "item";
        i.putExtra(PE_name, (Parcelable) item);
        startActivity(i);
    }

}