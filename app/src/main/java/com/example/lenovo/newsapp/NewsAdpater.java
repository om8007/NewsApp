package com.example.lenovo.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NewsAdpater extends ArrayAdapter<News> {

    public NewsAdpater(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_card, parent, false);
        }

        News currentNews = getItem(position);
        TextView newsTitleTextView = (TextView) listItemView.findViewById(R.id.news_title);
        String title = currentNews.getmTitle();
        newsTitleTextView.setText(title);

        TextView newsCategorytextView = (TextView) listItemView.findViewById(R.id.news_category);
        String category = currentNews.getmCategory();
        newsCategorytextView.setText(category);

        TextView newsDatetextView = (TextView) listItemView.findViewById(R.id.news_date);
        String date = currentNews.getmDate();
        newsDatetextView.setText(date);

        TextView newsAuthortextView = (TextView) listItemView.findViewById(R.id.news_author);
        String author = currentNews.getmAuthor();
        newsAuthortextView.setText(author);

        return listItemView;
    }
}
