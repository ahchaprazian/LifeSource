package com.example.lifesoruce;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        AndroidNetworking.initialize(getActivity().getApplicationContext());

        AndroidNetworking.setParserFactory(new JacksonParserFactory());

        mProgressBar = binding.progressbarId;
        mRecyclerView = binding.recyclerviewId;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mArticleList = new ArrayList<>();

        get_news_from_api();

        return view;
    }

    private void get_news_from_api() {
        mArticleList.clear();

        /* Sends the get request to the news api while displaying everything
        * set it to display everything related to blood donation using the api
        * key specified
        */
        AndroidNetworking.get("https://newsapi.org/v2/everything")
                .addQueryParameter("q", "blood-donation")
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