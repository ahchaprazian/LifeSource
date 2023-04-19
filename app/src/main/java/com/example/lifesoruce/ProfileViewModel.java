package com.example.lifesoruce;

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

    public void addItem(String item) {
        items.add(item);
    }

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

}
