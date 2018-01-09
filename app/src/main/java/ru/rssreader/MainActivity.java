package ru.rssreader;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    GridView gridView;
    GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView)findViewById(R.id.grid_view);
        adapter = new GridAdapter(initSections());
        gridView.setAdapter(adapter);
    }

    private List<Section> initSections(){
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("Политика", "http://k.img.com.ua/rss/ru/politics.xml", R.drawable.politics));
        sections.add(new Section("Экономика", "http://k.img.com.ua/rss/ru/economics.xml", R.drawable.economics));
        sections.add(new Section("Технологии", "http://k.img.com.ua/rss/ru/technews.xml", R.drawable.technology));
        sections.add(new Section("Стиль", "http://k.img.com.ua/rss/ru/lifestyle.xml", R.drawable.lifestyle));
        sections.add(new Section("Культура", "http://k.img.com.ua/rss/ru/culture.xml", R.drawable.culture));
        sections.add(new Section("Спорт", "http://k.img.com.ua/rss/ru/sport.xml", R.drawable.sport));
        sections.add(new Section("Кино", "http://k.img.com.ua/rss/ru/cinema.xml", R.drawable.cinema));
        sections.add(new Section("Наука", "http://k.img.com.ua/rss/ru/science.xml", R.drawable.science));
        sections.add(new Section("Мода", "http://k.img.com.ua/rss/ru/worldabus.xml", R.drawable.fashion));
        return sections;
    }
}
