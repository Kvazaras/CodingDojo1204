package com.example.codingdojo1204;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private EditText editTextLink;
    private ListView listViewResult;
    private ArrayAdapter<String> adapter;
    private List<String> resultList;

    private static final String LINKS_PREF_KEY = "links";
    public static final String BARBORA_URL_PREFIX = "https://www.barbora.lt/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLink = findViewById(R.id.addLinkEditText);
        listViewResult = findViewById(R.id.itemListView);

        resultList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultList);
        listViewResult.setAdapter(adapter);

        // Load stored links from SharedPreferences
        loadLinksFromSharedPreferences();
    }

    public void parseHtml(View view) {
        String url = editTextLink.getText().toString().trim();
        if (!url.isEmpty() && url.startsWith(MainActivity.BARBORA_URL_PREFIX)) {
            new ParseHtmlTask(this, adapter, resultList).execute(url);
        }
        else{
            showToast("Invalid URL");
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    String saveLinksToSharedPreferences(String url) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Set<String> savedList = sharedPreferences.getStringSet(LINKS_PREF_KEY, new HashSet<>());
        savedList.add(url);

        Set<String> newSet = new HashSet<>();
        newSet.add(url);

        newSet.addAll(resultList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(LINKS_PREF_KEY, newSet);
        editor.apply();


        return url;
    }

    private void loadLinksFromSharedPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Set<String> linksSet = sharedPreferences.getStringSet(LINKS_PREF_KEY, new HashSet<>());

        resultList.addAll(linksSet);
        adapter.notifyDataSetChanged();
    }
}
