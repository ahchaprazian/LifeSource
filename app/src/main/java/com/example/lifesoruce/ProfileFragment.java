// File to handle profile fragment.
// Sets up main behavior of fragment
// with customizable user profile
// picture, name, and reminders.

package com.example.lifesoruce;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lifesoruce.databinding.FragmentProfileBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ListView listView;
    private ListViewAdapter adapter;
    private AlertDialog dialog;
    private EditText newReminderPopup;
    private static ProfileViewModel profileViewModel;

    private int pos = 0;
    /**
     * A private final ActivityResultLauncher<Intent> used to handle the result of a user's image selection from their device.
     * This launcher is initialized with an ActivityResultContracts.StartActivityForResult() contract to start the image
     * selection process. When an image is successfully chosen, the launcher retrieves the image, converts it to a bitmap,
     * saves it to the internal storage, and updates the user's profile picture in both SharedPreferences and the UI.
     * @see ActivityResultLauncher
     * @see ActivityResultContracts.StartActivityForResult
     * @see MediaStore.Images.Media
     */
    private final ActivityResultLauncher<Intent> imageChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                        String imagePath = saveImageToInternalStorage(bitmap);

                        SharedPreferences sharedPref = getActivity().getSharedPreferences("profilePic", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("savedImagePath", imagePath);
                        editor.apply();

                        binding.profileImageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    /**
     * Saves the given Bitmap image to the internal storage of the application as a JPEG file, with the name "profileImage.jpg".
     * The image is stored in a private directory named "profileImages" which is accessible only to this application.
     * @param bitmap The Bitmap image to be saved in the internal storage
     * @return The absolute file path of the saved image
     * @throws IOException If there is an error while saving the image to internal storage
     * @see Bitmap
     * @see ContextWrapper
     */
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

    /**
     * Function for creation of fragment view.
     * Sets up view binding, view model, and loads saved data
     * for profile picture, name, and reminders.
     * Sets basic behavior for user input keyboard
     * and adding reminders.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Set view binding and inflate view.
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // Set up view model.
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        listView = binding.listView;

        // Set on click listener for profile picture.
        // Allows users to change profile picture.
        binding.profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        // Loads saved profile picture if it exists.
        String savedImagePath = profileViewModel.getSavedImagePath(getActivity());
        if (savedImagePath != null) {
            try {
                File file = new File(savedImagePath);
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                binding.profileImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        // Loads saved user name if it exists.
        String savedName = profileViewModel.getSavedName(getActivity());//sharedPref.getString("name", "");
        EditText editNameView = binding.nameView;
        // Configure behavior of edit text input field with keyboard.
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

        // Sets saved user name if it exists.
        editNameView.setText(savedName);
        // Save inputted user name after it's entered.
        editNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
                profileViewModel.saveName(getActivity(), name);
            }
        });

        // Load saved reminders and display them
        // via an adapter class.
        profileViewModel.loadSavedItems(getActivity());
        adapter = new ListViewAdapter(getActivity().getApplicationContext(), profileViewModel.getItems(), this);
        listView.setAdapter((adapter));

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

    // Function to open device camera roll to
    // select a profile picture.
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageChooserLauncher.launch(intent);
    }

    // Function to save reminders and update
    // adapter for displaying them.
    private void saveItems() {
        profileViewModel.saveItems(getActivity());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Update the adapter
    }

    // Function to retrieve specific reminder
    // within array of them based on index.
    public String getItem(int position) {
        return profileViewModel.getItem(position);
    }

    /**
     *  Function to delete specific reminder
     *  within array based on index. Saves
     *  reminders afterward with changes.*/
    public void removeItem(int remove) {
        profileViewModel.removeItem(getActivity(), remove);
        saveItems(); // Add this line to save the updated list to SharedPreferences.
    }

    /**
     * Function for destruction of fragment view.
     * Saves user name and reminders beforehand.
     * Resets view binding.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        String name = binding.nameView.getText().toString();
        profileViewModel.saveName(getActivity(), name);

        saveItems();

        binding = null;
    }

    /** Function to handle add reminder button's
     * basic behavior. Allows button to open
     * pop-up window with options to add
     * new reminder, enter reminder name,
     * add calendar date, and exit window.
     */
    public void createNewReminderDialog() {
        // Set up add reminder button.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        newReminderPopup = contactPopupView.findViewById(R.id.reminderText);
        Button addItemPopupWindow = contactPopupView.findViewById(R.id.addReminderButton);
        Button exitPopupWindow = contactPopupView.findViewById(R.id.exitPopupButton);
        Button dateButton = contactPopupView.findViewById(R.id.dateButton);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        /** Set listener for when pop-up window closes.
         * Clears selected calendar date so it doesn't
         * get reused for future reminders unintentionally.
         */
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                profileViewModel.setSelectedDate(null);
            }
        });
        dialog.show();

        /** Set on click listener for adding configured reminder.
         * Constructs full reminder name from entered name and
         * calendar date. Checks if entered name is valid.
         */
        addItemPopupWindow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Check if text from editText is empty.
                if (!newReminderPopup.getText().toString().trim().isEmpty()) {
                    // If text is nonempty, construct full reminder name.
                    // Get text from editText.
                    String text = newReminderPopup.getText().toString();

                    // Append the date to the reminder text.
                    if (profileViewModel.getSelectedDate() != null) {
                        text += " (" + profileViewModel.getSelectedDate() + ")";
                    }

                    // Add reminder to reminders array
                    // and save its contents.
                    profileViewModel.addItem(text);
                    pos = pos + 1;
                    saveItems();
                } else {
                    // If text is empty, alert user of invalid reminder name.
                    Toast.makeText(getActivity(), "invalid reminder name", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });


        // Set on click listener for exit button of pop-up window.
        // Closes pop-up window.
        exitPopupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        /*
         Sets an OnClickListener for the dateButton, which opens a DatePickerDialog when clicked. The DatePickerDialog
         allows the user to select a specific date from a calendar view. Once a date is chosen, the selected date is
         saved in the profileViewModel as a formatted string (MM-DD-YYYY).
         */
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set up calendar object for date.
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Set up calendar window to select specific date.
                // Function to retrieve and save selected date.
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                profileViewModel.setSelectedDate((monthOfYear + 1) + "-" + dayOfMonth + "-" +  year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
    }
}