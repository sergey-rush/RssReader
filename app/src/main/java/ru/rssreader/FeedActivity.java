package ru.rssreader;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends Activity {

    private String urlLink;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private Feed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Bundle b = getIntent().getExtras();
        urlLink = b.getString("UrlLink");
        String title = b.getString("Title");
        ActionBar ab = getActionBar();
        ab.setTitle(title);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);
            }
        });

        new FetchFeedTask().execute((Void) null);
    }

    private void loadDataCallback() {
        mRecyclerView.setAdapter(new ItemAdapter(feed.Items));


    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        //private String urlLink = "https://iz.ru/xml/rss/all.xml";
        //private String urlLink = "http://k.img.com.ua/rss/ru/all_news2.0.xml";

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            FeedProvider feedProvider = new FeedProvider();
            feed = feedProvider.loadFeed(urlLink);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);
            if (success) {
                loadDataCallback();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Rss feed url", Toast.LENGTH_LONG).show();
            }
        }
    }
}



