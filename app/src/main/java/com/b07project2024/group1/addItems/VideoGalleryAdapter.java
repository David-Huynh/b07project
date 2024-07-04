package com.b07project2024.group1.addItems;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b07project2024.group1.R;

import java.util.List;

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder> {
    private List<String> allVideos;
    private List<String> selectedVideos;
    private OnVideoClickListener listener;

    public interface OnVideoClickListener {
        void onVideoClick(String videoPath);
    }

    public VideoGalleryAdapter(List<String> allVideos, List<String> selectedVideos, OnVideoClickListener listener) {
        this.allVideos = allVideos;
        this.selectedVideos = selectedVideos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String videoPath = allVideos.get(position);
        holder.videoView.setVideoURI(Uri.parse(videoPath));
        holder.videoView.start();
        holder.checkView.setVisibility(selectedVideos.contains(videoPath) ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(v -> listener.onVideoClick(videoPath));
    }

    @Override
    public int getItemCount() {
        return allVideos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        View checkView;

        ViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            checkView = itemView.findViewById(R.id.checkView);
        }
    }
}