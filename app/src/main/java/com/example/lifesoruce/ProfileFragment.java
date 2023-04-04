package com.example.lifesoruce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.lifesoruce.databinding.FragmentProfileBinding;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    static ListView listView;
    static ArrayList<String> items;
    static ListViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        listView = binding.listView;
        items = new ArrayList<>();

        /*
        * When the button in the bottom corner is clicked
        * the pop up that prompts the user to enter their text is displayed
        */
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show popupLayout
                binding.popupLayout.setVisibility(View.VISIBLE);
            }
        });

        /*
        * When the add button in the pop up window is pressed it should
        * close out then display it inside the recent reminders view
        * still a work in progress
        */
        binding.addButton.setOnClickListener(new View.OnClickListener() {
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
        });

        return view;
    }

    public static void removeItem(int remove) {
        items.remove(remove);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}