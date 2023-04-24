package com.example.lifesoruce;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.lifesoruce.databinding.FragmentNewsBinding;
import com.jacksonandroidnetworking.JacksonParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NewsFragment extends Fragment {

    private FragmentNewsBinding binding;
    private static String API_KEY = "NEWS_API_KEY";
    private static String TAG="NewsFragment";

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ArrayList<NewsArticle> mArticleList;
    private ArticleAdapter mArticleAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String searchQuery = "blood-donation";

        final SwipeRefreshLayout swipeRefreshLayout = binding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Create a Handler and Runnable to add a delay
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Perform the refresh operation here, e.g., load new data
                        get_news_from_api(searchQuery);

                        // Remember to call setRefreshing(false) when the refresh is complete
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500); // The delay is set to 2000 milliseconds (2 seconds)
            }
        });

        SearchView searchView = binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search operation here
                if(!query.isEmpty()) {
                    get_news_from_api(query);
                }
                else {
                    get_news_from_api("blood-donation");
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Update the search results as the user types
                if(!newText.isEmpty()) {
                    get_news_from_api(newText);
                }
                else {
                    get_news_from_api("blood-donation");
                }
                return true;
            }
        });

        ImageView gotoBookmarksImageView = binding.gotoBookmarksImageview;
        gotoBookmarksImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_newsFragment_to_bookmarkFragment);
            }
        });


        AndroidNetworking.initialize(getActivity().getApplicationContext());

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        mProgressBar = binding.progressbarId;
        mRecyclerView = binding.recyclerviewId;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mArticleList = new ArrayList<>();

        //loadBookmarkedArticles();
        get_news_from_api(searchQuery);

        return view;
    }

    private void get_news_from_api(String searchQuery) {
        mArticleList.clear();

        /* Sends the get request to the news api while displaying everything
        * set it to display everything related to blood donation using the api
        * key specified
        */
        AndroidNetworking.get("https://newsapi.org/v2/everything")
                .addQueryParameter("q", searchQuery)
                .addQueryParameter("apiKey",API_KEY)
                .addHeaders("token", "1234")
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        mProgressBar.setVisibility(View.GONE);

                        try {
                            JSONArray articles=response.getJSONArray("articles");

                            for (int j=0;j<articles.length();j++)
                            {
                                // accessing each article object in the JSONArray
                                JSONObject article=articles.getJSONObject(j);

                                // initializing an empty ArticleModel
                                NewsArticle currentArticle=new NewsArticle();

                                // storing values of the article object properties
                                String author=article.getString("author");
                                String title=article.getString("title");
                                String description=article.getString("description");
                                String url=article.getString("url");
                                String urlToImage=article.getString("urlToImage");
                                String publishedAt=article.getString("publishedAt");
                                String content=article.getString("content");

                                // setting the values of the ArticleModel
                                // using the set methods
                                currentArticle.setAuthor(author);
                                currentArticle.setTitle(title);
                                currentArticle.setDescription(description);
                                currentArticle.setUrl(url);
                                currentArticle.setUrlToImage(urlToImage);
                                currentArticle.setPublishedAt(publishedAt);
                                currentArticle.setContent(content);

                                // adding an article to the articles List
                                mArticleList.add(currentArticle);
                            }

                            // setting the adapter
                            mArticleAdapter=new ArticleAdapter(getActivity().getApplicationContext(),mArticleList);
                            mRecyclerView.setAdapter(mArticleAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // logging the JSONException LogCat
                            Log.d(TAG,"Error : "+e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                } );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}