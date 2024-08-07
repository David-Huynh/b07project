package com.b07project2024.group1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

                    if (item.getImageURLs().get(0) != null){
                        new ViewImageTask(itemPictureView).execute(item.getImageURLs().get(0));

                    }
                }
            }
        });
        return view;
    }

    private static class ViewImageTask extends AsyncTask<String, Integer, Bitmap>{
        ImageView imageView;

        public ViewImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        public static Bitmap getBitmap(String src){
            try{
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap pic = BitmapFactory.decodeStream(input);
                return pic;
            } catch(IOException e){
                Log.d("error", "error");
                return null;
            }
        }

        @Override
        protected Bitmap doInBackground(String... url){
            String imageURL = url[0];
            return getBitmap(imageURL);
        }

        @Override
        protected void onPostExecute(Bitmap imageMap){
            if (imageMap != null) {
                imageView.setImageBitmap(imageMap);
            } else {
                Log.d("error", "error");
            }
        }
    }
}