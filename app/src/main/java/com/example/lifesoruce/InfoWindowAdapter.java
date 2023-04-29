// File for adapter to handle displaying
// custom map pin info windows.

package com.example.lifesoruce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

// Map pin info widow adapter class.
public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View myWindow;
    private Context myContext;

    // Constructor to set up info window via inflating respective XML file.
    public InfoWindowAdapter(Context context) {
        myContext = context;
        myWindow = LayoutInflater.from(context).inflate(R.layout.info_window, null);
    }

    // Function to render and display text onto info window.
    private void renderWindowText(Marker marker, View view) {
        // Set up info window title. (donation center name)
        String title = marker.getTitle();
        TextView textViewTitle = (TextView) view.findViewById(R.id.infoWindowTitle);

        if(!title.equals("")) {
            textViewTitle.setText(title);
        }

        // Set up info window snippet. (donation center description)
        String snippet = marker.getSnippet();
        TextView textViewSnippet = (TextView) view.findViewById(R.id.infoWindowSnippet);

        if(!snippet.equals("")) {
            textViewSnippet.setText(snippet);
        }
    }

    // Function to get contents of map pin info info windows.
    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        renderWindowText(marker, myWindow);
        return myWindow;
    }

    // function to get map pin info window.
    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        renderWindowText(marker, myWindow);
        return myWindow;
    }
}
