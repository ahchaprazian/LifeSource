package com.example.lifesoruce;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerview_bookmarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the SwipeRefreshLayout
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_bookmarks);

        // Get the bookmarked articles and set the adapter
        updateBookmarkedArticles();

        // Set up the SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateBookmarkedArticles();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void updateBookmarkedArticles() {
        ArrayList<NewsArticle> bookmarkedArticles = getBookmarkedArticles();
        if (articleAdapter == null) {
            articleAdapter = new ArticleAdapter(getContext(), bookmarkedArticles);
            recyclerView.setAdapter(articleAdapter);
        } else {
            articleAdapter.updateArticlesList(bookmarkedArticles);
            articleAdapter.notifyDataSetChanged();
        }
    }


    private ArrayList<NewsArticle> getBookmarkedArticles() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        String bookmarksJson = sharedPreferences.getString("bookmarked_articles", "[]");

        ArrayList<NewsArticle> bookmarkedArticles = new ArrayList<>();

        try {
            JSONArray bookmarksArray = new JSONArray(bookmarksJson);

            for (int i = 0; i < bookmarksArray.length(); i++) {
                JSONObject articleJson = bookmarksArray.getJSONObject(i);

                NewsArticle article = new NewsArticle(
                        articleJson.getString("author"),
                        articleJson.getString("title"),
                        articleJson.getString("description"),
                        articleJson.getString("url"),
                        articleJson.getString("urlToImage"),
                        articleJson.getString("publishedAt"),
                        articleJson.getString("content")
                );

                bookmarkedArticles.add(article);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bookmarkedArticles;
    }


}