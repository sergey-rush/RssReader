package ru.rssreader;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rash on 16.11.2017.
 */

public class GridAdapter extends BaseAdapter {

    List<Section> sections;

    public GridAdapter(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public Object getItem(int position) {
        return sections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, final ViewGroup parent) {

        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(parent.getContext());
            view = vi.inflate(R.layout.grid_item, null);
        }

        final Section section = this.sections.get(position);

        ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        ivIcon.setImageResource(section.IconId);

        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(section.Title);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), FeedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("UrlLink", section.UrlLink);
                bundle.putString("Title", section.Title);
                intent.putExtras(bundle);
                parent.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
