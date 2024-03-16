// VideoAdapter.java
package com.surakshamitra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class videoAdaptor extends RecyclerView.Adapter<videoAdaptor.ViewHolder> {

    private List<youtubeVideo> youtubeVideos;

    public videoAdaptor(List<youtubeVideo> youtubeVideos) {
        this.youtubeVideos = youtubeVideos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videoview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        youtubeVideo video = youtubeVideos.get(position);
        String videoUrl = "https://www.youtube.com/embed/" + video.getVideoId();
        holder.webView.loadUrl(videoUrl);
    }

    @Override
    public int getItemCount() {
        return youtubeVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        WebView webView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.videoView);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
        }
    }
}
