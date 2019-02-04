package com.example.lenovo.newsapp;

//import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    String mURL;
    //private static String REQUEST_URL = "http://content.guardianapis.com/search?show-tags=contributor&api-key=test";

    public NewsLoader(Context context,String url) {
        super(context);
        this.mURL = url;
    }

    @Override
    protected void onStartLoading() {
        //Toast.makeText(getContext(), mURL, Toast.LENGTH_LONG).show();
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        List<News> newsList = QueryUtils.fetchNewsData(mURL);
        return newsList;
    }
}