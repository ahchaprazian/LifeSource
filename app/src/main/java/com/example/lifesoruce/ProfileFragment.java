package com.example.lifesoruce;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.lifesoruce.databinding.FragmentProfileBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    private String selectedDate;

    private int pos = 0;
    private final ActivityResultLauncher<Intent> imageChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                        String imagePath = saveImageToInternalStorage(bitmap);

                        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("savedImagePath", imagePath);
                        editor.apply();

                        binding.profileImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private String saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper wrapper = new ContextWrapper(getActivity().getApplicationContext());
        File file = wrapper.getDir("profileImages", Context.MODE_PRIVATE);
        file = new File(file, "profileImage.jpg");

        try {
            OutputStream stream = null;
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        listView = binding.listView;
        items = new ArrayList<>();

        binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });


        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);

        String savedImagePath = sharedPref.getString("savedImagePath", null);
        if (savedImagePath != null) {
            try {
                File file = new File(savedImagePath);
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                binding.profileImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        String savedName = sharedPref.getString("name", "");
        EditText editNameView = binding.nameView;
        editNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editNameView.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editNameView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

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
                createNewReminderDialog();
            }
        });

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }



    public static void removeItem(int remove) {
        items.remove(remove);
        listView.setAdapter(adapter);
    }

    private void saveItems() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int itemSize = items.size();
        editor.putInt("numOfItems", itemSize);
        for (int i = 0; i < itemSize; i++) {
            editor.putString(String.valueOf(i), items.get(i));
        }

        editor.apply();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.LifeSource.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        EditText editNameView = binding.nameView;
        editor.putString("name", editNameView.getText().toString());

        saveItems();

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

                // Append the date to the reminder text
                if (selectedDate != null) {
                    text += " (" + selectedDate + ")";
                }

                items.add(text);
                pos = pos + 1;
                adapter = new ListViewAdapter(getActivity().getApplicationContext(), items);
                listView.setAdapter((adapter));
                dialog.dismiss();
                saveItems();
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
                                selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }
}