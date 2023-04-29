package com.example.lifesoruce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.androidnetworking.widget.ANImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * A custom RecyclerView.Adapter class that displays news articles and handles user interactions
 * with the displayed articles, such as bookmarking and opening the article's web page.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    // setting the TAG for debugging purposes
    private static String TAG="ArticleAdapter";

    private ArrayList<NewsArticle> mArrayList;
    private Context mContext;
    private ImageView bookmarkIcon;

    /**
     * Checks if the given article is bookmarked by searching through the stored bookmarks in SharedPreferences.
     *
     * @param article The NewsArticle object to check for being bookmarked
     * @return true if the article is bookmarked, false otherwise
     */
    private boolean isArticleBookmarked(NewsArticle article) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        String bookmarksJson = sharedPreferences.getString("bookmarked_articles", "[]");

        try {
            JSONArray bookmarksArray = new JSONArray(bookmarksJson);

            for (int i = 0; i < bookmarksArray.length(); i++) {
                JSONObject bookmarkedArticle = bookmarksArray.getJSONObject(i);
                if (bookmarkedArticle.getString("url").equals(article.getUrl())) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Toggles the bookmark status of a given article. If the article is bookmarked, it is removed from
     * the bookmarks; if it is not bookmarked, it is added to the bookmarks. The updated bookmarks are
     * then saved in SharedPreferences.
     *
     * @param article The NewsArticle object to toggle the bookmark status of
     * @return true if the article is now bookmarked, false if it was removed from the bookmarks
     */
    private boolean toggleBookmark(NewsArticle article) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("bookmarks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String bookmarksJson = sharedPreferences.getString("bookmarked_articles", "[]");

        try {
            JSONArray bookmarksArray = new JSONArray(bookmarksJson);
            boolean isBookmarked = false;
            int bookmarkedIndex = -1;

            for (int i = 0; i < bookmarksArray.length(); i++) {
                JSONObject bookmarkedArticle = bookmarksArray.getJSONObject(i);
                if (bookmarkedArticle.getString("url").equals(article.getUrl())) {
                    isBookmarked = true;
                    bookmarkedIndex = i;
                    break;
                }
            }

            if (isBookmarked) {
                // Remove the article from the bookmarks
                bookmarksArray.remove(bookmarkedIndex);
            } else {
                // Add the article to the bookmarks
                JSONObject articleJson = new JSONObject();
                articleJson.put("author", article.getAuthor());
                articleJson.put("title", article.getTitle());
                articleJson.put("description", article.getDescription());
                articleJson.put("url", article.getUrl());
                articleJson.put("urlToImage", article.getUrlToImage());
                articleJson.put("publishedAt", article.getPublishedAt());
                articleJson.put("content", article.getContent());
                bookmarksArray.put(articleJson);
            }

            // Save the updated bookmarks
            editor.putString("bookmarked_articles", bookmarksArray.toString());
            editor.apply();

            return !isBookmarked;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Constructs an ArticleAdapter instance with the given context and list of news articles.
     *
     * @param context The context in which the adapter will be used
     * @param list The ArrayList of NewsArticle objects to display
     */
    public ArticleAdapter(Context context,ArrayList<NewsArticle> list){
        // initializing the constructor
        this.mContext=context;
        this.mArrayList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating the layout with the article view (R.layout.article_item)
        View view=LayoutInflater.from(mContext).inflate(R.layout.article_item,parent,false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data from the given NewsArticle object at the specified position to the ViewHolder.
     * Sets the article's title, description, contributor, and date, as well as handling bookmark
     * icon click events and article click events.
     *
     * @param holder The ViewHolder to bind the data to
     * @param position The position of the article within the list of articles
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // the parameter position is the index of the current article
        // getting the current article from the ArrayList using the position
        NewsArticle currentArticle=mArrayList.get(position);

        // setting the text of textViews
        holder.title.setText(currentArticle.getTitle());
        holder.description.setText(currentArticle.getDescription());

        // subString(0,10) trims the date to make it short
        holder.contributordate.setText(currentArticle.getAuthor()+
                " | "+currentArticle.getPublishedAt().substring(0,10));

        // Loading image from network into
        // Fast Android Networking View ANImageView
        holder.image.setDefaultImageResId(R.drawable.ic_launcher_background);
        holder.image.setErrorImageResId(R.drawable.ic_launcher_foreground);
        holder.image.setImageUrl(currentArticle.getUrlToImage());

        // setting the content Description on the Image
        holder.image.setContentDescription(currentArticle.getContent());

        // Check if the article is bookmarked
        if (isArticleBookmarked(currentArticle)) {
            holder.bookmarkIcon.setImageResource(R.drawable.bookmark_filled);
        } else {
            holder.bookmarkIcon.setImageResource(R.drawable.bookmark_icon);
        }

        // handling click event of the bookmark icon
        holder.bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBookmarked = toggleBookmark(currentArticle);
                if (isBookmarked) {
                    holder.bookmarkIcon.setImageResource(R.drawable.bookmark_filled);
                } else {
                    holder.bookmarkIcon.setImageResource(R.drawable.bookmark_icon);
                }
            }
        });

        // handling click event of the article
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // an intent to the WebActivity that display web pages
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url_key",currentArticle.getUrl());

                // starting an Activity to display the page of the article
                mContext.startActivity(intent);
            }
        });

    }

    /**
     * Returns the total number of news articles in the list.
     *
     * @return The number of articles in the list
     */
    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    /**
     * A custom ViewHolder class that holds the views necessary to display a news article within
     * a RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        // declaring the views
        private TextView title,description,contributordate;
        private ANImageView image;
        private ImageView bookmarkIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // assigning views to their ids
            title=itemView.findViewById(R.id.title_id);
            description=itemView.findViewById(R.id.description_id);
            image=itemView.findViewById(R.id.image_id);
            contributordate=itemView.findViewById(R.id.contributordate_id);
            bookmarkIcon = itemView.findViewById(R.id.bookmark_icon);
        }
    }

    /**
     * Updates the list of news articles displayed by the adapter.
     *
     * @param newArticlesList An ArrayList of NewsArticle objects representing the new list of articles
     */
    public void updateArticlesList(ArrayList<NewsArticle> newArticlesList) {
        this.mArrayList = newArticlesList;
    }


}
