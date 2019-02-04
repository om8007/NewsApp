package com.example.lenovo.newsapp;

//import android.app.LoaderManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static String REQUEST_URL = "http://content.guardianapis.com/search?show-tags=contributor&api-key=test";

    private static final String KEY_API_KEY = "api-key";
    private static final String KEY_SHOW_TAGS = "show-tags";
    private static final String KEY_PAGE_SIZE = "page-size";
    private static final String KEY_ORDER_BY = "order-by";
    private static final String KEY_SECTION = "section";
    ProgressDialog progressDialog;
    LinearLayout errorLayout;
    private NewsAdpater mAdapter;
    TextView mainText;

    Uri baseUri = Uri.parse(REQUEST_URL);
    Uri.Builder uriBuilder = baseUri.buildUpon();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating news");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        ListView lview = this.findViewById(R.id.news_list_view);
        errorLayout = this.findViewById(R.id.error_layout);
        mainText = this.findViewById(R.id.main_text_view);
        mAdapter = new NewsAdpater(this, new ArrayList<News>());
        lview.setAdapter(mAdapter);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getmUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            progressDialog.dismiss();
            mainText.setText(R.string.internet_error_msg);
        }
    }

    @Override
    @NonNull
    public NewsLoader onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minNews = sharedPreferences.getString(getString(R.string.settings_min_news_key), "");
        if (minNews == "") {
            minNews = sharedPreferences.getString(getString(R.string.settings_min_news_key), getString(R.string.settings_min_news_default));
        }
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), "");
        if (orderBy == "") {
            orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        }
        String section = sharedPreferences.getString(getString(R.string.settings_section_news_key), "");
        if (section == "") {
            section = sharedPreferences.getString(getString(R.string.settings_section_news_key), getString(R.string.settings_section_news_default));
        }

        uriBuilder.appendQueryParameter(KEY_PAGE_SIZE, minNews);
        uriBuilder.appendQueryParameter(KEY_ORDER_BY, orderBy);

        if (!section.equals(getString(R.string.settings_section_news_default))) {
            uriBuilder.appendQueryParameter(KEY_SECTION, section);
        }

        return new NewsLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<List<News>> loader, List<News> news) {

        progressDialog.dismiss();
        mainText.setText(R.string.content_error_msg);
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
            errorLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh_button:
                recreate();
                return true;
            case R.id.action_setting:
                Intent settingsIntent = new Intent(this, settings_activity.class);
                startActivity(settingsIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

