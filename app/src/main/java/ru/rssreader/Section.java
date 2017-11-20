package ru.rssreader;

/**
 * Created by rash on 16.11.2017.
 */

public class Section {
    public String Title;
    public String UrlLink;
    public int IconId;

    public Section(String title, String urlLink, int iconId) {
        Title = title;
        UrlLink = urlLink;
        IconId = iconId;
    }
}
