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

// List view adapter class extended from Array adapter class.
public class ListViewAdapter extends ArrayAdapter<String> {

    ArrayList<String> list;
    Context context;
    private ProfileFragment profileFragment;

    // Constructor sets up list view with array of reminders
    // and uses appropriate XML file.
    public ListViewAdapter(Context context, ArrayList<String> items, ProfileFragment profileFragment) {
        super(context, R.layout.list_row, items);
        this.context = context;
        this.profileFragment = profileFragment;
        list = items;
    }

    // Function to set up display of text for reminders
    // and behavior of remove reminder button image.
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
