package ru.rssreader;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class FeedProvider {

    public Feed loadFeed(String urlLink) {
        Feed feed = null;
        InputStream stream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/xml; charset=utf-8");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                feed = parseFeed(stream);
            }

        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return feed;
    }

    protected String deserializeToString(HttpURLConnection connection) throws IOException {
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();

        String output = buffer.toString();
        return output;
    }

    private Feed parseFeed(InputStream inputStream) {

        Feed feed = new Feed();
        feed.Items = new ArrayList<>();

        String title = null;
        String link = null;
        String description = null;
        String imageUrl = null;
        String fullText = null;
        String pubDate = null;

        boolean isItem = false;

        try {

            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();

            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("TAG", "Parsing name ==> " + name);

                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                    imageUrl = getUrl(description);
                } else if (name.equalsIgnoreCase("fulltext")) {
                    fullText = result;
                } else if (name.equalsIgnoreCase("pubDate")) {
                    pubDate = result;
                }

                if (title != null && link != null && description != null) {
                    if (isItem) {
                        Item item = new Item();
                        item.setTitle(title);
                        item.setLink(link);
                        item.setDescription(description);
                        item.setImageUrl(imageUrl);
                        item.setFullText(fullText);
                        item.setPubDate(pubDate);
                        feed.Items.add(item);
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                } else {
                    if (feed.Title == null) {
                        feed.Title = title;
                    }
                    if (feed.Link == null) {
                        feed.Link = link;
                    }
                    if (feed.Description == null) {
                        feed.Description = description;
                    }
                }
            }
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return feed;
    }

    private String getUrl(String input){
        Pattern p = Pattern.compile("src=\"(.*?)\"");
        Matcher m = p.matcher(input);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }
}
