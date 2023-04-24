package com.example.lifesoruce;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class ProfileViewModel extends ViewModel {
    static ArrayList<String> items;
    private String selectedDate;

    public ProfileViewModel() {
        items = new ArrayList<>();
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void addItem(String item) { items.add(item); }

    public void removeItem(int position) {
        items.remove(position);
    }

    public void clearItems() {
        items.clear();
    }


    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSavedImagePath(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        return sharedPref.getString("savedImagePath", null);
    }

    public String getSavedName(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        return sharedPref.getString("name", "");
    }

    public void saveName(Context context, String name) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.apply();
    }

    public void loadSavedItems(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        int numOfSavedItems = sharedPref.getInt("numOfItems", 0);

        if (numOfSavedItems > 0) {
            clearItems();
            for (int i = 0; i < numOfSavedItems; i++) {
                addItem(sharedPref.getString(String.valueOf(i), ""));
            }
        }
    }

    public void saveItems(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int itemSize = getItems().size();
        editor.putInt("numOfItems", itemSize);
        for (int i = 0; i < itemSize; i++) {
            editor.putString(String.valueOf(i), getItems().get(i));
        }

        editor.apply();
    }

}
