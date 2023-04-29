// File for profile fragment's view model
// Contains data and functions used by fragment.

package com.example.lifesoruce;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class ProfileViewModel extends ViewModel {
    static ArrayList<String> items;
    private String selectedDate;

    // Constructor to set up array of reminders.
    public ProfileViewModel() {
        items = new ArrayList<>();
    }

    // Function to retrieve array of reminders.
    public ArrayList<String> getItems() {
        return items;
    }

    // Function to index array for specific reminder.
    public String getItem(int position) { return items.get(position); }

    // Function to add new reminder to array.
    public void addItem(String item) { items.add(item); }

    // Function to remove indexed reminder from array.
    public void removeItem(Context context, int position) { items.remove(position); }

    // Function to clear array of reminders.
    public void clearItems() { items.clear(); }

    // Function to retrieve saved selected date from calendar.
    public String getSelectedDate() { return selectedDate; }

    // Function to save selected date from calendar.
    public void setSelectedDate(String selectedDate) { this.selectedDate = selectedDate; }

    // Function to load saved profile picture from shared preferences.
    public String getSavedImagePath(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("profilePic", Context.MODE_PRIVATE);
        return sharedPref.getString("savedImagePath", null);
    }

    // Function to load saved user name from shared preferences.
    public String getSavedName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        return sharedPref.getString("name", "");
    }

    /**
     * Saves the given name to the SharedPreferences under the key "name".
     *
     * @param context The Context used to access the SharedPreferences
     * @param name The name to be saved in the SharedPreferences
     * @see SharedPreferences
     */
    public void saveName(Context context, String name) {
        SharedPreferences sharedPref = context.getSharedPreferences("username", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.apply();
    }

    /**
     * Loads saved items from SharedPreferences and populates the items array with the retrieved data.
     * The SharedPreferences file is identified by the key "com.example.LifeSource.PREFERENCE_FILE_KEY".
     *
     * @param context The Context used to access the SharedPreferences
     * @see SharedPreferences
     */
    public void loadSavedItems(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        // Get total number of reminders saved.
        int numOfSavedItems = sharedPref.getInt("numOfItems", 0);

        // If reminders exist, iterate through each to retrieve.
        if (numOfSavedItems > 0) {
            // Clear existing array in case it has old data.
            clearItems();
            // Iterate through all saved reminders.
            for (int i = 0; i < numOfSavedItems; i++) {
                addItem(sharedPref.getString(String.valueOf(i), ""));
            }
        }
    }

    /**
     * Saves the items array to SharedPreferences for persistence across app launches.
     * The SharedPreferences file is identified by the key "com.example.LifeSource.PREFERENCE_FILE_KEY".
     * Old saved data is cleared before the current items array is saved.
     *
     * @param context The Context used to access the SharedPreferences
     * @see SharedPreferences
     */
    public void saveItems(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Clear all old saved data in shared preferences for reminders.
        editor.clear();
        // Save total number of reminders in array.
        int itemSize = getItems().size();
        editor.putInt("numOfItems", itemSize);
        // Save all reminders in array.
        for (int i = 0; i < itemSize; i++) {
            editor.putString(String.valueOf(i), getItems().get(i));
        }

        editor.apply();
    }

}
