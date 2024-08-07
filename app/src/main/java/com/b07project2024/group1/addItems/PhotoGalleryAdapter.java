package com.b07project2024.group1.addItems;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b07project2024.group1.R;

import java.util.List;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ViewHolder> {
    private List<String> allImages;
    private List<String> selectedImages;
    private OnImageClickListener listener;

    public interface OnImageClickListener {
        void onImageClick(String imagePath);
    }

    public PhotoGalleryAdapter(List<String> allImages, List<String> selectedImages, OnImageClickListener listener) {
        this.allImages = allImages;
        this.selectedImages = selectedImages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagePath = allImages.get(position);
        holder.imageView.setImageURI(Uri.parse(imagePath));
        holder.checkView.setVisibility(selectedImages.contains(imagePath) ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(v -> listener.onImageClick(imagePath));
    }

    @Override
    public int getItemCount() {
        return allImages.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View checkView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            checkView = itemView.findViewById(R.id.checkView);
        }
    }
}