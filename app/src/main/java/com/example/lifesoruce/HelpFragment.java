// File to handle help fragment
// displaying help text about app.
// No extra logic needed as help
// text directly placed by strings.

package com.example.lifesoruce;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lifesoruce.databinding.FragmentHelpBinding;

public class HelpFragment extends Fragment {

    private FragmentHelpBinding binding;

    // Function to handle creation of fragment view.
    // Inflates view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHelpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }

    // Function to handle destroying fragment view.
    // Resets view binding.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}