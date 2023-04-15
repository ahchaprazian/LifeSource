package com.example.lifesoruce;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.lifesoruce.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    static ListView listView;
    static ArrayList<String> items;
    static ListViewAdapter adapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newReminder_popup;
    private Button exit_popupWindow, addItem_popupWindow;
    private Button dateButton;
    private int pos = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        listView = binding.listView;
        items = new ArrayList<>();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);

        String savedName = sharedPref.getString("name", "");
        EditText editNameView = binding.nameView;
        editNameView.setText(savedName);

        int numOfSavedItems = sharedPref.getInt("numOfItems", 0);
        if (numOfSavedItems > 0) {
            for (int i = 0; i < numOfSavedItems; i++) {
                items.add(sharedPref.getString(String.valueOf(i), ""));
                pos = pos + 1;
            }
            adapter = new ListViewAdapter(getActivity().getApplicationContext(), items);
            listView.setAdapter((adapter));
        }

        /*
        * When the button in the bottom corner is clicked
        * the pop up that prompts the user to enter their text is displayed
        */
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show popupLayout
                //binding.popupLayout.setVisibility(View.VISIBLE);
                createNewReminderDialog();
            }
        });

        /*
        * When the add button in the pop up window is pressed it should
        * close out then display it inside the recent reminders view
        * still a work in progress
        */
        /*binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text from editText
                String text = binding.editText.getText().toString();

                items.add(text);
                adapter = new ListViewAdapter(getActivity().getApplicationContext(), items);
                listView.setAdapter((adapter));
                // Hide popupLayout
                binding.popupLayout.setVisibility(View.GONE);
            }
        });*/

        return view;
    }

    public static void removeItem(int remove) {
        items.remove(remove);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();

        EditText editNameView = binding.nameView;
        editor.putString("name", editNameView.getText().toString());

        int itemSize = items.size();
        editor.putInt("numOfItems", itemSize);
        for (int i = 0; i < itemSize; i++) {
            editor.putString(String.valueOf(i), items.get(i));
        }

        editor.apply();

        binding = null;
    }

    public void createNewReminderDialog() {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        newReminder_popup = contactPopupView.findViewById(R.id.reminderText);
        addItem_popupWindow = contactPopupView.findViewById(R.id.addReminderButton);
        exit_popupWindow = contactPopupView.findViewById(R.id.exitPopupButton);
        dateButton = contactPopupView.findViewById(R.id.dateButton);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();


        addItem_popupWindow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Get text from editText
                String text = newReminder_popup.getText().toString();

                items.add(text);
                pos = pos + 1;
                adapter = new ListViewAdapter(getActivity().getApplicationContext(), items);
                listView.setAdapter((adapter));
            }
        });

        exit_popupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                //String s = items.get
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }
}