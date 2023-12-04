package com.example.codingdojo1204;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

public class ParseHtmlTask extends AsyncTask<String, Void, String> {

    private final Activity activity;
    private final ArrayAdapter<String> adapter;
    private final List<String> resultList;

    public ParseHtmlTask(Activity activity, ArrayAdapter<String> adapter, List<String> resultList) {
        this.activity = activity;
        this.adapter = adapter;
        this.resultList = resultList;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];

        try {
            Document document = Jsoup.connect(url).get();
            Element discountedElement = document.select("del.b-product-crossed-out-price").first();
            if (discountedElement != null) {
                return url + " discounted";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((MainActivity) activity).saveLinksToSharedPreferences(url);
        return url + " No discount found.";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            resultList.add(result);
            adapter.notifyDataSetChanged();

            // Save links to SharedPreference
        }
    }
}