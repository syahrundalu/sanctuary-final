package com.example.sanctuary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> {

    private List<FeedItem> feedItems; // Replace with your data source

    public FeedAdapter(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        FeedItem item = feedItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {

        // ViewHolder members (e.g., TextView, ImageView) can be added here

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize and link your layout elements (TextView, ImageView, etc.)
        }

        public void bind(FeedItem item) {
            // Update UI elements with data from FeedItem
        }
    }
}
