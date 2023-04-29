// File for adapter for list view.
// Handles how reminders get displayed
// in list view and behavior of remove
// reminder button.

package com.example.lifesoruce;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * A custom ListViewAdapter class that extends ArrayAdapter<String>, designed to display a list of items
 * in a ListView with a remove button for each item. This adapter is used in conjunction with the
 * ProfileFragment to display and manage the items.
 */
public class ListViewAdapter extends ArrayAdapter<String> {

    ArrayList<String> list;
    Context context;
    private ProfileFragment profileFragment;

    /**
     * Constructs a ListViewAdapter with the provided context, items, and ProfileFragment.
     *
     * @param context The Context used to access the resources and services
     * @param items The ArrayList of items to be displayed in the ListView
     * @param profileFragment The ProfileFragment used to manage item removal
     * @see ArrayAdapter
     * @see ProfileFragment
     */
    public ListViewAdapter(Context context, ArrayList<String> items, ProfileFragment profileFragment) {
        super(context, R.layout.list_row, items);
        this.context = context;
        this.profileFragment = profileFragment;
        list = items;
    }

    /**
     * Sets up the display of each item in the ListView, including the text and the remove button behavior.
     * This method inflates the list_row layout for each entry in the ListView, sets the item text, and
     * configures the remove button to remove the corresponding item.
     *
     * @param position The position of the item within the adapter's data set
     * @param convertView The old view to reuse, if possible
     * @param parent The parent ViewGroup that the returned view will be attached to
     * @return A View that displays the data at the specified position in the data set
     * @see LayoutInflater
     * @see TextView
     * @see ImageView
     * @see View.OnClickListener
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflates XML for entry in list view if a new one is needed.
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row, null);
        }

        // Set name of list view entry based on reminder name
        TextView name = convertView.findViewById(R.id.name);
        name.setText(profileFragment.getItem(position));

        // Set remove reminder image to be button that
        // removes that reminder.
        ImageView remove = convertView.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileFragment.removeItem(position);
            }
        });

        return convertView;
    }
}
