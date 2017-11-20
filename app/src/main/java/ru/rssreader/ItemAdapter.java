package ru.rssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by rash on 15.11.2017.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> mItems;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View feedView;

        public ViewHolder(View view) {
            super(view);
            feedView = view;
        }
    }

    public ItemAdapter(List<Item> feeds) {
        mItems = feeds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item feed = mItems.get(position);

        if (feed.ImageUrl != null) {
            ImageView imageView = holder.feedView.findViewById(R.id.imageView);
            DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(feed.ImageUrl, imageView);
            downloadAsyncTask.execute();
        }

        ((TextView) holder.feedView.findViewById(R.id.titleText)).setText(feed.Title);
        ((TextView) holder.feedView.findViewById(R.id.descriptionText)).setText("");

        //if (feed.Description != null) {
            //((TextView) holder.feedView.findViewById(R.id.descriptionText)).setText(Html.fromHtml(feed.Description));
        //}

        if (feed.FullText != null) {
            ((TextView) holder.feedView.findViewById(R.id.fullText)).setText(Html.fromHtml(feed.FullText));
        }

        ((TextView) holder.feedView.findViewById(R.id.linkText)).setText(feed.Link);
        ((TextView) holder.feedView.findViewById(R.id.pubDate)).setText(feed.PubDate);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class DownloadAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private Bitmap bitmap;
        private String uplLink;
        private ImageView imageView;

        public DownloadAsyncTask(String uplLink, ImageView imageView) {
            this.uplLink = uplLink.replace("190x120", "610x385");
            this.imageView = imageView;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL imageURL = new URL(uplLink);
                bitmap = BitmapFactory.decodeStream(imageURL.openStream());
            } catch (IOException e) {
                bitmap = null;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            } else {

            }
        }
    }
}
